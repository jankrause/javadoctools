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
package com.github.jankrause.javadoctools.utils;

import java.io.*;
import java.util.Optional;

import com.github.jankrause.javadoctools.domain.Column;
import com.github.jankrause.javadoctools.domain.Table;

public final class JavaAnnotationParser {

    private static int startSearchingHere(int javadocCommentLine) {
        return javadocCommentLine - 1;
    }

    public static Optional<Table> fetchTableFromEntityAnnotation(int javadocCommentLine, File javaFile) throws IOException {
        Optional<String> tableName = fetchAnnotationAttribute(javadocCommentLine, javaFile, "table", "name", true);

        return tableName.isPresent() ? Optional.<Table>of(new Table(tableName.get())) : Optional.<Table>empty();
    }

    private static int deriveEndIndex(String line, int start) {
        return line.toLowerCase().indexOf(')', start + 2) - 1;
    }

    private static int deriveStartIndex(String line, String pattern) {
        return line.toLowerCase().indexOf(pattern) + pattern.length() + 2;
    }

    private static boolean contains(String line, String pattern) {
        return line.toLowerCase().contains(pattern);
    }

    private static Optional<String> fetchAnnotationAttribute(int javadocCommentLine, File javaFile, String annotationName, String attributeName, boolean shouldFetchFirst) throws IOException {
        BufferedReader javaReader= null;
        try {
            javaReader = new BufferedReader(new InputStreamReader(new FileInputStream(javaFile.getAbsolutePath()), "UTF8"));
            String line = javaReader.readLine();
            String annotationValue = null;
            int currentRow = 1;
            boolean found = false;

            while (line != null && !found) {
                String pattern = "@" + annotationName + "(" + attributeName;
                if (contains(line, pattern)) {
                    if (!found && (currentRow >= startSearchingHere(javadocCommentLine) || shouldFetchFirst)) {
                        annotationValue = extractAttributeValue(line, pattern);
                        found = true;
                    }
                }

                line = javaReader.readLine();
                currentRow++;
            }

            return Optional.ofNullable(annotationValue);
        } finally {
            if(javaReader != null) {
                javaReader.close();
            }
        }
    }

    private static String extractAttributeValue(String line, String pattern) {
        String tableName;
        int start = deriveStartIndex(line, pattern);
        int end = deriveEndIndex(line, start);
        tableName = line.toLowerCase().substring(start, end);
        return tableName;
    }

    public static Optional<Column> fetchColumnFromColumnAnnotation(Table table, int javadocCommentLine, File javaFile) throws IOException{
        Optional<String> columnName = fetchAnnotationAttribute(javadocCommentLine, javaFile, "column", "name", false);

        if(columnName.isPresent()) {
            return Optional.of(new Column(table, columnName.get()));
        } else {
            return Optional.empty();
        }
    }

}
