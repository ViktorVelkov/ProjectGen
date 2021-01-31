package com.mycompany.app;

public class Duplet {

    private int iHours;
    private String sLect;

    public Duplet(String sLect,int iHours){
        this.iHours = iHours;
        this.sLect = sLect;
    }

    public int getiHours() {
        return iHours;
    }

    public void setiHours(int iHours) {
        this.iHours = iHours;
    }

    public String getsLect() {
        return sLect;
    }

    public void setsLect(String sLect) {
        this.sLect = sLect;
    }
    public String toString(){
        return sLect + " " + Integer.toString(iHours);
    }
}
