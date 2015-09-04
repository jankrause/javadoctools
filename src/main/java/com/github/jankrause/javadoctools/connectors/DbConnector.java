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

import org.apache.commons.io.IOUtils;

import com.github.jankrause.javadoctools.domain.Column;
import com.github.jankrause.javadoctools.domain.Table;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

public abstract class DbConnector {

    public static final String DB_PROPERTY_FILE = "database.properties";

    private static ResourceBundle createResourceBundle() {
        InputStream resourcesFileAsStream = null;

        try {
            resourcesFileAsStream = new FileInputStream(DB_PROPERTY_FILE);
            return new PropertyResourceBundle(resourcesFileAsStream);
        } catch (IOException ioEx) {
            throw new RuntimeException("Could not read the expected resource-file " + DB_PROPERTY_FILE, ioEx);
        } finally {
            IOUtils.closeQuietly(resourcesFileAsStream);
        }
    }

    public static DbConnector createConnector() {
        return createConnector(createResourceBundle());
    }

    public static DbConnector createConnector(ResourceBundle connectorConfiguration) {
        String dbms = connectorConfiguration.getString("dbms");
        if("mysql".equals(dbms)) {
            return new MysSqlConnector(connectorConfiguration);
        } else {
            throw new RuntimeException("Unknown dbms");
        }
    }

    public abstract void connect () throws SQLException;

    public abstract void disconnect() throws SQLException;

    public abstract String fetchTablesComment(Table table) throws SQLException;

    public abstract String fetchColumnsComment(Column column) throws SQLException;

    public abstract void comment(Table table, String comment) throws SQLException;

    public abstract void comment(Column column, String comment) throws SQLException;
}
