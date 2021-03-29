package com.mycompany.app.timetabling;

import java.util.ArrayList;
import java.util.HashSet;

public class Student implements Comparable<Student>{

    private String sFirstName = "";
    private String sLastName = "";
    private int iKingsID = 0;
    private int iYearOfStudy = 0;
    private int iAssignedSGTs = 0;

    HashSet<Integer> coursesCodes = new HashSet<>();
    ArrayList<SGT> courses = new ArrayList<>();
    ArrayList<Duplet> assignedSGT = new ArrayList<>();
    ArrayList<DataSetStudents> preferences = new ArrayList<>();


    public Student(){}

    public Student(String sFirstName, String sLastName, int iKingsID, int iYearOfStudy) {
        this.sFirstName = sFirstName;
        this.sLastName = sLastName;
        this.iKingsID = iKingsID;
        this.iYearOfStudy = iYearOfStudy;
    }


    public Student(int iKingsID, int iYearOfStudy) {
        this.iKingsID = iKingsID;
        this.iYearOfStudy = iYearOfStudy;
    }

    public int getiKingsID() {
        return iKingsID;
    }

    public void setiKingsID(int iKingsID) {
        this.iKingsID = iKingsID;
    }

    public void increaseAssignedCoursesNumber(){ this.iAssignedSGTs++;}

    public int numberOfAssignedCourses(){
        return this.courses.size();
    }

    public ArrayList<SGT> getCourses() { return courses; }

    public void setCourses(ArrayList<SGT> courses) { this.courses = courses; }

    public void addToCourses(SGT event){ this.courses.add(event); }

    public void addToCoursesCodes(int event_code){ this.coursesCodes.add(event_code); }

    public HashSet<Integer> getCoursesCodes() { return coursesCodes; }

    public ArrayList<Duplet> getAssignedSGT() { return assignedSGT; }

    public ArrayList<DataSetStudents> getPreferences() { return preferences; }

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





    @Override
    public int hashCode() {
        return this.iKingsID;
    }

    @Override
    public boolean equals(Object obj) {
        int a = this.hashCode();
        int b = obj.hashCode();
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Timeslot other = (Timeslot) obj;
        if (this.hashCode() != 0) {
            if (other.hashCode() == 0)
                return false;
        }
        if (this.hashCode() != (other.hashCode()))
            return false;
        return true;
    }


    @Override
    public int compareTo(Student o) {
        return o.iKingsID - this.iKingsID;
    }
}
