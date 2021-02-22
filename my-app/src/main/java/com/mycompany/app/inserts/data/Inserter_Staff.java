package com.mycompany.app.inserts.data;

import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.SQLException;

public class Inserter_Staff {

    private Connection connection;
    private static String[][] sStashedNames = {
            {"Jerry", "Klein"},
            {"Austin", "Hutchinson"},
            {"Shame", "Cross"},
            {"Floyd", "Holt"},
            {"Harley", "Hawkins"},
            {"Howard", "Horn"},
            {"Edward","Anderson"},
            {"Stephen", "Gregory"},
            {"Hamzah","Weaver"},
            {"Willie","Shepherd"},
            {"Barbara", "Kane"},
            {"Catherine", "Gordon"},
            {"Grace","Mcintyre"},
            {"Hazel", "Nicolson"},
            {"Florence","Conway"},
            {"Heather", "Wiggins"},
            {"Aidan","Bradford"},
            {"Kyla", "McCann"},
            {"Patricia","Gill"},
            {"Aaliyah","White"},
            {"Alana", "Mcbride"},
            {"Eve", "Underwood"}
    };


    public  Inserter_Staff(Connection connection){
        this.connection = connection;
    }

    void v_insertStatement(int iteachers_id, String sfirst_name, String slast_name, int ilecturer, String sCourse, double heuristics, int iYearsTeaching) throws SQLException {
        String sql2 = "INSERT INTO CURICULUM.STAFF VALUES (?,?,?,?,?,?,?)";
        PreparedStatement preparedStmt = connection.prepareStatement(sql2);
        preparedStmt.setInt(1, iteachers_id);
        preparedStmt.setString(2, sfirst_name);
        preparedStmt.setString(3, slast_name );
        preparedStmt.setInt(4, ilecturer);
        preparedStmt.setString(5, sCourse );
        preparedStmt.setDouble(6, heuristics);
        preparedStmt.setInt(7, iYearsTeaching);



        preparedStmt.execute();

    }

    void v_truncateStaffTable() throws SQLException {
        String sql2 = "TRUNCATE TABLE CURICULUM.STAFF";
        PreparedStatement preparedStmt = connection.prepareStatement(sql2);
        preparedStmt.execute();

    }

    void v_populate() throws SQLException {
        Statement stmt = connection.createStatement();
        String sql2 = "SELECT abreviation FROM curiculum.s_courses";
        ResultSet rs2 = stmt.executeQuery(sql2);

        int counterA = 0;
        while(rs2.next()){
            int random_int = (int)(Math.random() * (15 - 1 + 1) + 1);
            double heuristics = random_int/2 + 1;
            v_insertStatement((counterA + 1)*100, sStashedNames[counterA][0],sStashedNames[counterA][1],1,rs2.getString("abreviation"),heuristics, random_int);
            counterA++;
        }
        rs2.close();
    }

}
