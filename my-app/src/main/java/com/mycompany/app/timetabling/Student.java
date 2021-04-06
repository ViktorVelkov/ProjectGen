package com.mycompany.app.timetabling;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class Student implements Comparable<Student>{

    private String sFirstName = "";
    private String sLastName = "";
    private int iKingsID = 0;
    private int iYearOfStudy = 0;
    private int iAssignedSGTs = 0;
    private File personalSchedule;
    private HashSet<Integer> coursesCodes = new HashSet<>();
    private ArrayList<SGT> courses = new ArrayList<>();
    private ArrayList<Duplet> assignedSGT = new ArrayList<>();
    private ArrayList<Duplet> assignedLGT = new ArrayList<>();
    private HashSet<DataSetStudents> preferences = new HashSet<>();
    private double percentSatisfiability; // each portion of his/her preferences stands for 25 percent

    public Student(){}

    public Student(String sFirstName, String sLastName, int iKingsID, int iYearOfStudy) {
        this.sFirstName = sFirstName;
        this.sLastName = sLastName;
        this.iKingsID = iKingsID;
        this.iYearOfStudy = iYearOfStudy;
    }


    public Student(int iKingsID, int iYearOfStudy) {
        this.iKingsID = iKingsID;
        this.iYearOfStudy = iYearOfStudy;
    }

    public ArrayList<Duplet> getAssignedLGT() {
        return assignedLGT;
    }

    public void setAssignedLGT(ArrayList<Duplet> assignedLGT) {
        this.assignedLGT = assignedLGT;
    }

    public void addPercertageSat(double dpp){
        percentSatisfiability += (dpp * 0.25);
    }

    public void subtractPercertageSat(double dpp){
        percentSatisfiability -= dpp * 0.25;
    }

    public int getiKingsID() {
        return iKingsID;
    }

    public void setiKingsID(int iKingsID) {
        this.iKingsID = iKingsID;
    }

    public void increaseAssignedCoursesNumber(){ this.iAssignedSGTs++;}

    public int numberOfAssignedCourses(){
        return this.courses.size();
    }

    public ArrayList<SGT> getCourses() { return courses; }

    public void setCourses(ArrayList<SGT> courses) { this.courses = courses; }

    public void addToCourses(SGT event){ this.courses.add(event); }

    public void addToAssignedCourses(Duplet event){ this.assignedSGT.add(event); }


    public void addToCoursesCodes(int event_code){ this.coursesCodes.add(event_code); }

    public HashSet<Integer> getCoursesCodes() { return coursesCodes; }

    public ArrayList<Duplet> getAssignedSGT() { return assignedSGT; }

    public HashSet<DataSetStudents> getPreferences() { return preferences; }

    public void generate_and_addToFile(int WeekNum,Week_Timetable timetable) throws IOException {
        //most inefficient method written in the project, but works
        int iCounter = 0;
        //"C:\\ProjectGen\\" +
        String sPath =  Integer.toString(this.iKingsID) + ".csv";
        //Path path = Paths.get(sPath);
        String currentDir = System.getProperty("user.dir");
        ArrayList <Timeperiod> timeperiods = new ArrayList<>();
        File file = new File(sPath);

        boolean exists = file.exists();
        if(exists){
            //
        }
        else{
            personalSchedule = new File(Integer.toString(this.getiKingsID()) + ".csv");
            personalSchedule.createNewFile();
        }

//        personalSchedule.setWritable(true);
        FileWriter fileWriter = new FileWriter(sPath, true);
        fileWriter.write("WEEK " + WeekNum + "," + timetable.getsStartDay() + "-" + timetable.getsEndDay() + ", " +",\n");


        for(Duplet event : this.assignedSGT){
            SGT sgt_converter = new SGT(event.getsLect(), event.getsDayOfWeek(),event.getsLectureHall(), event.getiHourScheduled(), event.getiHours());
            this.courses.add(sgt_converter);
        }
        for(Duplet event : this.assignedLGT){
                    SGT sgt_converter = new SGT(event.getsLect(), event.getsDayOfWeek(),event.getsLectureHall(), event.getiHourScheduled(), event.getiHours());
                    this.courses.add(sgt_converter);
        }



        //I am sorry for the lines that follow, there are many more efficient ways to do this, especially the printing by day
        //complexity is O(n^2) + O(5*n) = O(n^2)
        SGT array_very_primitive [] = new SGT[this.courses.size()];
        this.courses.toArray(array_very_primitive);

        //https://www.geeksforgeeks.org/bubble-sort/
        for(int i =0; i < courses.size() - 1; i++){
            for(int j = 0; j < courses.size()-i - 1; j++){
                if(array_very_primitive[j].getiHourScheduled() > array_very_primitive[j+1].getiHourScheduled()){
                    SGT temp = array_very_primitive[j];
                    array_very_primitive[j] = array_very_primitive[j+1];
                    array_very_primitive[j+1] = temp;
                }
            }
        }

        //System.out.print(iKingsID + " ");
        {
            for (SGT personalCourses : array_very_primitive) {
                iCounter++;
                if (iCounter == 1) {
                    fileWriter.write("Monday,\n");
                }
                if (personalCourses.getsDayOfWeek().equals("Monday")) {
                    fileWriter.write(+personalCourses.getiHourScheduled() + " ,Duration: " + personalCourses.getiHours() + ",Venue: " + personalCourses.getsLectureHall() + "," + personalCourses.getsLect() + ",\n");
                    //System.out.print(" M: " + iCounter);

                }

            }
            iCounter = 0;
            for (SGT personalCourses : array_very_primitive) {
                iCounter++;
                if (iCounter == 1) {
                    fileWriter.write("Tuesday,\n");
                }
                if (personalCourses.getsDayOfWeek().equals("Tuesday")) {
                    fileWriter.write(+personalCourses.getiHourScheduled() + " ,Duration: " + personalCourses.getiHours() + ",Venue: " + personalCourses.getsLectureHall() + "," + personalCourses.getsLect() + ",\n");
                    //System.out.print(" Tu: " + iCounter);

                }

            }
            iCounter = 0;
            for (SGT personalCourses : array_very_primitive) {

                iCounter++;
                if (iCounter == 1) {
                    fileWriter.write("Wednesday,\n");
                }
                if (personalCourses.getsDayOfWeek().equals("Wednesday")) {
                    fileWriter.write(+personalCourses.getiHourScheduled() + " ,Duration: " + personalCourses.getiHours() + ",Venue: " + personalCourses.getsLectureHall() + "," + personalCourses.getsLect() + ",\n");
                    //System.out.print(" W: " + iCounter);
                }

            }
            iCounter = 0;
            for (SGT personalCourses : array_very_primitive) {
                iCounter++;
                if (iCounter == 1) {
                    fileWriter.write("Thursday,\n");
                }
                if (personalCourses.getsDayOfWeek().equals("Thursday")) {
                    fileWriter.write(+personalCourses.getiHourScheduled() + " ,Duration: " + personalCourses.getiHours() + ",Venue: " + personalCourses.getsLectureHall() + "," + personalCourses.getsLect() + ",\n");
                    //System.out.print(" Th: " + iCounter);

                }

            }
            iCounter = 0;
            for (SGT personalCourses : array_very_primitive) {
                iCounter++;
                if (iCounter == 1) {
                    fileWriter.write("Friday,\n");
                }
                if (personalCourses.getsDayOfWeek().equals("Friday")) {
                    fileWriter.write(+personalCourses.getiHourScheduled() + " ,Duration: " + personalCourses.getiHours() + ",Venue: " + personalCourses.getsLectureHall() + "," + personalCourses.getsLect() + ",\n");
                    //System.out.print(" F: " + iCounter);

                }

            }
        }


        for(SGT event : array_very_primitive){
            //for testing purposes:
            int getiHourScheduled= event.getiHourScheduled();
            timeperiods.add(new Timeperiod(getiHourScheduled, event.getsDayOfWeek()));
            for(int i = 0; i< ((event.getiHours()*2) - 1); i++){
                if(event.getiHourScheduled() %100 != 0){
                    if(i % 2 == 0){
                        getiHourScheduled = getiHourScheduled+ 70;
                        timeperiods.add(new Timeperiod(getiHourScheduled, event.getsDayOfWeek()));
                    }
                    else{
                        getiHourScheduled = getiHourScheduled+ 30 ;
                        timeperiods.add(new Timeperiod(getiHourScheduled, event.getsDayOfWeek()));
                    };

                }
                else{
                    if(event.getiHourScheduled() %100 == 0){
                        if(i % 2 != 0){
                            getiHourScheduled = getiHourScheduled+ 70;
                            timeperiods.add(new Timeperiod(getiHourScheduled, event.getsDayOfWeek()));

                        }
                        else{
                            getiHourScheduled = getiHourScheduled+ 30 ;
                            timeperiods.add(new Timeperiod(getiHourScheduled, event.getsDayOfWeek()));
                        };
                    }
                }
            }
            //testing
        }

        //for testing purposes
        iCounter=0;
        for(int j = 0;j < timeperiods.size(); j++){
            for(int k = 0; k< timeperiods.size(); k++){
                if(j != k)
                if(timeperiods.get(k).getsDay().equals(timeperiods.get(j).getsDay()) && timeperiods.get(j).getiTime() == timeperiods.get(k).getiTime()){
                    iCounter++;
                    if(iCounter == 1) {
                        System.out.println();
                        System.out.println("Overlapping, my kings_id is: " + iKingsID);
                        System.out.println();
                    }
                }
            }
        }
        System.out.println();

        fileWriter.write("\n\n\n");
        fileWriter.close();


    }




    public  void reassign(Duplet event){}

    public void assignSGT(Duplet event){
        assignedSGT.add(event);
    }

    public void addToPrefs(DataSetStudents data){
        preferences.add(data);
    }
    public int checkIfOverlappingSGT(Duplet event){
        for(Duplet sgt : assignedSGT){
            if(event.getiHourScheduled() <= sgt.getiHourScheduled() + sgt.getiHours()*100 && event.getiHours()*100 + event.getiHourScheduled() <= 1){}
        }
        return 1;
    }





    @Override
    public int hashCode() {
        return this.iKingsID;
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
    public int compareTo(Student o) {
        return o.iKingsID - this.iKingsID;
    }
}
