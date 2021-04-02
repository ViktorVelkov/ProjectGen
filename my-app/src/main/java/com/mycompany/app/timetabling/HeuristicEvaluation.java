package com.mycompany.app.timetabling;

import java.util.ArrayList;
import java.util.Arrays;

public class HeuristicEvaluation {

    private double dHeuristics = 0.0;
    private int numPeopleCompleteSatisfiability = 0;
    private SGT eventEvaluated;  //remove
    private SGT eventEvaluated_swapped;  //remove
    private double rawHeuristics;
    private double scaledHeuristics;
    private ArrayList<Integer> daySatisfiability = new ArrayList<>();
    private ArrayList<Integer> hourSatisfiability = new ArrayList<>();
    private double iTakeIntoConsideration =0; //if swapping two sgts, use this to show the heuristic outcome for the second sgt
    private SGT sgt;       //remove
    private String sDayOfWeek = "";
    private String sHall = "";
    private int iDate;
    private int iMonth;
    private int iYear;
    private int iHour;
    public HeuristicEvaluation(){}

    public void calculateFromTwoObjects(HeuristicEvaluation h1, HeuristicEvaluation h2,HeuristicEvaluation h3, HeuristicEvaluation h4){
        //h1 == original position of the sgt1 to swap, h2 = new position tested of the sgt1,h3 == original position of the sgt2 to swap, h4 = new position tested of the sgt2
        this.setEventEvaluated(h1.getSgt());
        this.setEventEvaluated_swapped(h2.getSgt());
        //how to do the heuristics here????????
        // original might be very high
        if(h1.getdHeuristics() >= h2.getdHeuristics()){ // if the heuristics of the new position have not improved the originals of sgt1
            if(h3.getdHeuristics() >= h4.getdHeuristics()){ // and the heuristics of the new position of the sgt2 do not improve the originals
                    //no improvement over the swap of the two
                this.dHeuristics = 0;
                this.rawHeuristics = 0;
                this.scaledHeuristics = 0.0;
            }
        }

        if(h3.getdHeuristics() >= h4.getdHeuristics()){ // if the heuristics of the new position have not improved the originals of sgt1
            if(h1.getdHeuristics() >= h2.getdHeuristics()){ // and the heuristics of the new position of the sgt2 do not improve the originals
                //no improvement over the swap of the two
                this.dHeuristics = 0;
                this.rawHeuristics = 0;
                this.scaledHeuristics = 0.0;
            }
        }


        // check if beneficial ....
        if(h1.getdHeuristics() <= h2.getdHeuristics()){
            if(h3.getdHeuristics() > h4.getdHeuristics())
                if( h2.getdHeuristics() > h1.getdHeuristics() + (h3.getdHeuristics() - h4.getdHeuristics()) ){
                    // beneficial
                    this.dHeuristics = h2.getdHeuristics();
                    this.rawHeuristics = h2.getRawHeuristics();
                    this.scaledHeuristics = h2.getScaledHeuristics();
                    this.iTakeIntoConsideration = h4.getiHour() - h3.getdHeuristics();
                }
            else
            if( h2.getdHeuristics() > h1.getdHeuristics() + (h4.getdHeuristics() - h3.getdHeuristics()) ){
                // beneficial
                this.dHeuristics = h2.getdHeuristics();
                this.rawHeuristics = h2.getRawHeuristics();
                this.scaledHeuristics = h2.getScaledHeuristics();
                this.iTakeIntoConsideration = h3.getiHour() - h4.getdHeuristics();
            }
        }

    }


    public String getsHall() {
        return sHall;
    }

    public void setsHall(String sHall) {
        this.sHall = sHall;
    }

    public void setDaySatisfiability(ArrayList<Integer> daySatisfiability) {
        this.daySatisfiability = daySatisfiability;
    }

    public void setHourSatisfiability(ArrayList<Integer> hourSatisfiability) {
        this.hourSatisfiability = hourSatisfiability;
    }

    public void setEventEvaluated(SGT eventEvaluated) {
        this.eventEvaluated = eventEvaluated;
    }

    public SGT getEventEvaluated_swapped() {
        return eventEvaluated_swapped;
    }

    public void setEventEvaluated_swapped(SGT eventEvaluated_swapped) {
        this.eventEvaluated_swapped = eventEvaluated_swapped;
    }

    public double getdHeuristics() {
        return dHeuristics;
    }

    public void setdHeuristics(double dHeuristics) {
        this.dHeuristics = dHeuristics;
    }

    public int getNumPeopleCompleteSatisfiability() {
        return numPeopleCompleteSatisfiability;
    }

    public void setNumPeopleCompleteSatisfiability(int numPeopleCompleteSatisfiability) {
        this.numPeopleCompleteSatisfiability = numPeopleCompleteSatisfiability;
    }


    public SGT getSgt() {
        return sgt;
    }

    public void setSgt(SGT sgt) {
        this.sgt = sgt;
    }


    public String getsDayOfWeek() {
        return sDayOfWeek;
    }

    public void setsDayOfWeek(String sDayOfWeek) {
        this.sDayOfWeek = sDayOfWeek;
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

    public int getiHour() {
        return iHour;
    }

    public void setiHour(int iHour) {
        this.iHour = iHour;
    }

    public double getRawHeuristics() {
        return rawHeuristics;
    }

    public void setRawHeuristics(double rawHeuristics) {
        this.rawHeuristics = rawHeuristics;
    }

    public double getScaledHeuristics() {
        return scaledHeuristics;
    }

    public void setScaledHeuristics(double scaledHeuristics) {
        this.scaledHeuristics = scaledHeuristics;
    }

    public ArrayList<Integer> getDaySatisfiability() {
        return daySatisfiability;
    }

    public ArrayList<Integer> getHourSatisfiability() {
        return hourSatisfiability;
    }

    public SGT getEventEvaluated() {
        return eventEvaluated;
    }

    public void addToDaySatisfiability(int kings_id_token){
        this.daySatisfiability.add(kings_id_token);
    }

    public void addToHourSatisfiability(int kings_id){
        this.daySatisfiability.add(kings_id);
    }
}
