/*******************************************************************************
 * Copyright 2015 Jan Christian Krause
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.github.jankrause.javadoctools.doclets;

import com.github.jankrause.javadoctools.connectors.DbConnector;
import com.github.jankrause.javadoctools.domain.Definition;
import com.github.jankrause.javadoctools.exporters.DefinitionsAsHtmlExporter;
import com.github.jankrause.javadoctools.taglets.DefinitionTaglet;
import com.github.jankrause.javadoctools.traversers.ClassDocTraverser;
import com.github.jankrause.javadoctools.traversers.DefinitionCommentVisitor;
import com.sun.javadoc.DocErrorReporter;
import com.sun.javadoc.Doclet;
import com.sun.javadoc.RootDoc;

import java.io.IOException;
import java.util.*;

public class GlossaryDoclet extends Doclet {

    private static List<Definition> definitions = new LinkedList<Definition>();

    private static String outputFile;

    public static int optionLength(String option) {
        return 2;
    }

    public static boolean validOptions(String options[][],
                                       DocErrorReporter reporter) {
        boolean foundOutputFile = false;

        for(int option = 0; option < options.length; option++){
            for(int value = 0; value < options[option].length; value++){
                String optionName = options[option][value];

                if("-of".equals(optionName)) {
                    outputFile = options[option][value + 1];
                    foundOutputFile = true;
                }
            }
        }

        return foundOutputFile;
    }

    public static boolean start(RootDoc root) {
        DefinitionCommentVisitor visitor = new DefinitionCommentVisitor(DbConnector.createConnector());
        new ClassDocTraverser(visitor).traverseComments(root, DefinitionTaglet.NAME);

        definitions = visitor.definitions();
            Collections.sort(definitions);
            try {
                new DefinitionsAsHtmlExporter()
                        .setDefinition(definitions)
                        .setOutputFile(outputFile)
                        .export();
            } catch (IOException ioEx) {
                ioEx.printStackTrace();
            }

        return true;
    }
}
