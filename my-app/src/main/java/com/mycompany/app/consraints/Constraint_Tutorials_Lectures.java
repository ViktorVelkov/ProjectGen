package com.mycompany.app.consraints;

public class Constraint_Tutorials_Lectures {
    private String sLecture;
    private String sTutorial;
    private int iDaysBetween;
    private long iLectureTime;
    private int iGroupTutorial;
    private long iTutorialTime;
    private int iLectureCode;
    private int iTutorialCode;

    public Constraint_Tutorials_Lectures(){}

    public String getsLecture() {
        return sLecture;
    }

    public void setsLecture(String sLecture) {
        this.sLecture = sLecture;
    }

    public String getsTutorial() {
        return sTutorial;
    }

    public void setsTutorial(String sTutorial) {
        this.sTutorial = sTutorial;
    }

    public int getiDaysBetween() {
        return iDaysBetween;
    }

    public void setiDaysBetween(int iDaysBetween) {
        this.iDaysBetween = iDaysBetween;
    }

    public long getiLectureTime() {
        return iLectureTime;
    }

    public void setiLectureTime(long iLectureTime) {
        this.iLectureTime = iLectureTime;
    }

    public long getiTutorialTime() {
        return iTutorialTime;
    }

    public void setiTutorialTime(long iTutorialTime) {
        this.iTutorialTime = iTutorialTime;
    }

    public boolean b_checkTutorialAfterLecture(){

        //make it more elaborate
        if(iLectureCode == iTutorialCode) {
            if (iDaysBetween > 0) {
                return true;
            } else if (iDaysBetween == 0) {
               // if(iLectureTime ){}
            } else {
                return false;
            }
        }
        System.out.println("Tutorial and Lecture out of line");
        return false;
    }

}
