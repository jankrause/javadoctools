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
package de.jankrause.javadoctools.exporters;

import de.jankrause.javadoctools.domain.Definition;
import de.jankrause.javadoctools.utils.DefinitionUtils;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class DefinitionsAsHtmlExporter {

    private String outputFile;

    private List<Definition> definitions;

    public DefinitionsAsHtmlExporter(){}

    public DefinitionsAsHtmlExporter(String outputFile, List<Definition> definitions){
        this.outputFile = outputFile;
        this.definitions = new LinkedList<Definition>();
        this.definitions.addAll(definitions);
    }

    public DefinitionsAsHtmlExporter setOutputFile(String outputFile){
        return new DefinitionsAsHtmlExporter(outputFile, this.definitions);
    }

    public DefinitionsAsHtmlExporter setDefinition(List<Definition> definitions){
        return new DefinitionsAsHtmlExporter(this.outputFile, definitions);
    }

    public void export() throws IOException {
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_22);
        File tmpFile = File.createTempFile("tmp_", ".tpl");

        FileOutputStream copyFileOutputStream = null;
        InputStream templateInputStream = null;

        cfg.setDirectoryForTemplateLoading(new File(tmpFile.getParent()));
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

        try {
            templateInputStream = DefinitionsAsHtmlExporter.class.getResourceAsStream("glossary.tpl");
            copyFileOutputStream = new FileOutputStream(tmpFile);
            IOUtils.copy(templateInputStream, copyFileOutputStream);
        } finally {
            IOUtils.closeQuietly(templateInputStream);
            IOUtils.closeQuietly(copyFileOutputStream);
        }

        Writer out = null;

        try {
            Map<Character, List<Definition>> sortedByFirstCharacter = DefinitionUtils.sortByFirstCharacter(definitions);
            Template temp = cfg.getTemplate(tmpFile.getName());

            out = new FileWriter(this.outputFile);

            temp.process(sortedByFirstCharacter, out);
        } catch(TemplateException templateEx){
            throw new IOException(templateEx);
        }
        finally {
            IOUtils.closeQuietly(out);
        }
    }
}
