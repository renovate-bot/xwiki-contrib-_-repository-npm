package org.xwiki.contrib.repository.npm.internal.dto.packageinfo;

import org.junit.Test;
import org.xwiki.contrib.repository.npm.internal.TestUtils;
import org.xwiki.contrib.repository.npm.internal.dto.packageinfo.NpmPackageInfoJSONDto;

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.junit.Assert.*;

public class NpmPackageInfoJSONDtoTest
{
    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void deserializationTestReact() throws Exception
    {
        String filenameOfJson = "packageInfoReact.json";
        String json = TestUtils.getFileAsString(filenameOfJson, this);
        NpmPackageInfoJSONDto npmPackageInfoJSONDto = objectMapper.readValue(json, NpmPackageInfoJSONDto.class);
        //no exception expected - all fields are mapped

        assertEquals("react", npmPackageInfoJSONDto.getName());

        assertEquals(5, npmPackageInfoJSONDto.getDependencies().size());
        assertEquals("create-react-class", npmPackageInfoJSONDto.getDependencies().get(0).getName());
        assertEquals("^15.6.0", npmPackageInfoJSONDto.getDependencies().get(0).getVersion());
    }

    @Test
    public void deserializationTestJQuery() throws Exception
    {
        String filenameOfJson = "packageInfoJQuery.json";
        String json = TestUtils.getFileAsString(filenameOfJson, this);
        NpmPackageInfoJSONDto npmPackageInfoJSONDto = objectMapper.readValue(json, NpmPackageInfoJSONDto.class);
        //no exception expected - all fields are mapped

        assertEquals("jquery", npmPackageInfoJSONDto.getName());

        assertEquals(0, npmPackageInfoJSONDto.getDependencies().size());
    }
}