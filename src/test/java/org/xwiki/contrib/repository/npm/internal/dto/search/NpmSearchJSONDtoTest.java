package org.xwiki.contrib.repository.npm.internal.dto.search;

import org.junit.Test;
import org.xwiki.contrib.repository.npm.internal.TestUtils;
import org.xwiki.contrib.repository.npm.internal.dto.packageinfo.NpmPackageInfoJSONDto;

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.junit.Assert.assertEquals;

public class NpmSearchJSONDtoTest
{
    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void deserializationTestReact() throws Exception
    {
        String filenameOfJson = "searchReact.json";
        String json = TestUtils.getFileAsString(filenameOfJson, this);
        NpmSearchJSONDto npmSearchJSONDto = objectMapper.readValue(json, NpmSearchJSONDto.class);
        //no exception expected - all fields are mapped

        assertEquals(35030, npmSearchJSONDto.getTotal());

        assertEquals(20, npmSearchJSONDto.getObjects().size());
        assertEquals("react", npmSearchJSONDto.getObjects().get(0).getPackageDto().getName());
        assertEquals("https://www.npmjs.com/package/react", npmSearchJSONDto.getObjects().get(0).getPackageDto().getLinks().getNpm());
    }

    @Test
    public void deserializationTestJQuery() throws Exception
    {
        String filenameOfJson = "searchJQuery.json";
        String json = TestUtils.getFileAsString(filenameOfJson, this);
        NpmSearchJSONDto npmSearchJSONDto = objectMapper.readValue(json, NpmSearchJSONDto.class);
        //no exception expected - all fields are mapped

        assertEquals(6116, npmSearchJSONDto.getTotal());

        assertEquals(20, npmSearchJSONDto.getObjects().size());
        assertEquals("jquery", npmSearchJSONDto.getObjects().get(0).getPackageDto().getName());
        assertEquals("https://www.npmjs.com/package/jquery", npmSearchJSONDto.getObjects().get(0).getPackageDto().getLinks().getNpm());
    }
}