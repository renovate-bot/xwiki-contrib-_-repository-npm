package org.xwiki.contrib.repository.npm.internal;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.xwiki.contrib.repository.npm.internal.version.NpmVersion;
import org.xwiki.contrib.repository.npm.internal.version.NpmVersionConstraint;
import org.xwiki.extension.repository.result.CollectionIterableResult;
import org.xwiki.extension.version.Version;

import static org.junit.Assert.assertEquals;

public class NpmExtensionRepositoryTest
{
    @Test
    public void getNewestVersionTest() throws Exception
    {
        NpmExtensionRepository npmExtensionRepository = new NpmExtensionRepository();

        List<NpmVersion> npmVersions = Arrays.asList(
                new NpmVersion("0.2.3"),
                new NpmVersion("0.1.3"),
                new NpmVersion("0.1.1"),
                new NpmVersion("1.2.3"),
                new NpmVersion("2.2.3"),
                new NpmVersion("0.2.4"),
                new NpmVersion("0.4.1")
        );
        CollectionIterableResult collectionIterableResult =
                new CollectionIterableResult(npmVersions.size(), 0, npmVersions);

        NpmVersionConstraint npmVersionConstraint = new NpmVersionConstraint(">=0.2.3 || <0.0.1");
        Optional<Version> newestCompatibleVersion =
                npmExtensionRepository.getNewestCompatibleVersion(npmVersionConstraint, collectionIterableResult);
        assertEquals("2.2.3", newestCompatibleVersion.get().getValue());

        NpmVersionConstraint npmVersionConstraint2 = new NpmVersionConstraint("~1.2.1 1.2.3 >=1.2.3");
        Optional<Version> newestCompatibleVersion2 =
                npmExtensionRepository.getNewestCompatibleVersion(npmVersionConstraint2, collectionIterableResult);
        assertEquals("1.2.3", newestCompatibleVersion2.get().getValue());

    }
}