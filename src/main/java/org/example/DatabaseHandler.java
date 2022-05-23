package org.example;

import java.sql.*;
import java.util.List;

import static java.lang.System.exit;

public class DatabaseHandler {
    static final String DB_URL = "jdbc:mysql://localhost:3306/wines";
    static final String USER = "root";
    static final String PASS = "password";
    public void init(List<Product> list){

        create();
        access();
        table();
        insert(list);
        //truncate();

        select();




    }

    public void create(){
        try(Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306", USER, PASS);
            Statement stmt = conn.createStatement();
        ) {
            String sql = "CREATE DATABASE IF NOT EXISTS wines";
            stmt.executeUpdate(sql);
            System.out.println("Database created successfully...");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void access(){
        try {
            //DEPRECATED Class.forName("com.mysql.jdbc.Driver");
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/wines",
                    "root",
                    "password");
        } catch (Exception e) {
            System.out.println(e);
            exit(-1);
        }
    }

    public void table(){
        try(Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            Statement stmt = conn.createStatement();
        ) {
            String sql = "CREATE TABLE IF NOT EXISTS winestable " +
                    "(id INTEGER not NULL, " +
                    " name VARCHAR(255), " +
                    " price double, " +
                    " type VARCHAR(255), " +
                    " PRIMARY KEY ( id ))";

            stmt.executeUpdate(sql);
            System.out.println("Created table in given database...");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    public void insert( List<Product> list) {
        String SQL = "INSERT INTO winestable(id,name,price,type) "
                + "VALUES(?,?,?,?)";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS); PreparedStatement statement = conn.prepareStatement(SQL);) {
            int count = 0;

            for (Product products : list) {
                statement.setFloat(1, products.getId());
                statement.setString(2, products.getName());
                statement.setDouble(3, products.getPrice());
                statement.setString(4, products.getType());

                statement.addBatch();
                count++;
                // execute every 100 rows or less
                if (count % 100 == 0 || count == list.size()) {
                    statement.executeBatch();
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void select(){
        try {
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("select * from winestable");
            while (rs.next())
                System.out.println(rs.getInt(1) + "  " + rs.getString(2) + "  " + rs.getString(3)+ "  " + rs.getString(4));
            conn.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void truncate(){
        Statement statement=null ;
        try {
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            statement = conn.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            int result = statement.executeUpdate("TRUNCATE winestable" );
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
