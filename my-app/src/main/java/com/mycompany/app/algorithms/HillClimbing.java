package com.mycompany.app.algorithms;

import com.mycompany.app.timetabling.DataSetStudents;
import com.mycompany.app.timetabling.Duplet;
import com.mycompany.app.timetabling.PreferredDays;
import com.mycompany.app.timetabling.Week_Timetable;

import javax.swing.plaf.nimbus.State;
import java.io.IOException;
import java.sql.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HillClimbing {

    Week_Timetable timetableont;
    Week_Timetable timetabletwo;
    Connection connection;
    GreedyAlgorithm grdAlg;


    public HillClimbing(Connection connection) {
        this.connection = connection;
        this.grdAlg = new GreedyAlgorithm(connection);
    }


    public Week_Timetable getTimetableont() {
        return timetableont;
    }

    public void setTimetableont(Week_Timetable timetableont) {
        this.timetableont = timetableont;
    }

    public Week_Timetable getTimetabletwo() {
        return timetabletwo;
    }

    public void setTimetabletwo(Week_Timetable timetabletwo) {
        this.timetabletwo = timetabletwo;
    }

    public GreedyAlgorithm getGrdAlg() {
        return grdAlg;
    }

    public void setGrdAlg(GreedyAlgorithm grdAlg) {
        this.grdAlg = grdAlg;
    }

    public void countPeopleVotedForPrefDays(PreferredDays prefs, ArrayList<String> days, String sCourse) throws SQLException {
        String sql = "SELECT COUNT(*) FROM main_preferences_lectures_students WHERE course_abreviation = ? AND LOWER(prefday) = ?";
        String sDay;
        ArrayList<Integer> people = new ArrayList<>();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, sCourse);
        for(int i = 0; i < days.size(); i++){
            sDay = days.get(i).toLowerCase(Locale.ROOT);
            preparedStatement.setString(2,sDay);
            ResultSet rst = preparedStatement.executeQuery();
            while(rst.next()){
                int numberOfPeople = rst.getInt(1);
                people.add(numberOfPeople);
            }
            rst.close();
        }
        preparedStatement.close();
        prefs.setpeopleVoted(people);

    }


    private PreferredDays o_getPreferencesStudents(String sCourse, int iOnPrivilege, int iMultiplicator) throws SQLException {

        PreferredDays prefdays = new PreferredDays();

        ArrayList<String> mylist = new ArrayList<>();                                   //days M, T, W ...
        ArrayList<String> myResult = new ArrayList<>();                                 // chosen day(s)

        List<Integer> studentsChoice = new ArrayList<>();                               // for each day a number of people voted
        List<Double> studentsChoiceHeuristics = new ArrayList<>();                      // for each day calculate heuristics, based on student's preference
        List<Integer> preferredHour = new ArrayList<>();                                // preferred hour of day
        List<Integer> preferredHour_2 = new ArrayList<>();                              // preferred hour of day .2
        List<Integer> preferredHourHeuristics = new ArrayList<>();                      // for each Hour count people voted
        List<Integer> preferredHour_2Heuristics = new ArrayList<>();                    // for each Hour count people voted

        ArrayList<Double> weightOnChoice = new ArrayList<>();
        String sSS = "";
        String sql =
                "SELECT c.prefday, c.scale, c.privileged, c.preftime, c.preftime_secondchoice FROM( " +
                        "SELECT * FROM curiculum.main_preferences_lectures_students  a " +
                        "INNER JOIN preferences b " +
                        "ON a.kings_id_fk = b.kings_id )as c " +
                        "WHERE c.course_abreviation = ?";  //

        mylist.add("Monday");
        mylist.add("Tuesday");
        mylist.add("Wednesday");
        mylist.add("Thursday");
        mylist.add("Friday");

        studentsChoice.add(0);
        studentsChoice.add(0);
        studentsChoice.add(0);
        studentsChoice.add(0);
        studentsChoice.add(0);

        studentsChoiceHeuristics.add(0.0);
        studentsChoiceHeuristics.add(0.0);
        studentsChoiceHeuristics.add(0.0);
        studentsChoiceHeuristics.add(0.0);
        studentsChoiceHeuristics.add(0.0);

        for(int i= 0; i < 11; i++){
            preferredHour.add(900 + i*100);
            preferredHour.add(900 + i*100 + 30);
            preferredHour_2.add(900 + i*100);
            preferredHour_2.add(900 + i*100 + 30);
            preferredHourHeuristics.add(0);
            preferredHourHeuristics.add(0);
            preferredHour_2Heuristics.add(0);
            preferredHour_2Heuristics.add(0);
        }

        PreparedStatement prt = connection.prepareStatement(sql);
        prt.setString(1, sCourse);
        ResultSet rst = prt.executeQuery();

        while(rst.next()) {
            sSS = rst.getString("prefday");
            sSS = sSS.toLowerCase(Locale.ROOT);

            int iHour = rst.getInt("c.preftime");
            int iHour2 = rst.getInt("c.preftime_secondchoice");

            for(int i = 0; i < preferredHour.size(); i++) {
                if (iHour == preferredHour.get(i)) {
                    preferredHourHeuristics.set(i, preferredHourHeuristics.get(i) + 1);
                    break;
                }
            }
            for(int i = 0; i < preferredHour_2.size(); i++){
                if(iHour2 == preferredHour_2.get(i)){
                    preferredHour_2Heuristics.set(i, preferredHour_2Heuristics.get(i) + 1 );
                    break;
                }
            }

            if (sSS.equals("monday")) {
                studentsChoice.set(0, studentsChoice.get(0) + 1);
                if (iOnPrivilege == 1)      // this means if I have chosen to count privileged students as better heuristics
                    studentsChoiceHeuristics.set(0, studentsChoiceHeuristics.get(0) + rst.getInt("c.scale") * 0.9 + rst.getInt("c.privileged") * iMultiplicator);
                else studentsChoiceHeuristics.set(0, studentsChoiceHeuristics.get(0) + rst.getInt("c.scale"));            }
            ;

            if (sSS.equals("tuesday")) {
                studentsChoice.set(1, studentsChoice.get(1) + 1);
                if (iOnPrivilege == 1)
                    studentsChoiceHeuristics.set(1, studentsChoiceHeuristics.get(1) + rst.getInt("c.scale") * 0.9 + rst.getInt("c.privileged") * iMultiplicator);
                else studentsChoiceHeuristics.set(1, studentsChoiceHeuristics.get(1) + rst.getInt("c.scale"));            }
            ;

            if (sSS.equals("wednesday")) {
                studentsChoice.set(2, studentsChoice.get(2) + 1);
                if (iOnPrivilege == 1)
                    studentsChoiceHeuristics.set(2, studentsChoiceHeuristics.get(2) + rst.getInt("c.scale") * 0.9 + rst.getInt("c.privileged") * iMultiplicator);
                else studentsChoiceHeuristics.set(2, studentsChoiceHeuristics.get(2) + rst.getInt("c.scale"));            }
            ;

            if (sSS.equals("thursday")) {
                studentsChoice.set(3, studentsChoice.get(3) + 1);
                if (iOnPrivilege == 1)
                    studentsChoiceHeuristics.set(3, studentsChoiceHeuristics.get(3) + rst.getInt("c.scale") * 0.9 + rst.getInt("c.privileged") * iMultiplicator);
                else studentsChoiceHeuristics.set(3, studentsChoiceHeuristics.get(3) + rst.getInt("c.scale"));            }
            ;

            if (sSS.equals("friday")) {
                studentsChoice.set(4, studentsChoice.get(4) + 1);
                if (iOnPrivilege == 1)
                    studentsChoiceHeuristics.set(4, studentsChoiceHeuristics.get(4) + rst.getInt("c.scale") * 0.9 + rst.getInt("c.privileged") * iMultiplicator);
                else studentsChoiceHeuristics.set(4, studentsChoiceHeuristics.get(4) + rst.getInt("c.scale"));

            }
        }
        ///

        prt.close();
        rst.close();


        int   highestIndex = -1;
        double     highest = 0.0;
        int highestIndex2 = -1;
        int highestIndex3 = -1;
        int bestHour = 0;
        int bestHour_2 = 0;
        for(int i = 0; i < studentsChoiceHeuristics.size(); i++){
            if(  highest < studentsChoiceHeuristics.get(i)){
                highest = studentsChoiceHeuristics.get(i);
                highestIndex = i;
            }
        }

        myResult.add(mylist.get(highestIndex));
        weightOnChoice.add(studentsChoiceHeuristics.get(highestIndex));
        for(int i = 0; i < studentsChoice.size(); i++){

            if(  highest == studentsChoiceHeuristics.get(i) && i != highestIndex ){
                myResult.add(mylist.get(i));
                weightOnChoice.add(studentsChoiceHeuristics.get(i));
            }
        }

        for(int i = 0; i < preferredHourHeuristics.size(); i++){
            if(  bestHour < preferredHourHeuristics.get(i)){
                bestHour = preferredHourHeuristics.get(i);
                highestIndex2 = i;
            }
        }
        for(int i = 0; i < preferredHour_2Heuristics.size(); i++){
            if(  bestHour_2 < preferredHour_2Heuristics.get(i)) {
                bestHour_2 = preferredHour_2Heuristics.get(i);
                highestIndex3 = i;
            }
        }


        prefdays.setPrefDay(myResult);
        //empty for peopleVoted
        countPeopleVotedForPrefDays(prefdays, myResult, sCourse);
        prefdays.setDayHeuristics(weightOnChoice);
        prefdays.setiPrefHour(preferredHour.get(highestIndex2));
        prefdays.setiPrefHour_2(preferredHour_2.get(highestIndex3));
        return prefdays;
    }




    public void createTableAssignedStudents() throws SQLException {
        String sql66 = "CREATE TABLE assignedStudents(kings_id int unsigned not null, " +
                                                        "lecture_code int unsigned not null," +
                                                        "abreviation varchar(10), " +
                                                        "prefday varchar(10), " +
                                                        "preftime int(4), " +
                                                        "preftime_secondchoice int(4), " +
                                                        "scale int(2), " +
                                                        "privileged int(1)  " +
                        ")";

        Statement statement = connection.createStatement();
        statement.executeUpdate(sql66);
        statement.close();

    }


    public void getHCData3() throws SQLException {
        ArrayList<DataSetStudents> searchData = new ArrayList<>();
        String sql99 =  "SELECT c.kings_id, lecture_code,abreviation, prefday, preftime, preftime_secondchoice, scale, privileged " +
                        "FROM ( (select * " +
                                "FROM students_lectures a " +
                                "JOIN courses b ON a.lecture_code = b.inside_code ) AS c " +
                                "JOIN main_preferences_lectures_students m ON m.course_abreviation = c.abreviation " + "AND c.kings_id = m.kings_id_fk " +
                                "JOIN preferences p ON m.kings_id_fk = p.kings_id ) WHERE c.kings_id < 1900000";

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql99);
        while (resultSet.next()){
            searchData.add(new DataSetStudents
                            (resultSet.getInt(1),
                            resultSet.getInt(2),
                            resultSet.getString(3),
                            resultSet.getString(4),
                            resultSet.getInt(5),
                            resultSet.getInt(6),
                            resultSet.getInt(7),
                            resultSet.getInt(8))
                        );
        }

        //start by assigning people with stronger preferences, for example the privileged people
        //or maybe not;


        resultSet.close();
        statement.close();

    }
    public void getHCData2() throws SQLException {
        ArrayList<DataSetStudents> searchData = new ArrayList<>();
        String sql99 =  "SELECT c.kings_id, lecture_code,abreviation, prefday, preftime, preftime_secondchoice, scale, privileged " +
                        "FROM ( (select * " +
                                "FROM students_lectures a " +
                                "JOIN courses b ON a.lecture_code = b.inside_code ) AS c " +
                                "JOIN main_preferences_lectures_students m ON m.course_abreviation = c.abreviation " + "AND c.kings_id = m.kings_id_fk " +
                                "JOIN preferences p ON m.kings_id_fk = p.kings_id ) WHERE c.kings_id > 1900000 && c.kings_id < 2000000";

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql99);
        while (resultSet.next()){
            searchData.add(new DataSetStudents
                            (resultSet.getInt(1),
                            resultSet.getInt(2),
                            resultSet.getString(3),
                            resultSet.getString(4),
                            resultSet.getInt(5),
                            resultSet.getInt(6),
                            resultSet.getInt(7),
                            resultSet.getInt(8))
                        );
        }

        //start by assigning people with stronger preferences, for example the privileged people
        //or maybe not;


        resultSet.close();
        statement.close();

    }


    public void info(){

        /*SELECT distinct lecture_code,abreviation
FROM ( (select * FROM students_lectures a JOIN courses b ON a.lecture_code = b.inside_code ) AS c
		JOIN main_preferences_lectures_students m ON m.course_abreviation = c.abreviation AND c.kings_id = m.kings_id_fk
		JOIN preferences p ON m.kings_id_fk = p.kings_id ) WHERE c.kings_id < 1900000 order by privileged desc, SCALE desc, c.KINGS_ID
         */
    }





    public ArrayList<DataSetStudents> getHCData1() throws SQLException {
        ArrayList<DataSetStudents> searchData = new ArrayList<>();
        String sql99 =  "SELECT c.kings_id, lecture_code,abreviation, prefday, preftime, preftime_secondchoice, scale, privileged " +
                        "FROM ( (select * " +
                                "FROM students_lectures a " +
                                "JOIN courses b ON a.lecture_code = b.inside_code ) AS c " +
                                "JOIN main_preferences_lectures_students m ON m.course_abreviation = c.abreviation " + "AND c.kings_id = m.kings_id_fk " +
                                "JOIN preferences p ON m.kings_id_fk = p.kings_id ) WHERE c.kings_id > 2000000";

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql99);
        while (resultSet.next()){
            searchData.add(new DataSetStudents
                            (resultSet.getInt(1),
                            resultSet.getInt(2),
                            resultSet.getString(3),
                            resultSet.getString(4),
                            resultSet.getInt(5),
                            resultSet.getInt(6),
                            resultSet.getInt(7),
                            resultSet.getInt(8))
                        );
        }

        //start by assigning people with stronger preferences, for example the privileged people
        //or maybe not;


        resultSet.close();
        statement.close();
        return searchData;
    }

    public ArrayList<Duplet> checkDuplication(Week_Timetable one, Week_Timetable two) throws SQLException {

        ArrayList<Duplet> notMatchingLectures = new ArrayList<>();
        String sTableName = "";
        for(int i = 0; i< timetableont.getAssignedLectures().size();i++){
            Duplet temp = timetableont.getAssignedLectures().get(i);
            if(temp.getsLectureHall().startsWith("B")){
                sTableName = "two_weeks_availability_halls_bush_house";
            }
            else if(temp.getsLectureHall().startsWith("W")){
                sTableName = "two_weeks_availability_halls_waterloo";
            }


            String sql77 = "SELECT hour, available FROM " + sTableName
                            + " WHERE hall = '" + (temp.getsLectureHall()) + "'"
                            + " AND DATE = " + Integer.toString(temp.getiDayScheduled())
                            + " AND MONTH = " + Integer.toString(temp.getiMonthScheduled() - 1)
                            + " AND YEAR = " + Integer.toString(temp.getiYearScheduled())
                            + " AND HOUR >= " + Integer.toString(temp.getiHourScheduled())
                            + " AND HOUR <= " + Integer.toString((int) (temp.getiHourScheduled() + 100*temp.getiHours()));
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql77);
            int iCount = 0;
            while (resultSet.next()){
               if(resultSet.getInt(2) == 1) iCount++;
            }
            if(iCount != temp.getiHours()*2){
                notMatchingLectures.add(temp);
            }
        }

        return notMatchingLectures;
    }

    private void copyWeekOneExcept(ArrayList<Duplet> dups) throws CloneNotSupportedException, ParseException {

        for(int i=0; i<dups.size();i++){
            //remove all the unavailable lectures from the assignedArray
            for(int k = 0; k< timetabletwo.getAssignedLectures().size(); k++){
                if(timetabletwo.getAssignedLectures().get(k).getsLect().equals(dups.get(i).getsLect())){
                    timetabletwo.getLectures().add(timetabletwo.getAssignedLectures().get(k));
                    timetabletwo.getAssignedLectures().remove(k);
                }
            }
        }
        //now I need to find suitable times for those lectures as well
    }




    public Week_Timetable sortOutWeek_nextWeek(Week_Timetable toClone) throws SQLException, CloneNotSupportedException, ParseException, IOException {
        Week_Timetable timetable = (Week_Timetable) toClone.clone();
        timetable.v_updateDates();

        ArrayList<Duplet> forWeekTwo = checkDuplication(timetable, toClone);
        if(forWeekTwo.isEmpty()){
        }
        else{
            copyWeekOneExcept(forWeekTwo);
            grdAlg.generateGreedySolution2(grdAlg.sCoursesTable, grdAlg.sStudentsTable,grdAlg.iLocalMin, grdAlg.iLocalMax ,grdAlg.prefdDays, timetabletwo); //make these universal and sharable for the class
            timetabletwo = grdAlg.getWeek_timetable_spare();
        }

        return timetable;
    }




    public int generateHCSolution() throws SQLException, CloneNotSupportedException, IOException, ParseException {

        getHCData3();


        ArrayList<DataSetStudents> toPrint = getHCData1();
        sortOutWeek_nextWeek(timetableont);
        System.out.println("==========================================================================================");
        System.out.println(timetableont);
        //System.out.println(toPrint);
        if(1 == 1){             //if solution found return 0
            return 0;
        }
        return 1;
    }

}
