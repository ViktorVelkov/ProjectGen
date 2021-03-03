package com.mycompany.app.timetabling;

import java.util.ArrayList;

public class Student {
    private String sFirstName = "";
    private String sLastName = "";
    private int iKingsID = 0;
    private int iYearOfStudy = 0;
    ArrayList<Integer> coursesCodes;


    public Student(){}

    public Student(String sFirstName, String sLastName, int iKingsID, int iYearOfStudy) {
        this.sFirstName = sFirstName;
        this.sLastName = sLastName;
        this.iKingsID = iKingsID;
        this.iYearOfStudy = iYearOfStudy;
    }
}
