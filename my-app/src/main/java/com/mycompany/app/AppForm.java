package com.mycompany.app;

import com.mycompany.app.timetabling.ActionListenerExample;
import com.mycompany.app.timetabling.Schedule;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

public class AppForm {
    private JTextField textField1;
    private JComboBox comboBox1;
    private Schedule schedule;
    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }

    public void runInitialWindow(){

        JLabel label0 = new JLabel();
        JLabel label1 = new JLabel();
        JLabel label2 = new JLabel();
        JLabel label3 = new JLabel();
        JLabel label4 = new JLabel();
        JLabel label5 = new JLabel();
        JLabel label6 = new JLabel();
        JLabel label7 = new JLabel();

        String studentsTables [] = {"s_students", "students"};
        String coursesTables [] = {"s_courses", "courses"};
        JComboBox dropMenu_students = new JComboBox(studentsTables);
        JComboBox dropMenu_courses = new JComboBox(coursesTables);
        JTextArea mintext = new JTextArea();
        JTextArea maxtext = new JTextArea();
        JTextArea scale1 = new JTextArea();
        JTextArea scale2 = new JTextArea();
        JTextArea scale3 = new JTextArea();
        JButton button = new JButton("RUN ALGORITHM");

        button.setBounds(200, 350, 200, 20);
        dropMenu_students.setBounds(320, 50, 200, 20);
        dropMenu_courses.setBounds(320, 88, 200, 20);
        mintext.setBounds(320, 130, 200, 20);
        mintext.setText("1");
        maxtext.setBounds(320, 170, 200, 20);
        maxtext.setText("1");
        scale1.setBounds(320, 210, 200, 20);
        scale1.setText("1");
        scale2.setBounds(320, 250, 200, 20);
        scale2.setText("1");
        scale3.setBounds(320, 290, 200, 20);
        scale3.setText("1");
        //        label1.setHorizontalTextPosition(JLabel.LEFT);
        label0.setBounds(150, 0, 600, 20);
        //label0.setHorizontalTextPosition(JLabel.CENTER);
        label0.setVerticalAlignment(JLabel.TOP);
        label1.setBounds(50, 10, 100,100);
        label2.setBounds(50, 50, 100,100);
        label3.setBounds(50, 90, 190,100);
        label4.setBounds(50, 130, 190,100);
        label5.setBounds(50, 170, 100,100);
        label6.setBounds(50, 210, 100,100);
        label7.setBounds(50, 250, 150,100);

        label0.setText("Greedy Algorithm with Hill Climbing Time Scheduler");
        label1.setText("Table students:");
        label2.setText("Table courses:");
        label3.setText("Students minimum for tutorial:");
        label4.setText("Students maximum for tutorial:");
        label5.setText("Scale for day:");
        label6.setText("Scale for hour:");
        label7.setText("Scale for close to hour:");

        JFrame frame = new JFrame("Prototype");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setLayout(null);
        frame.add(label0);
        frame.add(label1);
        frame.add(label2);
        frame.add(label3);
        frame.add(label4);
        frame.add(label5);
        frame.add(label6);
        frame.add(label7);
        frame.add(dropMenu_students);
        frame.add(dropMenu_courses);
        frame.add(mintext);
        frame.add(maxtext);
        frame.add(scale1);
        frame.add(scale2);
        frame.add(scale3);
        frame.add(button);

        frame.setSize(600,450);
        String minimum;

        ActionListenerExample e =  new ActionListenerExample(connection, dropMenu_courses,dropMenu_students,frame,button,mintext,maxtext,scale1,scale2,scale3);
        button.addActionListener(e);
    }

}
