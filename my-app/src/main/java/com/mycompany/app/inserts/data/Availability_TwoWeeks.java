package com.mycompany.app.inserts.data;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

public class Availability_TwoWeeks {
    Connection connection;
    public Availability_TwoWeeks(Connection connection){
        this.connection = connection;
    }
    // Taken from https://howtodoinjava.com/java/date-time/dates-between-two-dates/

    public static List<Date> getDaysBetweenDates(Date startdate, Date enddate)
    {
        List<Date> dates = new ArrayList<Date>();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(startdate);

        while (calendar.getTime().before(enddate))
        {
            Date result = calendar.getTime();
            int a = result.getDay();                                //delete this after test
            if(result.getDay() != 6) {
                if (result.getDay() != 0) {
                    dates.add(result);
                }
            }
            calendar.add(Calendar.DATE, 1);

        }
        return dates;
    }

    public void v_generateTable_and_populate(String sTable, String sStartDate, String sEndDate) throws SQLException, ParseException {
        //1.start by assigning the lectures in the timeslots available from the week days
        //2.assign the students to the lectures following the simple rules of the hard constraints
        //3.
        // find a suitable layout of tutorials and lectures . Only Lectures first. since tutorials are not yet inserted
        int iCounter = 0;
        Statement statement = connection.createStatement();

        SimpleDateFormat formatter6=new SimpleDateFormat("dd-MMM-yyyy");
        Date date00 = (Date)formatter6.parse(sStartDate);
        Date date88 = (Date)formatter6.parse(sEndDate);

        List<Date> allDates = getDaysBetweenDates(date00,date88);

        String sql33 = "DROP TABLE IF EXISTS two_weeks_" + sTable;
        String sql88 =
                "CREATE TABLE two_weeks_" + sTable + "(" +
                        "AVAILABLE INT(1)," +
                        "HALL VARCHAR(10)," +
                        "HOUR INT(2), " +
                        "DAY VARCHAR(10)," +
                        "DATE INT(2)," +
                        "MONTH INT(2)," +
                        "YEAR INT(4))";
//
//        String sql44 = "SELECT AVAILABLE, HALL, HOUR FROM availability_halls_bush_house";
//        String sql55 = "SELECT AVAILABLE, HALL, HOUR FROM availability_halls_waterloo";
        String sql44 = "SELECT AVAILABLE, HALL, HOUR FROM " + sTable;
        String sql66 = "INSERT INTO two_weeks_"+ sTable + " (AVAILABLE, HALL, HOUR, DAY, DATE, MONTH, YEAR) VALUES( ?,?,?,?,?,?,? )";
        //String sql66 = "INSERT INTO halls_availability_tweeks(AVAILABLE, HALL, HOUR, DAY, DATE, MONTH, YEAR) VALUES( ?,?,?,? )";

        statement.executeUpdate(sql33);
        statement.executeUpdate(sql88);

        ResultSet rst1 = statement.executeQuery(sql44);
        PreparedStatement prst = connection.prepareStatement(sql66);
        while(!allDates.isEmpty()){

            while(rst1.next()){

                prst.setInt(1, rst1.getInt(1));
                prst.setString(2, rst1.getString("HALL"));
                prst.setInt(3, rst1.getInt(3));

                switch( allDates.get(iCounter).getDay() ) {
                    case 1: prst.setString(4, "Monday"); break;    // switch case needed here
                    case 2: prst.setString(4, "Tuesday"); break;
                    case 3: prst.setString(4, "Wednesday"); break;
                    case 4: prst.setString(4, "Thursday"); break;
                    case 5: prst.setString(4, "Friday"); break;
                }


                prst.setInt(5, (int)allDates.get(iCounter).getDate());
                prst.setInt(6, (int)allDates.get(iCounter).getMonth());
                prst.setInt(7, (int)allDates.get(iCounter).getYear() + 1900);
                prst.executeUpdate();
            }
            allDates.remove(iCounter);
            rst1.beforeFirst();
        }
        statement.close();
        rst1.close();
        prst.close();
    }

    private void generateAssignedLecturesTable()throws SQLException{
        //1.start by assigning the lectures in the timeslots available from the week days
        //2.assign the students to the lectures following the simple rules of the hard constraints
        //3.
        // find a suitable layout of tutorials and lectures . Only Lectures first. since tutorials are not yet inserted
        String sql22 = "SELECT abreviation,hours_twoweeks FROM s_courses WHERE hours_twoweeks != 0";
        Statement statement = connection.createStatement();
        ResultSet resSet = statement.executeQuery(sql22);
        int index = 0;
        String sql99 = "DROP TABLE IF EXISTS assigned_lects";
        String sql11 =
                "CREATE TABLE assigned_lects(" +
                        "ABBREV VARCHAR(10), " +
                        "HOURS INT(2), " +
                        "HALL VARCHAR(30)," +
                        "DAYTIME INT(4)," +
                        "DAY VARCHAR(10)," +
                        "DATE INT(2)," +
                        "MONTH INT(2)," +
                        "YEAR INT(4))";


        statement.executeUpdate(sql99);
        statement.executeUpdate(sql11);

    }

    private int i_calcStudentsHalls(){
        return  0;
    }

    /* /* Same functionality in class ParserHalls */

    /*
    void v_setAvailable_HallsTW_day_toZero(String sHall, String sDay, int iTimeStart, int iTimeEnd) throws SQLException {
        String sql21 = "UPDATE halls_availability_tweeks " +
                "SET AVAILABLE = 0 " +
                "WHERE HALL = '" + sHall + "' " +
                "AND DAY = '" + sDay + "' " +
                "AND HOUR BETWEEN " + iTimeStart + " AND " + iTimeEnd;
        Statement statement = connection.createStatement();
        statement.executeUpdate(sql21);
    }

    void v_setAvailable_HallsTW_day_toOne(String sHall, String sDay, int iTimeStart, int iTimeEnd) throws SQLException {
        String sql21 = "UPDATE halls_availability_tweeks " +
                "SET AVAILABLE = 1 " +
                "WHERE HALL = '" + sHall + "' " +
                "AND DAY = '" + sDay + "' " +
                "AND HOUR BETWEEN " + iTimeStart + " AND " + iTimeEnd;
        Statement statement = connection.createStatement();
        statement.executeUpdate(sql21);
    }

    void v_setAvailable_HallsTW_date_toZero(String sHall, int iDateOfMonth, int iMonth, int iTimeStart, int iTimeEnd) throws SQLException {
        String sql21 = "UPDATE halls_availability_tweeks " +
                "SET AVAILABLE = 0 " +
                "WHERE HALL = '" + sHall + "' " +
                "AND DATE = " + iDateOfMonth + " " +
                "AND MONTH = " + iMonth + " " +
                "AND HOUR BETWEEN " + iTimeStart + " AND " + iTimeEnd;
        Statement statement = connection.createStatement();
        statement.executeUpdate(sql21);
    }

    void v_setAvailable_HallsTW_date_toOne(String sHall, int iDateOfMonth, int iMonth, int iTimeStart, int iTimeEnd) throws SQLException {
        String sql21 = "UPDATE halls_availability_tweeks " +
                "SET AVAILABLE = 1 " +
                "WHERE HALL = '" + sHall + "' " +
                "AND DATE = " + iDateOfMonth + " " +
                "AND MONTH = " + iMonth + " " +
                "AND HOUR BETWEEN " + iTimeStart + " AND " + iTimeEnd;
        Statement statement = connection.createStatement();
        statement.executeUpdate(sql21);
    }
    */


    /**/

}
