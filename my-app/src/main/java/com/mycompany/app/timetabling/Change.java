package com.mycompany.app.timetabling;

public class Change implements Comparable<Change>, Cloneable{
    private HeuristicEvaluation evalMain;                               //the sgt that the step/change is started from
    private HeuristicEvaluation evalSecondary;                          //the sgt or the timeslot the step is made towards to
    private SGT sgtMain;
    private SGT sgtSecondary;
    private int iCodeMain = -1;
    private int iCodeSecondary = -1;
    private double finalEffect = 0;
    private String sDayMain = "";
    private String sDaySecondary;
    private String sHallMain = "";
    private String sHallSecondary = "";
    private int iHourMain = 0;
    private int iHourSecondary = 0;

    public Change() {
    }



    public double getFinalEffect() {
        return finalEffect;
    }

    public void setFinalEffect(double finalEffect) {
        this.finalEffect = finalEffect;
    }

    public HeuristicEvaluation getEvalMain() {
        return evalMain;
    }

    public void setEvalMain(HeuristicEvaluation evalMain) {
        this.evalMain = evalMain;
    }

    public HeuristicEvaluation getEvalSecondary() {
        return evalSecondary;
    }

    public void setEvalSecondary(HeuristicEvaluation evalSecondary) {
        this.evalSecondary = evalSecondary;
    }

    public SGT getSgtMain() {
        return sgtMain;
    }

    public void setSgtMain(SGT sgtMain) {
        this.sgtMain = sgtMain;
    }

    public SGT getSgtSecondary() {
        return sgtSecondary;
    }

    public void setSgtSecondary(SGT sgtSecondary) {
        this.sgtSecondary = sgtSecondary;
    }

    public int getiCodeMain() {
        return iCodeMain;
    }

    public void setiCodeMain(int iCodeMain) {
        this.iCodeMain = iCodeMain;
    }

    public int getiCodeSecondary() {
        return iCodeSecondary;
    }

    public void setiCodeSecondary(int iCodeSecondary) {
        this.iCodeSecondary = iCodeSecondary;
    }

    public String getsDayMain() {
        return sDayMain;
    }

    public void setsDayMain(String sDayMain) {
        this.sDayMain = sDayMain;
    }

    public String getsDaySecondary() {
        return sDaySecondary;
    }

    public void setsDaySecondary(String sDaySecondary) {
        this.sDaySecondary = sDaySecondary;
    }

    public String getsHallMain() {
        return sHallMain;
    }

    public void setsHallMain(String sHallMain) {
        this.sHallMain = sHallMain;
    }

    public String getsHallSecondary() {
        return sHallSecondary;
    }

    public void setsHallSecondary(String sHallSecondary) {
        this.sHallSecondary = sHallSecondary;
    }

    public int getiHourMain() {
        return iHourMain;
    }

    public void setiHourMain(int iHourMain) {
        this.iHourMain = iHourMain;
    }

    public int getiHourSecondary() {
        return iHourSecondary;
    }

    public void setiHourSecondary(int iHourSecondary) {
        this.iHourSecondary = iHourSecondary;
    }

    @Override
    public int compareTo(Change o) {
        return (int)((o.finalEffect - this.finalEffect)*10000);
    }


    @Override
    public Object clone()throws CloneNotSupportedException{
        return (Change)super.clone();
    }

    public String toString(){
        return ((sgtMain == null)?" null ": sgtMain.toString()) + " || "
                + ((sgtSecondary == null)?" null ": sgtSecondary.toString()) + " || "
                + ((evalMain == null)?" null ": evalMain.toString()) + " || "
                +((evalSecondary == null)?" null ": evalSecondary.toString());
    };

    //either pass the two sgts or pass their parameters
}
