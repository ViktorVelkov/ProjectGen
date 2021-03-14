package com.mycompany.app.timetabling;

import java.util.ArrayList;

public class Duplet implements Comparable<Duplet>, Cloneable{

    private double iHours = 0.0;
    private String sLect = "";
    private PreferredDays preferredDays;
    private int iSemester = 0;
    private String sTeachersPreference = "";
    private int iNumberOfStudentsAttending = 0;
    private int iScheduledMoreThanOnce = 0;                                         // scheduled 0 == no changes made, 1 == scheduled multiple times
    private int iCode = 0;
    private String sLectureHall = "";
    private String sDayOfWeek = "";
    private int iHourScheduled = 0;
    private int iDayScheduled = -1;
    private int iMonthScheduled = -1;
    private int iYearScheduled = -1;
    private int lgtAssigned = 0;
    private boolean isALecture;
    private String dayAssigned = "";
    private ArrayList<Duplet> dependentOn;
    private ArrayList<Duplet> dependentOnTutorials;
    //    private ArrayList<CoupledData> constraintsAllocation;


    public Duplet(String sLect, double iHours, int iNumberOfStudentsAttending){
        this.iHours = iHours;
        this.sLect = sLect;
        this.iNumberOfStudentsAttending = iNumberOfStudentsAttending;
    }


    public Duplet(String sLect, double iHours, int iNumberOfStudentsAttending, int iCode){
        this.iHours = iHours;
        this.sLect = sLect;
        this.iNumberOfStudentsAttending = iNumberOfStudentsAttending;
        this.iCode = iCode;
    }

    public Duplet(String sLect, int iCode) {
        this.sLect = sLect;
        this.iCode = iCode;
    }

    public Duplet(int iCode){
        this.iCode =iCode;
    }

    public double getiHours() {
        return iHours;
    }

    public void setiHours(double iHours) {
        this.iHours = iHours;
    }

    public String getsLect() {
        return sLect;
    }

    public void setsLect(String sLect) {
        this.sLect = sLect;
    }

    public ArrayList<Duplet> getDependentOnTutorials() {
        return dependentOnTutorials;
    }

    public void setDependentOnTutorials(ArrayList<Duplet> dependentOnTutorials) {
        //this.dependentOnTutorials = dependentOnTutorials;
        this.dependentOnTutorials = (ArrayList<Duplet>) dependentOn.clone();
        for(Duplet duplet : dependentOnTutorials){
            this.dependentOnTutorials.add(duplet);
        }
    }



    public int getiSemester() {
        return iSemester;
    }

    public void setiSemester(int iSemester) {
        this.iSemester = iSemester;
    }

    public String getsTeachersPreference() {
        return sTeachersPreference;
    }

    public void setsTeachersPreference(String sTeachersPreference) {
        this.sTeachersPreference = sTeachersPreference;
    }

    public int getiNumberOfStudentsAttending() {
        return iNumberOfStudentsAttending;
    }

    public void setiNumberOfStudentsAttending(int iNumberOfStudentsAttending) {
        this.iNumberOfStudentsAttending = iNumberOfStudentsAttending;
    }

    public PreferredDays getPreferredDays() {
        return preferredDays;
    }

    public void setPreferredDays(PreferredDays preferredDays) {
        this.preferredDays = preferredDays;
    }

    public int getiScheduledMoreThanOnce() {
        return iScheduledMoreThanOnce;
    }

    public void setiScheduledMoreThanOnce(int iScheduledMoreThanOnce) {
        this.iScheduledMoreThanOnce = iScheduledMoreThanOnce;
    }

    public int getiCode() {
        return iCode;
    }

    public void setiCode(int iCode) {
        this.iCode = iCode;
    }


    public String getsDayOfWeek() {
        return sDayOfWeek;
    }

    public void setsDayOfWeek(String sDayOfWeek) {
        this.sDayOfWeek = sDayOfWeek;
    }

    public int getiDayScheduled() {
        return iDayScheduled;
    }

    public void setiDayScheduled(int iDayScheduled) {
        this.iDayScheduled = iDayScheduled;
    }

    public int getiMonthScheduled() {
        return iMonthScheduled;
    }

    public void setiMonthScheduled(int iMonthScheduled) {
        this.iMonthScheduled = iMonthScheduled;
    }

    public int getiYearScheduled() {
        return iYearScheduled;
    }

    public void setiYearScheduled(int iYearScheduled) {
        this.iYearScheduled = iYearScheduled;
    }

    public int getiHourScheduled() {
        return iHourScheduled;
    }

    public void setiHourScheduled(int iHourScheduled) {
        this.iHourScheduled = iHourScheduled;
    }

    public String getsLectureHall() {
        return sLectureHall;
    }

    public void setsLectureHall(String sLectureHall) {
        this.sLectureHall = sLectureHall;
    }


    public ArrayList<Duplet> getDependentOn() {
        return dependentOn;
    }

    public void setDependentOn(ArrayList<Duplet> dependentOn) {
        this.dependentOn = dependentOn;
    }

    public String getDayAssigned() {
        return dayAssigned;
    }

    public void setDayAssigned(String dayAssigned) {
        this.dayAssigned = dayAssigned;
    }

    public boolean isALecture() {
        return isALecture;
    }

    public void setALecture(boolean ALecture) {
        isALecture = ALecture;
    }

    public int getLgtAssigned() {
        return lgtAssigned;
    }

    public void setLgtAssigned(int lgtAssigned) {
        this.lgtAssigned = lgtAssigned;
    }
    // one tutorial per week, two for two weeks, where one is SGT, one is LGT




    public void print(){        //System.out.println("%-10s %-10s %-10s %-10s\n", "Lecture","Duration", "Students", "Teacher");

        for(int i = 0; i < preferredDays.getPrefDay().size(); i++){
            //System.out.printf("%-10s %-10f %-10s %-10s %-10d\n", sLect, iHours, preferredDays.getPrefDay().get(i) , sTeachersPreference, iNumberOfStudentsAttending);
            System.out.println(sLect + " " +iHours  + " " + preferredDays  + " " +sTeachersPreference  + " " + iNumberOfStudentsAttending + " " + iCode);
        }
    }

    public String toString(){
        return sLect + " "  +Double.toString(iHours) + " " + preferredDays + " " + iNumberOfStudentsAttending + " Scheduled:" + sDayOfWeek + " " + Integer.toString(iCode) +  " " + Integer.toString(iDayScheduled) + " " + Integer.toString(iMonthScheduled)+ " " + Integer.toString(iYearScheduled) + "\n";
    }


    @Override
    public int compareTo(Duplet obj) {
        if(this.iNumberOfStudentsAttending - obj.iNumberOfStudentsAttending == 0){
            //return (int)(this.getPreferredDays().getDayHeuristics().get(0) - obj.getPreferredDays().getDayHeuristics().get(0));
            return obj.sLect.compareTo(this.sLect);
        }
        return obj.iNumberOfStudentsAttending - this.iNumberOfStudentsAttending;
        //return this.iNumberOfStudentsAttending - obj.iNumberOfStudentsAttending;            //tackle the easiest ones first
    }

    @Override
    public Object clone()throws CloneNotSupportedException{
        return super.clone();
    }

}
