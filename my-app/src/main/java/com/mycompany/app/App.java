package com.mycompany.app;

import com.mycompany.app.algorithms.BetterGreedyAlgorithm;
import com.mycompany.app.algorithms.GreedyAlgorithm;
import com.mycompany.app.algorithms.HillClimbing;
import com.mycompany.app.inserts.data.Generator;
import com.mycompany.app.inserts.data.Inserter_LecturesAssigned;
import com.mycompany.app.inserts.data.LGT_Inserter;
import com.mycompany.app.timetabling.*;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

public class App
{

    public static void runApplication() throws SQLException, ParseException, CloneNotSupportedException, IOException, InterruptedException {
        Connection connection = JDBCHelper.getConnection();

        Generator generator = new Generator(connection, "s_students", "courses");
//
//            generator.v_run_populate_availability("28-Sep-2020", "14-Dec-2020");
//            generator.populateChoicesOfLectures(1);
//            generator.v_finalevents(40,100);
//            generator.v_run_preferences(1);
//            generator.v_finalevents(10,100);

        ArrayList<String> days = new ArrayList<>();
        days.add("Monday");
        days.add("Tuesday");
        days.add("Thursday");

        Schedule schedule = new Schedule(connection, generator.getLectureEvents());
        schedule.generate(days, generator.getsTableCourses(), generator.getsTableStudents(), 0.9,3,5,10, 1.5, 1, 0.5);

        connection.close();
    }


    public static void main( String[] args ) throws SQLException, ParseException, CloneNotSupportedException, IOException, InterruptedException {
        //simplistic
        runApplication();
    }

}
