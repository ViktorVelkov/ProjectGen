package com.mycompany.app;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ParserHalls {

    private Connection connection;
    private Availability_Bush av_bush;
    private Availability_Waterloo av_waterloo;
    //A class here for the tutorials av_waterloot_tut
    //A class here for the tutorials av_bush_house_tut

    public ParserHalls(Connection connection){
        this.connection = connection;
        av_bush = new Availability_Bush(connection);
        av_waterloo = new Availability_Waterloo(connection);
    }

    public void v_parse_initial()throws SQLException {
        String sql = "SELECT DISTINCT(inside_code), intended_for_lectures FROM FACILITIES";
        String sHall = "";
        Statement statement = connection.createStatement();
        Statement statement2 = connection.createStatement();
        ResultSet rst = statement.executeQuery(sql);

        //truncate tables if this is initial parse:
        String sql22 = "TRUNCATE TABLE availability_halls_bush_house";
        String sql33 = "TRUNCATE TABLE availability_halls_waterloo";
        statement2.executeUpdate(sql22);
        statement2.executeUpdate(sql33);

        //parsing time, it could go either to waterloo or to bushHouse
        while (rst.next()){

            sHall = rst.getString("inside_code");

            if(rst.getInt("intended_for_lectures") == 1) {
                if (sHall.startsWith("B")) { av_bush.v_populate(sHall); }
                if (sHall.startsWith("W")) { av_waterloo.v_populate(sHall); }
            }

            else{
                if (sHall.startsWith("B")) { //add to the tutorials table hall
                     }
                if (sHall.startsWith("W")) { //add to the tutorials table hall
                     }
            }

        }
        rst.close();
        statement.close();
        statement2.close();

    }

    public void v_update_twoWeeksTable_bushhouse_to_One(String sHallName, int iDate, int iMonth, int iYear, int iTimeStart, int iTimeEnd) throws SQLException {
            av_bush.v_update_twoWeeksTableToOne(sHallName,iDate,iMonth,iYear,iTimeStart,iTimeEnd);
    }

    public void v_update_twoWeeksTable_waterloo_to_One(String sHallName,int iDate, int iMonth, int iYear, int iTimeStart, int iTimeEnd) throws SQLException {
            av_waterloo.v_update_twoWeeksTableToOne(sHallName,iDate,iMonth,iYear,iTimeStart,iTimeEnd);
    }


    public void v_update_twoWeeksTable_bushhouse_to_Zero(String sHallName, int iDate, int iMonth, int iYear, int iTimeStart, int iTimeEnd) throws SQLException {
        av_bush.v_update_twoWeeksTableToZero(sHallName,iDate,iMonth,iYear,iTimeStart,iTimeEnd);
    }

    public void v_update_twoWeeksTable_waterloo_to_Zero(String sHallName, int iDate, int iMonth, int iYear, int iTimeStart, int iTimeEnd) throws SQLException {
        av_waterloo.v_update_twoWeeksTableToZero(sHallName,iDate,iMonth,iYear,iTimeStart,iTimeEnd);
    }

    public boolean b_check_twoWeeks_Availability_Bush_House(String sHallName,int iDate, int iMonth, int iYear, int iTimeStart, int iTimeEnd) throws SQLException {
        return av_bush.v_check_twoWeeks_Availability(sHallName,iDate,iMonth,iYear,iTimeStart,iTimeEnd);
    }

    public boolean b_check_twoWeeks_Availability_Waterloo(String sHallName,int iDate, int iMonth, int iYear, int iTimeStart, int iTimeEnd) throws SQLException {
        return av_waterloo.v_check_twoWeeks_Availability(sHallName,iDate,iMonth,iYear,iTimeStart,iTimeEnd);
    }

        //End of Class
}
