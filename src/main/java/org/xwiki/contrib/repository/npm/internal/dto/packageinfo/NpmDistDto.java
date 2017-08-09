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
package org.xwiki.contrib.repository.npm.internal.dto.packageinfo;

import java.net.URI;
import java.net.URISyntaxException;

import org.xwiki.extension.ResolveException;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class NpmDistDto
{
    private String shasum;
    private String tarball;

    public String getShasum()
    {
        return shasum;
    }

    public void setShasum(String shasum)
    {
        this.shasum = shasum;
    }

    public String getTarball()
    {
        return tarball;
    }

    public void setTarball(String tarball)
    {
        this.tarball = tarball;
    }

    public URI getURI() throws ResolveException
    {
        try {
            return new URI(tarball);
        } catch (URISyntaxException e) {
            throw new ResolveException("Badly formed URI for downloading package: " + tarball);
        }
    }
}
