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
package de.jankrause.javadoctools.traversers;

import com.sun.javadoc.Tag;
import de.jankrause.javadoctools.connectors.DbConnector;
import de.jankrause.javadoctools.domain.Definition;
import de.jankrause.javadoctools.taglets.ImportFromDbTaglet;
import de.jankrause.javadoctools.utils.JavaCommentUtil;

import java.util.*;

public class DefinitionCommentVisitor implements ClassDocVisitor {

    private List<Definition> definitions;
    private DbConnector dbConnector;

    public DefinitionCommentVisitor(DbConnector dbConnector){
        this.definitions = new LinkedList<Definition>();
        this.dbConnector = dbConnector;
    }

    private Optional<String> fetchCommentsFromDb(Tag importFromDb){
        ImportFromDbTaglet taglet = new ImportFromDbTaglet(this.dbConnector);
        return Optional.ofNullable(taglet.toString(importFromDb));
    }

    public void visitClassComment(Tag classComment) {
        String definitionText = JavaCommentUtil.extractIntroductionText(classComment.holder().getRawCommentText());

        if(definitionText.equals("{@" + ImportFromDbTaglet.NAME + "}")){
            Definition definition = new Definition(classComment.holder().name(), fetchCommentsFromDb(classComment).get());
            definitions.add(definition);
        }
        else if(!definitionText.isEmpty()) {
            Definition definition = new Definition(classComment.holder().name(), definitionText);
            definitions.add(definition);
        }
    }

    public List<Definition> definitions(){
        return this.definitions;
    }
}
