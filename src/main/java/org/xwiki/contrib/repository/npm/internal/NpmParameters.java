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

public interface NpmParameters
{
    String API_URL = "https://registry.npmjs.org/";
    String PACKAGE_VERSION_INFO_JSON = "http://registry.npmjs.org/{package_name}/{version}";
    String PACKAGE_INFO_JSON = "http://registry.npmjs.org/{package_name}";
    String SEARCH_JSON = "http://registry.npmjs.org/-/v1/search?text={search_query}+keywords:-documentation,-test&size={size}&from={from}";

    String DEFAULT_GROUPID = "org.webjar";
    String PACKAGE_TYPE = "webjar";
}
