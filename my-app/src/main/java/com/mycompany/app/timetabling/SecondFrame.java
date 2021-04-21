package com.mycompany.app.timetabling;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SecondFrame extends JFrame {
    JButton button = new JButton("");
    String sText = "";
    Week_Timetable timetable;
    Week_Timetable timetable2;


    public Week_Timetable getTimetable2() {
        return timetable2;
    }

    public void setTimetable2(Week_Timetable timetable2) {
        this.timetable2 = timetable2;
    }

    public Week_Timetable getTimetable() {
        return timetable;
    }

    public void setTimetable(Week_Timetable timetable) {
        this.timetable = timetable;
    }

    public SecondFrame(){
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setVisible(true);
        this.setSize(700,500);
        this.setResizable(false);
        JTextField field = new JTextField();
//        JPanel panel_week1 = new JPanel();
//        panel_week1.setBackground(Color.CYAN);
//        panel_week1.setBounds(0, 0, 300, 200);


        //this.add(panel_week1);
        this.add(field);

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(e.getSource()==button){
                    System.exit(0);
                }
            }
        });
//        this.add(button);
    }

}
