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
package de.jankrause.javadoctools.connectors;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import org.junit.Test;
import org.mockito.Mockito;

import de.jankrause.javadoctools.domain.CommentType;
import de.jankrause.javadoctools.domain.Table;
import de.jankrause.javadoctools.utils.DbCommentProcessor;

public class FetchDatabaseCommentTest {

    @Test
    public void shouldFindTheTableAnyway()throws IOException, SQLException {
        Table testComments = new Table("test_comments");
        DbConnector dbConnector = Mockito.mock(DbConnector.class);
        Mockito.when(dbConnector.fetchTablesComment(testComments)).
                thenReturn("Comment for table test_comments");

        DbCommentProcessor fetcher = new DbCommentProcessor(26, new File("src/test/resources/testpackage/AnnotatedEntity.java"), "", CommentType.INTERFACE);
        assertEquals("Comment for table test_comments",
                fetcher.applyOnComment(new FetchDatabaseComment().with(dbConnector)).fetch());

        fetcher = new DbCommentProcessor(26, new File("src/test/resources/testpackage/AnnotatedEntity.java"), "", CommentType.CLASS);
        assertEquals("Comment for table test_comments",
                fetcher.applyOnComment(new FetchDatabaseComment().with(dbConnector)).fetch());

        fetcher = new DbCommentProcessor(-100, new File("src/test/resources/testpackage/AnnotatedEntity.java"), "", CommentType.CLASS);
        assertEquals("Comment for table test_comments",
                fetcher.applyOnComment(new FetchDatabaseComment().with(dbConnector)).fetch());
    }

    @Test
    public void shouldNotFindTheTableAnyway()throws IOException, SQLException {
        DbConnector dbConnector = Mockito.mock(DbConnector.class);

        DbCommentProcessor fetcher = new DbCommentProcessor(26, new File("src/test/resources/testpackage/MyTermDefinition.java"), "", CommentType.INTERFACE);
        assertEquals("<MISSING COMMENT>",
                fetcher.applyOnComment(new FetchDatabaseComment().with(dbConnector)).fetch());

        fetcher = new DbCommentProcessor(-100, new File("src/test/resources/testpackage/MyTermDefinition.java"), "", CommentType.CLASS);
        assertEquals("<MISSING COMMENT>",
                fetcher.applyOnComment(new FetchDatabaseComment().with(dbConnector)).fetch());
    }

}
