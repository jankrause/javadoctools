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
package de.jankrause.javadoctools.utils;

import de.jankrause.javadoctools.domain.Column;
import de.jankrause.javadoctools.domain.Table;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import static org.junit.Assert.assertEquals;

public class JavaAnnotationParserTest {

    @Test
    public void shouldFindTable() throws IOException {
        File javaFile = new File("src/test/resources/testpackage/AnnotatedEntity.java");
        int startCommentLine = 8;
        assertEquals(new Table("test_comments"), JavaAnnotationParser.fetchTableFromEntityAnnotation(
                startCommentLine, javaFile).get());
    }

    @Test
    public void shouldFindColumn() throws IOException {
        File javaFile = new File("src/test/resources/testpackage/AnnotatedEntity.java");
        Table table = new Table("test_comments");
        int startCommentLine = 15;
        assertEquals(new Column(table, "name"), JavaAnnotationParser.fetchColumnFromColumnAnnotation(
                new Table("test_comments"), startCommentLine, javaFile).get());

        startCommentLine = 38;
        assertEquals(new Column(table, "id"), JavaAnnotationParser.fetchColumnFromColumnAnnotation(
                table, startCommentLine, javaFile).get());
    }

}
