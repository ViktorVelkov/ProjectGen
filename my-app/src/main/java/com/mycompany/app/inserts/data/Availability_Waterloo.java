package com.mycompany.app.inserts.data;

import java.sql.*;

public class Availability_Waterloo {
        private Connection connection;

        public Availability_Waterloo(Connection connection){
            this.connection = connection;
        }

        private void v_insert_Waterloo(int itime, String sHall, int iAvail) throws SQLException {
            String sql2 = "INSERT INTO CURICULUM.availability_halls_waterloo VALUES(?, ?, ?)";
            PreparedStatement preparedStmt = this.connection.prepareStatement(sql2);
            preparedStmt.setInt(1, itime);
            preparedStmt.setString(2, sHall);
            preparedStmt.setInt(3, iAvail);

            preparedStmt.execute();
            preparedStmt.close();
        }

        private void v_insert_Waterloo_tutorials(int itime, String sHall, int iAvail) throws SQLException {
        String sql2 = "INSERT INTO CURICULUM.availability_halls_waterloo_tutorials VALUES(?, ?, ?)";
        PreparedStatement preparedStmt = this.connection.prepareStatement(sql2);
        preparedStmt.setInt(1, itime);
        preparedStmt.setString(2, sHall);
        preparedStmt.setInt(3, iAvail);

        preparedStmt.execute();
        preparedStmt.close();
    }


    public void v_populate(String sWaterloo) throws SQLException {
                Statement stmt = connection.createStatement();
                String sql3 = "SELECT hour FROM curiculum.timeslots";
                ResultSet rs3 = stmt.executeQuery(sql3);
                while(rs3.next()) {
                    v_insert_Waterloo(rs3.getInt("hour"), sWaterloo , 1);
                }
                rs3.close();
        }

        public void v_populate_tutorials(String sWaterloo) throws SQLException {
            Statement stmt = connection.createStatement();
            String sql3 = "SELECT hour FROM curiculum.timeslots";
            ResultSet rs3 = stmt.executeQuery(sql3);
            while(rs3.next()) {
                v_insert_Waterloo_tutorials(rs3.getInt("hour"), sWaterloo , 1);
            }
            rs3.close();
        }


        public void v_update_waterloo_to_Zero(int itime) throws SQLException {
            String s=String.valueOf(itime);
            String sql = "UPDATE CURICULUM.availability_halls_waterloo " +
                    "     SET available = 0 " +
                    "     WHERE hour = " + s;


            PreparedStatement preparedStmt = connection.prepareStatement(sql);
            preparedStmt.execute();
            preparedStmt.close();
        }

        public void v_update_waterloo_to_One(int itime) throws SQLException {
            String s=String.valueOf(itime);
            String sql = "UPDATE CURICULUM.availability_halls_waterloo " +
                "     SET available = 1 " +
                "     WHERE hour = " + s;


            PreparedStatement preparedStmt = connection.prepareStatement(sql);
            preparedStmt.execute();
            preparedStmt.close();
        }

        public boolean b_checkAvailableAt(int itime) throws SQLException {
            String s=String.valueOf(itime);
            Statement stmt = connection.createStatement();
            String sql = "SELECT available " +
                    "     FROM curiculum.timeslots " +
                    "     WHERE  hour = " + s;


            ResultSet rs = stmt.executeQuery(sql);
            stmt.close();
            if(rs.getInt("available") == 1){
                rs.close();
                return true;
            }
            else{ rs.close(); return  false; }
        }


        public void v_update_waterloo_to_Zero_tutorials(int itime) throws SQLException {
            String s=String.valueOf(itime);
            String sql = "UPDATE CURICULUM.availability_halls_waterloo_tutorials " +
                    "     SET available = 0 " +
                    "     WHERE hour = " + s;


            PreparedStatement preparedStmt = connection.prepareStatement(sql);
            preparedStmt.execute();
            preparedStmt.close();
        }

        public void v_update_waterloo_to_One_tutorials(int itime) throws SQLException {
            String s=String.valueOf(itime);
            String sql = "UPDATE CURICULUM.availability_halls_waterloo_tutorials " +
                    "     SET available = 1 " +
                    "     WHERE hour = " + s;


            PreparedStatement preparedStmt = connection.prepareStatement(sql);
            preparedStmt.execute();
            preparedStmt.close();
        }

        public boolean b_checkAvailableAt_tutorials(int itime) throws SQLException {
            String s=String.valueOf(itime);
            Statement stmt = connection.createStatement();
            String sql = "SELECT available " +
                    "     FROM curiculum.timeslots " +
                    "     WHERE  hour = " + s;


            ResultSet rs = stmt.executeQuery(sql);
            stmt.close();
            if(rs.getInt("available") == 1){
                rs.close();
                return true;
            }
            else{ rs.close(); return  false; }
        }


    /**/

        public void v_create_availability_halls() throws SQLException {
            String sql99 = "DROP TABLE IF EXISTS availability_halls_waterloo";
            String sql87 =
                        "CREATE TABLE `availability_halls_waterloo` (\n" +
                        "  `hour` int NOT NULL,\n" +
                        "  `hall` varchar(10) NOT NULL,\n" +
                        "  `available` int DEFAULT NULL,\n" +
                        "  PRIMARY KEY (`hour`,`hall`),\n" +
                        "  CONSTRAINT `availability_halls_waterloo_wafk_11` FOREIGN KEY (`hour`) REFERENCES `timeslots` (`Hour`)\n" +
                        ") ";
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql99);
            statement.executeUpdate(sql87);
            statement.close();

        }
        public void v_create_availability_halls_tutorials() throws SQLException {
            String sql99 = "DROP TABLE IF EXISTS availability_halls_waterloo_tutorials";
            String sql87 =
                        "CREATE TABLE `availability_halls_waterloo_tutorials` (\n" +
                        "  `hour` int NOT NULL,\n" +
                        "  `hall` varchar(10) NOT NULL,\n" +
                        "  `available` int DEFAULT NULL,\n" +
                        "  PRIMARY KEY (`hour`,`hall`),\n" +
                        "  CONSTRAINT `availability_halls_waterloo_wafk_tutorials_11` FOREIGN KEY (`hour`) REFERENCES `timeslots` (`Hour`)\n" +
                        ") ";
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql99);
            statement.executeUpdate(sql87);
            statement.close();

        }
    /**/

        public void v_update_twoWeeksTableToZero(String sHallName, int iDate, int iMonth, int iYear, int iTimeStart, int iTimeEnd) throws SQLException {
            String sql11 =  "UPDATE two_weeks_availability_halls_waterloo " +
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
            String sql11 =  "UPDATE two_weeks_availability_halls_waterloo " +
                            "SET AVAILABLE = 1 " +
                            "WHERE HALL = '" + sHallName + "' " +
                            "AND DATE = " + Integer.toString(iDate) + " " +
                            "AND MONTH = " + Integer.toString(iMonth) + " " +
                            "AND YEAR = " + Integer.toString(iYear) + " " +
                            "AND HOUR BETWEEN " + Integer.toString(iTimeStart) + " AND " + Integer.toString(iTimeEnd);
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql11);
            statement.close();    }

        public boolean v_check_twoWeeks_Availability(String sHallName,int iDate, int iMonth, int iYear, int iTimeStart, int iTimeEnd) throws SQLException {

            int iResult = 0;
            int iCount = 0;
            String sql11 =  "SELECT AVAILABLE " +
                    "FROM two_weeks_availability_halls_waterloo " +
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


    /**/

        public void v_placeReedingWeek(int iTimeStart, int iTimeEnd, int iMonth) throws SQLException {
            String sql11 =  "UPDATE two_weeks_availability_halls_waterloo " +
                            "SET AVAILABLE = 0" +
                            "WHERE DATE BETWEEN " + Integer.toString(iTimeStart) + " AND " + Integer.toString(iTimeEnd) + " " +
                            "AND MONTH = " + Integer.toString(iMonth);
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql11);
            statement.close();
        }

        public void v_removeReedingWeek(int iTimeStart, int iTimeEnd, int iMonth) throws SQLException {
            String sql11 =  "UPDATE two_weeks_availability_halls_waterloo " +
                    "SET AVAILABLE = 1" +
                    "WHERE DATE BETWEEN " + Integer.toString(iTimeStart) + " AND " + Integer.toString(iTimeEnd) + " " +
                    "AND MONTH = " + Integer.toString(iMonth);
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql11);
            statement.close();
        }


//End of Class

}
