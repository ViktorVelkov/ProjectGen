package com.mycompany.app.timetabling;

import com.mycompany.app.algorithms.HillClimbing;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;

public class ActionListenerFrame2 implements ActionListener {
    HillClimbing hillClimbing_insideFrame;
    JFrame frame2_toClose;
    double dScale;
    int iNumIterations;
    int iStudentsForTutorialsMinumym;
    int iStudentsForTutorialsMaximum;
    double scaleForDay;
    double scaleForHour;
    double scaleCloseToHour;
    String sDateStart;


    public ActionListenerFrame2(HillClimbing hillClimbing_insideFrame, double dScale, int iNumIterations, int iStudentsForTutorialsMinumym, int iStudentsForTutorialsMaximum, double scaleForDay, double scaleForHour, double scaleCloseToHour, String sDateStart) {
        this.hillClimbing_insideFrame = hillClimbing_insideFrame;
        this.dScale = dScale;
        this.iNumIterations = iNumIterations;
        this.iStudentsForTutorialsMinumym = iStudentsForTutorialsMinumym;
        this.iStudentsForTutorialsMaximum = iStudentsForTutorialsMaximum;
        this.scaleForDay = scaleForDay;
        this.scaleForHour = scaleForHour;
        this.scaleCloseToHour = scaleCloseToHour;
        this.sDateStart = sDateStart;
    }

    public HillClimbing getHillClimbing() {
        return hillClimbing_insideFrame;
    }

    public void setHillClimbing(HillClimbing hillClimbing_insideFrame) {
        this.hillClimbing_insideFrame = hillClimbing_insideFrame;
    }

    public JFrame getFrame2_toClose() {
        return frame2_toClose;
    }

    public void setFrame2_toClose(JFrame frame2_toClose) {
        this.frame2_toClose = frame2_toClose;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            hillClimbing_insideFrame.transitionOfSGTsBetweenWeeks(hillClimbing_insideFrame.getGrdAlg().getWeek_timetable_spare());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        hillClimbing_insideFrame.setTIMETABLEONT_GLOBAL(hillClimbing_insideFrame.getGrdAlg().getWeek_timetable_ont());
        hillClimbing_insideFrame.setTIMETABLETWO_GLOBAL(hillClimbing_insideFrame.getGrdAlg().getWeek_timetable_spare());
        try {
            hillClimbing_insideFrame.generateHCSolution(dScale, iNumIterations, iStudentsForTutorialsMinumym,iStudentsForTutorialsMaximum, 1.5, 1, 0.5);  //(0.9, 3, 5)
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (CloneNotSupportedException cloneNotSupportedException) {
            cloneNotSupportedException.printStackTrace();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        } catch (ParseException parseException) {
            parseException.printStackTrace();
        } catch (InterruptedException interruptedException) {
            interruptedException.printStackTrace();
        }
        frame2_toClose.dispose();
    }
}
