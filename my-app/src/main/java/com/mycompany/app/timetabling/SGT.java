package com.mycompany.app.timetabling;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;

public class SGT implements Comparable<SGT>, Cloneable{

    private double iHours = 0.0;
    private String sLect = "";
    private PreferredDays preferredDays;
    private int iSemester = 0;
    private int iNumberOfStudentsAttending = 0;
    private int iCode = 0;
    private int iAdditionalCode;
    private int iSizeOfHall = 0;
    private String sLectureHall = "";
    private String sDayOfWeek = "";
    private int iHourScheduled = 0;
    private int iDayScheduled = -1;
    private int iMonthScheduled = -1;
    private int iYearScheduled = -1;
    private String dayAssigned = "";
    //    private ArrayList<CoupledData> constraintsAllocation;
    //private ArrayList<Duplet> dependentOn;
    private HashSet<SGT> dependentOn = new HashSet<>();
    private ArrayList<Student> students_assigned = new ArrayList<>();
    private ArrayList<Timeslot> notAvailableSlots =new ArrayList<>();
    private ArrayList<Timeslot> availableSlots = new ArrayList<>();
    private ArrayList<Timeslot> availableSlots_noDependencies = new ArrayList<>();
    private ArrayList<Integer> codesOfStudentsAssigned= new ArrayList<>();

    public SGT(){}
    public SGT(String sAbrev, String sDayOfWeek, int iHourScheduled, double dDuration){
        this.sLect = sAbrev;
        this.sDayOfWeek = sDayOfWeek;
        this.iHourScheduled = iHourScheduled;
        this.iHours = dDuration;
    }
 public SGT(String sAbrev, String sDayOfWeek, String sLectureHall,int iHourScheduled, double dDuration){
        this.sLect = sAbrev;
        this.sDayOfWeek = sDayOfWeek;
        this.iHourScheduled = iHourScheduled;
        this.iHours = dDuration;
        this.sLectureHall = sLectureHall;
    }


    public void setDependentOn(HashSet<SGT> dependentOn) {
        this.dependentOn = dependentOn;
    }

    public int getiAdditionalCode() {
        return iAdditionalCode;
    }

    public void setiAdditionalCode(int iAdditionalCode) {
        this.iAdditionalCode = iAdditionalCode;
    }

    public ArrayList<Timeslot> getAvailableSlots_noDependencies() {
        return availableSlots_noDependencies;
    }

    public void setAvailableSlots_noDependencies(ArrayList<Timeslot> availableSlots_noDependencies) {
        this.availableSlots_noDependencies = availableSlots_noDependencies;
    }

    public ArrayList<Integer> getCodesOfStudentsAssigned() {
        return codesOfStudentsAssigned;
    }

    public void setCodesOfStudentsAssigned(ArrayList<Integer> codesOfStudentsAssigned) {
        this.codesOfStudentsAssigned = codesOfStudentsAssigned;
    }

    public void addToCodesOfStudentsAssigned(int iCode){
        this.codesOfStudentsAssigned.add(iCode);
    }

    public int getiSizeOfHall() {
        return iSizeOfHall;
    }

    public void setiSizeOfHall(int iSizeOfHall) {
        this.iSizeOfHall = iSizeOfHall;
    }

    public HashSet<SGT> getDependentOn() {
        return dependentOn;
    }


    public void addToDependentOn(SGT duplet) throws CloneNotSupportedException { this.dependentOn.add((SGT)duplet.clone()); }

    public double getiHours() {
        return iHours;
    }

    public void setiHours(double iHours) {
        this.iHours = iHours;
    }

    public String getsLect() {
        return sLect;
    }

    public void setsLect(String sLect) {
        this.sLect = sLect;
    }

    public int getiSemester() {
        return iSemester;
    }

    public void setiSemester(int iSemester) {
        this.iSemester = iSemester;
    }


    public int getiNumberOfStudentsAttending() {
        return iNumberOfStudentsAttending;
    }

    public void setiNumberOfStudentsAttending(int iNumberOfStudentsAttending) {
        this.iNumberOfStudentsAttending = iNumberOfStudentsAttending;
    }

    public PreferredDays getPreferredDays() {
        return preferredDays;
    }

    public void setPreferredDays(PreferredDays preferredDays) {
        this.preferredDays = preferredDays;
    }


    public int getiCode() {
        return iCode;
    }

    public void setiCode(int iCode) {
        this.iCode = iCode;
    }


    public String getsDayOfWeek() {
        return sDayOfWeek;
    }

    public void setsDayOfWeek(String sDayOfWeek) {
        this.sDayOfWeek = sDayOfWeek;
    }

    public int getiDayScheduled() {
        return iDayScheduled;
    }

    public void setiDayScheduled(int iDayScheduled) {
        this.iDayScheduled = iDayScheduled;
    }

    public int getiMonthScheduled() {
        return iMonthScheduled;
    }

    public void setiMonthScheduled(int iMonthScheduled) {
        this.iMonthScheduled = iMonthScheduled;
    }

    public int getiYearScheduled() {
        return iYearScheduled;
    }

    public void setiYearScheduled(int iYearScheduled) {
        this.iYearScheduled = iYearScheduled;
    }

    public int getiHourScheduled() {
        return iHourScheduled;
    }

    public void setiHourScheduled(int iHourScheduled) {
        this.iHourScheduled = iHourScheduled;
    }

    public String getsLectureHall() {
        return sLectureHall;
    }

    public void setsLectureHall(String sLectureHall) {
        this.sLectureHall = sLectureHall;
    }

    public String getDayAssigned() {
        return dayAssigned;
    }

    public void setDayAssigned(String dayAssigned) {
        this.dayAssigned = dayAssigned;
    }

    public ArrayList<Timeslot> getNotAvailableSlots() {
        return notAvailableSlots;
    }

    public void setNotAvailableSlots(ArrayList<Timeslot> notAvailableSlots) {
        this.notAvailableSlots = notAvailableSlots;
    }

    public ArrayList<Timeslot> getAvailableSlots() {
        return availableSlots;
    }

    public void setAvailableSlots(ArrayList<Timeslot> availableSlots) {
        this.availableSlots = availableSlots;
    }




    public void updateDate(int iNumber) throws ParseException {
        String currDate = Integer.toString(iDayScheduled) + "/"+Integer.toString(iMonthScheduled) + "/" + Integer.toString(iYearScheduled);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new SimpleDateFormat("dd/MM/yyyy").parse(currDate));
        calendar.add(Calendar.DATE, iNumber);
        Date newDate = calendar.getTime();
        this.iDayScheduled = newDate.getDate();
        this.iMonthScheduled =newDate.getMonth() + 1;
        this.iYearScheduled = newDate.getYear() + 1900;
    }



    public void print(){

        for(int i = 0; i < preferredDays.getPrefDay().size(); i++){
            System.out.println(sLect + " " + sLectureHall + " " +iHours  + " " + preferredDays  + " " + iNumberOfStudentsAttending + " " + iCode);
        }
    }

    public String toString(){
        return sLect + " " + sLectureHall + " " +Double.toString(iHours)  + " " + iNumberOfStudentsAttending + " " + sDayOfWeek + " "  + iHourScheduled + " Date: " + Integer.toString(iDayScheduled) + " " + Integer.toString(iMonthScheduled)+ " " + Integer.toString(iYearScheduled) + "\n";
    }


    @Override
    public int compareTo(SGT obj) {


//        if(obj.sLect.equals(this.sLect)){
//            //return (int)(this.getPreferredDays().getDayHeuristics().get(0) - obj.getPreferredDays().getDayHeuristics().get(0));
//            return this.sLectureHall.compareTo(obj.sLectureHall);
//        }
//        return this.sLect.compareTo(obj.sLect);
//        //return this.iNumberOfStudentsAttending - obj.iNumberOfStudentsAttending;            //tackle the easiest ones first
        if(this.sDayOfWeek.equals(obj.sDayOfWeek)) return this.iHourScheduled - obj.iHourScheduled;
        return this.sDayOfWeek.compareTo(obj.sDayOfWeek);
    }

    @Override
    public Object clone()throws CloneNotSupportedException{
        return super.clone();
    }

    public int hashCode() {
        int iPrime = 17;
        int result = ((sLect.isEmpty())?0:sLect.hashCode());
        result+= ((sLectureHall.isEmpty())?0:sLectureHall.hashCode());
        result+= ((sDayOfWeek.isEmpty())?0:sDayOfWeek.hashCode());
        result += iHourScheduled*iPrime;
        result += iHours*iPrime;
        result += iNumberOfStudentsAttending;
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
        SGT other = (SGT) obj;
        if (this.hashCode() != 0) {
            if (other.hashCode() == 0)
                return false;
        }
        if (this.hashCode() != (other.hashCode()))
            return false;
        return true;
    }



}
