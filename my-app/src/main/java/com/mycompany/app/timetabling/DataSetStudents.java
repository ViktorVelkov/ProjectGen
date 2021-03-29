package com.mycompany.app.timetabling;

public class DataSetStudents implements Comparable<DataSetStudents>{

    private int kings_id;
    private int lecture_code;
    private String abbrev;
    private String prefDay;
    private int iHour1;
    private int iHour2;
    private int scale;
    private int privileged;
    private int iDay;
    private int iMonth;
    private int iYear;
    private String iCourse;
    private int iHour;
    private double dDuration;
    private int numberforCombinations = -1;

    public DataSetStudents(int kings_id, int lecture_code, String abbrev, String prefDay, int iHour1, int iHour2, int scale, int privileged) {
        this.kings_id = kings_id;
        this.lecture_code = lecture_code;
        this.abbrev = abbrev;
        this.prefDay = prefDay;
        this.iHour1 = iHour1;
        this.iHour2 = iHour2;
        this.scale = scale;
        this.privileged = privileged;
    }

    public DataSetStudents(int kings_id, int lecture_code) { //just for testing
        this.kings_id = kings_id;
        this.lecture_code = lecture_code;
    }

    public DataSetStudents(){}

    public int getNumberforCombinations() {
        return numberforCombinations;
    }

    public void setNumberforCombinations(int numberforCombinations) {
        this.numberforCombinations = numberforCombinations;
    }

    public int getiDay() {
        return iDay;
    }

    public void setiDay(int iDay) {
        this.iDay = iDay;
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

    public String getiCourse() {
        return iCourse;
    }

    public void setiCourse(String iCourse) {
        this.iCourse = iCourse;
    }

    public int getiHour() {
        return iHour;
    }

    public void setiHour(int iHour) {
        this.iHour = iHour;
    }

    public double getdDuration() {
        return dDuration;
    }

    public void setdDuration(double dDuration) {
        this.dDuration = dDuration;
    }

    public int getKings_id() {
        return kings_id;
    }

    public void setKings_id(int kings_id) {
        this.kings_id = kings_id;
    }

    public int getLecture_code() {
        return lecture_code;
    }

    public void setLecture_code(int lecture_code) {
        this.lecture_code = lecture_code;
    }

    public String getAbbrev() {
        return abbrev;
    }

    public void setAbbrev(String abbrev) {
        this.abbrev = abbrev;
    }

    public String getPrefDay() {
        return prefDay;
    }

    public void setPrefDay(String prefDay) {
        this.prefDay = prefDay;
    }

    public int getiHour1() {
        return iHour1;
    }

    public void setiHour1(int iHour1) {
        this.iHour1 = iHour1;
    }

    public int getiHour2() {
        return iHour2;
    }

    public void setiHour2(int iHour2) {
        this.iHour2 = iHour2;
    }

    public int getScale() {
        return scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    public int getPrivileged() {
        return privileged;
    }

    public void setPrivileged(int privileged) {
        this.privileged = privileged;
    }

    @Override
    public String toString() {

        String toReturn= "DSet{";
       if(numberforCombinations != -1){
          toReturn+= " " + Integer.toString(numberforCombinations) + " ";
       }
       toReturn += "DSet{" +
                "kings_id=" + kings_id +
                ", lecture_code=" + lecture_code +
                ", abbrev='" + abbrev + '\'' +
                ", prefDay='" + prefDay + '\'' +
                ", iHour1=" + iHour1 +
                ", iHour2=" + iHour2 +
                ", scale=" + scale +
                ", privileged=" + privileged +
                '}';
       return toReturn;
    }

    @Override
    public int compareTo(DataSetStudents o) {

        if(this.lecture_code - o.lecture_code == 0){
            return this.kings_id - o.kings_id;
        }
        return this.kings_id - o.kings_id;
    }


    @Override
    public int hashCode() {
        int result = 1;
        result = 17 * result + kings_id + abbrev.hashCode();
        result = kings_id * result + lecture_code + iHour*result + iHour2;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        DataSetStudents other = (DataSetStudents) obj;

        return Integer.compare(this.kings_id, other.kings_id) == 0 && Integer.compare(this.lecture_code, other.lecture_code) == 0;
    }


}
