package com.mycompany.app.timetabling;

import java.util.ArrayList;
import java.util.Arrays;

public class Timeslot implements Comparable<Timeslot> {

    private int itime = 0;
    private double duration=0.0;
    private String sHall = "";
    private String sActivity = "";
    private String sName = "";
    private int iEmpty = 0;
    private ArrayList<Timeperiod> timePeriod = new ArrayList<>();
    private int iAttending = 0;
    public Timeslot(){
    }

    public Timeslot(int itime, double duration) {
        this.itime = itime;
        this.duration = duration;
        if((itime - 30) %100 == 0){
            for(int i = 0; i < duration ; i++){
//                timePeriod.add(new Timeperiod(itime + (i*100), duration, sLectureHall,1));
//                timePeriod.add(new Timeperiod((itime + (i*100)) + 70, duration, sLectureHall, 1));
            }
        }
        else{
            for(int i = 0; i < duration ; i++){
//                timePeriod.add(new Timeperiod(itime + (i*100), duration, sLectureHall,1));
//                timePeriod.add(new Timeperiod((itime + (i*100)) + 30, duration, sLectureHall, 1));
            }
        }


    }

    public Timeslot(double duration, int iHour, String sLectureHall, String sActivity){
        this.itime = iHour;
        this.sHall = sLectureHall;
        this.duration = duration;
        this.sActivity = sActivity;

        if((iHour - 30) %100 == 0){
            for(int i = 0; i < duration ; i++){
                timePeriod.add(new Timeperiod(iHour + (i*100), duration, sLectureHall,1));
                timePeriod.add(new Timeperiod((iHour + (i*100)) + 70, duration, sLectureHall, 1));
            }
        }
        else{
            for(int i = 0; i < duration ; i++){
                timePeriod.add(new Timeperiod(iHour + (i*100), duration, sLectureHall,1));
                timePeriod.add(new Timeperiod((iHour + (i*100)) + 30, duration, sLectureHall, 1));
            }
        }

    }

    public Timeslot(double duration, String sDay, int iHour, String sLectureHall, String sActivity, int iDayOfWeek, int iMonth, int iYear){
        this.itime = iHour;
        this.sHall = sLectureHall;
        this.duration = duration;
        this.sActivity = sActivity;

        if((iHour - 30) %100 == 0){
            for(int i = 0; i < duration ; i++){

                timePeriod.add( new Timeperiod(sDay,1, iHour + (i*100), iDayOfWeek, iMonth, iYear));
                timePeriod.add( new Timeperiod(sDay,1, iHour + (i*100) + 70, iDayOfWeek, iMonth, iYear));

            }
        }
        else{
            for(int i = 0; i < duration ; i++){

               timePeriod.add( new Timeperiod(sDay,1, iHour + (i*100), iDayOfWeek, iMonth, iYear));
               timePeriod.add( new Timeperiod(sDay,1, iHour + (i*100) + 30, iDayOfWeek, iMonth, iYear));

            }
        }

    }

    public void setAvailable(){
        for(Timeperiod timeperiod : this.timePeriod){
            timeperiod.setiAvailable(1);
        }
    }



    public void setUnavailable(){
        for(Timeperiod timeperiod : this.timePeriod){
            timeperiod.setiAvailable(0);
        }
    }



    public int getiAttending() {
        return iAttending;
    }

    public void setiAttending(int iAttending) {
        this.iAttending = iAttending;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
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

    public double getDurationTiperiods(){
        return (double) timePeriod.size();
    }

    public void print() {

        if (sHall.isEmpty() || sActivity.isEmpty()) {
            System.out.print("T:" + itime + "H: empty" + "\tE: empty");
        }
        else{
            System.out.print("T:" + itime + "H:" + sHall + "\tE:" + sActivity);
        }
    }


    //https://www.baeldung.com/java-hashcode
    @Override
    public int compareTo(Timeslot o) {
        if(this.sHall.compareTo(o.sHall) == 0) return this.itime - o.itime;
        else return this.sHall.compareTo(o.sHall);
    }

    @Override
    public int hashCode() {

        final int prime = 31;
        int result = 0;
        result =  itime + ((sHall == null) ? 0 : sHall.hashCode()) + ((sActivity == null) ? 0 : sActivity.hashCode());

        if(!timePeriod.isEmpty()){
            result += ((timePeriod.get(0).getsDay() == null) ? 0 : timePeriod.get(0).getsDay().hashCode());
        }

        //both lines are buggy
        return result;
        //return (int) (prime*duration*itime + ((sActivity == null) ? 1 : sActivity.hashCode())+((sHall == null) ? 1 : sHall.hashCode())+((sName == null) ? 1 : sName.hashCode()));

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
    public String toString() {
        String sReturn =  "{" +
                " H=" + sHall + '\'' +
                " L=" + sActivity + '\'' +
                " Hash=" + Integer.toString(this.hashCode())
                + "}";
        for(Timeperiod tp : this.timePeriod){
            sReturn += tp.toString() + " ";
        }
        sReturn += "\n";
        return sReturn;
    }



}


