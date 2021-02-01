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

    void v_addTeachersChoice(List<Integer> teachersChoices) throws SQLException {
        String sql = "SELECT prefday FROM curiculum.main_preferences_lectures_teachers WHERE course_abreviation = ? ";
        String sSS = "";
        PreparedStatement prt = connection.prepareStatement(sql);
        ResultSet rst = prt.executeQuery();

        while(rst.next()){
        if( sSS.equals("monday")){teachersChoices.set(0, teachersChoices.get(0) + 1);}
        if( sSS.equals("tuesday")){teachersChoices.set(1, teachersChoices.get(1) + 1);}
        if( sSS.equals("wednesday")){teachersChoices.set(2, teachersChoices.get(2) + 1);}
        if( sSS.equals("thursday" )){teachersChoices.set(3, teachersChoices.get(3) + 1);}
        if( sSS.equals("friday")){teachersChoices.set(4, teachersChoices.get(4) + 1);}

        }
    }

    void generateGreedySolution()throws SQLException{
            //1.start by assigning the lectures in the timeslots available from the week days
            //2.assign the students to the lectures following the simple rules of the hard constraints
            //3.
            // find a suitable layout of tutorials and lectures . Only Lectures first. since tutorials are not yet inserted
            int iCounter = 0;
            ArrayList<String> mylist = new ArrayList<>();
            mylist.add("Monday");
            mylist.add("Tuesday");
            mylist.add("Wednesday");
            mylist.add("Thursday");
            mylist.add("Friday");
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
            //create a table of assigned lectures:
            String sql99 = "DROP TABLE IF EXISTS assigned_lects";
            String sql33 = "DROP TABLE IF EXISTS halls_availability_tweeks";
            String sql11 =
                    "CREATE TABLE assigned_lects(" +
                    "ABBREV VARCHAR(10), " +
                    "HOURS INT(2), " +
                    "HALL VARCHAR(30)," +
                    "DAYTIME INT(4))";
            String sql88 =
                    "CREATE TABLE halls_availability_tweeks(" +
                    "AVAILABLE INT(1)," +
                    "HALL VARCHAR(10)," +
                    "HOUR INT(2), " +
                    "DAY VARCHAR(10))";
            String sql44 = "SELECT AVAILABLE, HALL, HOUR FROM availability_halls_bush_house";
            String sql55 = "SELECT AVAILABLE, HALL, HOUR FROM availability_halls_waterloo";
            String sql66 = "INSERT INTO halls_availability_tweeks VALUES( ?,?,?,? )";


            statement.executeUpdate(sql99);
            statement.executeUpdate(sql11);
            statement.executeUpdate(sql33);
            statement.executeUpdate(sql88);

            ResultSet rst1 = statement.executeQuery(sql44);
            PreparedStatement prst = connection.prepareStatement(sql66);
            while(!mylist.isEmpty()){
                //iCounter++;
                while(rst1.next()){
                    prst.setInt(1, rst1.getInt("AVAILABLE"));
                    prst.setString(2, rst1.getString("HALL"));
                    prst.setInt(3, rst1.getInt("HOUR"));
                    prst.setString(4, mylist.get(iCounter));
                    prst.executeUpdate();
                }
                //System.out.println("Removed" + mylist.get(iCounter));
                mylist.remove(iCounter);
                rst1.beforeFirst();
            }

    }
}
