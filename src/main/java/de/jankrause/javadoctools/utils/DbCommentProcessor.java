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

import de.jankrause.javadoctools.connectors.DatabaseCommentStrategy;
import de.jankrause.javadoctools.domain.Column;
import de.jankrause.javadoctools.domain.CommentType;
import de.jankrause.javadoctools.domain.Table;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

public class DbCommentProcessor {

    private int startCommentLine = 0;
    private File javaFile = null;
    private String text = "";
    private CommentType commentType;

    public DbCommentProcessor(int startCommentLine, File javaFile, String text, CommentType commentType){
        this.startCommentLine = startCommentLine;
        this.javaFile = javaFile;
        this.text = text;
        this.commentType = commentType;
    }

    private boolean isLastCharacter(String string, char character){
        return string.charAt(string.length() - 1) == character;
    }

    public DatabaseCommentStrategy applyOnComment(DatabaseCommentStrategy strategy) throws IOException, SQLException {
        if((text != null) && !text.isEmpty()){
            if((text.indexOf('.') >= 0) && !isLastCharacter(text, '.')) {
                return strategy.apply(JavaInlineTagParser.fetchColumn(text));
            }

            return strategy.apply(JavaInlineTagParser.fetchTable(text));
        } else {
            Optional<Table> table = JavaAnnotationParser.fetchTableFromEntityAnnotation(startCommentLine, javaFile);

            if(table.isPresent()) {
                if (CommentType.FIELD.equals(this.commentType)) {
                    Optional<Column> column = JavaAnnotationParser.fetchColumnFromColumnAnnotation(table.get(), startCommentLine, javaFile);

                    if (column.isPresent()) {
                        return strategy.apply(column.get());
                    }
                } else if (CommentType.CLASS.equals(this.commentType) || CommentType.INTERFACE.equals(this.commentType)) {
                    return strategy.apply(table.get());
                }
            }
        }

        return strategy;
    }
}
