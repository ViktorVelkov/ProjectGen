package com.mycompany.app;
import java.sql.*;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

//
import java.util.Date;
import java.time.LocalDate;
import java.time.LocalTime;
import java.text.SimpleDateFormat;
//
public class App
{
    public static void main( String[] args ) throws SQLException, ParseException {
        Connection connection = JDBCHelper.getConnection();
        Statement stmt = connection.createStatement();

        Day ddA = new Day();
        ddA.v_assignEvent(900,1200, "Bush_House", "Lecture");
        ddA.v_reassign_event(1100,1300, "Bush_House", "Lecture");
        ddA.v_anull();
        GreedyAlgorithm gr = new GreedyAlgorithm(connection);
        gr.generateGreedySolution();
//       Day myDay = new Day();
//       myDay.v_assignEvent(900, 1200, "BushHouse", "Lecture");
//       myDay.print();
//        Inserter_Preferences myPrefs = new Inserter_Preferences(connection);
//        myPrefs.v_truncatePreferencesTable();
//        myPrefs.v_populate();
        connection.close();
        LocalDate localDate = LocalDate.now();
        LocalTime localTime = LocalTime.now();
        System.out.println((int)localDate.getDayOfMonth());
        System.out.println((int)localTime.getHour());
        String sDate6 = "31-Dec-1998 23:37:50";
        SimpleDateFormat formatter6=new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
        Date date6= (Date) formatter6.parse(sDate6);
        System.out.println(date6.getMinutes());
    }



}
