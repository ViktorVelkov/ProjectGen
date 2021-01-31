package com.mycompany.app;


import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class GreedyAlgorithm {
    private Connection connection;
    private ArrayList<Duplet> initial = new ArrayList<>();
    private ArrayList<Duplet> assigned = new ArrayList<>();
    public GreedyAlgorithm(Connection connection){
        this.connection = connection;
    }

    void v_findPreferences(String sCourse) throws SQLException {

        ArrayList<String> mylist = new ArrayList<>();
        mylist.add("Monday");
        mylist.add("Tuesday");
        mylist.add("Wednesday");
        mylist.add("Thursday");
        mylist.add("Friday");
        List<Integer> studentsChoice = new ArrayList<>();
        studentsChoice.add(0);studentsChoice.add(0);studentsChoice.add(0);studentsChoice.add(0);studentsChoice.add(0);

        //String sql1 = "SELECT abreviation FROM curiculum.s_courses"; //implement if no course is specified
        String sql = "SELECT prefday FROM curiculum.main_preferences_lectures_students WHERE course_abreviation = ? ";
        PreparedStatement prt = connection.prepareStatement(sql);
        prt.setString(1, sCourse);
        ResultSet rst = prt.executeQuery();
        while(rst.next()){
            String sSS = rst.getString("prefday");
            sSS = sSS.toLowerCase(Locale.ROOT);
            if( sSS.equals("monday")){studentsChoice.set(0,studentsChoice.get(0) + 1);};
            if( sSS.equals("tuesday")){studentsChoice.set(1,studentsChoice.get(1) + 1);};
            if( sSS.equals("wednesday")){studentsChoice.set(2,studentsChoice.get(2) + 1);};
            if( sSS.equals("thursday" )){ studentsChoice.set(3,studentsChoice.get(3) + 1);};
            if( sSS.equals("friday")){int iNum = studentsChoice.get(4) + 1; studentsChoice.set(4,iNum);};
        }

    }

    void v_addTeachersChoice(List<Integer> studentsChoice) throws SQLException {
        String sql = "SELECT prefday FROM curiculum.main_preferences_lectures_teachers WHERE course_abreviation = ? ";
        String sSS = "";
        PreparedStatement prt = connection.prepareStatement(sql);
        ResultSet rst = prt.executeQuery();

        while(rst.next()){
        if( sSS.equals("monday")){};
        if( sSS.equals("tuesday")){};
        if( sSS.equals("wednesday")){};
        if( sSS.equals("thursday" )){};
        if( sSS.equals("friday")){};

        }
    }

    void generateGreedySolution()throws SQLException{
        //1.start by assigning the lectures in the timeslots available from the week days
        //2.assign the students to the lectures following the simple rules of the hard constraints
        //3.
        // find a suitable layout of tutorials and lectures . Only Lectures first. since tutorials are not yet inserted

        String sql22 = "SELECT abreviation,hours_twoweeks FROM s_courses WHERE hours_twoweeks != 0";
        Statement statement = connection.createStatement();
        ResultSet resSet = statement.executeQuery(sql22);
        int index = 0;
        while(resSet.next()){
            String abrev = resSet.getString("abreviation");
            int iHours = resSet.getInt("hours_twoweeks")/2;
            initial.add(new Duplet(abrev,iHours));
            initial.add(new Duplet(abrev,iHours));
            System.out.println(initial.get(index));
            index++;
            index++;
        }

    }
}
///checkk