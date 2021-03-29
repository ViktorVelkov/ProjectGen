package com.mycompany.app.timetabling;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Timeperiod implements Cloneable{
    private int iTime;
    private int iAvailable;
    private String sDay;
    private String sLectureHall;
    private int iDate;
    private int iMonth;
    private int iYear;

    public Timeperiod(int iHour, double v, String sLectureHall, int i){
        this.iTime = iHour;
        this.sLectureHall = sLectureHall;
        this.iAvailable = i;
    }

    public Timeperiod(int iTime, String sDay) {
        this.iTime = iTime;
        this.sDay = sDay;
    }

    public Timeperiod(String sDay, int iAvailable, int iTime, int iDate, int iMonth, int iYear){
        this.sDay = sDay;
        this.iAvailable =iAvailable;
        this.iDate = iDate;
        this.iTime = iTime;
        this.iMonth = iMonth;
        this.iYear = iYear;

    }

    public void updateTimeslot(int iNumber) throws ParseException {
        String currDate = Integer.toString(iDate) + "/"+Integer.toString(iMonth) + "/" + Integer.toString(iYear);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new SimpleDateFormat("dd/MM/yyyy").parse(currDate));
        calendar.add(Calendar.DATE, iNumber);
        Date newDate = calendar.getTime();
        this.iDate = newDate.getDate();
        this.iMonth =newDate.getMonth() + 1;
        this.iYear = newDate.getYear() + 1900;
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

    public String s_forTimeSlotClass(){
        return Integer.toString(iTime) + " " + sLectureHall + " " + Integer.toString(iAvailable) + "\n";
    }




    @Override
    public int hashCode() {
//        private int itime = 0;
//        private double duration=0.0;
//        private String sHall = "";
//        private String sActivity = "";
//        private String sName = "";
//        private int iEmpty = 0;
//        private ArrayList<Timeperiod> timePeriod = new ArrayList<>();


        final int prime = 7;
        final int prime2 = 41;
        final int prime3 = 101;
        int result = 1;
        result = prime * this.iDate + ((this.sDay == null) ? 0 : this.sDay.hashCode());
        result += (int)prime3*this.iMonth  + prime2*this.iYear;
        result += this.iTime/10;
        if(!sLectureHall.isEmpty())result += sLectureHall.hashCode();
        return result;
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
        return "{" +
                "" + iTime +
                ", " + iAvailable +
                ", " + sDay +
                ", " + iDate +
                ", " + iMonth +
                ", " + iYear +
                '}' + "\n";
    }
    @Override
    public Object clone()throws CloneNotSupportedException{
        return super.clone();
    }

}
