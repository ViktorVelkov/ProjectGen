package com.mycompany.app;


import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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
        prt.close();
        rst.close();
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
        prt.close();
        rst.close();
    }



    private ArrayList<Duplet> lecturesToBeAssigned() throws SQLException {

        String sql22 = "SELECT abreviation,hours_twoweeks FROM s_courses WHERE hours_twoweeks != 0";
        Statement statement = connection.createStatement();
        ResultSet resSet = statement.executeQuery(sql22);
        int index = 0;
        while(resSet.next()){
            String abrev = resSet.getString("abreviation");
            int iHours = resSet.getInt("hours_twoweeks")/2;
            initial.add(new Duplet(abrev,iHours));
            initial.add(new Duplet(abrev,iHours));
            //System.out.println(initial.get(index));
            //index++;
            //index++;
        }

        statement.close();
        resSet.close();

        return initial;

    }

    // Taken from https://howtodoinjava.com/java/date-time/dates-between-two-dates/

    public static List<Date> getDaysBetweenDates(Date startdate, Date enddate)
    {
        List<Date> dates = new ArrayList<Date>();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(startdate);

        while (calendar.getTime().before(enddate))
        {
            Date result = calendar.getTime();
            int a = result.getDay();                                //delete this after test
            if(result.getDay() != 6) {
                if (result.getDay() != 0) {
                    dates.add(result);
                }
            }
            calendar.add(Calendar.DATE, 1);

        }
        return dates;
    }

    //



    public void v_generateTableAvailabilityOfHalls_and_populate(String sTable, String sStartDate, String sEndDate) throws SQLException, ParseException {
        //1.start by assigning the lectures in the timeslots available from the week days
        //2.assign the students to the lectures following the simple rules of the hard constraints
        //3.
        // find a suitable layout of tutorials and lectures . Only Lectures first. since tutorials are not yet inserted
        int iCounter = 0;
        Statement statement = connection.createStatement();

        SimpleDateFormat formatter6=new SimpleDateFormat("dd-MMM-yyyy");
        Date date00 = (Date)formatter6.parse(sStartDate);
        Date date88 = (Date)formatter6.parse(sEndDate);

        List<Date> allDates = getDaysBetweenDates(date00,date88);
        for(int i = 0; i < allDates.size(); i ++){
            System.out.println(allDates.get(i));
        }


        String sql33 = "DROP TABLE IF EXISTS two_weeks_" + sTable;
        String sql88 =
                "CREATE TABLE two_weeks_" + sTable + "(" +
                        "AVAILABLE INT(1)," +
                        "HALL VARCHAR(10)," +
                        "HOUR INT(2), " +
                        "DAY VARCHAR(10)," +
                        "DATE INT(2)," +
                        "MONTH INT(2)," +
                        "YEAR INT(4))";
//
//        String sql44 = "SELECT AVAILABLE, HALL, HOUR FROM availability_halls_bush_house";
//        String sql55 = "SELECT AVAILABLE, HALL, HOUR FROM availability_halls_waterloo";
        String sql44 = "SELECT AVAILABLE, HALL, HOUR FROM " + sTable;
        String sql66 = "INSERT INTO two_weeks_"+ sTable + " (AVAILABLE, HALL, HOUR, DAY, DATE, MONTH, YEAR) VALUES( ?,?,?,?,?,?,? )";
        //String sql66 = "INSERT INTO halls_availability_tweeks(AVAILABLE, HALL, HOUR, DAY, DATE, MONTH, YEAR) VALUES( ?,?,?,? )";

        statement.executeUpdate(sql33);
        statement.executeUpdate(sql88);

        ResultSet rst1 = statement.executeQuery(sql44);
        PreparedStatement prst = connection.prepareStatement(sql66);
        while(!allDates.isEmpty()){

            while(rst1.next()){

                prst.setInt(1, rst1.getInt(3));
                prst.setString(2, rst1.getString("HALL"));
                prst.setInt(3, rst1.getInt(1));

                switch( allDates.get(iCounter).getDay() ) {
                    case 1: prst.setString(4, "Monday"); break;    // switch case needed here
                    case 2: prst.setString(4, "Tuesday"); break;
                    case 3: prst.setString(4, "Wednesday"); break;
                    case 4: prst.setString(4, "Thursday"); break;
                    case 5: prst.setString(4, "Friday"); break;
                }


                prst.setInt(5, (int)allDates.get(iCounter).getDate());
                prst.setInt(6, (int)allDates.get(iCounter).getMonth());
                prst.setInt(7, (int)allDates.get(iCounter).getYear());
                prst.executeUpdate();
            }
            allDates.remove(iCounter);
            rst1.beforeFirst();
        }
        statement.close();
        rst1.close();
        prst.close();
    }


    //


    private void generateAssignedLecturesTable()throws SQLException{
        //1.start by assigning the lectures in the timeslots available from the week days
        //2.assign the students to the lectures following the simple rules of the hard constraints
        //3.
        // find a suitable layout of tutorials and lectures . Only Lectures first. since tutorials are not yet inserted
        String sql22 = "SELECT abreviation,hours_twoweeks FROM s_courses WHERE hours_twoweeks != 0";
        Statement statement = connection.createStatement();
        ResultSet resSet = statement.executeQuery(sql22);
        int index = 0;
        String sql99 = "DROP TABLE IF EXISTS assigned_lects";
        String sql11 =
                "CREATE TABLE assigned_lects(" +
                        "ABBREV VARCHAR(10), " +
                        "HOURS INT(2), " +
                        "HALL VARCHAR(30)," +
                        "DAYTIME INT(4)," +
                        "DAY VARCHAR(10)," +
                        "DATE INT(2)," +
                        "MONTH INT(2)," +
                        "YEAR INT(4))";


        statement.executeUpdate(sql99);
        statement.executeUpdate(sql11);

    }

    //
    private int i_calcStudentsHalls(){
        return  0;
    }

    void v_setNotAvailable_HallsTW_day(String sHall, String sDay, int iTimeStart, int iTimeEnd) throws SQLException {
        String sql21 = "UPDATE halls_availability_tweeks " +
                        "SET AVAILABLE = 0 " +
                        "WHERE HALL = '" + sHall + "' " +
                        "AND DAY = '" + sDay + "' " +
                        "AND HOUR BETWEEN " + iTimeStart + " AND " + iTimeEnd;
        Statement statement = connection.createStatement();
        statement.executeUpdate(sql21);
    }

    void v_setAvailable_HallsTW_day(String sHall, String sDay, int iTimeStart, int iTimeEnd) throws SQLException {
        String sql21 = "UPDATE halls_availability_tweeks " +
                "SET AVAILABLE = 1 " +
                "WHERE HALL = '" + sHall + "' " +
                "AND DAY = '" + sDay + "' " +
                "AND HOUR BETWEEN " + iTimeStart + " AND " + iTimeEnd;
        Statement statement = connection.createStatement();
        statement.executeUpdate(sql21);
    }

    void v_setNotAvailable_HallsTW_date(String sHall, int iDateOfMonth, int iMonth, int iTimeStart, int iTimeEnd) throws SQLException {
        String sql21 = "UPDATE halls_availability_tweeks " +
                        "SET AVAILABLE = 0 " +
                        "WHERE HALL = '" + sHall + "' " +
                        "AND DATE = " + iDateOfMonth + " " +
                        "AND MONTH = " + iMonth + " " +
                        "AND HOUR BETWEEN " + iTimeStart + " AND " + iTimeEnd;
        Statement statement = connection.createStatement();
        statement.executeUpdate(sql21);
    }

    void v_setAvailable_HallsTW_date(String sHall, int iDateOfMonth, int iMonth, int iTimeStart, int iTimeEnd) throws SQLException {
        String sql21 = "UPDATE halls_availability_tweeks " +
                "SET AVAILABLE = 1 " +
                "WHERE HALL = '" + sHall + "' " +
                "AND DATE = " + iDateOfMonth + " " +
                "AND MONTH = " + iMonth + " " +
                "AND HOUR BETWEEN " + iTimeStart + " AND " + iTimeEnd;
        Statement statement = connection.createStatement();
        statement.executeUpdate(sql21);
    }

    void generateGreedySolution()throws SQLException{
        //generate a table for availability and a table for assigning lectures and maybe start updating them

        /*working code
        v_generateTableAvailabilityOfHalls_and_populate();  //table for availability of the halls: available, hall, hour, day (must add date)
        generateAssignedLecturesTable();     //table for assigned lectures abbrev, hours, halls, daytime
        */
        ArrayList<Duplet> myList = lecturesToBeAssigned();
        String sql77 = "SELECT COUNT(*) FROM S_STUDENTS WHERE KINGS_ID LIKE '18%'";
        String sql78 = "SELECT COUNT(*) FROM S_STUDENTS WHERE KINGS_ID LIKE '19%'";
        String sql79 = "SELECT COUNT(*) FROM S_STUDENTS WHERE KINGS_ID LIKE '20%'";
        Statement statement = connection.createStatement();
        ResultSet rst = statement.executeQuery(sql77);


    }


    //END of Class

}
