package com.mycompany.app.timetabling;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LastFrame  extends JFrame{
    String sText = "";
    JButton button = new JButton("CLOSE CONNECTION");


    public LastFrame(){
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setVisible(true);
        this.setSize(600,450);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(e.getSource()==button){
                    System.exit(0);
                }
            }
        });
        this.add(button);
    }
}
