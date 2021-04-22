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

        //Generator generator = new Generator(connection, "s_students", "courses");
        Generator generator = new Generator(connection, "students10", "courses");
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
        schedule.generate(days, generator.getsTableCourses(), generator.getsTableStudents(), 0.9,3,10,10, 1.5, 1, 0.5);

        connection.close();
    }

    public static void runApplication2(String sStudents, String sCourses, int minPerCourse) throws SQLException, ParseException, CloneNotSupportedException, IOException, InterruptedException {
        Connection connection = JDBCHelper.getConnection();

        //Generator generator = new Generator(connection, "s_students", "courses");
        Generator generator = new Generator(connection, sStudents, sCourses);
//            generator.v_run_populate_availability("28-Sep-2020", "14-Dec-2020");
            generator.populateChoicesOfLectures(1);
            generator.v_run_preferences(1);
            generator.v_finalevents(minPerCourse,100);

        ArrayList<String> days = new ArrayList<>();
        days.add("Monday");
        days.add("Tuesday");
        days.add("Thursday");

        Schedule schedule = new Schedule(connection, generator.getLectureEvents());
        schedule.generate(days, generator.getsTableCourses(), generator.getsTableStudents(), 0.9,3,10,15, 1.5, 1, 0.5);

        connection.close();
    }


    public static void main( String[] args ) throws SQLException, ParseException, CloneNotSupportedException, IOException, InterruptedException {
        //simplistic
        //runApplication();
        System.out.println("1======================================");
        //runApplication2("students10", "courses", 4);
        //runApplication2("students10", "s_courses",4);
//        System.out.println("3======================================");
//        runApplication2("students30", "courses",10);
//        runApplication2("students30", "s_courses",10);
//        System.out.println("4======================================");
//        runApplication2("students40", "courses",15);
//        runApplication2("students40", "s_courses",15);
//        System.out.println("5======================================");
//        runApplication2("students50", "courses",20);
//        runApplication2("students50", "s_courses",20);
//        System.out.println("6======================================");
//        runApplication2("students60", "courses",25);
//        runApplication2("students60", "s_courses",25);
//        System.out.println("7======================================");
//        runApplication2("students70", "courses",30);
//        runApplication2("students70", "s_courses",30);
//        System.out.println("8======================================");
//        runApplication2("students80", "courses",34);
//        runApplication2("students80", "s_courses",34);
//        System.out.println("9======================================");
//        runApplication2("students90", "courses",37);
//        runApplication2("students90", "s_courses",37);
//        System.out.println("10======================================");
//        runApplication2("students100", "courses",40);
//        runApplication2("students100", "s_courses",40);
//        System.out.println("11======================================");
//        runApplication2("students110", "courses",43);
//        runApplication2("students110", "s_courses",43);
        System.out.println("12======================================");
        runApplication2("students120", "courses",45);
        runApplication2("students120", "s_courses",45);
    }

}
