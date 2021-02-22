package com.mycompany.app.inserts.data;



import com.mycompany.app.timetabling.Duplet;

import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Generator {

    private Connection connection;
    private ArrayList<Duplet> initial = new ArrayList<>();
    private ArrayList<Duplet> assigned = new ArrayList<>();
    private Availability_TwoWeeks twoWeeks;
    private Inserter_Preferences prefs;
    private ParserHalls prsHalls;
    private LGT_Inserter lgt;


    public Generator(Connection connection){
        this.connection = connection;
        prsHalls = new ParserHalls(connection);
        prefs = new Inserter_Preferences(connection);
        twoWeeks = new Availability_TwoWeeks(connection);
        lgt = new LGT_Inserter(connection);
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


    public int v_run_initial(String sStartSemester, String sEndSemeseter) throws SQLException, ParseException {


        Date date = new SimpleDateFormat("dd-MMM-yyyy").parse(sStartSemester);
        if(date.getDay() != 1){
            System.out.println("Please enter a date which is on a Monday. The date you have enter is on the" + Integer.toString(date.getDay()) + "day of the week");
           return 1;
        }
        {
            lgt.v_dropLGT_Tutorials_table();
            lgt.v_createLGT_Tutorials_table();
            lgt.v_populateLGT_table("s_courses");

        }
        {
            prefs.v_createTablePreferences();
            prefs.v_populate_preferences_initial();
            prefs.v_create_studentsp_table();
            prefs.v_create_teachersp_table();
            prefs.v_populate_students_preferences_initial(0,0, 1,1);
            prefs.v_populate_students_preferences_initial(0,0, 1,2);
            prefs.v_populate_students_preferences_initial(0,0, 1,3);
            prefs.v_populate_teachers_preferences_initial(0);
        }
        {
            prsHalls.v_parse_initial();
            twoWeeks.v_generateTable_and_populate("availability_halls_bush_house",sStartSemester,sEndSemeseter);
            twoWeeks.v_generateTable_and_populate("availability_halls_waterloo",sStartSemester,sEndSemeseter);
        }
        return 0;
    }


    /**/


    //END of Class

}

