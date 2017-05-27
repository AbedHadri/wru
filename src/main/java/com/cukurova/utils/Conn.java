package com.cukurova.utils;

import java.sql.DriverManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

public class Conn {

    private final String urlKey = "jdbc:mysql://localhost:3306/wru_database?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true";//&useSSL=false
    private final String className = "com.mysql.jdbc.Driver";
    private final String dbusername = "rootwru";//"client";
    private final String dbpassword = "123123";

    private static Connection connection;

    private PreparedStatement onHoldPs;

    public Conn() {

    }

    public final String startConnectionDebugger() throws SQLException {
        if (connection == null) {

            try {
                Class.forName(className);
                connection = DriverManager.getConnection(urlKey, dbusername, dbpassword);
            } catch (SQLException e) {
                return "An error occured while connecting!\nError message:\n. " + e.getMessage();
            } catch (ClassNotFoundException c) {
                return " Error! \nError message: " + c.getMessage();
            }
            connection.close();
            return "connection succeeded!";

        } else {
            connection.close();
            return "Connection established and database pinged successfully.";
        }
    }

    public final boolean startConnection() {
        if (connection == null) {
            try {
                Class.forName(className);
                connection = DriverManager.getConnection(urlKey, dbusername, dbpassword);
            } catch (SQLException | ClassNotFoundException e) {
                return false;
            }
            return true;
        } else {
            return true;
        }
    }

    public java.sql.Connection getConnKey() {

        return Conn.connection;
    }

    public boolean sqlExecuteInsert(String sql, Object... variable) throws SQLException {
        int i = 1;
        startConnection();
        PreparedStatement pstmt = connection.prepareStatement(sql);

        for (Object variable1 : variable) {

            pstmt.setObject(i, variable1);

            i++;
        }
        return pstmt.execute();//returns true if there are rows returned and false if no row returned

    }

    public ResultSet sqlExecuteSelect(String sql, Object... variable) throws SQLException {
        int i = 1;
        startConnection();
        PreparedStatement pstmt = connection.prepareStatement(sql);
        for (Object variable1 : variable) {
            pstmt.setObject(i, variable1);
            i++;
        }
        return pstmt.executeQuery();

    }

    public ResultSet pullSqlList(String sql, List<String> variable) throws SQLException {
        int i = 1;
        startConnection();
        PreparedStatement pstmt = connection.prepareStatement(sql);
        for (Object variable1 : variable) {
            pstmt.setObject(i, variable1);
            i++;
        }
        return pstmt.executeQuery();

    }

    public int sqlExecuteUpdate(String sql, Object... variable) throws SQLException {
        int i = 1;
        startConnection();
        PreparedStatement pstmt = connection.prepareStatement(sql);
        for (Object variable1 : variable) {
            pstmt.setObject(i, variable1);
            i++;
        }
        int eri = pstmt.executeUpdate();
        connection.close();
        return eri;
    }

    public void initBatch(String sql) throws SQLException {
        startConnection();
        onHoldPs = connection.prepareStatement(sql);
    }

    public void addToBatch(Object... params) throws SQLException {
        int i = 1;
        for (Object param : params) {
            onHoldPs.setObject(i, param);
            i++;
        }
        onHoldPs.addBatch();
    }

    public void executeBatch() throws SQLException {
        onHoldPs.executeBatch();
        onHoldPs.close();
    }

}
