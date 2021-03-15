package com.mycompany.app;

import com.mycompany.app.algorithms.BetterGreedyAlgorithm;
import com.mycompany.app.algorithms.GreedyAlgorithm;
import com.mycompany.app.algorithms.HillClimbing;
import com.mycompany.app.inserts.data.Generator;
import com.mycompany.app.inserts.data.Inserter_LecturesAssigned;
import com.mycompany.app.inserts.data.LGT_Inserter;
import com.mycompany.app.timetabling.*;

import java.io.IOException;
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
    public static void main( String[] args ) throws SQLException, ParseException, CloneNotSupportedException, IOException {
            Connection connection = JDBCHelper.getConnection();
            Statement stmt = connection.createStatement();

            Generator generator = new Generator(connection, "students", "courses");

//            generator.v_run_populate_availability("28-Sep-2020", "14-Dec-2020");
//            generator.populateChoicesOfLectures(1);
//            generator.v_finalevents(40,100);
//            generator.v_run_preferences(1);
//            generator.v_finalevents(10,100);


            Students_Test students_test = new Students_Test(connection);

            ArrayList<String> days = new ArrayList<>();
            days.add("Monday");
            days.add("Tuesday");
            days.add("Thursday");


        BetterGreedyAlgorithm bgt = new BetterGreedyAlgorithm(connection);
        //bgt.generateGreedySolution("s_courses");

        HillClimbing hillClimbing = new HillClimbing(connection);
        hillClimbing.getGrdAlg().setTwoInts(generator.getLectureEvents());
        hillClimbing.getGrdAlg().generateGreedySolution_TwoWeeks("courses", "students", 5, 21,days, "", 0);
        hillClimbing.setTimetableont(hillClimbing.getGrdAlg().getWeek_timetable_ont());
        hillClimbing.setTimetabletwo(hillClimbing.getGrdAlg().getWeek_timetable_spare());
        hillClimbing.generateHCSolution();

        String jj = "4CCS";
        String kk = "5CCS";

        System.out.println(jj.compareTo(kk));

        connection.close();

    }




}
