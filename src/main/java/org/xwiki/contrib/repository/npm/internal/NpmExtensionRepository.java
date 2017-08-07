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

import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.http.HttpException;
import org.apache.http.client.protocol.HttpClientContext;
import org.slf4j.Logger;
import org.xwiki.component.annotation.Component;
import org.xwiki.contrib.repository.npm.internal.dto.NpmPackageInfoJSONDto;
import org.xwiki.contrib.repository.npm.internal.utils.NpmHttpUtils;
import org.xwiki.contrib.repository.npm.internal.utils.NpmUtils;
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
import org.xwiki.extension.repository.result.IterableResult;
import org.xwiki.extension.repository.search.SearchException;
import org.xwiki.extension.repository.search.Searchable;
import org.xwiki.extension.version.Version;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @version $Id: 81a55f3a16b33bcf2696d0cac493b25c946b6ee4 $
 * @since 1.0
 */
@Component(roles = NpmExtensionRepository.class)
@Singleton
public class NpmExtensionRepository extends AbstractExtensionRepository
        implements Searchable
{
    private static ObjectMapper objectMapper = new ObjectMapper();

    @Inject
    private ExtensionLicenseManager licenseManager;

    @Inject
    private ExtensionFactory extensionFactory;

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
            return NpmExtension.constructFrom(npmPackageInfo, this, licenseManager, httpClientFactory);
        } catch (HttpException e) {
            throw new ExtensionNotFoundException("Failed to resolve package [" + packageName + "] version: [" + version + "]", e);
        }
    }

    @Override
    public Extension resolve(ExtensionDependency extensionDependency) throws ResolveException
    {
        return null;
    }

    @Override
    public IterableResult<Version> resolveVersions(String packageName, int offset, int nb) throws ResolveException
    {
        return null;
    }

    @Override public IterableResult<Extension> search(String searchQuery, int offset, int hitsPerPage)
            throws SearchException
    {
        return null;
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
}
