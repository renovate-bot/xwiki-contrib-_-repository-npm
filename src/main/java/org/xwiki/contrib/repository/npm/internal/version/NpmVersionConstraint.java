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

import java.util.Collection;
import java.util.Collections;

import org.xwiki.extension.version.IncompatibleVersionConstraintException;
import org.xwiki.extension.version.Version;
import org.xwiki.extension.version.VersionConstraint;
import org.xwiki.extension.version.VersionRangeCollection;
import org.xwiki.extension.version.internal.DefaultVersionRangeCollection;

public class NpmVersionConstraint implements VersionConstraint
{
    private String rawVersionConstraint;

    private final NpmVersionRange versionRange;

    private Version version;

    public NpmVersionConstraint(String rawVersionConstraint)
    {
        this.rawVersionConstraint = rawVersionConstraint;
        versionRange = new NpmVersionRange(rawVersionConstraint);
    }

    @Override public Collection<VersionRangeCollection> getRanges()
    {
        return Collections.singleton(new DefaultVersionRangeCollection(Collections.singleton(versionRange)));
    }

    @Override public Version getVersion()
    {
        return version;
    }

    @Override public String getValue()
    {
        return rawVersionConstraint;
    }

    @Override public boolean containsVersion(Version version)
    {

        return versionRange.containsVersion(version);
    }

    @Override public boolean isCompatible(Version version)
    {
        return versionRange.containsVersion(version);
    }

    @Override public VersionConstraint merge(VersionConstraint versionConstraint)
            throws IncompatibleVersionConstraintException
    {
        return this;
        // TODO: 08.08.2017 to implement in future
    }
}
