package com.mycompany.app.timetabling;

import com.mycompany.app.GUI.MainMenu;
import com.mycompany.app.algorithms.HillClimbing;
import com.mycompany.app.inserts.data.TwoInts;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;

public class Schedule {
    private Connection connection;
    private ArrayList<Week_Timetable> schedule = new ArrayList<>();
    private HillClimbing hillClimbing;
    ArrayList<String> days ;
    String sCoursesTable;
    String sStudentsTable;
    double dScale;
    int iNumIterations;
    int iStudentsForTutorialsMinumym;
    int iStudentsForTutorialsMaximum;
    double scaleForDay;
    double scaleForHour;
    double scaleCloseToHour;
    String sDateStart;

    public Schedule(Connection connection, ArrayList<TwoInts> events) {
        this.connection = connection;
        hillClimbing = new HillClimbing(connection);
        hillClimbing.getGrdAlg().setTwoInts(events);

    }

    public void generate( ArrayList<String> days ,String sCoursesTable, String sStudentsTable , double dScale, int iNumIterations, int iStudentsForTutorialsMinumym, int iStudentsForTutorialsMaximum, double scaleForDay, double scaleForHour, double scaleCloseToHour,String sDateStart) throws SQLException, ParseException, CloneNotSupportedException, IOException, InterruptedException {

        this.days = days;
        this.sCoursesTable = sCoursesTable;
        this.sStudentsTable = sStudentsTable;
        this.dScale = dScale;
        this.iNumIterations = iNumIterations;
        this.iStudentsForTutorialsMaximum = iStudentsForTutorialsMaximum;
        this.iStudentsForTutorialsMinumym = iStudentsForTutorialsMinumym;
        this.scaleForDay = scaleForDay;
        this.scaleForHour =scaleForHour;
        this.scaleCloseToHour = scaleCloseToHour;
        this.sDateStart = sDateStart;

            hillClimbing.getGrdAlg().generateGreedySolution_TwoWeeks(sCoursesTable, sStudentsTable, 5, 21, days, sDateStart, 0);

            MainMenu mainMenu = new MainMenu();
            mainMenu.setSize(1152,835);
            mainMenu.setResizable(false);
            mainMenu.setSchedule(this);
            mainMenu.setVisible(true);




//            SecondFrame frame2 = new SecondFrame();
//            frame2.setLayout(null);
//            frame2.setTimetable(hillClimbing.getGrdAlg().getWeek_timetable_ont());
//            frame2.setTimetable2(hillClimbing.getGrdAlg().getWeek_timetable_spare());
//            ActionListenerFrame2 hillClimb_activator = new ActionListenerFrame2(hillClimbing,dScale, iNumIterations, iStudentsForTutorialsMinumym,iStudentsForTutorialsMaximum, scaleForDay, scaleForHour, scaleCloseToHour,sDateStart);
//            hillClimb_activator.setFrame2_toClose(frame2);
//            JButton runHC = new JButton("RUN HC");
//            runHC.setVisible(true);
//            runHC.setBounds(300, 400, 100, 20);
//            JPanel panel1 = new JPanel();
//            panel1.setBounds(0, 0, 300, 200);
//
//            JTextArea textArea = new JTextArea();
//            textArea.setText(hillClimbing.getGrdAlg().getWeek_timetable_ont().getAssignedLectures().toString());
//            textArea.setBounds(0, 0, 250, 200);
//            JScrollPane scrollPane = new JScrollPane(textArea);
//            panel1.add(scrollPane, BorderLayout.CENTER);
//            //frame2.setLayout(new BorderLayout());
//            //frame2.setContentPane(panel1);
//            frame2.add(panel1);
//
//            runHC.addActionListener(hillClimb_activator);
//
//            frame2.add(runHC);

//
//            hillClimbing.transitionOfSGTsBetweenWeeks(hillClimbing.getGrdAlg().getWeek_timetable_spare());
//            hillClimbing.setTIMETABLEONT_GLOBAL(hillClimbing.getGrdAlg().getWeek_timetable_ont());
//            hillClimbing.setTIMETABLETWO_GLOBAL(hillClimbing.getGrdAlg().getWeek_timetable_spare());
//            hillClimbing.generateHCSolution(dScale, iNumIterations, iStudentsForTutorialsMinumym,iStudentsForTutorialsMaximum, 1.5, 1, 0.5);  //(0.9, 3, 5)

//
//        for(int i = 0; i < 5; i++) {
//            if(i == 3){
//                hillClimbing.getGrdAlg().generateGreedySolution_TwoWeeks(sCoursesTable, sStudentsTable, iStudentsForTutorialsMinumym, iStudentsForTutorialsMaximum, days, "", 1);
//            }
//            else {
//                hillClimbing.getGrdAlg().generateGreedySolution_TwoWeeks(sCoursesTable, sStudentsTable, iStudentsForTutorialsMinumym, iStudentsForTutorialsMaximum, days, "", 0);
//            }
//            hillClimbing.setTIMETABLEONT_GLOBAL(hillClimbing.getGrdAlg().getWeek_timetable_ont());
//            hillClimbing.setTIMETABLETWO_GLOBAL(hillClimbing.getGrdAlg().getWeek_timetable_spare());
//            hillClimbing.generateHCSolution(dScale, iNumIterations, iStudentsForTutorialsMinumym,iStudentsForTutorialsMaximum, scaleForDay, scaleForHour, scaleCloseToHour);  //(0.9, 3, 5)
//            hillClimbing.clearAllData();
//        }


       // hillClimbing.checkDuplication(hillClimbing.getTIMETABLEONT_GLOBAL(),hillClimbing.getTIMETABLETWO_GLOBAL());

    }


    public HillClimbing getHillClimbing() {
        return hillClimbing;
    }

    public void setHillClimbing(HillClimbing hillClimbing) {
        this.hillClimbing = hillClimbing;
    }


    public ArrayList<String> getDays() {
        return days;
    }

    public void setDays(ArrayList<String> days) {
        this.days = days;
    }

    public String getsCoursesTable() {
        return sCoursesTable;
    }

    public void setsCoursesTable(String sCoursesTable) {
        this.sCoursesTable = sCoursesTable;
    }

    public String getsStudentsTable() {
        return sStudentsTable;
    }

    public void setsStudentsTable(String sStudentsTable) {
        this.sStudentsTable = sStudentsTable;
    }

    public double getdScale() {
        return dScale;
    }

    public void setdScale(double dScale) {
        this.dScale = dScale;
    }

    public int getiNumIterations() {
        return iNumIterations;
    }

    public void setiNumIterations(int iNumIterations) {
        this.iNumIterations = iNumIterations;
    }

    public int getiStudentsForTutorialsMinumym() {
        return iStudentsForTutorialsMinumym;
    }

    public void setiStudentsForTutorialsMinumym(int iStudentsForTutorialsMinumym) {
        this.iStudentsForTutorialsMinumym = iStudentsForTutorialsMinumym;
    }

    public int getiStudentsForTutorialsMaximum() {
        return iStudentsForTutorialsMaximum;
    }

    public void setiStudentsForTutorialsMaximum(int iStudentsForTutorialsMaximum) {
        this.iStudentsForTutorialsMaximum = iStudentsForTutorialsMaximum;
    }

    public double getScaleForDay() {
        return scaleForDay;
    }

    public void setScaleForDay(double scaleForDay) {
        this.scaleForDay = scaleForDay;
    }

    public double getScaleForHour() {
        return scaleForHour;
    }

    public void setScaleForHour(double scaleForHour) {
        this.scaleForHour = scaleForHour;
    }

    public double getScaleCloseToHour() {
        return scaleCloseToHour;
    }

    public void setScaleCloseToHour(double scaleCloseToHour) {
        this.scaleCloseToHour = scaleCloseToHour;
    }

    public String getsDateStart() {
        return sDateStart;
    }

    public void setsDateStart(String sDateStart) {
        this.sDateStart = sDateStart;
    }
}
