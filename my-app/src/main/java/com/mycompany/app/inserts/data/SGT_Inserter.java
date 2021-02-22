package com.mycompany.app.inserts.data;

import java.sql.*;

public class SGT_Inserter {
    Connection connection;
    public SGT_Inserter(Connection connection){
        this.connection = connection;
    }

    public void v_createSGT_Tutorials_table() throws SQLException {
        String sql112 = "CREATE TABLE `SGT_table` ( " +
                "  `name` varchar(100) DEFAULT NULL, " +
                "  `inside_code` int unsigned NOT NULL, " +
                "  `abreviation` varchar(10) DEFAULT NULL, " +
                "  `year` int DEFAULT NULL, " +
                "  `department` varchar(30) DEFAULT NULL, " +
                "  `description` varchar(200) DEFAULT NULL, " +
                "  `additional_info` varchar(200) DEFAULT NULL, " +
                "  `academic_year` varchar(9) DEFAULT NULL, " +
                "  `faculty` varchar(60) DEFAULT NULL, " +
                "  PRIMARY KEY (`inside_code`) " +
                ") ";
        Statement statement = connection.createStatement();
        statement.executeUpdate(sql112);
        statement.close();
    }

    public void v_populateSGT_table(String sTableName) throws SQLException {
        String sql89 = "SELECT name, inside_code, abreviation, year,department, description, academic_year,faculty FROM " + sTableName;
        String sql78_insert = "INSERT INTO SGT_table(name, inside_code, abreviation, year, department, description, academic_year, faculty) " +
                "VALUE(?,?,?,?,?,?,?,?)";
        Statement statement = connection.createStatement();
        ResultSet rst = statement.executeQuery(sql89);
        PreparedStatement statementPrep = connection.prepareStatement(sql78_insert);
        while (rst.next()){
            statementPrep.setString(1,rst.getString("name"));
            statementPrep.setInt(2,rst.getInt( "inside_code"));
            statementPrep.setString(3, rst.getString("abreviation"));
            statementPrep.setInt(4,rst.getInt("year"));
            statementPrep.setString(5, rst.getString("department"));
            statementPrep.setString(6, rst.getString("description"));
            statementPrep.setString(7, rst.getString("academic_year"));
            statementPrep.setString(8, rst.getString("faculty"));
            statementPrep.execute();
        }
        rst.close();
        statement.close();
        statementPrep.close();
    }

    public void v_dropSGT_Tutorials_table() throws SQLException{
        String sql99 = "DROP TABLE IF EXISTS SGT_table";
        Statement statement = connection.createStatement();
        statement.executeUpdate(sql99);
        statement.close();
    }
}

