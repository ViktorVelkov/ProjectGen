package com.mycompany.app.timetabling;

import com.mycompany.app.timetabling.Day;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Week_Timetable {

    private int iNumDays;
    private ArrayList<Day> weekTimet = new ArrayList<>();
    private String sStartDay = "";
    private String sEndDay = "";
    ArrayList<Hall> halls;
    ArrayList <Duplet> lectures;
    ArrayList <Duplet> assignedLectures = new ArrayList<>();

    public Week_Timetable() {
//        this.iNumDays = 6;
//        //only six, I include Saturday here, because if there aren't many available
//        //halls Saturday and Friday could be used for lectures etc.
//        for (int i = 0; i < iNumDays; i++) {
//            weekTimet.add(new Day());
//        }
    }


    public Week_Timetable(int iNumDays){
        this.iNumDays = iNumDays;
        for(int i = 0 ; i < iNumDays; i++){
            weekTimet.add(new Day());
        }
    }

    public int getiNumDays() {
        return iNumDays;
    }

    public Day getDay(int index){
        return this.weekTimet.get(index);
    }

    public void addDay(int iDate, int iMonth, int iYear, String sDay){
        Day date = new Day();
        date.setiDate(iDate);
        date.setiMonth(iMonth);
        date.setiYear(iYear);
        date.setSname(sDay);

        weekTimet.add(date);
    }

    public ArrayList<Day> getWeekTimet() {
        return weekTimet;
    }

    public void v_print(){
        System.out.println("Days:" + iNumDays + " Start:" + sStartDay + " End:" + sEndDay + " LecturesAssigned:" + assignedLectures);
        for(int i = 0; i < weekTimet.size(); i++){
            System.out.println(weekTimet.get(i));
        }
    }

    public ArrayList<Duplet> getAssignedLectures() {
        return assignedLectures;
    }

    public void setAssignedLectures(ArrayList<Duplet> assignedLectures) {
        this.assignedLectures = assignedLectures;
    }

    public void setiNumDays(int iNumDays) {
        this.iNumDays = iNumDays;
    }

    public String getsStartDay() {
        return sStartDay;
    }

    public void setsStartDay(String sStartDay) {
        this.sStartDay = sStartDay;
    }

    public String getsEndDay() {
        return sEndDay;
    }

    public void setsEndDay(String sEndDay) {
        this.sEndDay = sEndDay;
    }

    public ArrayList<Hall> getHalls() {
        return halls;
    }

    public void setHalls(ArrayList<Hall> halls) {
        this.halls = halls;
    }

    public ArrayList<Duplet> getLectures() {
        return lectures;
    }

    public void setLectures(ArrayList<Duplet> lectures) {
        this.lectures = lectures;
    }

    public int assingLecture(Date date, ArrayList<Hall> halls)throws ParseException {


        for(int i = 0; i < weekTimet.size(); i++){
            if(weekTimet.get(i).getiDate() == date.getDate()){                                 //check if halls are available at that day

                return 1;
            }
        }
        return 0;
    }

    public String toString(){
        return "Empty, use v_print";
    }
}
