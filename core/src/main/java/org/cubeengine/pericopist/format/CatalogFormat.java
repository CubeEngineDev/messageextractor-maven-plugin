/*
 * The MIT License
 * Copyright © 2013 Cube Island
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
package org.cubeengine.pericopist.format;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Logger;

import org.cubeengine.pericopist.exception.CatalogFormatException;
import org.cubeengine.pericopist.message.MessageStore;

/**
 * This class is needed to read from or rather write a catalog.
 * It creates a message catalog which contains all messages from a message store.
 * Furthermore it's able to store the messages of an old catalog in a message store by reading it.
 *
 * @see org.cubeengine.pericopist.extractor.MessageExtractor
 */
public interface CatalogFormat
{
    /**
     * This method writes the catalog file.
     *
     * @param config          config which shall be used to write the catalog
     * @param outputStream    the output stream which shall be used to create the catalog
     * @param messageStore    the message store containing the messages for the catalog
     *
     * @return true if the catalog file was written
     *
     * @throws CatalogFormatException if an error occurs while writing the message catalog
     */
    boolean write(CatalogConfiguration config, OutputStream outputStream, MessageStore messageStore) throws CatalogFormatException;

    /**
     * This method reads the catalog file and returns a message store containing the messages.
     * This method never returns null and implementations have to ensure this.
     *
     * @param config      config which shall be used to read the catalog
     * @param inputStream the input stream which shall be used to read the catalog
     *
     * @return a {@link MessageStore} instance holding all messages
     *
     * @throws CatalogFormatException if an error occurs while reading the message catalog
     */
    MessageStore read(CatalogConfiguration config, InputStream inputStream) throws CatalogFormatException;

    /**
     * This method sets the logger which is used by the CatalogFormat
     *
     * @param logger the logger
     */
    void setLogger(Logger logger);
}
