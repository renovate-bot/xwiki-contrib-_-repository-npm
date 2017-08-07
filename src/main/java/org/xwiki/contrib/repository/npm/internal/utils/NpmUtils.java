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
package org.xwiki.contrib.repository.npm.internal.utils;

import org.xwiki.contrib.repository.npm.internal.NpmParameters;
import org.xwiki.extension.ExtensionId;
import org.xwiki.extension.ExtensionNotFoundException;
import org.xwiki.extension.ResolveException;

/**
 * @version $Id: 81a55f3a16b33bcf2696d0cac493b25c946b6ee4 $
 * @since 1.0
 */
final public class NpmUtils
{
    /**
     * This method assumes that extensionId is simply the npm package name
     * @param extensionId -
     * @return -
     * @throws ResolveException -
     */
    public static String getPackageName(ExtensionId extensionId) throws ResolveException
    {
        String[] parts = extensionId.getId().split(":");
        if (parts.length > 1) {
            if (NpmParameters.DEFAULT_GROUPID.equals(parts[0])) {
                return parts[1];
            } else {
                throw new ExtensionNotFoundException("That's not id of python package: " + extensionId);
            }
        } else {
            return extensionId.getId();
        }
    }

    public static String getVersion(ExtensionId extensionId)
    {
        return extensionId.getVersion().getValue();
    }
}
