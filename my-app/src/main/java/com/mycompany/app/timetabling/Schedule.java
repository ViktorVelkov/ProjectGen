package com.mycompany.app.timetabling;

import com.mycompany.app.algorithms.HillClimbing;
import com.mycompany.app.inserts.data.TwoInts;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;

public class Schedule {
    private Connection connection;
    private ArrayList<Week_Timetable> schedule = new ArrayList<>();
    private HillClimbing hillClimbing;

    public Schedule(Connection connection, ArrayList<TwoInts> events) {
        this.connection = connection;
        hillClimbing = new HillClimbing(connection);
        hillClimbing.getGrdAlg().setTwoInts(events);
    }

    public void generate( ArrayList<String> days ,double dScale, int iNumIterations, int iStudentsForTutorialsMinumym) throws SQLException, ParseException, CloneNotSupportedException, IOException, InterruptedException {

        hillClimbing.getGrdAlg().generateGreedySolution_TwoWeeks("courses", "students", 5, 21,days, "", 0);
        hillClimbing.setTIMETABLEONT_GLOBAL(hillClimbing.getGrdAlg().getWeek_timetable_ont());
        hillClimbing.setTIMETABLETWO_GLOBAL(hillClimbing.getGrdAlg().getWeek_timetable_spare());
        hillClimbing.generateHCSolution(dScale, iNumIterations, iStudentsForTutorialsMinumym);  //(0.9, 3, 5)
        hillClimbing.heuristicEvaluation(hillClimbing.ASSIGNED_SGTS_CURRENTWEEK_GLOBAL.get(0),1.5,1,0.5);
       // hillClimbing.calculationTestsHeuristicsArray(1.5, 1, 0.5);
        //hillClimbing.testingTestingTesting();

        //hillClimbing.updateSGT_Time(hillClimbing.ASSIGNED_SGTS_CURRENTWEEK_GLOBAL.get(0), "Monday", 2000, "WB3H300000", hillClimbing.getTIMETABLETWO_GLOBAL());
    }



}
