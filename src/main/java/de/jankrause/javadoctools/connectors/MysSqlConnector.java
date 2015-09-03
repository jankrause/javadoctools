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

import java.sql.*;
import java.util.ResourceBundle;

public class MysSqlConnector extends DbConnector {

    private Connection connection;

    private String schemaName;

    private String userName;

    private String password;

    private String host;

    public MysSqlConnector(ResourceBundle resourceBundle) {
        this.host = resourceBundle.getString("host");
        this.userName = resourceBundle.getString("user");
        this.password = resourceBundle.getString("password");
        this.schemaName = resourceBundle.getString("schema");
    }

    public void connect() throws SQLException {
        try{
            Class.forName("org.mariadb.jdbc.Driver").newInstance();
            connection = DriverManager.getConnection(
                    "jdbc:mariadb://" + host +"/" + schemaName,
                    userName,
                    password);
        }catch(ClassNotFoundException  ex){
            throw new RuntimeException("Could not resolve the MariaDB-connector in classpath.", ex);
        } catch(IllegalAccessException ex){
            throw new RuntimeException("Could not resolve the MariaDB-connector in classpath.", ex);
        } catch(InstantiationException ex){
            throw new RuntimeException("Could not resolve the MariaDB-connector in classpath.", ex);
        }
    }

    public void disconnect() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }

    public String fetchTablesComment(Table table) throws SQLException {
        String sql = "SELECT TABLE_NAME, TABLE_COMMENT FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = ? AND TABLE_NAME = ?;";

        if(connection.isClosed()){
            connect();
        }

        PreparedStatement stmnt = null;
        ResultSet resultSet = null;

        try {
            stmnt = connection.prepareStatement(sql);
            stmnt.setString(1, schemaName);
            stmnt.setString(2, table.name());
            resultSet = stmnt.executeQuery();

            if (resultSet.first()) {
                return resultSet.getString(2);
            } else {
                return "No Table with name " + table.name() + " found.";
            }
        } finally {
            if (stmnt != null) {
                stmnt.close();
            }

            if (resultSet != null) {
                resultSet.close();
            }
        }
    }

    public String fetchColumnsComment(Column column) throws SQLException {
        String sql = "SELECT COLUMN_COMMENT FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = ? AND TABLE_NAME = ? AND COLUMN_NAME = ?;";

        if(!connection.isClosed()) {
            connect();
        }

        PreparedStatement stmnt = null;

        try {
            stmnt = connection.prepareStatement(sql);
            stmnt.setString(1, schemaName);
            stmnt.setString(2, column.table().name());
            stmnt.setString(3, column.name());
            ResultSet resultSet = stmnt.executeQuery();

            if (resultSet.first()) {
                return resultSet.getString(1);
            } else {
                return "No column with name " + column.name() + " found.";
            }
        } finally {
            if(stmnt != null){
                stmnt.close();
            }
        }
    }

    public void comment(Table table, String comment) throws SQLException {
        String sql = "ALTER TABLE " + schemaName + "." + table.name() + " COMMENT = '"+ comment +"';";

        if(!connection.isClosed()) {
            connect();
        }

        PreparedStatement stmnt = null;

        try {
            stmnt = connection.prepareStatement(sql);
            stmnt.executeQuery();
        } finally {
            if(stmnt != null){
                stmnt.close();
            }
        }
    }

    private String fecthColumnsDatatype(Column column) throws SQLException {
        String sql = "SELECT DATA_TYPE FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = ? AND TABLE_NAME = ? AND COLUMN_NAME = ?;";
        PreparedStatement stmnt = null;
        System.out.print(sql);

        try {
            stmnt = connection.prepareStatement(sql);
            stmnt.setString(1, schemaName);
            stmnt.setString(2, column.table().name());
            stmnt.setString(3, column.name());
            ResultSet resultSet = stmnt.executeQuery();

            if (resultSet.first()) {
                return resultSet.getString(1);
            } else {
                return "No column with name " + column.name() + " found.";
            }
        } finally {
            if(stmnt != null){
                stmnt.close();
            }
        }

    }

    public void comment(Column column,String comment) throws SQLException {
        String sql = "ALTER TABLE " + schemaName + "." + column.table().name() + " CHANGE COLUMN " + column.name() + " " + column.name() + " " + fecthColumnsDatatype(column) + " COMMENT '"+ comment +"';";
        System.out.print(sql);
        if(!connection.isClosed()) {
            connect();
        }

        PreparedStatement stmnt = null;

        try {
            stmnt = connection.prepareStatement(sql);
            stmnt.executeQuery();
        } finally {
            if(stmnt != null){
                stmnt.close();
            }
        }
    }

}
