package org.xwiki.contrib.repository.npm.internal.dto.versions;

import org.junit.Test;
import org.xwiki.contrib.repository.npm.internal.TestUtils;
import org.xwiki.contrib.repository.npm.internal.dto.packageinfo.NpmPackageInfoJSONDto;

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.junit.Assert.assertEquals;

public class NpmAbrevMetadataJSONDtoTest
{
    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void deserializationTestReact() throws Exception
    {
        String filenameOfJson = "versionsReact.json";
        String json = TestUtils.getFileAsString(filenameOfJson, this);
        NpmAbrevMetaDataJSONDto npmAbrevMetaDataJSONDto = objectMapper.readValue(json, NpmAbrevMetaDataJSONDto.class);
        //no exception expected - all fields are mapped

        assertEquals("react", npmAbrevMetaDataJSONDto.getName());

        assertEquals(124, npmAbrevMetaDataJSONDto.getVersions().size());
        assertEquals("0.0.1", npmAbrevMetaDataJSONDto.getVersions().get(0).getValue());
    }

    @Test
    public void deserializationTestJQuery() throws Exception
    {
        String filenameOfJson = "versionsJQuery.json";
        String json = TestUtils.getFileAsString(filenameOfJson, this);
        NpmAbrevMetaDataJSONDto npmAbrevMetaDataJSONDto = objectMapper.readValue(json, NpmAbrevMetaDataJSONDto.class);
        //no exception expected - all fields are mapped

        assertEquals("jquery", npmAbrevMetaDataJSONDto.getName());

        assertEquals(46, npmAbrevMetaDataJSONDto.getVersions().size());
        assertEquals("1.5.1", npmAbrevMetaDataJSONDto.getVersions().get(0).getValue());
    }
}