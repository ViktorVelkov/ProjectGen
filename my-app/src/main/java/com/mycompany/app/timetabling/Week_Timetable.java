package com.mycompany.app.timetabling;

import com.mycompany.app.timetabling.Day;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

    public class Week_Timetable implements Cloneable{

    private int iNumDays;
    private ArrayList<Day> weekTimet = new ArrayList<>();
    private String sStartDay = "";
    private String sEndDay = "";
    private ArrayList<Hall> halls;
    private ArrayList <Duplet> lectures;
    private ArrayList <Duplet> assignedLectures = new ArrayList<>();
    private ArrayList <Duplet> sgt;
    private ArrayList <Duplet> assignedsgt;
    private ArrayList <Duplet> LGT;
    private ArrayList <Duplet> assignedLGT;
    private int previousRecursionWorked = 0;


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

        public ArrayList<Duplet> getSgt() {
            return sgt;
        }

        public void setSgt(ArrayList<Duplet> sgt) {
            this.sgt = (ArrayList<Duplet>) sgt.clone();
//            for(Duplet duplet : lectures){
//                this.sgt.add(duplet);
//            }

        }

        public ArrayList<Duplet> getAssignedsgt() {
            return assignedsgt;
        }

        public void updateDependentOnTutorials(){
            for(int i =0; i <sgt.size(); i++){
                sgt.get(i).setiHours(1.0);
            }
        }

        public void setAssignedsgt(ArrayList<Duplet> assignedsgt) {
            this.assignedsgt = assignedsgt;
        }

        public ArrayList<Day> getWeekTimet() {
        return weekTimet;
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

    public int getPreviousRecursionWorked() {
        return previousRecursionWorked;
    }

    public void setPreviousRecursionWorked(int previousRecursionWorked) {
        this.previousRecursionWorked = previousRecursionWorked;
    }


    public ArrayList<Duplet> getLGT() {
            return LGT;
    }

    public void setLGT(ArrayList<Duplet> LGT) {
            this.LGT = LGT;
    }

    public ArrayList<Duplet> getAssignedLGT() {
            return assignedLGT;
    }

    public void setAssignedLGT(ArrayList<Duplet> assignedLGT) {
            this.assignedLGT = assignedLGT;
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


    public void v_print(){
        System.out.println("Days:" + iNumDays + " Start:" + sStartDay + " End:" + sEndDay + "\nLecturesAssigned:" + assignedLectures);
        for(int i = 0; i < weekTimet.size(); i++){
            System.out.println(weekTimet.get(i));
        }
    }

    public void v_updateDates() throws ParseException {
        String stringDate = "";
        String endDate = "";
        for(int i = 0; i < weekTimet.size(); i++){
            stringDate = Integer.toString(weekTimet.get(i).getiDate()) + "/" + Integer.toString(weekTimet.get(i).getiMonth() + 1) + "/" + Integer.toString(weekTimet.get(i).getiYear()+1900);
            Date dates = new SimpleDateFormat("dd/MM/yyyy").parse(stringDate);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dates);
            calendar.add(Calendar.DAY_OF_MONTH, 7);
            Date date2 = calendar.getTime();

            weekTimet.get(i).setiDate(date2.getDate());
            weekTimet.get(i).setiMonth(date2.getMonth());
            weekTimet.get(i).setiYear(date2.getYear());
            endDate = Integer.toString(weekTimet.get(i).getiDate()) + "/" + Integer.toString(weekTimet.get(i).getiMonth() + 1) + "/" + Integer.toString(weekTimet.get(i).getiYear()+1900);
        }
        this.setsStartDay(this.getsEndDay());
        this.setsEndDay(endDate);
    }

    public String toString(){
        return "Empty, use v_print";
    }

        @Override
        public Object clone()throws CloneNotSupportedException{
            return super.clone();
        }
}
