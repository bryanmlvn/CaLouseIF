package util;

import java.sql.*;

public class Connect {
    
    private final String USERNAME = "root";
    private final String PASSWORD = "";
    private final String DATABASE = "CaLouseIF";
    private final String HOST = "localhost:3306";
    private final String CONNECTION = String.format("jdbc:mysql://%s/%s", HOST, DATABASE);
    private Connection con;
    private Statement st;
    private static Connect connect;
    public ResultSet rs;
    public ResultSetMetaData rsm;
    
    // Singleton pattern to ensure a single instance
    public static Connect getInstance() {
        if (connect == null) {
            connect = new Connect();
        }
        return connect;
    }

    private Connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");  // Updated for MySQL 8.x JDBC driver
            con = DriverManager.getConnection(CONNECTION, USERNAME, PASSWORD);
            st = con.createStatement();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // This method returns the existing Statement object
    public Statement getStatement() {
        return st;
    }

    // Executes a query and returns the ResultSet
    public ResultSet execQuery(String query) {
        try {
            rs = st.executeQuery(query);
            rsm = rs.getMetaData();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }

    // Executes an update (INSERT, UPDATE, DELETE)
    public void execUpdate(String query) {
        try {
            st.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Returns a PreparedStatement for parameterized queries
    public PreparedStatement preparedStatement(String query) {
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ps;
    }
}