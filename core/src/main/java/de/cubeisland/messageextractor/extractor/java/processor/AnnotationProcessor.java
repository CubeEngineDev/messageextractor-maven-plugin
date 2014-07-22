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
package de.cubeisland.messageextractor.extractor.java.processor;

import java.util.Map.Entry;

import de.cubeisland.messageextractor.extractor.java.Annotation;
import de.cubeisland.messageextractor.extractor.java.JavaExtractorConfiguration;
import de.cubeisland.messageextractor.message.MessageStore;
import spoon.reflect.code.CtExpression;
import spoon.reflect.declaration.CtAnnotation;

public class AnnotationProcessor extends MessageProcessor<CtAnnotation>
{
    public AnnotationProcessor(JavaExtractorConfiguration configuration, MessageStore messageStore)
    {
        super(configuration, messageStore);
    }

    @Override
    public void process(CtAnnotation element)
    {
        Annotation annotation = this.getConfiguration().getAnnotation(element.getAnnotationType().getQualifiedName());

        if (annotation == null)
        {
            return;
        }

        for (Object fieldValue : element.getElementValues().entrySet())
        {
            Entry<String, Object> fieldEntry = (Entry<String, Object>) fieldValue; // TODO

            if (!annotation.hasField(fieldEntry.getKey()))
            {
                continue;
            }

            if (fieldEntry.getValue() instanceof CtExpression<?>)
            {
                String message = this.getString((CtExpression<?>) fieldEntry.getValue());
                if (message != null)
                {
                    this.addMessage(element, message, null);
                }
            }
        }
    }
}