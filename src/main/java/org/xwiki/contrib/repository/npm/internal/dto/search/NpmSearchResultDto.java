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
package org.xwiki.contrib.repository.npm.internal.dto.search;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class NpmSearchResultDto
{
    @JsonProperty("package")
    private NpmSearchPackageDto packageDto;
    private NpmSearchScoreDto score;
    private double searchScore;

    public NpmSearchPackageDto getPackageDto()
    {
        return packageDto;
    }

    public void setPackageDto(NpmSearchPackageDto packageDto)
    {
        this.packageDto = packageDto;
    }

    public NpmSearchScoreDto getScore()
    {
        return score;
    }

    public void setScore(NpmSearchScoreDto score)
    {
        this.score = score;
    }

    public double getSearchScore()
    {
        return searchScore;
    }

    public void setSearchScore(double searchScore)
    {
        this.searchScore = searchScore;
    }
}
