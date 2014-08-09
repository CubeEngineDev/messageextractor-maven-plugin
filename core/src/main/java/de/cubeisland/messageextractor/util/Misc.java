/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 Phillip Schichtel, Stefan Wolf
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package de.cubeisland.messageextractor.util;

import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.log.SystemLogChute;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.List;

public class Misc
{
    /**
     * It's a FileFilter object which accepts every file
     */
    private static final FileFilter DUMMY_FILTER = new FileFilter()
    {
        @Override
        public boolean accept(File file)
        {
            return true;
        }
    };

    private Misc()
    {
        // nothing to do here. It's not permitted to create an instance of this class
    }

    /**
     * This method returns every file which is inside the specified directory
     *
     * @param baseDir the directory
     *
     * @return every file inside the directory
     *
     * @throws IOException
     */
    public static List<File> scanFilesRecursive(File baseDir) throws IOException
    {
        return scanFilesRecursive(baseDir, DUMMY_FILTER);
    }

    /**
     * This mehtod returns every file which is inside the specified directory and conforms
     * to the rules of the FileFilter instance.
     *
     * @param baseDir the directory
     * @param filter  the filter which shall be used
     *
     * @return every file inside the directory conforming the filter rules
     *
     * @throws IOException
     */
    public static List<File> scanFilesRecursive(File baseDir, FileFilter filter) throws IOException
    {
        if (baseDir == null)
        {
            throw new IllegalArgumentException("The base directory must not be null!");
        }
        if (filter == null)
        {
            throw new IllegalArgumentException("The filter must not be null!");
        }
        if (!baseDir.isDirectory())
        {
            throw new IllegalArgumentException("The base directory must actually be a directory...");
        }

        List<File> files = new LinkedList<File>();
        scanFilesRecursive0(baseDir, files, filter);

        return files;
    }

    private static void scanFilesRecursive0(File directory, List<File> files, FileFilter filter) throws IOException
    {
        final File[] directoryListing = directory.listFiles();
        if (directoryListing == null)
        {
            throw new IOException("Failed to enlist the directory '" + directory.getPath() + "' !");
        }
        for (File file : directoryListing)
        {
            if (file.isDirectory())
            {
                scanFilesRecursive0(file, files, filter);
            }
            else if (filter.accept(file))
            {
                files.add(file);
            }
        }
    }

    /**
     * This method parses the content of a file to into a char array
     *
     * @param file    the file
     * @param charset the charset of the file
     *
     * @return the content of the file as a char array
     *
     * @throws IOException
     */
    public static char[] parseFileToCharArray(File file, Charset charset) throws IOException
    {
        BufferedInputStream stream = new BufferedInputStream(new FileInputStream(file));
        StringBuilder sb = new StringBuilder();
        byte[] buffer = new byte[4096];

        int bytesRead = 0;
        while ((bytesRead = stream.read(buffer)) > -1)
        {
            if (bytesRead > 0)
            {
                sb.append(new String(buffer, 0, bytesRead, charset));
            }
        }

        stream.close();
        return sb.toString().toCharArray();
    }

    /**
     * This method returns a file which has an relative path from another file.
     *
     * @param base the base file
     * @param file the file which shall be related to the base
     *
     * @return a new file instance which is relative to the base and describes the same file like the file param
     */
    public static File getRelativizedFile(File base, File file)
    {
        return new File(base.toURI().relativize(file.toURI()).getPath());
    }

    /**
     * This method looks for the specified resource and returns it as an url object.
     * Therefore it looks at first whether it's a file path. As a second step it tries
     * to read it as an url.
     *
     * @param resource the resource url string
     *
     * @return the resource string as an url
     */
    public static URL getResource(String resource)
    {
        // 1. tries to read resource as a file object
        File file = new File(resource);
        if (file.exists() && file.canRead())
        {
            try
            {
                return file.toURI().toURL();
            }
            catch (MalformedURLException ignored)
            {
                // Ignore
            }
        }

        // 2. tries to read resource as an url
        try
        {
            URL url = new URL(resource);
            url.openStream().close();
            return url;
        }
        catch (Exception e)
        {
            return null;
        }
    }

    /**
     * This method reads an url and returns the content as a String object
     *
     * @param url     the url
     * @param charset the charset of the url content
     *
     * @return the content of the url
     *
     * @throws IOException
     */
    public static String getContent(URL url, Charset charset) throws IOException
    {
        URLConnection connection = url.openConnection();
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), charset));

        StringBuilder content = new StringBuilder();
        String inputLine;

        while ((inputLine = reader.readLine()) != null)
        {
            content.append(inputLine);
            content.append('\n');
        }

        reader.close();
        return content.toString();
    }

    public static VelocityEngine createVelocityEngine()
    {
        VelocityEngine velocityEngine = new VelocityEngine();

        velocityEngine.setProperty(RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS, SystemLogChute.class.getName());
        velocityEngine.setProperty(SystemLogChute.RUNTIME_LOG_LEVEL_KEY, "info");
        velocityEngine.setProperty(SystemLogChute.RUNTIME_LOG_SYSTEM_ERR_LEVEL_KEY, "warn");

        return velocityEngine;
    }
}
