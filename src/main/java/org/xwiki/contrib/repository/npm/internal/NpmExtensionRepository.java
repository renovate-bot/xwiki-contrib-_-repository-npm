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

import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.http.client.protocol.HttpClientContext;
import org.slf4j.Logger;
import org.xwiki.component.annotation.Component;
import org.xwiki.environment.Environment;
import org.xwiki.extension.Extension;
import org.xwiki.extension.ExtensionDependency;
import org.xwiki.extension.ExtensionId;
import org.xwiki.extension.ExtensionLicenseManager;
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


    @Override
    public Extension resolve(ExtensionId extensionId) throws ResolveException
    {
        return null;
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

    public ExtensionRepository setUpRepository(ExtensionRepositoryDescriptor extensionRepositoryDescriptor)
    {
        setDescriptor(extensionRepositoryDescriptor);
        this.localContext = HttpClientContext.create();
        return this;
    }
}
