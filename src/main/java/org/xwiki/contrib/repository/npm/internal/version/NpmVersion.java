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
package org.xwiki.contrib.repository.npm.internal.version;

import org.xwiki.extension.version.Version;

public class NpmVersion implements Version
{
    private String rawVersion;

    private final com.github.yuchi.semver.Version version;

    public NpmVersion(String rawVersion)
    {
        this.rawVersion = rawVersion;
        this.version = new com.github.yuchi.semver.Version(rawVersion);
    }

    public NpmVersion(Version version)
    {
        rawVersion = version.getValue();
        this.version = new com.github.yuchi.semver.Version(rawVersion);
    }

    @Override public String getValue()
    {
        return rawVersion;
    }

    @Override public Type getType()
    {
        return Type.STABLE;
    }

    @Override public int compareTo(Version version)
    {
        return this.version.compareTo(new NpmVersion(version).getInnerVersion());
    }

    protected com.github.yuchi.semver.Version getInnerVersion()
    {
        return version;
    }

    public int isNewer(NpmVersion npmVersion)
    {
        return -compareTo(npmVersion);
    }
}
