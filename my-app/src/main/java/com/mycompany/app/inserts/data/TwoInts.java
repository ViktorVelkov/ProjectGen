package com.mycompany.app.inserts.data;

public class TwoInts implements Comparable<TwoInts>{
    private int iCode;
    private int iAttending;

    public TwoInts(int iCode, int iAttending) {
        this.iCode = iCode;
        this.iAttending = iAttending;
    }

    public int getiCode() {
        return iCode;
    }

    public void setiCode(int iCode) {
        this.iCode = iCode;
    }

    public int getiAttending() {
        return iAttending;
    }

    public void setiAttending(int iAttending) {
        this.iAttending = iAttending;
    }

    @Override
    public String toString() {
        return "TwoInts{" +
                "iCode=" + iCode +
                ", iAttending=" + iAttending +
                '}' + "\n";
    }

    @Override
    public int compareTo(TwoInts o) {
        return this.iAttending - o.iAttending;
    }
}
