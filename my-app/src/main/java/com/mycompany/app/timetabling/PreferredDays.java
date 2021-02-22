package com.mycompany.app.timetabling;

import java.util.ArrayList;

public class PreferredDays {
    private ArrayList<String> prefDay;
    private ArrayList<Integer> peopleVoted;
    private ArrayList<Double> dayHeuristics;
    private int iPrefHour;
    private int iPrefHour_2;

    public ArrayList<String> getPrefDay() {
        return prefDay;
    }

    public void setPrefDay(ArrayList<String> prefDay) {
        this.prefDay = prefDay;
    }

    public ArrayList<Integer> getpeopleVoted() {
        return peopleVoted;
    }

    public void setpeopleVoted(ArrayList<Integer> peopleVoted) {
        this.peopleVoted = peopleVoted;
    }

    public void setiPrefHour(int peopleVoted) {
        this.iPrefHour = peopleVoted;
    }


    public void setiPrefHour_2(int peopleVoted) {
        this.iPrefHour_2 = peopleVoted;
    }



    public void setDayHeuristics(ArrayList<Double> dayHeuristics) {
        this.dayHeuristics = dayHeuristics;
    }

    public ArrayList<Double> getDayHeuristics() {
        return dayHeuristics;
    }

    public int getiPrefHour() {
        return iPrefHour;
    }

    public int getiPrefHour_2() {
        return iPrefHour_2;
    }


    @Override
    public String toString() {
        String text = "Day(s): ";
        for(int i = 0; i < prefDay.size(); i++){
            text += prefDay.get(i) + "(" + Integer.toString(peopleVoted.get(i)) + ")" ;
            if(dayHeuristics.size() != 0) text += "(" + Double.toString(dayHeuristics.get(i)) + ")" ;
        }
        text += "";
        return text;
    }
}
