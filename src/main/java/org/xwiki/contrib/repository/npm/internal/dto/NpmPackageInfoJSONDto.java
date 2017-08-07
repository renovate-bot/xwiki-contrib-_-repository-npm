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
package org.xwiki.contrib.repository.npm.internal.dto;

import java.util.LinkedList;
import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.StreamSupport;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;

@JsonIgnoreProperties(ignoreUnknown = true)
public class NpmPackageInfoJSONDto
{
    private String name;

    private String title;

    private String description;

    private String version;

    private String main;

    private String homepage;

    private NpmHumanDto author;

    private NpmRepositoryDto repository;

    private List<String> keywords;

    private NpmBugsDto bugs;

    private String license;

    private JsonNode dependencies;

    private JsonNode devDependencies;

    private JsonNode scripts;

    private String gitHead;

    private String _id;

    private String _shasum;

    private String _npmVersion;

    private String _nodeVersion;

    private NpmHumanDto _npmUser;

    private NpmDistDto dist;

    private List<NpmHumanDto> maintainers;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getVersion()
    {
        return version;
    }

    public void setVersion(String version)
    {
        this.version = version;
    }

    public String getMain()
    {
        return main;
    }

    public void setMain(String main)
    {
        this.main = main;
    }

    public String getHomepage()
    {
        return homepage;
    }

    public void setHomepage(String homepage)
    {
        this.homepage = homepage;
    }

    public NpmHumanDto getAuthor()
    {
        return author;
    }

    public void setAuthor(NpmHumanDto author)
    {
        this.author = author;
    }

    public NpmRepositoryDto getRepository()
    {
        return repository;
    }

    public void setRepository(NpmRepositoryDto repository)
    {
        this.repository = repository;
    }

    public List<String> getKeywords()
    {
        return keywords;
    }

    public void setKeywords(List<String> keywords)
    {
        this.keywords = keywords;
    }

    public NpmBugsDto getBugs()
    {
        return bugs;
    }

    public void setBugs(NpmBugsDto bugs)
    {
        this.bugs = bugs;
    }

    public String getLicense()
    {
        return license;
    }

    public void setLicense(String license)
    {
        this.license = license;
    }

    public List<NpmDependencyDto> getDependencies()
    {
        LinkedList<NpmDependencyDto> dependencies = new LinkedList<>();

        this.dependencies.fields().forEachRemaining(
                depEntry -> dependencies.add(new NpmDependencyDto(depEntry.getKey(), depEntry.getValue().textValue()))
        );

        return dependencies;
    }

    public void setDependencies(JsonNode dependencies)
    {
        this.dependencies = dependencies;
    }

    public JsonNode getDevDependencies()
    {
        return devDependencies;
    }

    public void setDevDependencies(JsonNode devDependencies)
    {
        this.devDependencies = devDependencies;
    }

    public JsonNode getScripts()
    {
        return scripts;
    }

    public void setScripts(JsonNode scripts)
    {
        this.scripts = scripts;
    }

    public String getGitHead()
    {
        return gitHead;
    }

    public void setGitHead(String gitHead)
    {
        this.gitHead = gitHead;
    }

    public String get_id()
    {
        return _id;
    }

    public void set_id(String _id)
    {
        this._id = _id;
    }

    public String get_shasum()
    {
        return _shasum;
    }

    public void set_shasum(String _shasum)
    {
        this._shasum = _shasum;
    }

    public String get_npmVersion()
    {
        return _npmVersion;
    }

    public void set_npmVersion(String _npmVersion)
    {
        this._npmVersion = _npmVersion;
    }

    public String get_nodeVersion()
    {
        return _nodeVersion;
    }

    public void set_nodeVersion(String _nodeVersion)
    {
        this._nodeVersion = _nodeVersion;
    }

    public NpmHumanDto get_npmUser()
    {
        return _npmUser;
    }

    public void set_npmUser(NpmHumanDto _npmUser)
    {
        this._npmUser = _npmUser;
    }

    public NpmDistDto getDist()
    {
        return dist;
    }

    public void setDist(NpmDistDto dist)
    {
        this.dist = dist;
    }

    public List<NpmHumanDto> getMaintainers()
    {
        return maintainers;
    }

    public void setMaintainers(List<NpmHumanDto> maintainers)
    {
        this.maintainers = maintainers;
    }
}
