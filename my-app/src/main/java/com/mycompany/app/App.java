package com.mycompany.app;

import com.mycompany.app.algorithms.BetterGreedyAlgorithm;
import com.mycompany.app.algorithms.GreedyAlgorithm;
import com.mycompany.app.inserts.data.Generator;
import com.mycompany.app.inserts.data.Inserter_LecturesAssigned;
import com.mycompany.app.inserts.data.LGT_Inserter;
import com.mycompany.app.timetabling.*;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
//


//
public class App
{
    public static void main( String[] args ) throws SQLException, ParseException {
            Connection connection = JDBCHelper.getConnection();
            Statement stmt = connection.createStatement();

            Generator generator = new Generator(connection);
        //    generator.v_run_initial("28-Sep-2020", "13-Dec-2020");

            GreedyAlgorithm grdAlg = new GreedyAlgorithm(connection);
            //ArrayList<String> mylist = grdAlg.v_getPreferences_students("5CCS2CSL");
    //        for(int i = 0 ; i < mylist.size(); i++){
    //            System.out.println(mylist.get(i));
    //        }
    //        String choice = grdAlg.v_getTeachersChoice("5CCS2CSL");
    //        System.out.println(choice);

//            Week_Timetable week_timetable = grdAlg.myTimetable();
//            week_timetable.v_print();
//
//            System.out.println();
//            Week_Timetable week_timetable12 = grdAlg.myTimetable(7);
//            week_timetable12.v_print();


        //grdAlg.generateGreedySolution("s_courses");

        BetterGreedyAlgorithm bgt = new BetterGreedyAlgorithm(connection);
        //bgt.generateGreedySolution("s_courses");

        Inserter_LecturesAssigned lecturesAssigned = new Inserter_LecturesAssigned(connection);
        lecturesAssigned.createTable();
        lecturesAssigned.populateTable("courses", "s_students");
        lecturesAssigned.finalTable(5, 21, "courses", "s_students");
        connection.close();

    }



}
