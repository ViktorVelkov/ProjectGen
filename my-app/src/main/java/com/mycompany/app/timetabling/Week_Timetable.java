package com.mycompany.app.timetabling;

import java.sql.PreparedStatement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

    public class Week_Timetable implements Cloneable{

    private int iNumDays;
    private int iWeekNum;
    private ArrayList<Day> weekTimet = new ArrayList<>();
    private String sStartDay = "";
    private String sEndDay = "";
    private String sTableStudents = "";
    private String sTableCoures = "";
    private ArrayList<Hall> halls = new ArrayList<>();
    private ArrayList <Duplet> lectures = new ArrayList<>();
    private ArrayList <Duplet> assignedLectures = new ArrayList<>();
    private ArrayList <Duplet> unassignedLectures = new ArrayList<>();
    private ArrayList <Duplet> sgt = new ArrayList<>();
    private ArrayList <Duplet> assignedsgt = new ArrayList<>();
    private ArrayList<SGT> unassignedsgt = new ArrayList<SGT>();
    private ArrayList <Duplet> LGT = new ArrayList<>();
    private ArrayList <Duplet> assignedLGT = new ArrayList<>();
    private int previousRecursionWorked = 0;


        public Week_Timetable() {
//        this.iNumDays = 6;
//        //only six, I include Saturday here, because if there aren't many available
//        //halls Saturday and Friday could be used for lectures etc.
//        for (int i = 0; i < iNumDays; i++) {
//            weekTimet.add(new Day());
//        }
    }


    public Week_Timetable(int iNumDays){
        this.iNumDays = iNumDays;
        for(int i = 0 ; i < iNumDays; i++){
            weekTimet.add(new Day());
        }
    }

        public String getsTableStudents() {
            return sTableStudents;
        }

        public void setsTableStudents(String sTableStudents) {
            this.sTableStudents = sTableStudents;
        }

        public String getsTableCoures() {
            return sTableCoures;
        }

        public void setsTableCoures(String sTableCoures) {
            this.sTableCoures = sTableCoures;
        }

        public int getiNumDays() {
        return iNumDays;
    }

    public Day getDay(int index){
        return this.weekTimet.get(index);
    }

    public void addDay(int iDate, int iMonth, int iYear, String sDay){
        Day date = new Day();
        date.setiDate(iDate);
        date.setiMonth(iMonth);
        date.setiYear(iYear);
        date.setSname(sDay);

        weekTimet.add(date);
    }

        public ArrayList<Duplet> getSgt() {
            return sgt;
        }

        public void setSgt(ArrayList<Duplet> sgt) {
            this.sgt = (ArrayList<Duplet>) sgt.clone();
//            for(Duplet duplet : lectures){
//                this.sgt.add(duplet);
//            }

        }


        public int getiWeekNum() {
            return iWeekNum;
        }

        public void setiWeekNum(int iWeekNum) {
            this.iWeekNum = iWeekNum;
        }

        public ArrayList<SGT> getUnassignedsgt() {
            return unassignedsgt;
        }

        public void setUnassignedsgt(ArrayList<SGT> unassignedsgt) {
            this.unassignedsgt = unassignedsgt;
        }

        public void addToUnassignedSGT(SGT event) throws CloneNotSupportedException {
            this.unassignedsgt.add((SGT) event.clone());
        }
        public void addToUnassignedLectures(Duplet event) throws CloneNotSupportedException {
            this.unassignedLectures.add((Duplet) event.clone());
        }

        public ArrayList<Duplet> getAssignedsgt() {
            return assignedsgt;
        }

        public void updateDependentOnTutorials(){
            for(int i =0; i <sgt.size(); i++){
                sgt.get(i).setiHours(1.0);
            }
        }

        public void setAssignedsgt(ArrayList<Duplet> assignedsgt) {
            this.assignedsgt = assignedsgt;
        }

        public ArrayList<Day> getWeekTimet() {
        return weekTimet;
    }


        public ArrayList<Duplet> getUnassignedLectures() {
            return unassignedLectures;
        }

        public void setUnassignedLectures(ArrayList<Duplet> unassignedLectures) {
            this.unassignedLectures = unassignedLectures;
        }

        public ArrayList<Duplet> getAssignedLectures() {
        return assignedLectures;
    }

    public void setAssignedLectures(ArrayList<Duplet> assignedLectures) {
        this.assignedLectures = assignedLectures;
    }

    public void setiNumDays(int iNumDays) {
        this.iNumDays = iNumDays;
    }

    public String getsStartDay() {
        return sStartDay;
    }

    public void setsStartDay(String sStartDay) {
        this.sStartDay = sStartDay;
    }

    public String getsEndDay() {
        return sEndDay;
    }

    public void setsEndDay(String sEndDay) {
        this.sEndDay = sEndDay;
    }

    public ArrayList<Hall> getHalls() {
        return halls;
    }

    public void setHalls(ArrayList<Hall> halls) {
        this.halls = halls;
    }

    public int getPreviousRecursionWorked() {
        return previousRecursionWorked;
    }

    public void setPreviousRecursionWorked(int previousRecursionWorked) {
        this.previousRecursionWorked = previousRecursionWorked;
    }


    public ArrayList<Duplet> getLGT() {
            return LGT;
    }

    public void setLGT(ArrayList<Duplet> LGT) {
            this.LGT = LGT;
    }

    public ArrayList<Duplet> getAssignedLGT() {
            return assignedLGT;
    }

    public void setAssignedLGT(ArrayList<Duplet> assignedLGT) {
            this.assignedLGT = assignedLGT;
    }



    public ArrayList<Duplet> getLectures() {
        return lectures;
    }

    public void setLectures(ArrayList<Duplet> lectures) {
        this.lectures = lectures;
    }

    public int assingLecture(Date date, ArrayList<Hall> halls)throws ParseException {


        for(int i = 0; i < weekTimet.size(); i++){
            if(weekTimet.get(i).getiDate() == date.getDate()){                                 //check if halls are available at that day

                return 1;
            }
        }
        return 0;
    }


    public void v_print(){
        System.out.println("Days:" + iNumDays + " Start:" + sStartDay + " End:" + sEndDay + "\nLecturesAssigned:" + assignedLectures);
        for(int i = 0; i < weekTimet.size(); i++){
            System.out.println(weekTimet.get(i));
        }
    }

    public void v_updateDates() throws ParseException {
        String stringDate = "";
        String endDate = "";
        String currDate = sEndDay;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new SimpleDateFormat("dd/MM/yyyy").parse(currDate));
        calendar.add(Calendar.DATE, 7);
        Date newDate = calendar.getTime();
        this.setsStartDay(this.getsEndDay());
        endDate =  Integer.toString(newDate.getDate()) + "/"
                +Integer.toString(newDate.getMonth() + 1)  + "/"
                + Integer.toString(newDate.getYear() + 1900);
        this.setsEndDay(endDate);

        for(Day day : weekTimet){
            day.updateDate();
        }
    }



    public void v_updateLectureDates() throws ParseException {
            for(Duplet lecture : this.getAssignedLectures()){
                //update by a week
                lecture.updateDate(7);
            }
    }

    public String toString(){
        return "Empty, use v_print";
    }

        @Override
        public Object clone()throws CloneNotSupportedException{
            return super.clone();
        }
}
