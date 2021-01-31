package com.mycompany.app;

import java.util.ArrayList;

public class Week_Timetable {

    private int iNumDays;
    private ArrayList<Day> weekTimet = new ArrayList<>();

    public Week_Timetable() {
        this.iNumDays = 7;
        for (int i = 0; i < iNumDays; i++) {
            weekTimet.add(new Day());
        }
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



    public ArrayList<Day> getWeekTimet() {
        return weekTimet;
    }

    public void setWeekTimet(ArrayList<Day> weekTimet) {
        this.weekTimet = weekTimet;
    }

    public void setiNumDays(int iNumDays) {
        this.iNumDays = iNumDays;
    }

    void v_print(){

    }
}
