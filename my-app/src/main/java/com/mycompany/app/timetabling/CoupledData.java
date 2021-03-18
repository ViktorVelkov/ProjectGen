package com.mycompany.app.timetabling;

public class CoupledData implements Comparable<CoupledData>{
    private int iHour = 0;
    private String sDay = "";

    //those  two are later on added
    private int numPlp;
    private double heuristics;


    public CoupledData() {
    }

    public CoupledData(String sDay, int numPlp, double heuristics) {

        this.sDay = sDay;
        this.numPlp = numPlp;
        this.heuristics = heuristics;
    }

    public CoupledData(int iHour, int numPlp, double heuristics) {
        this.iHour = iHour;
        this.numPlp = numPlp;
        this.heuristics = heuristics;
    }

    public int getiHour() {
        return iHour;
    }

    public void setiHour(int iHour) {
        this.iHour = iHour;
    }

    public String getsDay() {
        return sDay;
    }

    public void setsDay(String sDay) {
        this.sDay = sDay;
    }

    public int getNumPlp() {
        return numPlp;
    }

    public void setNumPlp(int numPlp) {
        this.numPlp = numPlp;
    }

    public double getHeuristics() {
        return heuristics;
    }

    public void setHeuristics(double heuristics) {
        this.heuristics = heuristics;
    }

    @Override
    public int compareTo(CoupledData o) {
        if(this.heuristics - o.heuristics == 0){return o.numPlp - this.numPlp;}
        //return (int) (this.heuristics - o.heuristics);
        return (int) (o.heuristics - this.heuristics);

    }
}
