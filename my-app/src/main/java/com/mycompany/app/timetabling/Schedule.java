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

    public void generate( ArrayList<String> days ,String sCoursesTable, String sStudentsTable , double dScale, int iNumIterations, int iStudentsForTutorialsMinumym, int iStudentsForTutorialsMaximum, double scaleForDay, double scaleForHour, double scaleCloseToHour) throws SQLException, ParseException, CloneNotSupportedException, IOException, InterruptedException {


//        for(int i = 0; i < 5; i++) {
//            hillClimbing.getGrdAlg().generateGreedySolution_TwoWeeks(sCoursesTable, sStudentsTable, 5, 21, days, "", 0);
//            hillClimbing.transitionOfSGTsBetweenWeeks(hillClimbing.getGrdAlg().getWeek_timetable_spare());
//            hillClimbing.setTIMETABLEONT_GLOBAL(hillClimbing.getGrdAlg().getWeek_timetable_ont());
//            hillClimbing.setTIMETABLETWO_GLOBAL(hillClimbing.getGrdAlg().getWeek_timetable_spare());
//            hillClimbing.generateHCSolution(dScale, iNumIterations, iStudentsForTutorialsMinumym,iStudentsForTutorialsMaximum, 1.5, 1, 0.5);  //(0.9, 3, 5)
//        }

        for(int i = 0; i < 5; i++) {
            hillClimbing.getGrdAlg().generateGreedySolution_TwoWeeks(sCoursesTable, sStudentsTable, iStudentsForTutorialsMinumym, iStudentsForTutorialsMaximum, days, "", 0);
            hillClimbing.setTIMETABLEONT_GLOBAL(hillClimbing.getGrdAlg().getWeek_timetable_ont());
            hillClimbing.setTIMETABLETWO_GLOBAL(hillClimbing.getGrdAlg().getWeek_timetable_spare());
            hillClimbing.generateHCSolution(dScale, iNumIterations, iStudentsForTutorialsMinumym,iStudentsForTutorialsMaximum, scaleForDay, scaleForHour, scaleCloseToHour);  //(0.9, 3, 5)
            hillClimbing.clearAllData();
        }


       // hillClimbing.checkDuplication(hillClimbing.getTIMETABLEONT_GLOBAL(),hillClimbing.getTIMETABLETWO_GLOBAL());

    }



}
