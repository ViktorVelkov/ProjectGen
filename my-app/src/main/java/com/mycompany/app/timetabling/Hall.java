package com.mycompany.app.timetabling;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Hall implements Comparable<Hall>{

    private int iCapacity;

    private String sAbbrev;
    private String sTimeStart;
    private String sTimeEnd;
    private Connection connection;
    private int iAdditionalCode;
    private int iOccupants=0;

    private ArrayList<Timeperiod> availability;

    public Hall(Connection connection, int iCapacity, String sAbbrev, String sTimeStart, String sTimeEnd, int iAdditionalCode, int intendedForLecture) throws SQLException, ParseException {
        this.sAbbrev = sAbbrev;
        this.sTimeStart = sTimeStart;
        this.sTimeEnd = sTimeEnd;
        this.iCapacity = iCapacity;
        this.connection = connection;
        this.iAdditionalCode = iAdditionalCode;
        String sTableName = "";

        if(sAbbrev.startsWith("W") && intendedForLecture == 1){
            sTableName = "two_weeks_availability_halls_waterloo";
        }
        else if(sAbbrev.startsWith("B") && intendedForLecture == 1){
            sTableName = "two_weeks_availability_halls_bush_house";
        }
        if(sAbbrev.startsWith("W") && intendedForLecture == 0){
            sTableName = "two_weeks_availability_halls_waterloo_tutorials";
        }
        else if(sAbbrev.startsWith("B") && intendedForLecture == 0){
            sTableName = "two_weeks_availability_halls_bush_house_tutorials";
        }
        else{}
        //

//        Date date1 = new SimpleDateFormat("dd-MMM-yyyy").parse(sTimeStart);
//        Date date2 = new SimpleDateFormat("dd-MMM-yyyy").parse(sTimeEnd);
        Date date1 = new SimpleDateFormat("dd/MM/yyyy").parse(sTimeStart);
        Date date2 = new SimpleDateFormat("dd/MM/yyyy").parse(sTimeEnd);
        String sql34 = "";
        PreparedStatement prst;
        if(date1.getMonth() != date2.getMonth() && date2.getMonth() - date1.getMonth() == 1){
            sql34 = " SELECT AVAILABLE, HOUR, DAY, DATE, MONTH, YEAR FROM " + sTableName + " WHERE AVAILABLE = 1 AND MONTH = ? AND DATE >= ? AND HALL = ?" +
                    " UNION " +
                    " SELECT AVAILABLE, HOUR, DAY, DATE, MONTH, YEAR FROM " + sTableName + " WHERE AVAILABLE = 1 AND MONTH = ? AND DATE < ? AND HALL = ?";
            prst = connection.prepareStatement(sql34);
            prst.setInt(1, date1.getMonth());
            prst.setInt(2, date1.getDate());
            prst.setString(3, sAbbrev);
            prst.setInt(4, date2.getMonth());
            prst.setInt(5,date2.getDate());
            prst.setString(6, sAbbrev);
        }
        else {
            sql34 = " SELECT AVAILABLE, HOUR, DAY, DATE, MONTH, YEAR  FROM " + sTableName + " WHERE AVAILABLE = 1 AND MONTH = ? AND DATE >= ? AND HALL = ? AND MONTH = ? AND DATE < ? ";
            int iCounter = 1;
            int iSecondCounter = 0;

            for (int i = date1.getMonth() + 1; i < date2.getMonth(); i++) {
                sql34 += "UNION SELECT AVAILABLE, HOUR, DAY, DATE, MONTH, YEAR  FROM " + sTableName + " WHERE AVAILABLE = 1 AND MONTH = ? AND HALL = ? ";
                iSecondCounter++;
            }


            prst = connection.prepareStatement(sql34);
            prst.setInt(iCounter, date1.getMonth());
            prst.setInt(++iCounter, date1.getDate());
            prst.setString(++iCounter, sAbbrev);
            for (int i = 1; i < iSecondCounter + 1; i++) {
                prst.setInt(++iCounter, date2.getMonth() - i);
                prst.setString(++iCounter, sAbbrev);
            }
            prst.setInt(++iCounter, date2.getMonth());
            prst.setInt(++iCounter, date2.getDate());
        }
        ResultSet rst = prst.executeQuery();
        availability = new ArrayList<>();
        while (rst.next()){
            availability.add(new Timeperiod(rst.getString("DAY")
                                            , rst.getInt("AVAILABLE")
                                            , rst.getInt("HOUR")
                                            , rst.getInt("DATE")
                                            , rst.getInt("MONTH")
                                            , rst.getInt("YEAR")));
        }
        
        //
        
        
        
    }

    public Hall(int iCapacity, String sAbbrev, String sTimeStart, String sTimeEnd){
        this.iCapacity = iCapacity;
        this.sAbbrev = sAbbrev;
        this.sTimeStart = sTimeStart;
        this.sTimeEnd = sTimeEnd;
    }

    public int getiOccupants() {
        return iOccupants;
    }

    public void setiOccupants(int iOccupants) {
        this.iOccupants = iOccupants;
    }

    public void increaseOccupantsByOne(){
        iOccupants++;
    }

    public int getiCapacity() {
        return iCapacity;
    }

    public void setiCapacity(int iCapacity) {
        this.iCapacity = iCapacity;
    }


    public int getiAdditionalCode() {
        return iAdditionalCode;
    }

    public void setiAdditionalCode(int iAdditionalCode) {
        this.iAdditionalCode = iAdditionalCode;
    }

    public String getsAbbrev() {
        return sAbbrev;
    }

    public void setsAbbrev(String sAbbrev) {
        this.sAbbrev = sAbbrev;
    }

    public void setAvToZero(int iHourStart, int iHourEnd, String sDay){

        for (int i = 0; i < availability.size(); i++){
            String temp = availability.get(i).getsDay().toLowerCase(Locale.ROOT);
            int iTime = availability.get(i).getiTime();
            if(temp.equals(sDay.toLowerCase(Locale.ROOT)) ) {
                if (availability.get(i).getiTime() >= iHourStart && availability.get(i).getiTime() < iHourEnd) {
                    availability.get(i).setiAvailable(0);
                }
            }
        }

    }

    public void setAvToOne(int iHourStart, int iHourEnd,String sDay){

        for (int i = 0; i < availability.size(); i++) {
            if (availability.get(i).getsDay().toLowerCase(Locale.ROOT).equals(sDay.toLowerCase(Locale.ROOT))) {

            }
        }

    }

    public int getAvailability(int iHourStart, int iHourEnd, String sDay) {
        int iCount = 0;
        int iDuration =  iHourEnd - iHourStart;
        for (int i = 0; i < availability.size(); i++) {
           String temp = availability.get(i).getsDay().toLowerCase(Locale.ROOT);
            if(temp.equals(sDay.toLowerCase(Locale.ROOT))) {

                if (availability.get(i).getiTime() >= iHourStart && availability.get(i).getiTime() < iHourEnd && availability.get(i).getiAvailable() == 1) {
                    iCount++;
                }
            }
        }
        if(iCount/2 == iDuration/100){return 1;}
        return 0;
    }

    public ArrayList<Timeperiod> getArrayTimeperiods() {
        return availability;
    }

    public void setArrayTimeriods(ArrayList<Timeperiod> availability) {
        this.availability = availability;
    }

    public CoupledData findAvailableSlot(int iHourStart, int iDuration){
        int iCount = 0;
        int iTime = 0;
        CoupledData cp = new CoupledData();
        String sDay = "";
        String sPrevDay = "";
        for(int i = 0; i < availability.size(); i++){               //this doesn't account for the timeperiots being in the same day

            sDay = availability.get(i).getsDay();
            //sPrevDay = availability.get(i).getsDay();

            if(sDay.equals(sPrevDay) && availability.get(i).getiAvailable() == 1 && availability.get(i).getiTime() >= iHourStart){
                iCount++;
                if(iCount == 1){
                    iTime = availability.get(i).getiTime();
                }
            }
            else { iCount = 0; iTime = 0; };

            if(iCount == (iDuration*2) ){
                //spot found
                //I need to return the day and the hour corresponding to the hall
                cp.setiHour( iTime );
                cp.setsDay( availability.get(i).getsDay());
                break;
            }
            sPrevDay = availability.get(i).getsDay();
        }
        return cp;
    }

    public CoupledData findAvailableSlotTimeline(int iHourStart, int iDuration, Duplet event){
        int iCount = 0;
        int iTime = 0;
        CoupledData cp = new CoupledData();
        String sDay = "";
        String sPrevDay = "";
        ArrayList<Timeperiod> slotsToUse = new ArrayList<>();

        for(int i = 0; i < availability.size(); i++){
            int iCounter =0;
            while(!availability.get(i).getsDay().equals( event.getDayAssigned() ) && availability.get(i).getiTime() >= event.getiHourScheduled() + 100*event.getiHours() ){
                continue;
            }
            slotsToUse.add(availability.get(i));
        }
        //I also need to take into an account the other tutorials assigned so there is no overlapping and the lectures
        for(int i = 0; i < slotsToUse.size(); i++){               //this doesn't account for the timeperiots being in the same day

            sDay = slotsToUse.get(i).getsDay();
            //sPrevDay = availability.get(i).getsDay();

            if(sDay.equals(sPrevDay) && slotsToUse.get(i).getiAvailable() == 1 && slotsToUse.get(i).getiTime() >= iHourStart){
                iCount++;
                if(iCount == 1){
                    iTime = slotsToUse.get(i).getiTime();
                }
            }
            else { iCount = 0; iTime = 0; };

            if(iCount == (iDuration*2) ){
                //spot found
                //I need to return the day and the hour corresponding to the hall
                cp.setiHour( iTime );
                cp.setsDay( slotsToUse.get(i).getsDay());
                break;
            }
            sPrevDay = slotsToUse.get(i).getsDay();
        }
        return cp;
    }

    public CoupledData findAvailableSlotLectures(int iHourStart, int iDuration, ArrayList<String> daysToBeUsed, Duplet event){
        int iCount = 0;
        int iTime = 0;
        CoupledData cp = new CoupledData();
        String sDay = "";
        String sPrevDay = "";                               //here actually lies the 930 problem !!!!
        ArrayList<Timeperiod> slotsToUse = new ArrayList<>();

        for(int i = 0; i < availability.size(); i++){
            int iCounter =0;
            for(int j = 0 ; j < daysToBeUsed.size(); j++){
                if(availability.get(i).getsDay().equals(daysToBeUsed.get(j))){
                    slotsToUse.add(availability.get(i));
                }
            }
        }

        for(int i=0; i < event.getDependentOn().size(); i++){       //for each dependent

            Duplet dependent = event.getDependentOn().get(i);
            if(dependent.getiHourScheduled() < 900 && dependent.getsDayOfWeek().isEmpty()){
                continue;
            }

                    for(int k = 0; k < slotsToUse.size(); k++){        //this would usually take significant amount of time, maybe consider first sorting the time of the dependents

                        if(event.getsDayOfWeek().equals(slotsToUse.get(k).getsDay()) &&
                                slotsToUse.get(k).getiTime() >= event.getDependentOn().get(i).getiHourScheduled() &&        /* disregard the following comment, this is getting the wrong time ;;   if there are slots which are in the time period of the dependent duplets, set them to 0*/
                                slotsToUse.get(k).getiTime() < (event.getDependentOn().get(i).getiHourScheduled()+100*event.getDependentOn().get(i).getiHours())
                        ){
                            slotsToUse.get(k).setiAvailable(0);
                        }
                    }

        }


        for(int i = 0; i < slotsToUse.size(); i++){


            sDay = slotsToUse.get(i).getsDay();


                if(sDay.equals(sPrevDay) && slotsToUse.get(i).getiAvailable() == 1 && slotsToUse.get(i).getiTime() >= iHourStart){
                    iCount++;
                    if(iCount == 1){
                        iTime = slotsToUse.get(i).getiTime();
                    }

                    if(iCount == (iDuration*2) ){
                        //spot found
                        //I need to return the day and the hour corresponding to the hall
                        cp.setiHour( iTime );
                        cp.setsDay( slotsToUse.get(i).getsDay());
                        break;
                    }
                }
                else { iCount = 0; iTime = 0; };
            sPrevDay = slotsToUse.get(i).getsDay();

        }
        return cp;
    }

    public CoupledData findAvailableSlotLectures_withPreferences(int iHourStart, double iDuration, ArrayList<String> daysToBeUsed,Duplet event){
        int iCount = 0;
        int iTime = 0;
        CoupledData cp = new CoupledData();
        cp.setsDay("");
        cp.setiHour(0);
        String sDay = "";
        String sPrevDay = "";                               //here actually lies the 930 problem !!!!
        ArrayList<Timeperiod> slotsToUse = new ArrayList<>();

        if(event.getPreferredDays() == null){ return cp; }

        for(int i = 0; i < availability.size(); i++){
            int iCounter =0;
            for(int j = 0 ; j < daysToBeUsed.size(); j++){
                if(availability.get(i).getsDay().equals(daysToBeUsed.get(j))){
                    slotsToUse.add(availability.get(i));
                }
            }
        }

        //every lecture depends on other lectures, in order to not overlap them this code prevents the algorithm of assigning to times when dependent lectures appear
        for(int i=0; i < event.getDependentOn().size(); i++){

            Duplet dependent = event.getDependentOn().get(i);
            if(dependent.getiHourScheduled() < 900 && dependent.getsDayOfWeek().isEmpty()){
                continue;
            }

            for(int k = 0; k < slotsToUse.size(); k++){        //this would usually take significant amount of time, maybe consider first sorting the time of the dependents

                if(event.getsDayOfWeek().equals(slotsToUse.get(k).getsDay()) &&
                        slotsToUse.get(k).getiTime() >= event.getDependentOn().get(i).getiHourScheduled() &&        /* disregard the following comment, this is getting the wrong time ;;   if there are slots which are in the time period of the dependent duplets, set them to 0*/
                        slotsToUse.get(k).getiTime() < (event.getDependentOn().get(i).getiHourScheduled()+100*event.getDependentOn().get(i).getiHours())
                ){
                    slotsToUse.get(k).setiAvailable(0);
                }
            }

        }


    if(event.getPreferredDays().getPrefDay().size() != 0) {


        for (int e = 0; e < event.getPreferredDays().getPrefDay().size(); e++) {
            for (int i = 0; i < slotsToUse.size(); i++) {
                if(cp.getiHour()!=0 && !cp.getsDay().isEmpty()){break;}

                if (slotsToUse.get(i).getsDay().equals(event.getPreferredDays().getPrefDay().get(e))) {

                    sDay = slotsToUse.get(i).getsDay();


                    if (sDay.equals(sPrevDay) && slotsToUse.get(i).getiAvailable() == 1 && slotsToUse.get(i).getiTime() >= iHourStart) {
                        iCount++;
                        if (iCount == 1) {
                            iTime = slotsToUse.get(i).getiTime();
                        }

                        if (iCount == (int) (iDuration * 2)) {
                            //spot found
                            //I need to return the day and the hour corresponding to the hall
                            cp.setiHour(iTime);
                            cp.setsDay(slotsToUse.get(i).getsDay());
                            break;//return cp;
                        }
                    } else {
                        iCount = 0;
                        iTime = 0;
                    }
                    ;
                    sPrevDay = slotsToUse.get(i).getsDay();

                }
            }
        }
    }
        return cp;          //end of _withPreferredDays
    }

    public int checkAvailability(String sDay, int iHour, double duration){
        int iCounter =0;
        while(!availability.get(iCounter).getsDay().equals(sDay)){
            if(iCounter > availability.size() - 1){
                break;
            }
            iCounter++;
        }

        while (iHour != availability.get(iCounter).getiTime()){
            iCounter++;
        }
        String temp = "";
        for(int i =iCounter; i < iCounter + duration*2; i++){
            int iTime = availability.get(i).getiTime();     //for testing
            String checkDay = availability.get(i).getsDay();    //for testing
            int iAvailable = availability.get(i).getiAvailable();   //for testing
            if(!temp.isEmpty() && !temp.equals(checkDay)){
                return 0;
            }
            if(availability.get(i).getiAvailable() == 0){
                return 0;
            }
            checkDay = availability.get(i).getsDay();
            temp = checkDay;
        }
        return 1;
    }


    public int checkAvailability(Timeslot timeslot){
        int iCounter =0;
        while(availability.get(iCounter).getsDay().equals(timeslot.getTimePeriod().get(0).getsDay()) && availability.get(iCounter).getiTime() == timeslot.getTimePeriod().get(0).getiTime()){
            if(iCounter > availability.size() - 1){
                break;
            }
            iCounter++;
        }

        for(int i =iCounter; i < iCounter + timeslot.getDuration()*2; i++){
            if(availability.get(i).getiAvailable() == 0){
                return 0;
            }
        }
        return 1;
    }



    //THIS IS INSIDE BETTERGREEDY ALGORITHM, NOT PROPERLY USED
    public int findAvailableSlot_PreferredDay(int iHourStart, int iDuration, String sPrefDay){
//        int iCount = 0;
        int iTime = 0;
//        int iResult = 0;
//        String sDay = "";
//        String sPrevDay = "";                               //here actually lies the 930 problem !!!!
//
//        for(int i = 0; i < availability.size(); i++) {
//            sDay = availability.get(i).getsDay();
//            if(sDay.equals(sPrefDay)) {
//                if (availability.get(i).getsDay().equals(sPrefDay)) {
//
//                    if (availability.get(i).getiAvailable() == 1 && availability.get(i).getiTime() >= iHourStart && availability.get(i).getsDay().equals(sPrefDay)) {
//                        iCount++;
//                        if (iCount == 1) {
//                            iTime = availability.get(i).getiTime();
//                        }
//
//                    } else {
//                        iCount = 0;
//                        iTime = 0;
//                    }
//
//                    if (iCount == (iDuration * 2)) {
//                        //spot found
//                        //first one available
//                        //I need to return the day and the hour corresponding to the hall
//                        return iTime;
//                    }
//                }
//            }
//            sPrevDay = availability.get(i).getsDay();
//        }
        return iTime;
    }

    public int findAvailableSlot_PreferredDay_sgt(int iHourStart, double iDuration, String sPrefDay, Duplet event, ArrayList<Timeslot> unavailable){

        int iCount = 0;
        int iCount2 = 0;
        int iTime = 0;
        int iResult = 0;
        String sDay = "";
        String sPrevDay = "";                               //here actually lies the 930 problem !!!!
        ArrayList<Timeslot> timeslotsAvailable;
        ArrayList<Timeperiod> slotsToUse = new ArrayList<>();

        while(!availability.get(iCount2).getsDay().equals( event.getDayAssigned() )){
            iCount2++;
        }
        for(int i =iCount2; i< availability.size(); i++){
            if(availability.get(i).getsDay().equals( event.getDayAssigned() )){
                if(availability.get(i).getiTime() >= event.getiHourScheduled() && availability.get(i).getiTime() < event.getiHourScheduled() + event.getiHours()*100  /* + 100*n /*hours to put as unavailable after the lecture, for example a break*/){
                    continue;
                }
            }

            slotsToUse.add(availability.get(i));

        }


            for (Timeslot timeslot : unavailable) { // remove all the depending times from the slotsToUse
                for (Timeperiod slotts : slotsToUse) {
                    if (timeslot.getTimePeriod().get(0).getsDay().equals(slotts.getsDay())) {
                        for (int i = 0; i < timeslot.getTimePeriod().size(); i++) {
                            if(timeslot.getTimePeriod().get(i).getiTime() <= slotts.getiTime()) {
                                if (timeslot.getTimePeriod().get(i).getiTime() == slotts.getiTime()) {
                                    slotts.setiAvailable(0);
                                    break;
                                }
                            }
                            else{
                                break;
                            }
                        }
                    }
                }
            }




        for(int i = 0; i < slotsToUse.size(); i++) {
            sDay = slotsToUse.get(i).getsDay();
            if(sDay.equals(sPrefDay)) {

                if (slotsToUse.get(i).getsDay().equals(sPrefDay)) {
                    if (slotsToUse.get(i).getiAvailable() == 1 && slotsToUse.get(i).getiTime() >= iHourStart &&
                            (
                                    (slotsToUse.get(i).getiDate() >= event.getiDayScheduled() && slotsToUse.get(i).getiMonth() >= event.getiMonthScheduled() - 1 && slotsToUse.get(i).getiYear() >= event.getiYearScheduled() && slotsToUse.get(i).getiTime() >= event.getiHourScheduled() + 100))
                            || (slotsToUse.get(i).getiDate() > event.getiDayScheduled() && slotsToUse.get(i).getiMonth() >= event.getiMonthScheduled() - 1 && slotsToUse.get(i).getiYear() >= event.getiYearScheduled())
                    )
                    {
                        iCount++;
                        if (iCount == 1) {
                            iTime = slotsToUse.get(i).getiTime();
                        }

                    } else {
                        iCount = 0;
                        iTime = 0;
                    }

                    if (iCount == (int)(iDuration * 2)) {
                        //spot found
                        //first one available
                        //I need to return the day and the hour corresponding to the hall
                        return iTime;
                    }

                }
            }
            sPrevDay = slotsToUse.get(i).getsDay();
        }
        return iTime;
    }

    public ArrayList<Timeslot> findAvailableSlot_PreferredDay_sgt_remastered(int iHourStart, double iDuration, String sPrefDay, Duplet event, ArrayList<Timeslot> unavailable) throws CloneNotSupportedException {

        int iCount = 0;
        int iCount2 = 0;
        int iTime = 0;
        int iResult = 0;
        String sDay = "";
        String sPrevDay = "";                               //here actually lies the 930 problem !!!!
        ArrayList<Timeslot> timeslotsAvailable = new ArrayList<>();
        ArrayList<Timeperiod> slotsToUse = new ArrayList<>();

        while(!availability.get(iCount2).getsDay().equals( event.getDayAssigned() )){
            iCount2++;
        }
        for(int i =iCount2; i< availability.size(); i++){
            if(availability.get(i).getsDay().equals( event.getDayAssigned() )){
                if(availability.get(i).getiTime() >= event.getiHourScheduled() && availability.get(i).getiTime() < event.getiHourScheduled() + event.getiHours()*100  /* + 100*n /*hours to put as unavailable after the lecture, for example a break*/){
                    continue;
                }
            }

            slotsToUse.add((Timeperiod) availability.get(i).clone());
        }


//        for (Timeslot timeslot : unavailable) { // remove all the depending times from the slotsToUse
//            for (Timeperiod slotts : slotsToUse) {
//                if (timeslot.getTimePeriod().get(0).getsDay().equals(slotts.getsDay())) {
//                    for (int i = 0; i < timeslot.getTimePeriod().size(); i++) {
//                        if(timeslot.getTimePeriod().get(i).getiTime() <= slotts.getiTime()) {
//                            if (timeslot.getTimePeriod().get(i).getiTime() == slotts.getiTime()) {
//                                slotts.setiAvailable(0);
//                                break;
//                            }
//                        }
//                        else{
//                            break;
//                        }
//                    }
//                }
//            }
//        }




        for (Timeslot timeslot : unavailable) { // remove all the depending times from the slotsToUse
            for (int i = 0; i < timeslot.getTimePeriod().size(); i++) {
                for(Timeperiod slotts: slotsToUse){
                    if(slotts.getsDay().equals(timeslot.getTimePeriod().get(0).getsDay()) && slotts.getiTime() == timeslot.getTimePeriod().get(i).getiTime()  ){
                        slotts.setiAvailable(0);
                        break;
                    }
                }
            }
        }




        for(int i = 0; i < slotsToUse.size(); i++) {
            sDay = slotsToUse.get(i).getsDay();
            if(sDay.equals(sPrevDay)) {

                for (int k = 0; k < iDuration * 2; k++) {

                    if (i < slotsToUse.size() - iDuration * 2) {

                        String sCurrentItereationCheckDay = slotsToUse.get(i+k).getsDay();

                        if(!sDay.equals(sCurrentItereationCheckDay)){
                            iCount =0;
                            break;
                        }

                        if (slotsToUse.get(i + k).getiAvailable() == 1) {
                                iCount++;
                                if (iCount == 1) {
                                    iTime = slotsToUse.get(i + k).getiTime();
                                }

                        } else {
                                iCount = 0;
                                iTime = 0;
                        }

                            if (iCount == (int) (iDuration * 2)) {
                                //spot found
                                //I need to return the day and the hour corresponding to the hall
                                iCount =0;
                                Timeslot toAdd = new Timeslot(1.0, sDay, iTime,  this.sAbbrev, event.getsLect(), slotsToUse.get(i+k).getiDate(),slotsToUse.get(i+k).getiMonth(), slotsToUse.get(i+k).getiYear() );
                                timeslotsAvailable.add(toAdd);
                            }

                        }

                }
            }
            sPrevDay = slotsToUse.get(i).getsDay();
        }
        return timeslotsAvailable;
    }


    public void updateHalls(int iNumber) throws ParseException {
        //
        Date date1 = new SimpleDateFormat("dd/MM/yyyy").parse(sTimeEnd);
        String setTime = sTimeEnd;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date1);
        calendar.add(Calendar.DAY_OF_MONTH, iNumber);
        Date newDate = calendar.getTime();
        sTimeStart = sTimeEnd;
        sTimeEnd =  Integer.toString(newDate.getDate()) + "/"
         +Integer.toString(newDate.getMonth() + 1)  + "/"
         + Integer.toString(newDate.getYear() + 1900);

        for(Timeperiod time : availability){
            time.updateTimeslot(7);
        }

    }



    @Override
    public String toString() {

        String result = "Hall{" +
                "iCapacity=" + iCapacity +
                ", sAbbrev='" + sAbbrev + '\'' +
                ", sTimeStart='" + sTimeStart + '\'' +
                ", iCode=" + iAdditionalCode + "\n";
         for (int i = 0; i < availability.size(); i++){
            result += " " + availability.get(i).toString();
        }
         result += "\n";
        return result;
    }


    @Override
    public int compareTo(Hall o) {
        return this.iCapacity - o.iCapacity;
        //return o.iCapacity-this.iCapacity;
    }
}
