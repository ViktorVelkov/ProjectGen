package com.mycompany.app.timetabling;

public class Timeperiod {
    private int iTime;
    private int iAvailable;
    private String sDay;
    private int iDate;
    private int iMonth;
    private int iYear;

    public Timeperiod(){}

    public Timeperiod(String sDay, int iAvailable, int iTime, int iDate, int iMonth, int iYear){
        this.iAvailable =iAvailable;
        this.iDate = iDate;
        this.iTime = iTime;
        this.iMonth = iMonth;
        this.iYear = iYear;

    }


    public String getsDay() {
        return sDay;
    }

    public void setsDay(String sDay) {
        this.sDay = sDay;
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

    public int getiTime() {
        return iTime;
    }

    public void setiTime(int iTime) {
        this.iTime = iTime;
    }

    public int getiAvailable() {
        return iAvailable;
    }

    public void setiAvailable(int iAvailable) {
        this.iAvailable = iAvailable;
    }



    @Override
    public String toString() {
        return "Timeperiod{" +
                "iTime=" + iTime +
                ", iAvailable=" + iAvailable +
                ", iDate=" + iDate +
                ", iMonth=" + iMonth +
                ", iYear=" + iYear +
                '}' + "\n";
    }


}
