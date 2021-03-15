package com.mycompany.app.timetabling;

import java.util.ArrayList;

public class PreferredDays {
    private ArrayList<String> prefDay;
    private ArrayList<Integer> peopleVoted;
    private ArrayList<Double> dayHeuristics;
    private ArrayList<String> mandatory;
    private int iPrefHour = 0;
    private int iPrefHour_2 = 0;
    private String forsgt_usage;
    private int notAvailableBefore;
    //prefHour1&2 are re-considered to be as an array of
    //suitable times for the lectures

    public PreferredDays(ArrayList<String> prefDay) {
        this.prefDay = new ArrayList<>();
        this.prefDay = prefDay;
    }
    public PreferredDays(){}


    public int getNotAvailableBefore() {
        return notAvailableBefore;
    }

    public void setNotAvailableBefore(int notAvailableBefore) {
        this.notAvailableBefore = notAvailableBefore;
    }

    public String getForsgt_usage() {
        return forsgt_usage;
    }

    public void setForsgt_usage(String forsgt_usage) {
        this.forsgt_usage = forsgt_usage;
    }

    public ArrayList<String> getMandatory() {
        return mandatory;
    }

    public void setMandatory(ArrayList<String> mandatory) {
        this.mandatory = mandatory;
    }

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
            text += prefDay.get(i) + "(" + /*Integer.toString(peopleVoted.get(i)) +*/ ")" ;
            //if(dayHeuristics.size() != 0) text += "(" + Double.toString(dayHeuristics.get(i)) + ")" ;
        }
        text += "";
        return text;
    }
}
