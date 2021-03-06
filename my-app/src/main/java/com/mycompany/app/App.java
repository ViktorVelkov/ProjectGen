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

            Generator generator = new Generator(connection, "students", "courses");
            generator.v_run_initial("28-Sep-2020", "14-Dec-2020");
            generator.v_finalevents(40,100);
            generator.v_run_preferences(1);
//            generator.v_finalevents(10,100);
            GreedyAlgorithm grdAlg = new GreedyAlgorithm(connection);
            grdAlg.setTwoInts(generator.getEvents());
            grdAlg.generateGreedySolution("courses", 5, 21);


        BetterGreedyAlgorithm bgt = new BetterGreedyAlgorithm(connection);
        //bgt.generateGreedySolution("s_courses");


        connection.close();

    }




}
