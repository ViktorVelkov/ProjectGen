package com.mycompany.app.timetabling;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Day {

    private String sname = "";
    private String sDate = "";
    private int iDate = 0;
    private int iMonth = 0;
    private int iYear = 0;

    private static Timeslot[] oDSlot = new Timeslot[22];
    private ArrayList<Timeslot> oDslot2 = new ArrayList<>( );
    public Day(){
        int j = 0;
        for(int i = 1; i <= 22; i++) {
            this.oDSlot[i-1] = new Timeslot();
            if (i % 2 == 1) { this.oDSlot[i-1].setItime(900 + ((i-1)/2)*100 ); }
            else{ this.oDSlot[i-1].setItime(900 + ((i-1)/2) * 100 + 30); }
        }
    }

    public Day(String sDate){
        this.sDate = sDate;
        for(int i = 1; i <= 22; i++) {
            this.oDSlot[i-1] = new Timeslot();
            if (i % 2 == 1) { this.oDSlot[i-1].setItime(900 + ((i-1)/2)*100 ); }
            else{ this.oDSlot[i-1].setItime(900 + ((i-1)/2) * 100 + 30); }
        }// could be M, T, W, Th, F, S, Su
    }

    public Day(String sname, String sDate){
        this.sname = sname; // could be M, T, W, Th, F, S, Su
        this.sDate = sDate;
        for(int i = 1; i <= 22; i++) {
            this.oDSlot[i-1] = new Timeslot();
            if (i % 2 == 1) { this.oDSlot[i-1].setItime(900 + ((i-1)/2)*100 ); }
            else{ this.oDSlot[i-1].setItime(900 + ((i-1)/2) * 100 + 30); }
        }
    }



    /**/ /*Getters and Setters*/


    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public String getsDate() {
        return sDate;
    }

    public void setsDate(String sDate) {
        this.sDate = sDate;
    }


    public int getiDate() {
        return iDate;
    }

    public void setiDate(int iDate) {
        this.iDate = iDate;
    }

    public int getiMonth() {
        return iMonth;
    }

    public void setiMonth(int iMonth) {
        this.iMonth = iMonth;
    }

    public int getiYear() {
        return iYear;
    }

    public void setiYear(int iYear) {
        this.iYear = iYear;
    }

    public ArrayList<Timeslot> getoDslot2() {
        return oDslot2;
    }

    public void setoDslot2(ArrayList<Timeslot> oDslot2) {
        this.oDslot2 = oDslot2;
    }

    /**/

    public void v_assignEvent(int iHourStart, double duration, String sHall, String sActivity, String sLecture){
        //old functionality of the code
        //        for(int i = 0; i < oDSlot.length; i++){
//            if(oDSlot[i].getItime() >= sTimeStart && oDSlot[i].getItime() < sTimeEnd) {
//                oDSlot[i].setiEmpty(0);
//                oDSlot[i].setsActivity(sActivity);
//                oDSlot[i].setsHall(sHall);
//                oDSlot[i].setsName(sLecture);
//            }
//        }
        oDslot2.add(new Timeslot(duration, iHourStart, sHall,sActivity));
    }


    //don't use this one works with the previous functionality of the class
    public void v_reassign_event(int sTimeStart, int sTimeEnd, String sHall, String sActivity, String sLecture){
        //this means assigning to a slot already taken,
        // if taken, remove the whole event, and put the new one
        System.out.println("Please don't use me, I am v_reassign_event inside the Day class");

        int iFlag = 0;
        String s_reassign = "";
        for(int i = 0; i < oDSlot.length ; i++) {
            if (oDSlot[i].getItime() == sTimeStart) {
                if (oDSlot[i].getiEmpty() == 1) {
                    //event taken, reassign
                    s_reassign = oDSlot[i].getsActivity();
                }
            }
        }

            for (int i = 0; i < oDSlot.length; i++) {
                if (oDSlot[i].getsActivity().equals(s_reassign)) {
                    oDSlot[i].setiEmpty(0);
                }
            }
        v_assignEvent(sTimeStart,sTimeEnd,sHall,sActivity, sLecture);
    }

    public void v_anull(){
        oDSlot = new Timeslot[22];
        for(int i = 0;i < 22; i++){
            oDSlot[i] = new Timeslot();
        }
    }

    public int i_count_activities(){
        return 0;
    }

    public void updateDate() throws ParseException {
        String stringDate = "";
        String endDate = "";
        String currDate = Integer.toString(iDate) + "/"+Integer.toString(iMonth + 1) + "/" + Integer.toString(iYear + 1900);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new SimpleDateFormat("dd/MM/yyyy").parse(currDate));
        calendar.add(Calendar.DATE, 7);
        Date newDate = calendar.getTime();
        this.iDate = newDate.getDate();
        this.iMonth =newDate.getMonth() + 1;
        this.iYear = newDate.getYear() + 1900;
    }


    public void print(){
        if(sDate.isEmpty()){ System.out.println("Date:empty"); }
        else {System.out.println("Date:" + sDate + "\tDay of the week:" + sname);}
        for(int i = 0; i < 22; i++){
            if(oDSlot[i].getiEmpty() == 1)  {
                oDSlot[i].print();
                System.out.println();
            }
        }
        if(oDSlot[oDSlot.length - 1].getiEmpty() == 1){
            System.out.println("Rest Is Empty");
        }
    }

    /**/

    @Override
    public String toString() {
        String sResult =  "Day{" +
                "sname='" + sname +
                ", sDate='" + sDate  +
                ", iDate=" + iDate +
                ", iMonth=" + iMonth +
                ", iYear=" + iYear +
                '}' + "\n";
        for(Timeslot t: oDslot2){        //if you want the previous functionality, which was wrong, because it overlapped
                                        // events, swithc back to oDSlot
            sResult += t.toString() + "\n";
        }
        return sResult;
    }


}

