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

import de.jankrause.javadoctools.domain.Column;
import de.jankrause.javadoctools.domain.Table;

import java.sql.SQLException;

public interface DatabaseCommentStrategy {

    public DatabaseCommentStrategy with(DbConnector connector);

    public DatabaseCommentStrategy apply(Table table) throws SQLException;


    public DatabaseCommentStrategy apply(Column column) throws SQLException ;

    public String fetch();
}
