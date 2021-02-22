package com.mycompany.app.consraints;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Constraint_Free_Day {
    Connection connection;
    public Constraint_Free_Day(Connection connection){
        this.connection = connection;
    }

    public void v_setFreeDay(String sDate) throws SQLException, ParseException {
        String sql111 = "UPDATE two_weeks_availability_halls_bush_house SET AVAILABLE = 0 WHERE DATE = ? AND MONTH = ? AND YEAR = ?";
        String sql333 = "UPDATE two_weeks_availability_halls_waterloo SET AVAILABLE = 0 WHERE DATE = ? AND MONTH = ? AND YEAR = ?";

        Date date = new SimpleDateFormat("dd-MMM-yyyy").parse(sDate);

        PreparedStatement statement = connection.prepareStatement(sql111);
        PreparedStatement pstatementp = connection.prepareStatement(sql111);

        statement.setInt(1,date.getDate());
        statement.setInt(2,date.getMonth());
        statement.setInt(3,date.getYear());

        pstatementp.setInt(1,date.getDate());
        pstatementp.setInt(2,date.getMonth());
        pstatementp.setInt(3,date.getYear());

        statement.execute();
        pstatementp.execute();

        statement.close();
        pstatementp.close();
    }

}
