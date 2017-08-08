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

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.xwiki.contrib.repository.npm.internal.dto.NpmDependencyDto;
import org.xwiki.contrib.repository.npm.internal.dto.NpmPackageInfoJSONDto;
import org.xwiki.contrib.repository.npm.internal.version.NpmVersionConstraint;
import org.xwiki.environment.Environment;
import org.xwiki.extension.AbstractRemoteExtension;
import org.xwiki.extension.DefaultExtensionDependency;
import org.xwiki.extension.ExtensionId;
import org.xwiki.extension.ExtensionLicense;
import org.xwiki.extension.ExtensionLicenseManager;
import org.xwiki.extension.ResolveException;
import org.xwiki.extension.repository.ExtensionRepository;
import org.xwiki.extension.repository.http.internal.HttpClientFactory;
import org.xwiki.extension.version.internal.DefaultVersionConstraint;
import org.xwiki.extension.version.internal.DefaultVersionRange;

import com.github.yuchi.semver.Range;
import com.github.yuchi.semver.Version;

/**
 * @version $Id: 81a55f3a16b33bcf2696d0cac493b25c946b6ee4 $
 * @since 1.0
 */
public class NpmExtension extends AbstractRemoteExtension
{
    private NpmExtension(ExtensionRepository repository,
            ExtensionId id, String type)
    {
        super(repository, id, type);
    }

    public static NpmExtension constructFrom(NpmPackageInfoJSONDto npmPackageInfo,
            NpmExtensionRepository npmExtensionRepository, ExtensionLicenseManager licenseManager,
            HttpClientFactory httpClientFactory, Environment environment) throws ResolveException
    {
        String packageName = npmPackageInfo.getName();
        String version = npmPackageInfo.getVersion();
        ExtensionId extensionId = new ExtensionId(NpmParameters.DEFAULT_GROUPID + ":" + packageName, version);

        NpmExtension npmExtension = new NpmExtension(npmExtensionRepository, extensionId, NpmParameters.PACKAGE_TYPE);

        //set metadata
        npmExtension.setName(npmPackageInfo.getName());
        npmExtension.setDescription(npmPackageInfo.getDescription());
        npmExtension.setSummary(StringUtils.substring(npmPackageInfo.getDescription(), 0, 200));
        npmExtension.addLicences(npmPackageInfo.getLicense(), licenseManager);
        npmExtension.setWebsite(npmPackageInfo.getHomepage());
        npmExtension.addRepository(npmExtensionRepository.getDescriptor());
        npmExtension.setRecommended(false);

        npmExtension.setFile(new NpmExtensionFile(npmPackageInfo.getDist().getURI(), httpClientFactory, environment));
        //setFile TODO: 07.08.2017

        npmExtension.addDependencies(npmPackageInfo);

        return npmExtension;
    }

    private void addDependencies(NpmPackageInfoJSONDto npmPackageInfo) throws ResolveException
    {
        List<NpmDependencyDto> dependencies = npmPackageInfo.getDependencies();
        dependencies.forEach(npmDependencyDto ->
                addDependency(
                        new DefaultExtensionDependency(
                                NpmParameters.DEFAULT_GROUPID + ":" + npmDependencyDto.getName(),
                                new NpmVersionConstraint(npmDependencyDto.getVersion())
                        )
                )
        );
    }

    private void addLicences(String licenseName, ExtensionLicenseManager licenseManager)
    {
        if (licenseName != null) {
            ExtensionLicense extensionLicense = licenseManager.getLicense(licenseName);
            if (extensionLicense != null) {
                addLicense(extensionLicense);
            } else {
                List<String> content = null;
                addLicense(new ExtensionLicense(licenseName, content));
            }
        }
    }
}
