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
            preparedStmt.close();
        }

        public void v_populate(String sBushHall) throws SQLException {
                Statement stmt = connection.createStatement();
                String sql3 = "SELECT hour FROM curiculum.timeslots";
                ResultSet rs3 = stmt.executeQuery(sql3);
                while(rs3.next()) {
                    v_insert_BushHall(rs3.getInt("hour"), sBushHall , 1);
                }
                rs3.close();
                stmt.close();
        }

        /////////////////////////////////////----------------------------------------/////////////////////////////////////


        public void v_update_twoWeeksTableToZero(String sHallName, int iDate, int iMonth, int iYear, int iTimeStart, int iTimeEnd) throws SQLException {
            String sql11 =  "UPDATE two_weeks_availability_halls_bush_house " +
                    "SET AVAILABLE = 0 " +
                    "WHERE HALL = '" + sHallName + "' " +
                    "AND DATE = " + Integer.toString(iDate) + " " +
                    "AND MONTH = " + Integer.toString(iMonth) + " " +
                    "AND YEAR = " + Integer.toString(iYear) + " " +
                    "AND HOUR BETWEEN " + Integer.toString(iTimeStart) + " AND " + Integer.toString(iTimeEnd);
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql11);
            statement.close();
        }


        public void v_update_twoWeeksTableToOne(String sHallName,int iDate, int iMonth, int iYear, int iTimeStart, int iTimeEnd) throws SQLException {
            String sql11 =  "UPDATE two_weeks_availability_halls_bush_house " +
                    "SET AVAILABLE = 1 " +
                    "WHERE HALL = '" + sHallName + "' " +
                    "AND DATE = " + Integer.toString(iDate) + " " +
                    "AND MONTH = " + Integer.toString(iMonth) + " " +
                    "AND YEAR = " + Integer.toString(iYear) + " " +
                    "AND HOUR BETWEEN " + Integer.toString(iTimeStart) + " AND " + Integer.toString(iTimeEnd);
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql11);
            statement.close();
        }


    public boolean v_check_twoWeeks_Availability(String sHallName,int iDate, int iMonth, int iYear, int iTimeStart, int iTimeEnd) throws SQLException {

        int iResult = 0;
        int iCount = 0;
        String sql11 =  "SELECT AVAILABLE " +
                "FROM two_weeks_availability_halls_bush_house " +
                "WHERE HALL = '" + sHallName + "' " +
                "AND DATE = " + Integer.toString(iDate) + " " +
                "AND MONTH = " + Integer.toString(iMonth) + " " +
                "AND YEAR = " + Integer.toString(iYear) + " " +
                "AND HOUR BETWEEN " + Integer.toString(iTimeStart) + " AND " + Integer.toString(iTimeEnd);
        Statement statement = connection.createStatement();
        ResultSet resultSet= statement.executeQuery(sql11);

        while(resultSet.next()){
            iResult += resultSet.getInt("AVAILABLE");
            iCount++;
        }

        resultSet.close();
        statement.close();
        return ((iResult == iCount)?true:false);
    }

    /////////////////////////////////////----------------------------------------/////////////////////////////////////

        public void v_placeReedingWeek(int iTimeStart, int iTimeEnd, int iMonth) throws SQLException {
                String sql11 =  "UPDATE two_weeks_availability_halls_bush_house " +
                                "SET AVAILABLE = 0" +
                                "WHERE DATE BETWEEN " + Integer.toString(iTimeStart) + " AND " + Integer.toString(iTimeEnd) + " " +
                                "AND MONTH = " + Integer.toString(iMonth);
                Statement statement = connection.createStatement();
                statement.executeUpdate(sql11);
                statement.close();
            }


        public void v_removeReedingWeek(int iTimeStart, int iTimeEnd, int iMonth) throws SQLException {
            String sql11 =  "UPDATE two_weeks_availability_halls_bush_house " +
                            "SET AVAILABLE = 1" +
                            "WHERE DATE BETWEEN " + Integer.toString(iTimeStart) + " AND " + Integer.toString(iTimeEnd) + " " +
                            "AND MONTH = " + Integer.toString(iMonth);
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql11);
            statement.close();
        }
    /////////////////////////////////////----------------------------------------/////////////////////////////////////

}
