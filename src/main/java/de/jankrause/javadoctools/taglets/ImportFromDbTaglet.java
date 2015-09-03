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
package de.jankrause.javadoctools.taglets;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

import com.sun.javadoc.Tag;
import com.sun.tools.doclets.Taglet;

import de.jankrause.javadoctools.connectors.DbConnector;
import de.jankrause.javadoctools.connectors.FetchDatabaseComment;
import de.jankrause.javadoctools.domain.CommentType;
import de.jankrause.javadoctools.utils.CommentTypeUtil;
import de.jankrause.javadoctools.utils.DbCommentProcessor;

// TODO: Write code documentation and Readme.md
// TODO: Share in github and implement CI
public final class ImportFromDbTaglet implements Taglet {

    public static final String NAME = "importFromDb";
    private DbConnector dbConnector;

    public ImportFromDbTaglet(DbConnector dbConnector){
        this.dbConnector = dbConnector;
    }

    public static void register (Map tagletMap) {
        ImportFromDbTaglet taglet = new ImportFromDbTaglet(DbConnector.createConnector());

        if (tagletMap.containsKey(taglet.getName())) {
            tagletMap.remove(taglet.getName());
        }

        tagletMap.put(taglet.getName(), taglet);
    }

    public boolean inField() {return true;}

    public boolean inConstructor() {return false;}

    public boolean inMethod() {
        return true;
    }

    public boolean inOverview() {
        return true;
    }

    public boolean inPackage() {
        return false;
    }

    public boolean inType() {
        return false;
    }

    public boolean isInlineTag() {return true;}

    public String getName() {return NAME;}

    public String toString(Tag tag) {
        try {
            dbConnector.connect();
            int startCommentLine = tag.position().line();
            File javaFile = tag.position().file();
            String text = tag.text();
            CommentType type = CommentTypeUtil.deriveCommentType(tag);

            return new DbCommentProcessor(startCommentLine, javaFile, text, type)
                    .applyOnComment(new FetchDatabaseComment().with(dbConnector))
                    .fetch();
        } catch (IOException ioEx) {
            ioEx.printStackTrace();
            throw new RuntimeException("An error occured while reading a Java-source-file", ioEx);
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
            throw new RuntimeException("An error occured while fetching a comment from the database", sqlEx);
        } catch(Throwable t) {
            t.printStackTrace();
            throw new RuntimeException(t);
        } finally {
            try {
                dbConnector.disconnect();
            } catch (SQLException sqlEx) {
                throw new RuntimeException("An error occured while fetching a comment from the database", sqlEx);
            }
        }
    }

    public String toString(Tag[] tags) {
        return null;
    }
}
