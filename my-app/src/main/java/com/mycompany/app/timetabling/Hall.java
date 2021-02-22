package com.mycompany.app.timetabling;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Hall {
    private int iCapacity;

    private String sAbbrev;
    private String sTimeStart;
    private String sTimeEnd;
    private Connection connection;
    private int iAdditionalCode;

    private ArrayList<Timeperiod> availability;


    public Hall(Connection connection,int iCapacity, String sAbbrev, String sTimeStart, String sTimeEnd) throws SQLException, ParseException {
        this.sAbbrev = sAbbrev;
        this.sTimeStart = sTimeStart;
        this.sTimeEnd = sTimeEnd;
        this.iCapacity = iCapacity;
        this.connection = connection;
        String sTableName = "";

        if(sAbbrev.startsWith("W")){
            sTableName = "two_weeks_availability_halls_waterloo";
        }
        else if(sAbbrev.startsWith("B")){
            sTableName = "two_weeks_availability_halls_bush_house";
        }
        else{}
        //

        Date date1 = new SimpleDateFormat("dd-MMM-yyyy").parse(sTimeStart);
        Date date2 = new SimpleDateFormat("dd-MMM-yyyy").parse(sTimeEnd);
        String sql34 = "";
        PreparedStatement prst;
        if(date1.getMonth() != date2.getMonth() && date2.getMonth() - date1.getMonth() == 1){
            sql34 = " SELECT AVAILABLE, HOUR, DAY, DATE, MONTH, YEAR FROM " + sTableName + " WHERE AVAILABLE = 1 AND MONTH = ? AND DATE >= ? AND HALL = ?" +
                    " UNION " +
                    " SELECT AVAILABLE, HOUR, DAY, DATE, MONTH, YEAR FROM " + sTableName + " WHERE AVAILABLE = 1 AND MONTH = ? AND DATE <= ? AND HALL = ?";
            prst = connection.prepareStatement(sql34);
            prst.setInt(1, date1.getMonth());
            prst.setInt(2, date1.getDate());
            prst.setString(3, sAbbrev);
            prst.setInt(4, date2.getMonth());
            prst.setInt(5,date2.getDate());
            prst.setString(6, sAbbrev);
        }
        else {
            sql34 = " SELECT AVAILABLE, HOUR, DAY, DATE, MONTH, YEAR  FROM " + sTableName + " WHERE AVAILABLE = 1 AND MONTH = ? AND DATE >= ? AND HALL = ? UNION";
            int iCounter = 1;
            int iSecondCounter = 0;

            for (int i = date1.getMonth() + 1; i < date2.getMonth(); i++) {
                sql34 += " SELECT AVAILABLE, HOUR, DAY, DATE, MONTH, YEAR  FROM " + sTableName + " WHERE AVAILABLE = 1 AND MONTH = ? AND HALL = ? UNION ";
                iSecondCounter++;
            }
            sql34 += " SELECT AVAILABLE, HOUR, DAY, DATE, MONTH, YEAR  FROM " + sTableName + " WHERE AVAILABLE = 1 AND MONTH = ? AND DATE <= ? AND HALL = ? ";


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
            prst.setString(++iCounter, sAbbrev);
        }
        ResultSet rst = prst.executeQuery();
        availability = new ArrayList<>();
        while (rst.next()){
            availability.add(new Timeperiod(rst.getString("DATE")
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

    public int getiCapacity() {
        return iCapacity;
    }

    public void setiCapacity(int iCapacity) {
        this.iCapacity = iCapacity;
    }

    public void setAvToZero(int iHourStart, int iHourEnd, int iDate, int iMonth, int iYear){

        for (int i = 0; i < availability.size(); i++){
            if(availability.get(i).getiTime() >= iHourStart && availability.get(i).getiTime() <= iHourEnd){
                availability.get(i).setiAvailable(0);
            }
        }

    }

    public void setAvToOne(int iHourStart, int iHourEnd, int iDate, int iMonth, int iYear){

        for (int i = 0; i < availability.size(); i++){
            if(availability.get(i).getiTime() >= iHourStart && availability.get(i).getiTime() <= iHourEnd){
                availability.get(i).setiAvailable(1);
            }
        }

    }

    public int getAvailability(int iHourStart, int iHourEnd, int iDate, int iMonth, int iYear) {
        for (int i = 0; i < availability.size(); i++) {
            if ( availability.get(i).getiTime() == iHourStart ) {

                return availability.get(i).getiAvailable();

            }
        }
        return 0;
    }


    @Override
    public String toString() {

        String result = "Hall{" +
                "iCapacity=" + iCapacity +
                ", sAbbrev='" + sAbbrev + '\'' +
                ", sTimeStart='" + sTimeStart + '\'' +
                ", sTimeEnd='" + sTimeEnd + '\'' +'}';
        for (int i = 0; i < availability.size(); i++){
            result += " " + availability.get(i).toString();
        }
        return result;
    }


}
