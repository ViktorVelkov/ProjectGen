package com.mycompany.app;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class App
{
    public static void main( String[] args ) throws SQLException {
        Connection connection = JDBCHelper.getConnection();
        Statement stmt = connection.createStatement();

        Day ddA = new Day();
        ddA.v_assignEvent(900,1200, "Bush_House", "Lecture");
        ddA.v_reassign_event(1100,1300, "Bush_House", "Lecture");
        ddA.v_anull();
        ddA.print();
        GreedyAlgorithm gr = new GreedyAlgorithm(connection);
        gr.generateGreedySolution();
//       Day myDay = new Day();
//       myDay.v_assignEvent(900, 1200, "BushHouse", "Lecture");
//       myDay.print();
//        Inserter_Preferences myPrefs = new Inserter_Preferences(connection);
//        myPrefs.v_truncatePreferencesTable();
//        myPrefs.v_populate();
        connection.close();

    }



}
