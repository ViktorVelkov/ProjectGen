package com.mycompany.app;


public class Day {

    private String sname = "";
    private String sDate = "";

    public static Timeslot[] oDSlot = new Timeslot[22];

    public Day(){
        int j = 0;
        for(int i = 1; i <= 22; i++) {
            this.oDSlot[i-1] = new Timeslot();
            if (i % 2 == 1) { this.oDSlot[i-1].setItime(900 + ((i-1)/2)*100 ); }
            else{ this.oDSlot[i-1].setItime(900 + ((i-1)/2) * 100 + 30); }
        }
    }

    public Day(String sDate){
        this.sDate = sDate;
        for(int i = 1; i <= 22; i++) {
            this.oDSlot[i-1] = new Timeslot();
            if (i % 2 == 1) { this.oDSlot[i-1].setItime(900 + ((i-1)/2)*100 ); }
            else{ this.oDSlot[i-1].setItime(900 + ((i-1)/2) * 100 + 30); }
        }// could be M, T, W, Th, F, S, Su
    }

    public Day(String sname, String sDate){
        this.sname = sname; // could be M, T, W, Th, F, S, Su
        this.sDate = sDate;
        for(int i = 1; i <= 22; i++) {
            this.oDSlot[i-1] = new Timeslot();
            if (i % 2 == 1) { this.oDSlot[i-1].setItime(900 + ((i-1)/2)*100 ); }
            else{ this.oDSlot[i-1].setItime(900 + ((i-1)/2) * 100 + 30); }
        }
    }


    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public String getsDate() {
        return sDate;
    }

    public void setsDate(String sDate) {
        this.sDate = sDate;
    }

    public void v_assignEvent(int sTimeStart, int sTimeEnd, String sHall, String sActivity){
        int iFlag = 0;
        for(int i = 0; i < oDSlot.length ; i++) {    //Check if slots are available
            if (oDSlot[i].getItime() == sTimeStart) {
                if (oDSlot[i].getiEmpty() == 1) {
                    iFlag++;
                }
            }
        }

        if(iFlag == 0) {
            for (int i = 0; i < oDSlot.length; i++) {
                if (oDSlot[i].getItime() == sTimeStart) {
                    int j = i;
                    while (oDSlot[j].getItime() != sTimeEnd) {
                        oDSlot[j].setiEmpty(1);
                        oDSlot[j].setsHall(sHall);
                        oDSlot[j].setsActivity(sActivity);
                        j++;
                    }
                }
            }
        }else{
            System.out.println();
            System.out.println("Error[");
            System.out.println("can't assign to this time period, timeslot(s) are taken");
            System.out.println("Failed: " + sTimeStart + "-" + sTimeEnd + " " + sHall + " " + sActivity + "]");
            System.out.println();
        }
    }

    //
    public void v_reassign_event(int sTimeStart, int sTimeEnd, String sHall, String sActivity){
        //this means assigning to a slot already taken,
        // if taken, remove the whole event, and put the new one
        int iFlag = 0;
        String s_reassign = "";
        for(int i = 0; i < oDSlot.length ; i++) {
            if (oDSlot[i].getItime() == sTimeStart) {
                if (oDSlot[i].getiEmpty() == 1) {
                    //event taken, reassign
                    s_reassign = oDSlot[i].getsActivity();
                }
            }
        }

            for (int i = 0; i < oDSlot.length; i++) {
                if (oDSlot[i].getsActivity().equals(s_reassign)) {
                    oDSlot[i].setiEmpty(0);
                }
            }
        v_assignEvent(sTimeStart,sTimeEnd,sHall,sActivity);
    }

    //

    //
    public void v_anull(){
        oDSlot = new Timeslot[22];
        for(int i = 0;i < 22; i++){
            oDSlot[i] = new Timeslot();
        }
    }

    //
    public int i_count_activities(){
        return 0;
    }

    public void print(){
        if(sDate.isEmpty()){ System.out.println("Date:empty"); }
        else {System.out.println("Date:" + sDate + "\tDay of the week:" + sname);}
        for(int i = 0; i < 22; i++){
            if(oDSlot[i].getiEmpty() == 1)  {
                oDSlot[i].print();
                System.out.println();
            }
        }
        if(oDSlot[oDSlot.length - 1].getiEmpty() == 1){
            System.out.println("Rest Is Empty");
        }
    }

}
