/*
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.xwiki.contrib.repository.npm.internal;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.http.HttpException;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.message.BasicHeader;
import org.slf4j.Logger;
import org.xwiki.component.annotation.Component;
import org.xwiki.contrib.repository.npm.internal.dto.packageinfo.NpmPackageInfoJSONDto;
import org.xwiki.contrib.repository.npm.internal.dto.search.NpmSearchJSONDto;
import org.xwiki.contrib.repository.npm.internal.dto.versions.NpmAbrevMetaDataJSONDto;
import org.xwiki.contrib.repository.npm.internal.utils.NpmHttpUtils;
import org.xwiki.contrib.repository.npm.internal.utils.NpmUtils;
import org.xwiki.contrib.repository.npm.internal.version.NpmVersion;
import org.xwiki.environment.Environment;
import org.xwiki.extension.Extension;
import org.xwiki.extension.ExtensionDependency;
import org.xwiki.extension.ExtensionId;
import org.xwiki.extension.ExtensionLicenseManager;
import org.xwiki.extension.ExtensionNotFoundException;
import org.xwiki.extension.ResolveException;
import org.xwiki.extension.internal.ExtensionFactory;
import org.xwiki.extension.repository.AbstractExtensionRepository;
import org.xwiki.extension.repository.ExtensionRepository;
import org.xwiki.extension.repository.ExtensionRepositoryDescriptor;
import org.xwiki.extension.repository.http.internal.HttpClientFactory;
import org.xwiki.extension.repository.internal.RepositoryUtils;
import org.xwiki.extension.repository.result.CollectionIterableResult;
import org.xwiki.extension.repository.result.IterableResult;
import org.xwiki.extension.repository.search.SearchException;
import org.xwiki.extension.repository.search.Searchable;
import org.xwiki.extension.version.Version;
import org.xwiki.extension.version.VersionConstraint;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @version $Id: 81a55f3a16b33bcf2696d0cac493b25c946b6ee4 $
 * @since 1.0
 */
@Component(roles = NpmExtensionRepository.class)
@Singleton
public class NpmExtensionRepository extends AbstractExtensionRepository implements Searchable
{
    private static ObjectMapper objectMapper = new ObjectMapper();

    @Inject
    private ExtensionLicenseManager licenseManager;

    @Inject
    private HttpClientFactory httpClientFactory;

    @Inject
    private Environment environment;

    @Inject
    private Logger logger;

    private HttpClientContext localContext;

    public ExtensionRepository setUpRepository(ExtensionRepositoryDescriptor extensionRepositoryDescriptor)
    {
        setDescriptor(extensionRepositoryDescriptor);
        this.localContext = HttpClientContext.create();
        return this;
    }

    @Override
    public Extension resolve(ExtensionId extensionId) throws ResolveException
    {
        String packageName = NpmUtils.getPackageName(extensionId);
        String version = NpmUtils.getVersion(extensionId);
        return resolveNpmExtension(packageName, version);
    }

    public NpmExtension resolveNpmExtension(String packageName, String version) throws ResolveException
    {
        try {
            NpmPackageInfoJSONDto npmPackageInfo = getNpmPackageInfo(packageName, version);
            return NpmExtension.constructFrom(npmPackageInfo, this, licenseManager, httpClientFactory, environment);
        } catch (HttpException e) {
            throw new ExtensionNotFoundException(
                    "Failed to resolve package [" + packageName + "] version: [" + version + "]", e);
        }
    }

    @Override
    public Extension resolve(ExtensionDependency extensionDependency) throws ResolveException
    {
        String packageName = NpmUtils.getPackageName(extensionDependency.getId());
        IterableResult<Version> versions = resolveVersions(packageName, 0, -1);
        VersionConstraint versionConstraint = extensionDependency.getVersionConstraint();
        Optional<Version> validVersion = getNewestCompatibleVersion(versionConstraint, versions);
        Version version = validVersion.orElseThrow(
                () -> new ExtensionNotFoundException("Could not resolve dependency: [" + packageName + "]"));
        return resolve(new ExtensionId(NpmParameters.DEFAULT_GROUPID + ":" + packageName, version));
    }

    protected Optional<Version> getNewestCompatibleVersion(VersionConstraint versionConstraint,
            IterableResult<Version> versions)
    {
        return StreamSupport.stream(versions.spliterator(), false)
                .sorted((v1, v2) -> {
                    return new NpmVersion(v1).isNewer(new NpmVersion(v2));
                })
                .filter(version -> versionConstraint.isCompatible(version)).findFirst();
    }

    @Override
    public IterableResult<Version> resolveVersions(String packageName, int offset, int nb) throws ResolveException
    {
        try {
            NpmAbrevMetaDataJSONDto abreviatedMetadata = getAbreviatedMetadata(packageName);
            List<Version> versions = abreviatedMetadata.getVersions();
            return RepositoryUtils.getIterableResult(offset, nb, versions);
        } catch (HttpException e) {
            throw new ResolveException("Could not resolve versions of package: [" + packageName + "]", e);
        }
    }

    @Override public IterableResult<Extension> search(String searchQuery, int offset, int hitsPerPage)
            throws SearchException
    {
        try {
            NpmSearchJSONDto searchResults = getSearchResults(searchQuery, offset, hitsPerPage);
            List<Extension> result = searchResults.getObjects().stream().map(npmSearchResultDto ->
                    (Extension) NpmExtension.constructFrom(npmSearchResultDto.getPackageDto(), this))
                    .collect(Collectors.toList());
            return new CollectionIterableResult<Extension>(searchResults.getTotal(), offset, result);
        } catch (HttpException e) {
            throw new SearchException("Failure when performing search query: '" + searchQuery + "'", e);
        }
    }

    public NpmPackageInfoJSONDto getNpmPackageInfo(String packageName, String version) throws HttpException
    {
        URI uri = null;
        try {
            uri = new URI(NpmParameters.PACKAGE_VERSION_INFO_JSON
                    .replace("{package_name}", packageName)
                    .replace("{version}", version));
        } catch (URISyntaxException e) {
            new HttpException("Problem with created URI for resolving package info", e);
        }

        InputStream inputStream = NpmHttpUtils.performGet(uri, httpClientFactory, localContext);

        try {
            return objectMapper.readValue(inputStream, NpmPackageInfoJSONDto.class);
        } catch (IOException e) {
            throw new HttpException(String.format("Failed to parse response body of request [%s]", uri), e);
        }
    }

    private NpmAbrevMetaDataJSONDto getAbreviatedMetadata(String packageName) throws HttpException
    {
        URI uri = null;
        try {
            uri = new URI(NpmParameters.PACKAGE_INFO_JSON.replace("{package_name}", packageName));
        } catch (URISyntaxException e) {
            new HttpException("Problem with created URI for resolving package info", e);
        }

        BasicHeader acceptHeader = new BasicHeader("Accept", "application/vnd.npm.install-v1+json");
        InputStream inputStream =
                NpmHttpUtils.performGet(uri, httpClientFactory, localContext, ArrayUtils.toArray(acceptHeader));

        try {
            return objectMapper.readValue(inputStream, NpmAbrevMetaDataJSONDto.class);
        } catch (IOException e) {
            throw new HttpException(String.format("Failed to parse response body of request [%s]", uri), e);
        }
    }

    private NpmSearchJSONDto getSearchResults(String searchQuery, int offset, int hitsPerPage) throws HttpException
    {
        if (hitsPerPage < 0) {
            hitsPerPage = 250;
        }
        URI uri = null;
        try {
            uri = new URI(NpmParameters.SEARCH_JSON
                    .replace("{search_query}", searchQuery)
                    .replace("{size}", "" + hitsPerPage)
                    .replace("{from}", "" + offset));
        } catch (URISyntaxException e) {
            new HttpException("Problem with created URI when searching packages for query: '" + searchQuery + "'", e);
        }
        InputStream inputStream =
                NpmHttpUtils.performGet(uri, httpClientFactory, localContext);
        try {
            return objectMapper.readValue(inputStream, NpmSearchJSONDto.class);
        } catch (IOException e) {
            throw new HttpException(String.format("Failed to parse response body of request [%s]", uri), e);
        }
    }
}
