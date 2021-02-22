package com.mycompany.app.timetabling;

public class Timeslot {
    private int itime;
    private String sHall = "";
    private String sActivity = "";
    private int iEmpty = 0;

    public Timeslot(){

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

    public void print() {

        if (sHall.isEmpty() || sActivity.isEmpty()) {
            System.out.print("T:" + itime + "H: empty" + "\tE: empty");
        }
        else{
            System.out.print("T:" + itime + "H:" + sHall + "\tE:" + sActivity);
        }
    }
}


