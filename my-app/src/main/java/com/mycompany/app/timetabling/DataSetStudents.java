package com.mycompany.app.timetabling;

public class DataSetStudents {

    private int kings_id;
    private int lecture_code;
    private String abbrev;
    private String prefDay;
    private int iHour1;
    private int iHour2;
    private int scale;
    private int privileged;

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

    public DataSetStudents(){}

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
        return "DSet{" +
                "kings_id=" + kings_id +
                ", lecture_code=" + lecture_code +
                ", abbrev='" + abbrev + '\'' +
                ", prefDay='" + prefDay + '\'' +
                ", iHour1=" + iHour1 +
                ", iHour2=" + iHour2 +
                ", scale=" + scale +
                ", privileged=" + privileged +
                '}';
    }
}
