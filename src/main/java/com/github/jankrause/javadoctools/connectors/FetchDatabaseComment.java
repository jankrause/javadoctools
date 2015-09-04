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
package com.github.jankrause.javadoctools.connectors;

import java.sql.SQLException;

import com.github.jankrause.javadoctools.domain.Column;
import com.github.jankrause.javadoctools.domain.Table;

public class FetchDatabaseComment implements DatabaseCommentStrategy {

    private DbConnector connector;

    private String comment = "<MISSING COMMENT>";

    public FetchDatabaseComment with(DbConnector connector) {
        this.connector = connector;
        return this;
    }

    public FetchDatabaseComment apply(Table table) throws SQLException {
        this.comment = connector.fetchTablesComment(table);
        return this;
    }

    public FetchDatabaseComment apply(Column column)  throws SQLException  {
        this.comment = connector.fetchColumnsComment(column);
        return this;
    }

    public String fetch() {
        return this.comment;
    }
}
