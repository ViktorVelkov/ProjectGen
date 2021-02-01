package com.mycompany.app;

import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.SQLException;

public class Availability_Bush {
        private Connection connection;

        public Availability_Bush(Connection connection){
            this.connection = connection;
        }

        void v_insert_BushHall(int itime, String sHall, int iavail) throws SQLException {
            String sql2 = "INSERT INTO CURICULUM.availability_halls_bush_house VALUES(?, ?, ?)";
            PreparedStatement preparedStmt = this.connection.prepareStatement(sql2);
            preparedStmt.setInt(1, itime);
            preparedStmt.setString(2, sHall);
            preparedStmt.setInt(3, iavail);

            preparedStmt.execute();
        }

        void v_populate(String sBushHall) throws SQLException {
                Statement stmt = connection.createStatement();
                String sql3 = "SELECT hour FROM curiculum.timeslots";
                ResultSet rs3 = stmt.executeQuery(sql3);
                while(rs3.next()) {
                    v_insert_BushHall(rs3.getInt("hour"), sBushHall , 1);
                }
                rs3.close();
        }

        void v_update_Bush_to_Zero(int itime) throws SQLException {
            String s=String.valueOf(itime);
            String sql = "UPDATE CURICULUM.availability_halls_bush_house " +
                    "     SET available = 0 " +
                    "     WHERE hour = " + s;


            PreparedStatement preparedStmt = connection.prepareStatement(sql);
            preparedStmt.execute();
        }

        void v_update_Bush_to_One(int itime) throws SQLException {
            String s=String.valueOf(itime);
            String sql = "UPDATE CURICULUM.availability_halls_bush_house " +
                "     SET available = 1 " +
                "     WHERE hour = " + s;


            PreparedStatement preparedStmt = connection.prepareStatement(sql);
            preparedStmt.execute();
        }

        boolean b_checkAvailableAt(int itime) throws SQLException {
            String s=String.valueOf(itime);
            Statement stmt = connection.createStatement();
            String sql = "SELECT available " +
                    "     FROM curiculum.timeslots " +
                    "     WHERE  hour = " + s;


            ResultSet rs = stmt.executeQuery(sql);

            if(rs.getInt("available") == 1){
                rs.close();
                return true;
            }
            else{ rs.close(); return  false; }
        }
}
