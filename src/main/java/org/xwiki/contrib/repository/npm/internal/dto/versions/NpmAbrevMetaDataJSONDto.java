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
package org.xwiki.contrib.repository.npm.internal.dto.versions;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.xwiki.contrib.repository.npm.internal.version.NpmVersion;
import org.xwiki.extension.version.Version;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

@JsonIgnoreProperties(ignoreUnknown = true)
public class NpmAbrevMetaDataJSONDto
{
    private JsonNode versions;
    private String name;
    @JsonProperty("dist-tags")
    private NpmAbrevMetaDataDistTagsDto distTags;
    private Date modified;

    public List<Version> getVersions()
    {
        LinkedList<Version> versions = new LinkedList<>();
        this.versions.fields().forEachRemaining(
                versionEntry -> versions.add(new NpmVersion(versionEntry.getKey()))
        );
        return versions;
    }

    public void setVersions(JsonNode versions)
    {
        this.versions = versions;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public NpmAbrevMetaDataDistTagsDto getDistTags()
    {
        return distTags;
    }

    public void setDistTags(NpmAbrevMetaDataDistTagsDto distTags)
    {
        this.distTags = distTags;
    }

    public Date getModified()
    {
        return modified;
    }

    public void setModified(Date modified)
    {
        this.modified = modified;
    }
}
