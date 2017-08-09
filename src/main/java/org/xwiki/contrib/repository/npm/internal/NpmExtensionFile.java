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
package org.xwiki.contrib.repository.npm.internal;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Optional;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.reflect.FieldUtils;
import org.apache.http.HttpException;
import org.apache.http.client.protocol.HttpClientContext;
import org.rauschig.jarchivelib.ArchiveFormat;
import org.rauschig.jarchivelib.Archiver;
import org.rauschig.jarchivelib.ArchiverFactory;
import org.rauschig.jarchivelib.CompressionType;
import org.xwiki.contrib.repository.npm.internal.utils.NpmHttpUtils;
import org.xwiki.environment.Environment;
import org.xwiki.extension.ExtensionFile;
import org.xwiki.extension.repository.http.internal.HttpClientFactory;

public class NpmExtensionFile implements ExtensionFile
{
    private final String packageName;

    private final String version;

    private final URI uriToDownload;

    private final HttpClientFactory httpClientFactory;

    private final Environment environment;

    private final HttpClientContext localContext;

    private Optional<File> webJarFileOptional = Optional.empty();

    public NpmExtensionFile(String packageName, String version, URI uriToDownload, HttpClientFactory httpClientFactory,
            Environment environment)
    {
        this.packageName = packageName;
        this.version = version;
        this.uriToDownload = uriToDownload;
        this.httpClientFactory = httpClientFactory;
        this.environment = environment;
        this.localContext = HttpClientContext.create();
    }

    @Override public long getLength()
    {
        // not obtainable from api
        return -1;
    }

    @Override public InputStream openStream() throws IOException
    {
        if (!webJarFileOptional.isPresent()) {
            webJarFileOptional = Optional.of(downloadJSPackageAndPrepareWebJar());
        }
        File webJarFile = webJarFileOptional.get();
        return new FileInputStream(webJarFile);
    }

    private File downloadJSPackageAndPrepareWebJar() throws IOException
    {
        File downloadedPackage = downloadJSPackage();
        File unPackedPackage = unpack(downloadedPackage);
        File webJarDirectory = prepareDirectoryForWebJarStructure();
        prepareWebJarDirectoryStructure(unPackedPackage, webJarDirectory);
        File webJar = packToJar(webJarDirectory);
        FileUtils.forceDelete(downloadedPackage);
        FileUtils.deleteDirectory(unPackedPackage);
        FileUtils.deleteDirectory(webJarDirectory);
        return webJar;
    }

    private File downloadJSPackage() throws IOException
    {
        try {
            InputStream inputStream = NpmHttpUtils.performGet(uriToDownload, httpClientFactory, localContext);
            File temporaryDirectory = environment.getTemporaryDirectory();
            File destFile =
                    new File(environment.getTemporaryDirectory().getAbsolutePath() + File.separator
                            + generateUniqueFileName() + ".tgz");
            destFile.createNewFile();

            FileUtils.copyInputStreamToFile(inputStream, destFile);
            return destFile;
        } catch (HttpException e) {
            throw new IOException("Failed to download JS package from registry. URL: " + uriToDownload);
        }
    }

    private File unpack(File downloadedPackage) throws IOException
    {
        File temporaryDirectory = environment.getTemporaryDirectory();
        File destDirectory =
                new File(environment.getTemporaryDirectory().getAbsolutePath() + File.separator
                        + generateUniqueFileName());
        destDirectory.mkdirs();
        Archiver archiver = ArchiverFactory.createArchiver(ArchiveFormat.TAR, CompressionType.GZIP);
        archiver.extract(downloadedPackage, destDirectory);
        return destDirectory;
    }

    private File prepareDirectoryForWebJarStructure()
    {
        File temporaryDirectory = environment.getTemporaryDirectory();
        File webJarDirectory =
                new File(environment.getTemporaryDirectory().getAbsolutePath() + File.separator
                        + generateUniqueFileName());
        webJarDirectory.mkdirs();
        return webJarDirectory;
    }

    private void prepareWebJarDirectoryStructure(File unPackedPackage, File webJarDirectory) throws IOException
    {
        File jsLibrariesDirectory =
                new File(webJarDirectory.getAbsolutePath() + File.separator + "META-INF" + File.separator + "resources"
                        + File.separator + "webjars" + File.separator + packageName + File.separator + version);
        jsLibrariesDirectory.mkdirs();
        copyJsLibraries(unPackedPackage, jsLibrariesDirectory);
    }

    private void copyJsLibraries(File unPackedPackage, File jsLibrariesDirectory) throws IOException
    {
        unPackedPackage = new File(unPackedPackage.getAbsolutePath() + File.separator + "package");
        jsLibrariesDirectory.delete(); // this is required by FileUtils.moveDirectory
        // TODO: 08.08.2017 perhaps there should be some selection of copied js libraries
        FileUtils.moveDirectory(unPackedPackage, jsLibrariesDirectory);
    }

    private File packToJar(File webJarDirectoryStructure) throws IOException
    {
        File temporaryDirectoryDestinationForJar = environment.getTemporaryDirectory();
        Archiver archiver = ArchiverFactory.createArchiver(ArchiveFormat.JAR);
        return archiver.create(generateUniqueFileName() + ".jar", temporaryDirectoryDestinationForJar,
                webJarDirectoryStructure);
    }

    private String generateUniqueFileName()
    {
        return packageName + "-" + version + "-" + UUID.randomUUID().toString();
    }
}
