package com.mycompany.app;

import java.sql.*;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.*;

//
import java.time.LocalDate;
import java.time.LocalTime;
import java.text.SimpleDateFormat;
import java.util.Date;

//
public class App
{
    public static void main( String[] args ) throws SQLException, ParseException {
        Connection connection = JDBCHelper.getConnection();
        Statement stmt = connection.createStatement();

        GreedyAlgorithm gr = new GreedyAlgorithm(connection);
        System.out.println("Hello, I am alive ");
        //gr.v_generateTableAvailabilityOfHalls_and_populate("availability_halls_waterloo", "29-Sep-2020","14-Jan-2021");
        ParserHalls prsH = new ParserHalls(connection);
        //prsH.v_parse_initial();
        prsH.v_update_twoWeeksTable_waterloo_to_Zero("WA0H1",29,8,120, 900,900);
        prsH.v_update_twoWeeksTable_waterloo_to_One("WA0H1",29,8,120, 900,1000);
        boolean b = prsH.b_check_twoWeeks_Availability_Waterloo("WA0H1",29,8,120, 900,1000);

        System.out.println(b);

        connection.close();



    }



}
