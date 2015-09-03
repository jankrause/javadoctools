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

import de.jankrause.javadoctools.connectors.DbConnector;
import de.jankrause.javadoctools.connectors.FetchDatabaseComment;
import de.jankrause.javadoctools.domain.Column;
import de.jankrause.javadoctools.domain.CommentType;
import de.jankrause.javadoctools.domain.Table;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class ExternalCommandFetcherTest {

    @Test
    public void shouldFetchColumnCommentFromTagText() throws IOException, SQLException {
        DbCommentProcessor fetcher = new DbCommentProcessor(-1, null, "Tablename.Columnname", null);

        DbConnector mockedConnector = Mockito.mock(DbConnector.class);
        Mockito.when(mockedConnector.fetchColumnsComment(Mockito.<Column>any()))
                .thenReturn("Comment for column Columnname");

        FetchDatabaseComment strategy = new FetchDatabaseComment().with(mockedConnector);

        assertEquals("Comment for column Columnname", fetcher.applyOnComment(strategy).fetch());
    }

    @Test
    public void shouldFetchTableCommentFromTagText() throws IOException, SQLException {
        DbCommentProcessor fetcher = new DbCommentProcessor(-1, null, "Tablename", null);

        DbConnector mockedConnector = Mockito.mock(DbConnector.class);
        Mockito.when(mockedConnector.fetchTablesComment(Mockito.<Table>any()))
                .thenReturn("Comment for table Tablename");

        FetchDatabaseComment strategy = new FetchDatabaseComment().with(mockedConnector);

        assertEquals("Comment for table Tablename", fetcher.applyOnComment(strategy).fetch());
    }

    @Test
    public void shouldFetchColumnCommentFromAnnotation() throws IOException, SQLException {
        DbCommentProcessor fetcher = new DbCommentProcessor(15, new File("src/test/resources/testpackage/AnnotatedEntity.java"), "", CommentType.FIELD);

        DbConnector mockedConnector = Mockito.mock(DbConnector.class);
        Mockito.when(mockedConnector.fetchColumnsComment(Mockito.<Column>any()))
                .thenReturn("Comment for column name");

        FetchDatabaseComment strategy = new FetchDatabaseComment().with(mockedConnector);

        assertEquals("Comment for column name", fetcher.applyOnComment(strategy).fetch());

        DbConnector otherMockedConnector = Mockito.mock(DbConnector.class);
        Mockito.when(otherMockedConnector.fetchColumnsComment(Mockito.<Column>any()))
                .thenReturn("Comment for column id");

        FetchDatabaseComment otherStrategy = new FetchDatabaseComment().with(otherMockedConnector);

        fetcher = new DbCommentProcessor(21, new File("src/test/resources/testpackage/AnnotatedEntity.java"), "", CommentType.FIELD);
        assertEquals("Comment for column id", fetcher.applyOnComment(otherStrategy).fetch());
    }
}
