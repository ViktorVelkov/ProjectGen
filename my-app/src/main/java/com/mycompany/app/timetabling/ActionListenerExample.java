package com.mycompany.app.timetabling;
/*
* https://www.javatpoint.com/java-actionlistener
* https://www.youtube.com/watch?v=HgkBvwgciB4&list=PLZPZq0r_RZOMhCAyywfnYLlrjiVOkdAI1&index=57&ab_channel=BroCode
*
*/

import com.mycompany.app.inserts.data.Generator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;

//1st step
public class ActionListenerExample implements ActionListener{

    Connection connection;

    JComboBox dropMenu_students;
    JComboBox dropMenu_courses;

    JButton button;
    JFrame frame;
    JTextArea mintext;
    JTextArea maxtext;
    JTextArea scale1;
    JTextArea scale2;
    JTextArea scale3;

    public ActionListenerExample(Connection connection,   JComboBox j1, JComboBox j2, JFrame frame, JButton button, JTextArea mintext, JTextArea maxtext, JTextArea scale1, JTextArea scale2, JTextArea scale3) {

        this.connection = connection;
        this.dropMenu_courses = j1;
        this.dropMenu_students = j2;
        this.frame = frame;
        this.button = button;
        this.mintext = mintext;
        this.maxtext = maxtext;
        this.scale1 = scale1;
        this.scale2 = scale2;
        this.scale3 = scale3;
    }

    public JButton getButton() {
        return button;
    }

    public void setButton(JButton button) {
        this.button = button;
    }

    public JFrame getFrame() {
        return frame;
    }

    public void setFrame(JFrame frame) {
        this.frame = frame;
    }

    public JTextArea getMintext() {
        return mintext;
    }

    public void setMintext(JTextArea mintext) {
        this.mintext = mintext;
    }

    public JTextArea getMaxtext() {
        return maxtext;
    }

    public void setMaxtext(JTextArea maxtext) {
        this.maxtext = maxtext;
    }

    public JTextArea getScale1() {
        return scale1;
    }

    public void setScale1(JTextArea scale1) {
        this.scale1 = scale1;
    }

    public JTextArea getScale2() {
        return scale2;
    }

    public void setScale2(JTextArea scale2) {
        this.scale2 = scale2;
    }

    public JTextArea getScale3() {
        return scale3;
    }

    public void setScale3(JTextArea scale3) {
        this.scale3 = scale3;
    }





    public void actionPerformed(ActionEvent e){

        String minimum = mintext.getText();
        String maximum = maxtext.getText();
        String scaleDay = scale1.getText();
        String scaleHour = scale2.getText();
        String scaleCloseToHour = scale3.getText();

        System.out.println(minimum);
        System.out.println(maximum);
        System.out.println(scaleDay);
        System.out.println(scaleHour);
        System.out.println(scaleCloseToHour);




        //generate the algorithm

        frame.dispose();


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




        if(e.getSource()== button) {

            Generator generator = new Generator(this.connection, dropMenu_students.getSelectedItem().toString(), dropMenu_courses.getSelectedItem().toString());
            Schedule schedule = null;
            try {
                schedule = new Schedule(this.connection, generator.getLectureEvents());
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            try {
                //schedule.generate(days, generator.getsTableCourses(), generator.getsTableStudents(), 0.9,3,5,10, 1.5, 1, 0.5, "");
                schedule.generate(days, dropMenu_courses.getSelectedItem().toString(), dropMenu_students.getSelectedItem().toString(), 0.9, 10, Integer.valueOf(minimum), Integer.valueOf(maximum), Integer.valueOf(scaleDay), Integer.valueOf(scaleHour), Integer.valueOf(scaleCloseToHour), "");

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ParseException parseException) {
                parseException.printStackTrace();
            } catch (CloneNotSupportedException cloneNotSupportedException) {
                cloneNotSupportedException.printStackTrace();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
        }

    }
}  