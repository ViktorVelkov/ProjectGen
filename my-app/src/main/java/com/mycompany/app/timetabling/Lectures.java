package com.mycompany.app.timetabling;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Lectures {
    Connection connection;
    ArrayList<Duplet> lectureEvents = new ArrayList<>();
    public Lectures(){}
    public void assignLectures() throws SQLException {

        //assign the first year lectures, they are mandatory, easier to figure out

        String sql66 = "SELECT DISTINCT lecture_code FROM students_lectures";
        String sql67 = "SELECT  lecture_code FROM students_lectures";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql66);
        ArrayList<Duplet> lectures = new ArrayList<>();
        Week_Timetable week_timetable = new Week_Timetable();
        Week_Timetable week_timetable2 = new Week_Timetable();
        // assign to lectureEvents
        // this will be a array of lectures the way they appear in the week
        while (resultSet.next()){
            lectures.add(new Duplet(resultSet.getInt(1)));
            System.out.println();
        }
    }
}
