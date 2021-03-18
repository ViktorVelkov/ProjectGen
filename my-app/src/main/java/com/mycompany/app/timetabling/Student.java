package com.mycompany.app.timetabling;

import java.util.ArrayList;

public class Student {

    private String sFirstName = "";
    private String sLastName = "";
    private int iKingsID = 0;
    private int iYearOfStudy = 0;
    private int iUnassignedSGTs = 0;

    ArrayList<Integer> coursesCodes = new ArrayList<>();
    ArrayList<String> courses = new ArrayList<>();
    ArrayList<Duplet> assignedSGT = new ArrayList<>();
    ArrayList<DataSetStudents> preferences = new ArrayList<>();
    public Student(){}

    public Student(String sFirstName, String sLastName, int iKingsID, int iYearOfStudy) {
        this.sFirstName = sFirstName;
        this.sLastName = sLastName;
        this.iKingsID = iKingsID;
        this.iYearOfStudy = iYearOfStudy;
    }

    public Student(int iKingsID, int iUnassignedSGTs) {
        this.iKingsID = iKingsID;
        this.iUnassignedSGTs = iUnassignedSGTs;
    }

    public  void reassign(Duplet event){}
    public void assignSGT(Duplet event){
        assignedSGT.add(event);
    }
    public void addToPrefs(DataSetStudents data){
        preferences.add(data);
    }
    public int checkIfOverlappingSGT(Duplet event){
        for(Duplet sgt : assignedSGT){
            if(event.getiHourScheduled() <= sgt.getiHourScheduled() + sgt.getiHours()*100 && event.getiHours()*100 + event.getiHourScheduled() <= 1){}
        }
        return 1;
    }

}
