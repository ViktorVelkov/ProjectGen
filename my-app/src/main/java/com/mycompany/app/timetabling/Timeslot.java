package com.mycompany.app.timetabling;

import java.util.ArrayList;

public class Timeslot implements Comparable<Timeslot> {
    private int itime;
    private double duration;
    private String sHall = "";
    private String sActivity = "";
    private String sName = "";
    private int iEmpty = 0;
    private ArrayList<Timeperiod> timePeriod = new ArrayList<>();

    public Timeslot(){
    }

    public Timeslot(int itime, double duration, String sHall, String sActivity,  int iEmpty) {
        this.itime = itime;
        this.sHall = sHall;
        this.sActivity = sActivity;
        this.duration = duration;
        this.iEmpty = iEmpty;
    }

    public Timeslot(double duration, int iHour, String sLectureHall, String sActivity){
        this.itime = iHour;
        this.sHall = sLectureHall;
        this.duration = duration;
        this.sActivity = sActivity;

        if((iHour - 30) %100 == 0){
            for(int i = 0; i < duration ; i++){
                timePeriod.add(new Timeperiod(iHour + (i*100), duration, sLectureHall,0));
                timePeriod.add(new Timeperiod((iHour + (i*100)) + 70, duration, sLectureHall, 0));
            }
        }
        else{
            for(int i = 0; i < duration ; i++){
                timePeriod.add(new Timeperiod(iHour + (i*100), duration, sLectureHall,0));
                timePeriod.add(new Timeperiod((iHour + (i*100)) + 30, duration, sLectureHall, 0));
            }
        }

    }



    public void setItime(int itime) {
        this.itime = itime;
    }

    public void setsHall(String sHall) {
        this.sHall = sHall;
    }

    public void setsActivity(String sActivity) {
        this.sActivity = sActivity;
    }

    public void setiEmpty(int iEmpty) {
        this.iEmpty = iEmpty;
    }

    public int getItime() {
        return itime;
    }

    public String getsHall() {
        return sHall;
    }

    public String getsActivity() {
        return sActivity;
    }

    public int getiEmpty() {
        return iEmpty;
    }

    public String getsName() {
        return sName;
    }

    public void setsName(String sName) {
        this.sName = sName;
    }

    public ArrayList<Timeperiod> getTimePeriod() {
        return timePeriod;
    }

    public void setTimePeriod(ArrayList<Timeperiod> timePeriod) {
        this.timePeriod = timePeriod;
    }

    public void print() {

        if (sHall.isEmpty() || sActivity.isEmpty()) {
            System.out.print("T:" + itime + "H: empty" + "\tE: empty");
        }
        else{
            System.out.print("T:" + itime + "H:" + sHall + "\tE:" + sActivity);
        }
    }

    @Override
    public String toString() {
        String sReturn =  "{" +
                ", Hall='" + sHall + '\'' +
                ", Activity='" + sActivity + '\'' +
                ", Avail=" + iEmpty +
                ", Name=" + sName
                + "}\n";
        for(Timeperiod tp : timePeriod){
            sReturn += tp.s_forTimeSlotClass();
        }
        sReturn += "\n"; //test for this one
        return sReturn;
    }



    @Override
    public int compareTo(Timeslot o) {
        if(this.sHall.compareTo(o.sHall) == 0) return this.itime - o.itime;
        else return this.sHall.compareTo(o.sHall);
    }
}


