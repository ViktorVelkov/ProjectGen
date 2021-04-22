package com.mycompany.app.algorithms;

import com.mycompany.app.timetabling.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.sql.*;
import java.text.ParseException;
import java.util.*;


public class HillClimbing {


    //vital
    private Connection connection;
    private GreedyAlgorithm grdAlg;
    private Solution INITIAL_SOLUTION = new Solution();     //also a data container
    private Solution BEST_SOLUTION = new Solution();        //also a data container
    private Solution CURRENT_SOLUTION = new Solution();        //also a data container

    //scales
    private static double dayScale = 1.0;
    private static double hourScale = 1.0;
    private static double percentageScale = 1.0;
    private static double heuristicsScale = 1.0;
    private static double heuristicsRawScale = 1.0;
    private static double heuristicsScaledScale = 1.0;
    private static double impossibleSatScale = 1.0;
    private static double completeSatScale = 1.0;
    private static double zeroSatScale = 1.0;

    //data containers
    static int weekNumber = 0;
    private static double DSCALE_GLOBAL = 0.9;
    private Week_Timetable TIMETABLEONT_GLOBAL;
    private Week_Timetable TIMETABLETWO_GLOBAL;
    private HashMap<Integer, Student> ASSIGNED_STUDENTS_GLOBAL=new HashMap<>();
    private HashMap<Integer, Student> NOT_ASSIGNED_STUDENTS_GLOBAL=new HashMap<>();
    private HashMap<Integer, Student> STUDENTS_TO_BE_ASSINGED_GLOBAL = new HashMap<>();
    public ArrayList<SGT> ASSIGNED_SGTS_CURRENTWEEK_GLOBAL = new ArrayList<SGT>();
    private ArrayList<SGT> NOT_ASSIGNED_SGTS_CURRENTWEEK_GLOBAL = new ArrayList<SGT>();
    private ArrayList<DataSetStudents> data = new ArrayList<>();






    public HillClimbing(Connection connection) {
        this.connection = connection;
        this.grdAlg = new GreedyAlgorithm(connection);
    }


    public Solution getINITIAL_SOLUTION() {
        return INITIAL_SOLUTION;
    }

    public void setINITIAL_SOLUTION(Solution INITIAL_SOLUTION) {
        this.INITIAL_SOLUTION = INITIAL_SOLUTION;
    }

    public Solution getBEST_SOLUTION() {
        return BEST_SOLUTION;
    }

    public void setBEST_SOLUTION(Solution BEST_SOLUTION) {
        this.BEST_SOLUTION = BEST_SOLUTION;
    }

    public static double getDayScale() {
        return dayScale;
    }

    public static void setDayScale(double dayScale) {
        HillClimbing.dayScale = dayScale;
    }

    public static double getHourScale() {
        return hourScale;
    }

    public static void setHourScale(double hourScale) {
        HillClimbing.hourScale = hourScale;
    }

    public static double getHeuristicsScale() {
        return heuristicsScale;
    }

    public static void setHeuristicsScale(double heuristicsScale) {
        HillClimbing.heuristicsScale = heuristicsScale;
    }

    public static double getHeuristicsRawScale() {
        return heuristicsRawScale;
    }

    public static void setHeuristicsRawScale(double heuristicsRawScale) {
        HillClimbing.heuristicsRawScale = heuristicsRawScale;
    }

    public static double getHeuristicsScaledScale() {
        return heuristicsScaledScale;
    }

    public static void setHeuristicsScaledScale(double heuristicsScaledScale) {
        HillClimbing.heuristicsScaledScale = heuristicsScaledScale;
    }

    public static double getImpossibleSatScale() {
        return impossibleSatScale;
    }

    public static void setImpossibleSatScale(double impossibleSatScale) {
        HillClimbing.impossibleSatScale = impossibleSatScale;
    }

    public static double getCompleteSatScale() {
        return completeSatScale;
    }

    public static void setCompleteSatScale(double completeSatScale) {
        HillClimbing.completeSatScale = completeSatScale;
    }

    public static double getZeroSatScale() {
        return zeroSatScale;
    }

    public static void setZeroSatScale(double zeroSatScale) {
        HillClimbing.zeroSatScale = zeroSatScale;
    }

    public static int getWeekNumber() {
        return weekNumber;
    }

    public static void setWeekNumber(int weekNumber) {
        HillClimbing.weekNumber = weekNumber;
    }

    //NOT USED FOR NOW
    private void createTableAssignedStudents() throws SQLException {
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

    //NOT USED FOR NOW
    public void copyLeadingWeek(Week_Timetable timetable) throws CloneNotSupportedException, ParseException, SQLException {
        ArrayList<Duplet> lecturesToReassign = new ArrayList<>();
        ArrayList<SGT> sgtsToReassign = new ArrayList<>();


        for(Duplet unassigned : timetable.getLectures()){
            lecturesToReassign.add(unassigned);
        }

        for(Duplet lecture : timetable.getAssignedLectures()){
            int avail = lecture.checkAvailabilityOfHall();
            if(avail == 1){
                lecture.updateDate(7);
            }
        }
//        for(int i=0; i<dups.size();i++){
//            //remove all the unavailable lectures from the assignedArray
//            for(int k = 0; k< TIMETABLETWO_GLOBAL.getAssignedLectures().size(); k++){
//                if(TIMETABLETWO_GLOBAL.getAssignedLectures().get(k).getsLect().equals(dups.get(i).getsLect())){
//                    TIMETABLETWO_GLOBAL.getLectures().add(TIMETABLETWO_GLOBAL.getAssignedLectures().get(k));
//                    TIMETABLETWO_GLOBAL.getAssignedLectures().remove(k);
//                }
//            }
//        }
        //now I need to find suitable times for those lectures as well
    }

    //NOT USED FOR NOW
    private Week_Timetable sortOutWeek_nextWeek(Week_Timetable toClone) throws SQLException, CloneNotSupportedException, ParseException, IOException {
        Week_Timetable timetable = (Week_Timetable) toClone.clone();
        timetable.v_updateDates();              //need to update dates twice

        ArrayList<Duplet> forWeekTwo = checkDuplication(timetable, toClone);
        if(forWeekTwo.isEmpty()){
            //just update the dates

//            for(Duplet lecture : timetable.getAssignedLectures()){
//                lecture.updateDate(7);
//            }
//            for(Hall hall : timetable.getHalls()){
//                hall.updateHalls(7);
//            }
        }
        else{

//            grdAlg.generateGreedySolution2(grdAlg.sCoursesTable, grdAlg.sStudentsTable,grdAlg.iLocalMin, grdAlg.iLocalMax ,grdAlg.prefdDays, TIMETABLETWO_GLOBAL);
            timetable = grdAlg.getWeek_timetable_ont();
        }

        return timetable;
    }


    //GETTERS AND SETTERS
    public Week_Timetable getTIMETABLEONT_GLOBAL() { return TIMETABLEONT_GLOBAL; }

    public void setTIMETABLEONT_GLOBAL(Week_Timetable TIMETABLEONT_GLOBAL) { this.TIMETABLEONT_GLOBAL = TIMETABLEONT_GLOBAL; }



    public Week_Timetable getTIMETABLETWO_GLOBAL() { return TIMETABLETWO_GLOBAL; }

    public void setTIMETABLETWO_GLOBAL(Week_Timetable TIMETABLETWO_GLOBAL) { this.TIMETABLETWO_GLOBAL = TIMETABLETWO_GLOBAL; }

    public GreedyAlgorithm getGrdAlg() { return grdAlg; }

    public void setGrdAlg(GreedyAlgorithm grdAlg) { this.grdAlg = grdAlg; }

    //end of getters and setters


    private void updateTimeline_SGT(Week_Timetable TIMETABLEONT_GLOBAL){
        //what I do here is remove all the unneeded dependencies if any are left
        ArrayList<String> empty = new ArrayList<>();
        for(int i =0; i< TIMETABLEONT_GLOBAL.getSgt().size(); i++){
            TIMETABLEONT_GLOBAL.getSgt().get(i).getPreferredDays().setPrefDay(empty);
            TIMETABLEONT_GLOBAL.getSgt().get(i).getPreferredDays().setForsgt_usage(TIMETABLEONT_GLOBAL.getSgt().get(i).getsDayOfWeek());             //turning
            TIMETABLEONT_GLOBAL.getSgt().get(i).getPreferredDays().setNotAvailableBefore((int)(TIMETABLEONT_GLOBAL.getSgt().get(i).getiHourScheduled() + 100*TIMETABLEONT_GLOBAL.getSgt().get(i).getiHours()));
//            TIMETABLEONT_GLOBAL.getSgt().get(i).getPreferredDays().setMandatory();
//            TIMETABLEONT_GLOBAL.getSgt().get(i).getPreferredDays().setiPrefHour();
        }

    }

    private void extractSGTsFromLGTs(Week_Timetable TIMETABLEONT_GLOBAL){

        for(int z = 0; z < TIMETABLEONT_GLOBAL.getAssignedLectures().size(); z++) {
            int iCounter = 0;
            Duplet temp = TIMETABLEONT_GLOBAL.getAssignedLectures().get(z);
            for (int i = 0; i < TIMETABLEONT_GLOBAL.getAssignedLGT().size(); i++) {
                //if it's inside then we can't assign it a SGT for this week
                if (TIMETABLEONT_GLOBAL.getAssignedLGT().get(i).getsLect().equals(TIMETABLEONT_GLOBAL.getAssignedLectures().get(z).getsLect())) {
                    //do not add it to the sgt list
                    break;
                }
                iCounter++;
            }
            if(iCounter == TIMETABLEONT_GLOBAL.getAssignedLGT().size()){
                //temp.setiHours(1.0);
                TIMETABLEONT_GLOBAL.getSgt().add(temp);
            }
        }
    }

    private void countPeopleVotedForPrefDays(PreferredDays prefs, ArrayList<String> days, String sCourse) throws SQLException {
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

    private ArrayList<DataSetStudents> getHCData(int iYear, String sTableName, String sTableNameCourses) throws SQLException {
        ArrayList<DataSetStudents> searchData = new ArrayList<>();
        String sql99 = "";
        if(iYear == 3) {
            sql99 = "SELECT z.kings_id, lecture_code,abreviation, prefday, preftime, preftime_secondchoice, scale, privileged FROM "+  sTableName + " z JOIN ( ";
            sql99 += "SELECT c.kings_id, lecture_code,abreviation, prefday, preftime, preftime_secondchoice, scale, privileged " +
                    "FROM ( (select * " +
                    "FROM students_lectures a " +
                    "JOIN " + sTableNameCourses + " b ON a.lecture_code = b.inside_code ) AS c " +
                    "JOIN main_preferences_lectures_students m ON m.course_abreviation = c.abreviation " + "AND c.kings_id = m.kings_id_fk " +
                    "JOIN preferences p ON m.kings_id_fk = p.kings_id ) WHERE c.kings_id < 1900000 ORDER BY c.kings_id";
            sql99+= " ) d ON z.kings_id = d.kings_id";

        }
        if(iYear == 2) {
            sql99 = "SELECT z.kings_id, lecture_code,abreviation, prefday, preftime, preftime_secondchoice, scale, privileged FROM "+  sTableName + " z JOIN ( ";
            sql99 += "SELECT c.kings_id, lecture_code,abreviation, prefday, preftime, preftime_secondchoice, scale, privileged " +
                    "FROM ( (select * " +
                    "FROM students_lectures a " +
                    "JOIN " + sTableNameCourses + " b ON a.lecture_code = b.inside_code ) AS c " +
                    "JOIN main_preferences_lectures_students m ON m.course_abreviation = c.abreviation " + "AND c.kings_id = m.kings_id_fk " +
                    "JOIN preferences p ON m.kings_id_fk = p.kings_id ) WHERE c.kings_id > 1900000 && c.kings_id < 2000000 ORDER BY c.kings_id";
            sql99+= " ) d ON z.kings_id = d.kings_id";

        }

        if(iYear == 1){
            sql99 = "SELECT z.kings_id, lecture_code,abreviation, prefday, preftime, preftime_secondchoice, scale, privileged FROM "+  sTableName + " z JOIN ( ";
            sql99 +=  "SELECT c.kings_id, lecture_code,abreviation, prefday, preftime, preftime_secondchoice, scale, privileged " +
                    "FROM ( (select * " +
                    "FROM students_lectures a " +
                    "JOIN " + sTableNameCourses + " b ON a.lecture_code = b.inside_code ) AS c " +
                    "JOIN main_preferences_lectures_students m ON m.course_abreviation = c.abreviation " + "AND c.kings_id = m.kings_id_fk " +
                    "JOIN preferences p ON m.kings_id_fk = p.kings_id ) WHERE c.kings_id > 2000000 ORDER BY c.kings_id";
            sql99+= " ) d ON z.kings_id = d.kings_id";
        }


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
            Student student  = STUDENTS_TO_BE_ASSINGED_GLOBAL.get(resultSet.getInt(1));
            if(student == null){
                student = new Student(resultSet.getInt(1), iYear);
            }
            student.addToCoursesCodes(resultSet.getInt(2));
            student.addToPrefs(new DataSetStudents(resultSet.getInt(1),
                            resultSet.getInt(2),
                            resultSet.getString(3),
                            resultSet.getString(4),
                            resultSet.getInt(5),
                            resultSet.getInt(6),
                            resultSet.getInt(7),
                            resultSet.getInt(8)));
            this.STUDENTS_TO_BE_ASSINGED_GLOBAL.put(resultSet.getInt(1), student);
        }

        //start by assigning people with stronger preferences, for example the privileged people
        //or maybe not;


        resultSet.close();
        statement.close();
        return searchData;

    }


    //THIS COULD BE USEFUL
    public ArrayList<Duplet> checkDuplication(Week_Timetable one, Week_Timetable two) throws SQLException {

        ArrayList<Duplet> notMatchingLectures = new ArrayList<>();
        String sTableName = "";
        for(int i = 0; i< one.getAssignedLectures().size();i++){
            Duplet temp = one.getAssignedLectures().get(i);
            if(temp.getsLectureHall().startsWith("B")){
                sTableName = "two_weeks_availability_halls_bush_house";
            }
            else if(temp.getsLectureHall().startsWith("W")){
                sTableName = "two_weeks_availability_halls_waterloo";
            }


            String sql77 = "SELECT SUM( available ) FROM " + sTableName
                    + " WHERE hall = '" + (temp.getsLectureHall()) + "'"
                    + " AND DATE = " + Integer.toString(temp.getiDayScheduled())
                    + " AND MONTH = " + Integer.toString(temp.getiMonthScheduled() - 1)
                    + " AND YEAR = " + Integer.toString(temp.getiYearScheduled())
                    + " AND HOUR >= " + Integer.toString(temp.getiHourScheduled())
                    + " AND HOUR < " + Integer.toString((int) (temp.getiHourScheduled() + 100*temp.getiHours()));
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql77);
            int iCount = 0;
            while (resultSet.next()){
                if(resultSet.getInt(2) == resultSet.getInt(1)){
                    two.getAssignedLectures().add(temp);
                }else{
                    notMatchingLectures.add(temp);
                }
            }
        }

        return notMatchingLectures;
    }


    public ArrayList<SGT> checkForAQuickAssigning(Week_Timetable one) throws SQLException {

        ArrayList<SGT> availableAtTheSameTimeAsAssignedBefore = new ArrayList<>();
        ArrayList<SGT> not_AvailableAtTheSameTimeAsAssignedBefore = new ArrayList<>();
        String sTableName = "";
        ArrayList<SGT> sgts = new ArrayList<>();
        //if(one.getiWeekNum() == 2){return not_AvailableAtTheSameTimeAsAssignedBefore;}
        for(Duplet assignedEvents : one.getAssignedLectures()){
            for(SGT iterator: this.ASSIGNED_SGTS_CURRENTWEEK_GLOBAL){
                if(iterator.getsLect().equals(assignedEvents.getsLect())){
                    sgts.add(iterator);
                }
            }
        }

        for(int i = 0; i< sgts.size() ;i++){
            Duplet temp = one.getAssignedLectures().get(i);
            SGT tempo = sgts.get(i);
            if(tempo.getsLectureHall().startsWith("B")){
                sTableName = "two_weeks_availability_halls_bush_house_tutorials";
            }
            else if(tempo.getsLectureHall().startsWith("W")){
                sTableName = "two_weeks_availability_halls_waterloo_tutorials";
            }


            String sql77 = "SELECT SUM( available ) FROM " + sTableName
                    + " WHERE hall = '" + (temp.getsLectureHall()) + "'"
                    + " AND DATE = " + Integer.toString(temp.getiDayScheduled())
                    + " AND MONTH = " + Integer.toString(temp.getiMonthScheduled() - 1)
                    + " AND YEAR = " + Integer.toString(temp.getiYearScheduled())
                    + " AND HOUR >= " + Integer.toString(temp.getiHourScheduled())
                    + " AND HOUR < " + Integer.toString((int) (temp.getiHourScheduled() + 100*temp.getiHours()));
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql77);
            int iCount = 0;
            while (resultSet.next()){
                if(resultSet.getInt(2) == resultSet.getInt(1)){
                    availableAtTheSameTimeAsAssignedBefore.add(tempo);
                }else{
                    not_AvailableAtTheSameTimeAsAssignedBefore.add(tempo);
                }
            }
        }
        return not_AvailableAtTheSameTimeAsAssignedBefore;
    }


    public void transit_AllAssignedSGTs(ArrayList<SGT> toReassign, Week_Timetable timetable){

        if(timetable.getAssignedLGT().size() > 0){
            return;
        }
        //https://stackoverflow.com/questions/6075894/how-to-exclude-certain-elements-from-a-list
        this.ASSIGNED_SGTS_CURRENTWEEK_GLOBAL.removeAll(toReassign);
        //update the students
        for(SGT unAssign : toReassign){
            for(Student student : this.STUDENTS_TO_BE_ASSINGED_GLOBAL.values()){
                student.getCourses().remove(unAssign);
            }
        }

        int iCounter = 0;
        ArrayList<Duplet> duplets = new ArrayList<>(timetable.getAssignedLectures());
        ArrayList<Duplet> dupletsToPass = new ArrayList<>();
        for(Duplet duplet : duplets){
            iCounter=0;
            for(SGT sgt :  toReassign){
                if(  sgt.getsLect().equals(duplet.getsLect()) ) {
                    Duplet temp = new Duplet(sgt.getiCode());
                    temp.setsDayOfWeek(duplet.getsDayOfWeek());
                    temp.setiHours(duplet.getiHours());
                    temp.setsLect(duplet.getsLect());
                    temp.setiHourScheduled(duplet.getiHourScheduled());
                    temp.setiCode(duplet.getiCode());
                    temp.setiNumberOfStudentsAttending(duplet.getiNumberOfStudentsAttending());
                    temp.setiDayScheduled(duplet.getiDayScheduled());
                    temp.setiMonthScheduled(duplet.getiMonthScheduled());
                    temp.setiYearScheduled(duplet.getiYearScheduled());
                    dupletsToPass.add(temp);
                }
            }
        }
        if(weekNumber == 0){    //no iteration of the algorithm has passed

        }
        else {
            timetable.setAssignedLectures(dupletsToPass);
        }
    }


    public void transitionOfSGTsBetweenWeeks(Week_Timetable timetable) throws SQLException {
        transit_AllAssignedSGTs( checkForAQuickAssigning(timetable),timetable);
    }


///============================================================================

    //updates the preferred days based on the students' choices, writes directly to the PreferredDay class inside the Duplet class
    //information and update is passed in the form of CoupledData class concerning each day
    private void heuristics_sgt_preferreddays(Week_Timetable week_timetable, double iScale) throws SQLException {
        ArrayList<Duplet> year1 = new ArrayList<>();
        ArrayList<CoupledData> info = new ArrayList<>();
        ArrayList<String> days = new ArrayList<>();
        days.add("Monday");
        days.add("Tuesday");
        days.add("Wednesday");
        days.add("Thursday");
        days.add("Friday");

        ArrayList<Double> heuristics =new ArrayList<>();
        ArrayList<Integer> voted =new ArrayList<>();
        heuristics.add(0.0);
        heuristics.add(0.0);
        heuristics.add(0.0);
        heuristics.add(0.0);
        heuristics.add(0.0);

        voted.add(0);
        voted.add(0);
        voted.add(0);
        voted.add(0);
        voted.add(0);


        for(Duplet lect : week_timetable.getSgt()){
            if(lect.getsLect().startsWith("4CCS")){
                year1.add(lect);
            }
        }



        String sql44= "SELECT COUNT(*) FROM main_preferences_lectures_students WHERE course_abreviation = ? AND prefday = ?";
        String sql66 = "SELECT SCALE,PRIVILEGED FROM preferences a join main_preferences_lectures_students b on b.kings_id_fk = a.kings_id  WHERE b.course_abreviation = ? AND b.prefday = ? ";
        PreparedStatement preferredDays = connection.prepareStatement(sql44);
        PreparedStatement preferr66 = connection.prepareStatement(sql66);

        for(int z =0; z < week_timetable.getSgt().size(); z++) {
            preferredDays.setString(1,week_timetable.getSgt().get(z).getsLect());
            preferr66.setString(1,week_timetable.getSgt().get(z).getsLect());
            for (int i = 0; i < days.size(); i++) {
                double heuristicsSum = 0.0;
                int plpVoted = 0;
                preferredDays.setString(2, days.get(i));
                preferr66.setString(2, days.get(i));
                ResultSet resultSet = preferredDays.executeQuery();
                ResultSet resultSet2 = preferr66.executeQuery();

                while (resultSet.next()) {
                    plpVoted = resultSet.getInt(1);
                }

                while (resultSet2.next()) {
                    heuristicsSum += resultSet2.getInt(2)*10 + resultSet2.getInt(1)*iScale;
                }
                info.add(new CoupledData(days.get(i), plpVoted, heuristicsSum));
            }

            Collections.sort(info);

            //assign the heuristics to the duplet
            week_timetable.getSgt().get(z).getPreferredDays().setHeuristics(info);
            info = new ArrayList<>();
        }
    }

    //give me a day and I will return the heuristics of the hours by preference
    private ArrayList<CoupledData> heuristics_sgt_preferredHours(Duplet event, double dScale, String sDay) throws SQLException {

        if(event == null && event.getsLect().isEmpty()){
            return null;
        }

        ArrayList<CoupledData> info = new ArrayList<>();

        String sql =
                "SELECT  c.scale, c.preftime, c.preftime_secondchoice,  c.privileged FROM( " +
                        "SELECT * FROM curiculum.main_preferences_lectures_students  a " +
                        "INNER JOIN preferences b " +
                        "ON a.kings_id_fk = b.kings_id )as c " +
                        "WHERE c.course_abreviation = ? AND prefday = ?";  //

                 // for each Hour count people voted

        for(int i= 0; i < 11; i++){
            info.add(new CoupledData(900 + i*100, 0,0.0));
            info.add(new CoupledData(900 + i*100 + 30, 0,0.0));
        }


            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,event.getsLect());
            preparedStatement.setString(2, sDay);
            ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()){
                    for(int i = 0; i < info.size(); i++) {
                        if (resultSet.getInt(2) == info.get(i).getiHour()) {
                            info.get(i).setHeuristics( info.get(i).getHeuristics() + resultSet.getInt(1) + resultSet.getInt(4)*dScale);
                            break; //add how many peaople voted for it too
                        }
                    }
                }
                resultSet.beforeFirst();
                while (resultSet.next()){
                    for(int i = 0; i < info.size(); i++) {
                        if (resultSet.getInt(3) == info.get(i).getiHour()) {
                            info.get(i).setHeuristics( info.get(i).getHeuristics() + resultSet.getInt(1)*dScale);
                            info.get(i).setNumPlp( info.get(i).getNumPlp() + 1);

                            break;
                        }
                    }
                }

                //event.getPreferredDays().setPreferredHour(info);

        Collections.sort(info);

        return info;


    }

    private ArrayList<CoupledData> v_hoursHeuristics_RemainingStudents(ArrayList<DataSetStudents> remainingStudents,  double dScale){
        ArrayList<CoupledData> info = new ArrayList<>();
        for(int i= 0; i < 11; i++){
            info.add(new CoupledData(900 + i*100, 0,0.0));
            info.add(new CoupledData(900 + i*100 + 30, 0,0.0));
        }
        for (int j= 0; j < remainingStudents.size(); j++) {
            for (int i = 0; i < info.size(); i++) {
                if (remainingStudents.get(j).getiHour() == info.get(i).getiHour()) {
                    info.get(i).setHeuristics(info.get(i).getHeuristics() + remainingStudents.get(j).getScale() + remainingStudents.get(j).getPrivileged() * dScale);
                    info.get(i).setNumPlp(info.get(i).getNumPlp() + 1);
                    break;
                }
            }
        }


            for (int j= 0; j < remainingStudents.size(); j++){
                for(int i = 0; i < info.size(); i++) {

                    if (remainingStudents.get(j).getiHour2() == info.get(i).getiHour()) {
                        info.get(i).setHeuristics( info.get(i).getHeuristics() + remainingStudents.get(j).getScale() + remainingStudents.get(j).getPrivileged() * dScale);
                        info.get(i).setNumPlp( info.get(i).getNumPlp() + 1);
                        break;
                    }
                }
            }

        Collections.sort(info);
        return info;
    }

    private ArrayList<DataSetStudents> findInDataByHour(ArrayList<DataSetStudents> data, int iHour){
        ArrayList<DataSetStudents> reData = new ArrayList<>();
        for(int i = 0; i < data.size(); i++){
            if(data.get(i).getiHour() == iHour ){
                reData.add(data.get(i));
                break;
            }

            if(data.get(i).getiHour2() == iHour){
                reData.add(data.get(i));
            }
        }
        return reData;
    }

    private ArrayList<CoupledData> heuristics_sgt_preferredHours(SGT event, double dScale, String sDay) throws SQLException {

        if(event == null && event.getsLect().isEmpty()){
            return null;
        }
        ArrayList<CoupledData> info = new ArrayList<>();

        String sql =
                "SELECT  c.scale, c.preftime, c.preftime_secondchoice, c.privileged FROM( " +
                        "SELECT * FROM curiculum.main_preferences_lectures_students  a " +
                        "INNER JOIN preferences b " +
                        "ON a.kings_id_fk = b.kings_id )as c " +
                        "WHERE c.course_abreviation = ? AND prefday = ?";  //

        // for each Hour count people voted

        for(int i= 0; i < 11; i++){
            info.add(new CoupledData(900 + i*100, 0,0.0));
            info.add(new CoupledData(900 + i*100 + 30, 0,0.0));
        }


        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1,event.getsLect());
        preparedStatement.setString(2, sDay);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()){
            for(int i = 0; i < info.size(); i++) {
                if (resultSet.getInt(2) == info.get(i).getiHour()) {
                    info.get(i).setHeuristics( info.get(i).getHeuristics() + resultSet.getInt(1) + resultSet.getInt(4)*dScale);
                    info.get(i).setNumPlp( info.get(i).getNumPlp() + 1);
                    break;
                }
            }
        }
        resultSet.beforeFirst();
        while (resultSet.next()){
            for(int i = 0; i < info.size(); i++) {
                if (resultSet.getInt(3) == info.get(i).getiHour()) {
                    info.get(i).setHeuristics( info.get(i).getHeuristics() + resultSet.getInt(1)*dScale);
                    break;
                }
            }
        }

        //event.getPreferredDays().setPreferredHour(info);

        Collections.sort(info);
        return info;


    }

    private ArrayList<CoupledData> heuristics_sgt_preferredHours_fromPreferences(SGT event, double dScale, ArrayList<DataSetStudents> students) throws SQLException {

        if(event == null && event.getsLect().isEmpty()){
            return null;
        }
        ArrayList<CoupledData> info = new ArrayList<>();

        for(int i= 0; i < 11; i++){
            info.add(new CoupledData(900 + i*100, 0,0.0));
            info.add(new CoupledData(900 + i*100 + 30, 0,0.0));
        }



        for(DataSetStudents studentsChosen : students) {


            for (int i = 0; i < info.size(); i++) {
                if (studentsChosen.getiHour() == info.get(i).getiHour()) {
                    info.get(i).setHeuristics(info.get(i).getHeuristics() + studentsChosen.getScale()*dScale + studentsChosen.getPrivileged()*10*dScale);
                    info.get(i).setNumPlp(info.get(i).getNumPlp() + 1);
                    break;
                }
            }

        }

        for(DataSetStudents studentsChosen : students) {


            for (int i = 0; i < info.size(); i++) {
                if (studentsChosen.getiHour2() == info.get(i).getiHour()) {
                    info.get(i).setHeuristics(info.get(i).getHeuristics() + studentsChosen.getScale()*dScale + studentsChosen.getPrivileged()*10*dScale);
                    info.get(i).setNumPlp(info.get(i).getNumPlp() + 1);
                    break;
                }
            }

        }


        //event.getPreferredDays().setPreferredHour(info);

        Collections.sort(info);
        return info;


    }



    //updates the days heuristics inside PreferredDays, removes the days of the week prior to the lecture. Those are unavailable for heuristics
    private void removeUnusableDays(Week_Timetable timetable){

        for(Duplet event : timetable.getSgt()){
            String dayOfLecture = event.getsDayOfWeek();
            ArrayList<String> daysToRemove = new ArrayList<>();
            if(dayOfLecture.equals("Tuesday")){
                daysToRemove.add("Monday");
            }
            if(dayOfLecture.equals("Wednesday")){
                daysToRemove.add("Monday");
                daysToRemove.add("Tuesday");
            }
            if(dayOfLecture.equals("Thursday")){
                daysToRemove.add("Monday");
                daysToRemove.add("Tuesday");
                daysToRemove.add("Wednesday");
            }
            if(dayOfLecture.equals("Friday")){
                daysToRemove.add("Monday");
                daysToRemove.add("Tuesday");
                daysToRemove.add("Wednesday");
                daysToRemove.add("Thursday");
            }
            for(int i = 0; i < daysToRemove.size(); i++){
                for(int k = 0; k < event.getPreferredDays().getHeuristics().size(); k++){
                    if( event.getPreferredDays().getHeuristics().get(k).getsDay().equals(daysToRemove.get(i))){
                        event.getPreferredDays().getHeuristics().remove(k);
                    }
                }
            }
        }

    }


    private void removeHeuristicDaysWithNull(Week_Timetable timetable){
        //cleans the heuristics, removes any day which has 0 people voted for it

        for(Duplet event : timetable.getSgt()){
            for(int i =0; i<event.getPreferredDays().getHeuristics().size(); i++){
                if(event.getPreferredDays().getHeuristics().get(i).getHeuristics() == 0.0){
                    event.getPreferredDays().getHeuristics().remove(i);
                }
            }
        }
    }

    //if called and used, reorders the days of preference, moves the day of the lecture as last heuristics option
    private void reorderHeuristics_days(Week_Timetable timetable){
        //reorder heuristics to allow one day to be given between the lecture and the sgt
        //for each sgt reorder its heuristics so that it doesn't appear on the same day
        // as the lecture, if that is possible
        for(Duplet event : timetable.getSgt()) {
            String sDayOfLecture = event.getsDayOfWeek();
            for (int i = 0; i < event.getPreferredDays().getHeuristics().size(); i++) {
                if (event.getPreferredDays().getHeuristics().get(i).getsDay().equals(sDayOfLecture)) {
                    //put at the back
                    CoupledData attachAtTheBack = event.getPreferredDays().getHeuristics().get(i);
                    event.getPreferredDays().getHeuristics().remove(i);
                    event.getPreferredDays().getHeuristics().add(attachAtTheBack);
                    break;
                }
            }
        }

    }

    private void heuristics_sgt_hours(Week_Timetable week_timetable, ArrayList<DataSetStudents> data, double dFactor){

        ArrayList<CoupledData> preferredHour = new ArrayList<>();                                // preferred hour of day


        for(Duplet eventSGT : week_timetable.getSgt()) {

            for(int i= 0; i < 11; i++){
                preferredHour.add(new CoupledData((900 + i*100),0,0.0));
                preferredHour.add(new CoupledData((900 + i*100) + 30,0,0.0));
            }


            for (int z = 0; z < data.size(); z++) {
               if(eventSGT.getsLect().equals(data.get(z).getAbbrev())) {

                   for (int i = 0; i < preferredHour.size(); i++) {
                       int fortesting = preferredHour.get(i).getiHour();
                       int testing = data.get(z).getiHour1();
                       int testing2 = data.get(z).getiHour2();
                       if (data.get(z).getiHour1() == preferredHour.get(i).getiHour()) {
                           preferredHour.get(i).setNumPlp(preferredHour.get(i).getNumPlp() + 1);
                           preferredHour.get(i).setHeuristics(preferredHour.get(i).getHeuristics() + data.get(z).getScale() * 10 + data.get(z).getPrivileged() * dFactor);
                           break;
                       }
                   }
               }
            }

            for (int z = 0; z < data.size(); z++) {
                if (eventSGT.getsLect().equals(data.get(z).getAbbrev())) {

                    for (int i = 0; i < preferredHour.size(); i++) {
                        if (data.get(z).getiHour2() == preferredHour.get(i).getiHour() && eventSGT.getsLect().equals(data.get(z).getAbbrev())) {
                            preferredHour.get(i).setNumPlp(preferredHour.get(i).getNumPlp() + 1);
                            preferredHour.get(i).setHeuristics(preferredHour.get(i).getHeuristics() + data.get(z).getScale() * 10 + data.get(z).getPrivileged() * dFactor);
                            break;
                        }
                    }
                }
            }
            eventSGT.getPreferredDays().setPreferredHour(preferredHour);
            preferredHour = new ArrayList<>();
        }

    }


    //new function after submitting my code

    private int overlaps_time(SGT first, SGT second){
        //time overlapping function, if two functions have the same day and similar hours, they might overlap and appear in one's timetable at the same timeslot, making it impossible for
        // a student to participate at both at the same time
        ArrayList<Timeperiod> timeperiods_first = new ArrayList<>();
        ArrayList<Timeperiod> timeperiods_second = new ArrayList<>();
        if((first.getiHourScheduled() - 30) %100 == 0){
            for(int i = 0; i < first.getiHours() ; i++){
                timeperiods_first.add(new Timeperiod(first.getiHourScheduled() + (i*100), first.getsDayOfWeek()));
                timeperiods_first.add(new Timeperiod((first.getiHourScheduled() + (i*100) + 70), first.getsDayOfWeek()));
            }
        }
        else{
            for(int i = 0; i < first.getiHours()  ; i++){
                timeperiods_first.add(new Timeperiod(first.getiHourScheduled() + (i*100), first.getsDayOfWeek()));
                timeperiods_first.add(new Timeperiod((first.getiHourScheduled() + (i*100)  + 30), first.getsDayOfWeek()));
            }
        }

        if((second.getiHourScheduled() - 30) %100 == 0){
            for(int i = 0; i < second.getiHours() ; i++){
                timeperiods_first.add(new Timeperiod(second.getiHourScheduled() + (i*100), second.getsDayOfWeek()));
                timeperiods_first.add(new Timeperiod((second.getiHourScheduled() + (i*100) + 70), second.getsDayOfWeek()));
            }
        }
        else{
            for(int i = 0; i < second.getiHours()  ; i++){
                timeperiods_second.add(new Timeperiod(second.getiHourScheduled() + (i*100), second.getsDayOfWeek()));
                timeperiods_second.add(new Timeperiod((second.getiHourScheduled() + (i*100)  + 30), second.getsDayOfWeek()));
            }
        }

        for(Timeperiod time : timeperiods_first){
            for(Timeperiod time2 : timeperiods_second){
                if(time.getiTime() == time2.getiTime()){
                    return 1;
                }
            }
        }
        return 0;
    }


    private int overlap_lecture(SGT swapped, int iHour, double dDuration, String sDay){
        //overlap with a lecture if swapped ?
        String startsWith = "";
        ArrayList<Duplet> lectures = new ArrayList<>();
        ArrayList<Timeperiod> notAvailable = new ArrayList<>();



        ArrayList<Timeperiod> timeperiods_swapped = new ArrayList<>();

        if((iHour - 30) %100 == 0){
            for(int i = 0; i < dDuration ; i++){
                timeperiods_swapped.add(new Timeperiod(iHour + (i*100), sDay));
                timeperiods_swapped.add(new Timeperiod((iHour + (i*100) + 70), sDay));
            }
        }
        else{
            for(int i = 0; i < dDuration  ; i++){
                timeperiods_swapped.add(new Timeperiod(iHour, sDay));
                timeperiods_swapped.add(new Timeperiod((iHour + (i*100)  + 30), sDay));
            }
        }



        for(Timeslot check : swapped.getNotAvailableSlots()){
            for(Timeperiod timeperiod : timeperiods_swapped) {
                if(check.getTimePeriod().get(0).getsDay().equals(timeperiod.getsDay()))
                    for (int i = 0; i < check.getTimePeriod().size(); i++) {
                        if (check.getTimePeriod().get(i).getiTime() == timeperiod.getiTime()) {
                            return 1;
                        }
                    }
                else break;
            }
        }
        return 0;
    }


    //end of new function after submitting my code


    //find info on the students, used for exhaustive search, base of HC algorithm
    private ArrayList<DataSetStudents> findInData(ArrayList<DataSetStudents> data, String course){
        ArrayList<DataSetStudents> reData = new ArrayList<>();
        for(int i = 0; i < data.size(); i++){
            if(data.get(i).getAbbrev().equals(course)){
                reData.add(data.get(i));
            }
        }
        return reData;
    }

    private ArrayList<DataSetStudents> findInDataByDay(ArrayList<DataSetStudents> data, String course, String sDay){
        ArrayList<DataSetStudents> reData = new ArrayList<>();
        for(int i = 0; i < data.size(); i++){
            if(data.get(i).getAbbrev().equals(course) && sDay.equals(data.get(i).getPrefDay())){
                reData.add(data.get(i));
            }
        }
        return reData;
    }


    //focus on assigning sgts year by year



    //////////////////////!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

    private long availableForFiltering(int n,int r){         //USED TO DETERMINE THE NUMBER OF COMBINATIONS AVAILABLE, IF TOO LARGE ==> SKIP

        BigInteger fact = BigInteger.ONE;
        BigInteger fact2 = BigInteger.ONE;
        BigInteger fact3 = BigInteger.ONE;
        long twentyMillion =21000000L;

        if(n > 40 || r > 40) return twentyMillion;

        for(int i = 1; i <= n; ++i) {
            // factorial = factorial * i;
            BigInteger big = BigInteger.valueOf(i);
            fact  = fact.multiply(BigInteger.valueOf(i));
        }

        for(int i = 1; i <= r; ++i) {
            // factorial = factorial * i;
            fact2= fact2.multiply(BigInteger.valueOf(i));
        }

        for(int i = 1; i <= n - r; ++i) {
            // factorial = factorial * i;
            fact3 = fact3.multiply(BigInteger.valueOf(i));

        }

        fact = fact.divide(fact2);
        fact = fact.divide(fact3);
        if(fact.compareTo(BigInteger.valueOf(twentyMillion))==1){
            return twentyMillion;

        }
        return fact.longValue();
    }

    //based on  :  https://www.baeldung.com/java-combinations-algorithm


//
//    private void helper_myHelper(ArrayList<DataSetStudents[]> combinations, ArrayList<DataSetStudents> toSearch, DataSetStudents [] data, int start, int end, int index, ArrayList<DataSetStudents> solution){
//
//        if(index == data.length){
//            DataSetStudents[] combination = data.clone();
//        }
//        else if( start <= end){
//            for(int i = 0; i < toSearch.size(); i++){
//                if(start == toSearch.get(i).getNumberforCombinations()){
//                    data[index] = toSearch.get(i);
//                    break;
//                }
//            }
//            helper_myHelper(combinations,toSearch, data, start + 1, end, index + 1, solution);
//            helper_myHelper(combinations,toSearch, data, start + 1, end, index, solution);
//
//        }
//    }
//
//    private ArrayList<DataSetStudents[]> generate_myGenerate(int n, int r, ArrayList<DataSetStudents> toSearch){
//        ArrayList<DataSetStudents[]> combinations = new ArrayList<>();
//        ArrayList<DataSetStudents> solution = new ArrayList<>();
//        helper_myHelper(combinations,toSearch, new DataSetStudents[r], 0, n-1, 0,solution);
//        return combinations;
//    }




    private void helper_myHelper(ArrayList<DataSetStudents[]> combinations, ArrayList<DataSetStudents> toSearch, DataSetStudents [] data, int start, int end, int index){
        if(index == data.length){
            DataSetStudents[] combination = data.clone();
            for(DataSetStudents dataSetStudents : combination){
                System.out.print(dataSetStudents.getKings_id() + " ");
            }
            ArrayList<DataSetStudents> toarray = new ArrayList<>();
            Collections.addAll(toarray, combination);
            numofCodesStudents(toarray);
            System.out.println();
        }
        else if( start <= end){
            for(int i = 0; i < toSearch.size(); i++){
                if(start == toSearch.get(i).getNumberforCombinations()){
                    data[index] = toSearch.get(i);
                    break;
                }
            }
            helper_myHelper(combinations,toSearch, data, start + 1, end, index + 1);
            helper_myHelper(combinations,toSearch, data, start + 1, end, index);

        }
    }

    private ArrayList<DataSetStudents[]> generate_myGenerate(int n, int r, ArrayList<DataSetStudents> toSearch){
        ArrayList<DataSetStudents[]> combinations = new ArrayList<>();
        helper_myHelper(combinations,toSearch, new DataSetStudents[r], 0, n-1, 0);
        return combinations;
    }



    //THIS FUNCTION WOULD BE NEEDED TO SET DEPENDENCIES ON THE TIMES THAT ARE AVAILABLE. THAT IS ONE OF THE WAYS TO GO ABOUT THIS, HOWEVER I HAVE STARTED WORKING ON JUST FETCHING Students as classes and they would already have their codes for lectures added
    private ArrayList<Integer> lectureCodesOfStudents(ArrayList<DataSetStudents> dataset, ArrayList<DataSetStudents> studentsOfspecificYear){
        ArrayList<Integer> toReturn = new ArrayList<>();
        Set<Integer> finalSet =new HashSet<>();
        for(int i =0; i < dataset.size(); i++){
            int iKingsID = dataset.get(i).getKings_id();
            if(studentsOfspecificYear.isEmpty()) {
                for (int j = 0; j < this.data.size(); j++) {   //global array data must be initialized for this loop to work
                    if (this.data.get(j).getKings_id() == iKingsID) {
                        finalSet.add(this.data.get(j).getLecture_code());
                    }
                }
            }
            else{
                for (int j = 0; j < studentsOfspecificYear.size(); j++) {
                    if (studentsOfspecificYear.get(j).getKings_id() == iKingsID) {
                        finalSet.add(studentsOfspecificYear.get(j).getLecture_code());
                    }
                }
            }
        }

        toReturn = new ArrayList<>(finalSet);
        return toReturn;
    }

    private ArrayList<Duplet> lecturesOfStudentsFromCodes(ArrayList<Integer> dataset, Week_Timetable timetable){
        ArrayList<Integer> toReturn = new ArrayList<>();
        Set<Integer> finalSet =new HashSet<>();
        ArrayList<Duplet> lectures = new ArrayList<>();
        for(int i =0; i < dataset.size(); i++){
            int iKingsID = dataset.get(i);
            Student student = this.STUDENTS_TO_BE_ASSINGED_GLOBAL.get(iKingsID);
            for(Integer code : student.getCoursesCodes()){
                finalSet.add(code);
            }
        }

        for(Integer code: finalSet){
            for(Duplet event : timetable.getAssignedLectures()){
                if(event.getiCode() == code){
                    lectures.add(event);
                    break;
                }
            }
        }

        return lectures;
    }
    private ArrayList<Duplet> lecturesOfStudentsFromOnlyCodes(ArrayList<Integer> dataset, Week_Timetable timetable){
        ArrayList<Integer> toReturn = new ArrayList<>();
        Set<Integer> finalSet =new HashSet<>();
        ArrayList<Duplet> lectures = new ArrayList<>();

        for(Integer code: dataset){
            for(Duplet event : timetable.getAssignedLectures()){
                if(event.getiCode() == code){
                    lectures.add(event);
                    break;
                }
            }
        }

        return lectures;
    }
    private ArrayList<Duplet> LGTsOfStudentsFromOnlyCodes(ArrayList<Integer> dataset, Week_Timetable timetable){
        ArrayList<Integer> toReturn = new ArrayList<>();
        Set<Integer> finalSet =new HashSet<>();
        ArrayList<Duplet> lectures = new ArrayList<>();

        for(Integer code: dataset){
            for(Duplet event : timetable.getAssignedLGT()){
                if(event.getiCode() == code){
                    lectures.add(event);
                    break;
                }
            }
        }

        return lectures;
    }

    private int numofCodesStudents(ArrayList<DataSetStudents> combination){
        HashSet<Integer> lectureCodes = new HashSet<>();
        for(DataSetStudents student : combination){
            for(int lectureCode : this.STUDENTS_TO_BE_ASSINGED_GLOBAL.get(student.getKings_id()).getCoursesCodes()){
                lectureCodes.add(lectureCode);
            }
        }
        return lectureCodes.size();
    }


    private ArrayList<DataSetStudents> randomlyRemoveFromArray(ArrayList<DataSetStudents> students, int hallSize){
        ArrayList<DataSetStudents> students2 =new ArrayList<>(students);
        for(int i = students.size(); i > hallSize; i--){
            int random = (int) Math.random() * (students2.size() - 1);
            students2.remove(random);
        }
        return students2;
    }


    //pick random students, combines people with different heuristics, used when there aren't many students left, or if picked students are too many and not available for filtering
    private ArrayList<DataSetStudents> finalStudents(ArrayList<DataSetStudents> considered, int iTime, int size){

        Set<DataSetStudents> students =new HashSet<>();
        ArrayList<DataSetStudents> students2 =new ArrayList<>();

        for(int i=0; i < considered.size(); i++){
            if((considered.get(i).getiHour() == iTime  || considered.get(i).getiHour2() == iTime)&& size != students.size()){
                students.add(considered.get(i));
            }
        }

        while(students.size() != size){
            int random = (int) Math.random() * (considered.size() - 1);
            students.add(considered.get(random));
        }

        students2 = new ArrayList<>(students);
        return students2;
    }

    private ArrayList<DataSetStudents> randomStudents(ArrayList<DataSetStudents> considered, int size){

        if(considered.size() < size) size= considered.size();

        Set<DataSetStudents> students =new HashSet<>();
        ArrayList<DataSetStudents> students2 =new ArrayList<>(considered);
        int iCounter = 0;
        while(students.size() != size){

            if( iCounter == size*2){break;}
            int random = (int) Math.random() * (students2.size() - 1);
            if(!students2.isEmpty()){
                students.add(students2.get(random));
                students2.remove(random);
            }
            iCounter++;
        }

        students2 = new ArrayList<>(students);
        return students2;
    }



    //ALL UNAVAILABLE TIMEPERIODS WHEN LECTURES OCCUR OR ANOTHER SGT FOR THE GROUP OF STUDENTS.This includes all the lectures and all the sgt for each individual student
    ArrayList<Timeslot> notavailableTime_specificDay(ArrayList<DataSetStudents> students_data,  Week_Timetable timetable, SGT mySGT) throws CloneNotSupportedException {
        Set<Timeslot> time = new HashSet<>();               //timeslots for dependencies == not available for the students times
        Set<Integer> codesOfLectures = new HashSet<>();

        //for each student add the dependencies
        Set<Student> students = new HashSet<>();
        HashSet<SGT>dependsOn = new HashSet<>();
        for(DataSetStudents st_data: students_data){
            students.add(this.STUDENTS_TO_BE_ASSINGED_GLOBAL.get(st_data.getKings_id()));
            //students.add(this.ASSIGNED_STUDENTS_GLOBAL.get(st_data.getKings_id()));
        };

        for(Student student : students){
//        while (iterator.hasNext()){
//            Student student = (Student) iterator.next();
            if(!student.getCourses().isEmpty())
                for(SGT event: student.getCourses()){
//                 //   time.add(new Timeslot(event.getiHourScheduled(), event.getiHours()));
                    //    public Timeslot(double duration, String sDay, int iHour, String sLectureHall, String sActivity, int iDayOfWeek, int iMonth, int iYear){
                    mySGT.addToDependentOn(event);
                    //the following line is very buggy, couldn't propely use the hashCode function inside the Timeperiod, tried multiple variations
                    time.add(new Timeslot(1.0, event.getsDayOfWeek() , event.getiHourScheduled(), "", event.getsLect(),0,0,0));
                }
            if(!student.getCoursesCodes().isEmpty())
                for(Integer code : student.getCoursesCodes()){
                        codesOfLectures.add(code);
                        for (Duplet dayOfWeek : timetable.getAssignedLectures()) {
                                if(dayOfWeek.getiCode() == code){
                                    time.add(new Timeslot(dayOfWeek.getiHours(), dayOfWeek.getsDayOfWeek(), dayOfWeek.getiHourScheduled(), dayOfWeek.getsLectureHall(), dayOfWeek.getsLect(), 0, 0 ,0));
                                    break;
                                }        //add a break here
                            }
                }

        }

       // for(){}


        ArrayList<Timeslot> finalSlots = new ArrayList<>(time);
        return finalSlots;
    }

    //variation of the method above, doesn't write direcly to the SGT class, but instead returns arraylist of the not available slots, pass as parameters sgt.getCodesOfStudents...()
    ArrayList<Timeslot> notavailableTime_specificDay_fromSGT(SGT event_passed,  Week_Timetable timetable) throws CloneNotSupportedException {
        ArrayList<Timeslot> time_array = new ArrayList<>();               //timeslots for dependencies == not available for the students times
        //for each student add the dependencies

        for(SGT sgtsDependent : event_passed.getDependentOn()){
            time_array.add(new Timeslot(1.0,sgtsDependent.getsDayOfWeek(), sgtsDependent.getiHourScheduled(), sgtsDependent.getsLectureHall(), sgtsDependent.getsLect(), sgtsDependent.getiDayScheduled(),sgtsDependent.getiMonthScheduled(),sgtsDependent.getiYearScheduled()));
        }

        ArrayList<Duplet> duplets = lecturesOfStudentsFromCodes(event_passed.getCodesOfStudentsAssigned(),timetable);
        for(Duplet duplet :duplets){
            time_array.add(new Timeslot(duplet.getiHours(), duplet.getsDayOfWeek(), duplet.getiHourScheduled(), duplet.getsLectureHall(), duplet.getsLect(),duplet.getiDayScheduled(), duplet.getiMonthScheduled(), duplet.getiYearScheduled()));
        }

        return time_array;
    }




    ArrayList<Timeslot> notavailableTime_specificDay_onlyLectures(ArrayList<DataSetStudents> students_data,  Week_Timetable timetable){
        Set<Timeslot> time = new HashSet<>();               //timeslots for dependencies == not available for the students times
        //for each student add the dependencies
        Set<Student> students = new HashSet<>();
        for(DataSetStudents st_data: students_data){
            students.add(this.STUDENTS_TO_BE_ASSINGED_GLOBAL.get(st_data.getKings_id()));
            students.add(this.ASSIGNED_STUDENTS_GLOBAL.get(st_data.getKings_id()));
        }

        Iterator iterator = students.iterator();
        iterator.next();

        while (iterator.hasNext()){
            Student student = (Student) iterator.next();
            for(Integer code : student.getCoursesCodes()){
                for(Duplet lecture : timetable.getAssignedLectures()) {
                    if(lecture.getiCode() == code)
                        for (Day dayOfWeek : timetable.getWeekTimet()) {
                            for (Timeslot timeslot : dayOfWeek.getoDslot2()) {
                                if (timeslot.getsActivity().equals(lecture.getsLect())) {
                                    time.add(timeslot);
                                    break;
                                }
                            }
                        }
                }
            }
        }

        ArrayList<Timeslot> finalSlots = new ArrayList<>(time);
        return finalSlots;
    }

    ///////

    private void updateAvailabilitySlotsSGT(SGT event, String sDay,String sName, int iTime){

            int iAvailable = 0;
            int iSecondTiperiodTime = 0;
            for(Timeslot timeslot: event.getAvailableSlots()){
                if(timeslot.getTimePeriod().get(0).getsDay().equals(sDay) && sName.equals(timeslot.getsHall())) {
                    if (timeslot.getTimePeriod().get(0).getiTime() == iTime) {
//                        event.getAvailableSlots().remove(timeslot);
                        timeslot.setUnavailable();
                        iSecondTiperiodTime = timeslot.getTimePeriod().get(1).getiTime();
                        break;
                    }
                }
            }
        //some of the times overlap, so this will place a zero where the overlap is
        for(Timeslot timeslot: event.getAvailableSlots()) {
            if(timeslot.getTimePeriod().get(0).getsDay().equals(sDay) && sName.equals(timeslot.getsHall())){
                if (timeslot.getTimePeriod().get(0).getiTime() == iSecondTiperiodTime) {
                    timeslot.getTimePeriod().get(0).setiAvailable(0);
                    break;
                }
            }
        }

    }

    private void updateAvailailitySlotsSGT_AllEvents(SGT event){
        if(!event.getDependentOn().isEmpty())
        for(SGT assigned : this.ASSIGNED_SGTS_CURRENTWEEK_GLOBAL) {
            for (SGT matching : event.getDependentOn()) {
                if(assigned.equals(matching)) {//ALSO ADD TO THE UNAVAILABLE SLOTS
                    updateAvailabilitySlotsSGT(assigned, event.getDayAssigned(), event.getsLectureHall(), event.getiHourScheduled());//add a break after this
                }
            }
        }
    }

    private int i_prefferedTime_byDay_check(SGT event, String sDay, int iTime, String sName){

        int iAvailable = 0;
        for(Timeslot timeslot: event.getAvailableSlots()){
            if(timeslot.getTimePeriod().get(0).getsDay().equals(sDay) && sName.equals(timeslot.getsHall())) {
                if (timeslot.getTimePeriod().get(0).getiTime() == iTime
                        && timeslot.getTimePeriod().get(0).getiAvailable() == 1) {
                    if (timeslot.getTimePeriod().get(1).getiAvailable() == 1) {
                        iAvailable = iTime;
                        return iAvailable;
                    }
                }
            }
        }
        return iAvailable;
    }

    private ArrayList<Student> notYetAssignedStudents(){
        ArrayList<Student> notYetAssigned = new ArrayList<>();
        Iterator iterator = STUDENTS_TO_BE_ASSINGED_GLOBAL.values().iterator();
        while(iterator.hasNext()){
            Student student = (Student) iterator.next();
            if((student.numberOfAssignedCourses() != 4 && student.getiKingsID() > 1900000) || (student.numberOfAssignedCourses() != 3 && student.getiKingsID() < 1900000)){
                notYetAssigned.add(student);
            }

        }
        return notYetAssigned;
    }

    //PUBLIC for now
//    public HeuristicEvaluation heuristicEvaluation_function(SGT event, double scaleForDay, double scaleForHour, double scaleCloseToHour){
//
//        //https://stackoverflow.com/questions/2808535/round-a-double-to-2-decimal-places == Rounding doubles
//        //https://www.geeksforgeeks.org/method-within-method-in-java/#:~:text=Java%20does%20not%20support%20%E2%80%9Cdirectly,method%20so%20this%20does%20compile. == method inside a method
//
//
//
//        myInterface r = new myInterface() {
//            public double round(double value, int places)
//            {
//                if (places < 0) throw new IllegalArgumentException();
//
//                BigDecimal bd = BigDecimal.valueOf(value);
//                bd = bd.setScale(places, RoundingMode.HALF_UP);
//                return bd.doubleValue();            };
//        };
//
//
//
//        double dEvaluation = 0.0;
//        double dEvaluationRaw = 0.0;
//        double dEvaluationScaled = 0.0;
//        HeuristicEvaluation evaluation = new HeuristicEvaluation();
//        evaluation.setSgt(event);
//
//        for(int kings_id : event.getCodesOfStudentsAssigned()){
//            int iCounter = 0;
//            Student student = STUDENTS_TO_BE_ASSINGED_GLOBAL.get(kings_id);
//
//             dEvaluation = 0.0;
//             dEvaluationRaw = 0.0;
//             dEvaluationScaled = 0.0;
//
//            for(DataSetStudents preferences : student.getPreferences()) {
//
//                if (preferences.getAbbrev().equals(event.getsLect())) {
//                    //has entered evaluation
//                    if (event.getsDayOfWeek().equals(preferences.getPrefDay())) {
//                        dEvaluation += r.round(preferences.getScale(), 5);                                      //calculating only the scale from the preference
//                        dEvaluationScaled += r.round(scaleForDay * (preferences.getScale()), 5);             // calculating the scale predefined scale times the scale from the preference of the student
//                        dEvaluationRaw += scaleForDay;                              //only the predefined scale
//
//
//                    }
//                        if (event.getiHourScheduled() == preferences.getiHour1() || event.getiHourScheduled() == preferences.getiHour2()) {
//
//                            dEvaluation += r.round(preferences.getScale(), 5);
//                            dEvaluationScaled +=r.round( scaleForHour * (preferences.getScale()), 4);
//                            dEvaluationRaw += scaleForHour;
//
//                        }
//
//                    else {
//
//                        if (event.getiHourScheduled() % 100 == 0) {
//                            if (event.getiHourScheduled() + 30 == preferences.getiHour2() || event.getiHourScheduled() + 30 == preferences.getiHour2()) {
//                                dEvaluation += r.round(preferences.getScale(),5);                                      //calculating only the scale from the preference
//                                dEvaluationScaled += r.round(scaleCloseToHour * (preferences.getScale()), 4);             // calculating the scale predefined scale times the scale from the preference of the student
//                                dEvaluationRaw += scaleCloseToHour;
//
//                            } else if (event.getiHourScheduled() + 100 == preferences.getiHour2() || event.getiHourScheduled() + 100 == preferences.getiHour2()) {
//                                dEvaluation += r.round(preferences.getScale(),5);                                      //calculating only the scale from the preference
//                                dEvaluationScaled += r.round(scaleCloseToHour * (preferences.getScale()),4);             // calculating the scale predefined scale times the scale from the preference of the student
//                                dEvaluationRaw += scaleCloseToHour;
//
//                            } else if (event.getiHourScheduled() - 100 == preferences.getiHour2() || event.getiHourScheduled() - 100 == preferences.getiHour2()) {
//                                dEvaluation += r.round(preferences.getScale(),5);                                      //calculating only the scale from the preference
//                                dEvaluationScaled +=r.round( scaleCloseToHour * (preferences.getScale()),4 );             // calculating the scale predefined scale times the scale from the preference of the student
//                                dEvaluationRaw += scaleCloseToHour;
//
//                            } else if (event.getiHourScheduled() -70 == preferences.getiHour2() || event.getiHourScheduled() -70 == preferences.getiHour2()) {
//                                dEvaluation += r.round(preferences.getScale(),5);                                      //calculating only the scale from the preference
//                                dEvaluationScaled +=r.round( scaleCloseToHour * (preferences.getScale()), 4);             // calculating the scale predefined scale times the scale from the preference of the student
//                                dEvaluationRaw += scaleCloseToHour;
//
//                            }
//                            else{}
//
//
//
//                        } else {
//                            if (event.getiHourScheduled() + 70 == preferences.getiHour2() || event.getiHourScheduled() + 70 == preferences.getiHour2()) {
//                                dEvaluation += r.round(preferences.getScale(),5);                                      //calculating only the scale from the preference
//                                dEvaluationScaled += scaleCloseToHour * (preferences.getScale());             // calculating the scale predefined scale times the scale from the preference of the student
//                                dEvaluationRaw += scaleCloseToHour;
//
//                            } else if (event.getiHourScheduled() + 100 == preferences.getiHour2() || event.getiHourScheduled() + 100 == preferences.getiHour2()) {
//                                dEvaluation += r.round(preferences.getScale(),5);                                      //calculating only the scale from the preference
//                                dEvaluationScaled += scaleCloseToHour * (preferences.getScale());             // calculating the scale predefined scale times the scale from the preference of the student
//                                dEvaluationRaw += scaleCloseToHour;
//
//                            } else if (event.getiHourScheduled() - 100 == preferences.getiHour2() || event.getiHourScheduled() - 100 == preferences.getiHour2()) {
//                                dEvaluation += r.round(preferences.getScale(),5);                                      //calculating only the scale from the preference
//                                dEvaluationScaled += scaleCloseToHour * (preferences.getScale());             // calculating the scale predefined scale times the scale from the preference of the student
//                                dEvaluationRaw += scaleCloseToHour;
//
//                            } else if (event.getiHourScheduled() -30 == preferences.getiHour2() || event.getiHourScheduled() - 30 == preferences.getiHour2()) {
//                                dEvaluation += r.round(preferences.getScale(),5);                                      //calculating only the scale from the preference
//                                dEvaluationScaled += scaleCloseToHour * (preferences.getScale());             // calculating the scale predefined scale times the scale from the preference of the student
//                                dEvaluationRaw += scaleCloseToHour;
//
//                            } else {
//                            }
//                        }
//
//                    }
//
//                    evaluation.addToRawHeuristics(dEvaluationRaw);
//                    evaluation.addToHeuristics(dEvaluation);
//                    evaluation.addToScaledHeuristics(dEvaluationScaled);
//                    break;
//                }
//
//            }
//        }
//        return evaluation;
//    }
    public HeuristicEvaluation heuristicEvaluation_function(SGT event, double scaleForDay, double scaleForHour, double scaleCloseToHour){

            myInterface r = new myInterface() {
                public double round(double value, int places)
                {
                    if (places < 0) throw new IllegalArgumentException();

                    BigDecimal bd = BigDecimal.valueOf(value);
                    bd = bd.setScale(places, RoundingMode.HALF_UP);
                    return bd.doubleValue();            };
            };

            double dEvaluation = 0.0;
            double dEvaluationRaw = 0.0;
            double dEvaluationScaled = 0.0;
            HeuristicEvaluation evaluation = new HeuristicEvaluation();


            for(int kings_id : event.getCodesOfStudentsAssigned()){
                int iCounter = 0;
                Student student = STUDENTS_TO_BE_ASSINGED_GLOBAL.get(kings_id);
                dEvaluation = 0.0;
                dEvaluationRaw = 0.0;
                dEvaluationScaled = 0.0;

                for(DataSetStudents preferences : student.getPreferences()) {

                    if (preferences.getAbbrev().equals(event.getsLect())) {
                        //has entered evaluation
                        if (event.getsDayOfWeek().equals(preferences.getPrefDay())) {
                            dEvaluation += r.round(preferences.getScale(), 5);                                      //calculating only the scale from the preference
                            dEvaluationScaled += r.round(scaleForDay * (preferences.getScale()), 5);             // calculating the scale predefined scale times the scale from the preference of the student
                            dEvaluationRaw += scaleForDay;                              //only the predefined scale


                        }
                        if (event.getiHourScheduled() == preferences.getiHour1() || event.getiHourScheduled() == preferences.getiHour2()) {

                            dEvaluation += r.round(preferences.getScale(), 5);
                            dEvaluationScaled += scaleForHour * (preferences.getScale());
                            dEvaluationRaw += scaleForHour;

                        }

                        else {

                            if (event.getiHourScheduled() % 100 == 0) {
                                if (event.getiHourScheduled() + 30 == preferences.getiHour2() || event.getiHourScheduled() + 30 == preferences.getiHour2()) {
                                    dEvaluation += r.round(preferences.getScale(),5);                                      //calculating only the scale from the preference
                                    dEvaluationScaled += scaleCloseToHour * (preferences.getScale());             // calculating the scale predefined scale times the scale from the preference of the student
                                    dEvaluationRaw += scaleCloseToHour;

                                } else if (event.getiHourScheduled() + 100 == preferences.getiHour2() || event.getiHourScheduled() + 100 == preferences.getiHour2()) {
                                    dEvaluation += r.round(preferences.getScale(),5);                                      //calculating only the scale from the preference
                                    dEvaluationScaled += scaleCloseToHour * (preferences.getScale());             // calculating the scale predefined scale times the scale from the preference of the student
                                    dEvaluationRaw += scaleCloseToHour;

                                } else if (event.getiHourScheduled() - 100 == preferences.getiHour2() || event.getiHourScheduled() - 100 == preferences.getiHour2()) {
                                    dEvaluation += r.round(preferences.getScale(),5);                                      //calculating only the scale from the preference
                                    dEvaluationScaled += scaleCloseToHour * (preferences.getScale());             // calculating the scale predefined scale times the scale from the preference of the student
                                    dEvaluationRaw += scaleCloseToHour;

                                } else if (event.getiHourScheduled() -70 == preferences.getiHour2() || event.getiHourScheduled() -70 == preferences.getiHour2()) {
                                    dEvaluation += r.round(preferences.getScale(),5);                                      //calculating only the scale from the preference
                                    dEvaluationScaled += scaleCloseToHour * (preferences.getScale());             // calculating the scale predefined scale times the scale from the preference of the student
                                    dEvaluationRaw += scaleCloseToHour;

                                }
                                else{}



                            } else {
                                if (event.getiHourScheduled() + 70 == preferences.getiHour2() || event.getiHourScheduled() + 70 == preferences.getiHour2()) {
                                    dEvaluation += r.round(preferences.getScale(),5);                                      //calculating only the scale from the preference
                                    dEvaluationScaled += scaleCloseToHour * (preferences.getScale());             // calculating the scale predefined scale times the scale from the preference of the student
                                    dEvaluationRaw += scaleCloseToHour;

                                } else if (event.getiHourScheduled() + 100 == preferences.getiHour2() || event.getiHourScheduled() + 100 == preferences.getiHour2()) {
                                    dEvaluation += r.round(preferences.getScale(),5);                                      //calculating only the scale from the preference
                                    dEvaluationScaled += scaleCloseToHour * (preferences.getScale());             // calculating the scale predefined scale times the scale from the preference of the student
                                    dEvaluationRaw += scaleCloseToHour;

                                } else if (event.getiHourScheduled() - 100 == preferences.getiHour2() || event.getiHourScheduled() - 100 == preferences.getiHour2()) {
                                    dEvaluation += r.round(preferences.getScale(),5);                                      //calculating only the scale from the preference
                                    dEvaluationScaled += scaleCloseToHour * (preferences.getScale());             // calculating the scale predefined scale times the scale from the preference of the student
                                    dEvaluationRaw += scaleCloseToHour;

                                } else if (event.getiHourScheduled() -30 == preferences.getiHour2() || event.getiHourScheduled() - 30 == preferences.getiHour2()) {
                                    dEvaluation += r.round(preferences.getScale(),5);                                      //calculating only the scale from the preference
                                    dEvaluationScaled += scaleCloseToHour * (preferences.getScale());             // calculating the scale predefined scale times the scale from the preference of the student
                                    dEvaluationRaw += scaleCloseToHour;

                                } else {
                                }
                            }

                        }

                        evaluation.addToRawHeuristics(dEvaluationRaw);
                        evaluation.addToHeuristics(dEvaluation);
                        evaluation.addToScaledHeuristics(dEvaluationScaled);
                        break;
                    }

                }
            }
            return evaluation;

    }


    private String findDayOfLecture_By_name(Week_Timetable timetable, String sCourseName){
        for(Duplet lectures : timetable.getAssignedLectures()){
            if(lectures.getsLect().equals(sCourseName)){
                return lectures.getsDayOfWeek();
            }
        }
        return "";
    }



    //https://www.geeksforgeeks.org/method-within-method-in-java/#:~:text=Java%20does%20not%20support%20%E2%80%9Cdirectly,method%20so%20this%20does%20compile. == method inside a method
    interface myInterface {
        double round(double value,int places);
    }

    public HeuristicEvaluation heuristicEvaluation_percentage(SGT event, double scaleForDay, double scaleForHour, double scaleCloseToHour, Week_Timetable timetable, int iOption){

            //https://stackoverflow.com/questions/2808535/round-a-double-to-2-decimal-places == Rounding doubles
            //https://www.geeksforgeeks.org/method-within-method-in-java/#:~:text=Java%20does%20not%20support%20%E2%80%9Cdirectly,method%20so%20this%20does%20compile. == method inside a method


            myInterface r = new myInterface() {
                public double round(double value, int places)
                {
                    if (places < 0) throw new IllegalArgumentException();

                    BigDecimal bd = BigDecimal.valueOf(value);
                    bd = bd.setScale(places, RoundingMode.HALF_UP);
                    return bd.doubleValue();            };
            };


            double dEvaluation = 0.0;
            double dEvaluationPersonal = 0.0;
            int numPeopleWithZeroPercent = 0;
            int numPeopleImpossibleHeuristics = 0;
            int numPeopleFullSatisfiability = 0;
            HashMap<String, Integer> days = new HashMap<>();
            days.put("",0);
            days.put("Monday",1);
            days.put("Tuesday",2);
            days.put("Wednesday",3);
            days.put("Thursday", 4);
            days.put("Friday", 5);




            HeuristicEvaluation evaluation = new HeuristicEvaluation();
            //evaluation.setSgt(event);

            for(int kings_id : event.getCodesOfStudentsAssigned()){
                int iCounter = 0;
                Student student = STUDENTS_TO_BE_ASSINGED_GLOBAL.get(kings_id);
                dEvaluation = 0.0;
                dEvaluationPersonal = 0.0;

                for(DataSetStudents preferences : student.getPreferences()) {
//                dEvaluationPersonal = 0;
                    if (preferences.getAbbrev().equals(event.getsLect())) {

                        if (event.getsDayOfWeek().equals(preferences.getPrefDay())) {
                            dEvaluation = r.round(60.0/event.getCodesOfStudentsAssigned().size(), 4);                                      //calculating only the scale from the preference
                            dEvaluationPersonal = 60;
                            evaluation.addPerscentSatisfiability(dEvaluation);

                            if (event.getiHourScheduled() == preferences.getiHour1() || event.getiHourScheduled() == preferences.getiHour2()) {
                                dEvaluation = r.round(40.0/event.getCodesOfStudentsAssigned().size(), 4);
                                dEvaluationPersonal =40;
                                evaluation.addPerscentSatisfiability(dEvaluation);
                                numPeopleFullSatisfiability++;
                                evaluation.setNumPeopleCompleteSatisfiability(numPeopleFullSatisfiability);

                                if(iOption == 1)student.addPercertageSat(dEvaluationPersonal);

                                break;
                            }
                            else if (event.getiHourScheduled() + 30 == preferences.getiHour1() || event.getiHourScheduled() + 30 == preferences.getiHour2()) {
                                dEvaluation = r.round((20.0/*15.0*/) /event.getCodesOfStudentsAssigned().size(), 4);                                      //calculating only the scale from the preference
                                dEvaluationPersonal =15;
                                evaluation.addPerscentSatisfiability(dEvaluation);
                                if(iOption == 1)student.addPercertageSat(dEvaluationPersonal);

                                break;
                            } else if (event.getiHourScheduled() + 100 == preferences.getiHour1() || event.getiHourScheduled() + 100 == preferences.getiHour2()) {
                                dEvaluation = r.round((20.0/*15.0*/) /event.getCodesOfStudentsAssigned().size(), 4);                                      //calculating only the scale from the preference
                                dEvaluationPersonal =15;
                                evaluation.addPerscentSatisfiability(dEvaluation);
                                if(iOption == 1)student.addPercertageSat(dEvaluationPersonal);

                                break;
                            } else if (event.getiHourScheduled() - 100 == preferences.getiHour1() || event.getiHourScheduled() - 100 == preferences.getiHour2()) {
                                dEvaluation = r.round((20.0/*15.0*/) /event.getCodesOfStudentsAssigned().size(), 4);
                                dEvaluationPersonal =15;
                                evaluation.addPerscentSatisfiability(dEvaluation);
                                if(iOption == 1)student.addPercertageSat(dEvaluationPersonal);

                                break;
                            } else if (event.getiHourScheduled() - 70 == preferences.getiHour1() || event.getiHourScheduled() - 70 == preferences.getiHour2()) {
                                dEvaluation = r.round((20.0/*15.0*/) /event.getCodesOfStudentsAssigned().size(),4 );
                                dEvaluationPersonal =15;
                                evaluation.addPerscentSatisfiability(dEvaluation);
                                if(iOption == 1)student.addPercertageSat(dEvaluationPersonal);

                                break;
                            } else if (event.getiHourScheduled() + 70 == preferences.getiHour1() || event.getiHourScheduled() + 70 == preferences.getiHour2()) {
                                dEvaluation = r.round((20.0/*15.0*/) /event.getCodesOfStudentsAssigned().size(), 4);                                      //calculating only the scale from the preference
                                dEvaluationPersonal =15;
                                evaluation.addPerscentSatisfiability(dEvaluation);
                                if(iOption == 1)student.addPercertageSat(dEvaluationPersonal);

                                break;
                            } else if (event.getiHourScheduled() + 100 == preferences.getiHour1() || event.getiHourScheduled() + 100 == preferences.getiHour2()) {
                                dEvaluation = r.round((20.0/*15.0*/) /event.getCodesOfStudentsAssigned().size(), 4);                                      //calculating only the scale from the preference
                                dEvaluationPersonal =15;
                                evaluation.addPerscentSatisfiability(dEvaluation);
                                if(iOption == 1)student.addPercertageSat(dEvaluationPersonal);

                                break;
                            } else if (event.getiHourScheduled() - 100 ==  preferences.getiHour1() || event.getiHourScheduled() - 100 == preferences.getiHour2()) {
                                dEvaluation = r.round((20.0/*15.0*/) /event.getCodesOfStudentsAssigned().size(), 4);                                      //calculating only the scale from the preference
                                dEvaluationPersonal =15;
                                evaluation.addPerscentSatisfiability(dEvaluation);
                                if(iOption == 1)student.addPercertageSat(dEvaluationPersonal);

                                break;
                            } else if (event.getiHourScheduled() - 30 == preferences.getiHour1() || event.getiHourScheduled() - 30 == preferences.getiHour2()) {
                                dEvaluation = r.round((20.0/*15.0*/) /event.getCodesOfStudentsAssigned().size(), 4);                                      //calculating only the scale from the preference
                                dEvaluationPersonal =15;
                                evaluation.addPerscentSatisfiability(dEvaluation);
                                if(iOption == 1)student.addPercertageSat(dEvaluationPersonal);

                                break;
                            } else {
                                if(iOption == 1)student.addPercertageSat(dEvaluationPersonal);
                            }

                        }
                        else{


                            //Check how many peaple have heuristics for a day unsatisfiable
                            //and how many of them are not possible in the timeline of events occuring, no sgt before the lecture

                            int a = days.get(preferences.getPrefDay());
                            int b = days.get(findDayOfLecture_By_name(timetable, event.getsLect()));
                            if( a < b ) {
                                numPeopleImpossibleHeuristics++;
                                evaluation.setNumPeopleImpossibleHeuristics(numPeopleImpossibleHeuristics);
                            }
                            if (event.getiHourScheduled() == preferences.getiHour1() || event.getiHourScheduled() == preferences.getiHour2()) {
                                dEvaluation = r.round(40.0/event.getCodesOfStudentsAssigned().size(), 4);
                                dEvaluationPersonal =40;
                                evaluation.addPerscentSatisfiability(dEvaluation);
                                if(iOption == 1)student.addPercertageSat(dEvaluationPersonal);

                                break;
                            } else if (event.getiHourScheduled() + 30 == preferences.getiHour1() || event.getiHourScheduled() + 30 == preferences.getiHour2()) {
                                dEvaluation = r.round(20.0/event.getCodesOfStudentsAssigned().size(), 4);                                      //calculating only the scale from the preference
                                dEvaluationPersonal =20;
                                evaluation.addPerscentSatisfiability(dEvaluation);
                                if(iOption == 1)student.addPercertageSat(dEvaluationPersonal);

                                break;
                            } else if (event.getiHourScheduled() + 100 == preferences.getiHour1() || event.getiHourScheduled() + 100 == preferences.getiHour2()) {
                                dEvaluation = r.round(20.0/event.getCodesOfStudentsAssigned().size(), 4);                                      //calculating only the scale from the preference
                                dEvaluationPersonal =20;
                                evaluation.addPerscentSatisfiability(dEvaluation);
                                if(iOption == 1)student.addPercertageSat(dEvaluationPersonal);

                                break;
                            } else if (event.getiHourScheduled() - 100 == preferences.getiHour1() || event.getiHourScheduled() - 100 == preferences.getiHour2()) {
                                dEvaluation = r.round(20.0/event.getCodesOfStudentsAssigned().size(), 4);
                                dEvaluationPersonal =20;
                                evaluation.addPerscentSatisfiability(dEvaluation);
                                if(iOption == 1)student.addPercertageSat(dEvaluationPersonal);

                                break;
                            } else if (event.getiHourScheduled() - 70 == preferences.getiHour1() || event.getiHourScheduled() - 70 == preferences.getiHour2()) {
                                dEvaluation = r.round(20.0/event.getCodesOfStudentsAssigned().size(), 4);
                                dEvaluationPersonal =20;
                                evaluation.addPerscentSatisfiability(dEvaluation);
                                if(iOption == 1)student.addPercertageSat(dEvaluationPersonal);

                                break;
                            } else if (event.getiHourScheduled() + 70 == preferences.getiHour1() || event.getiHourScheduled() + 70 == preferences.getiHour2()) {
                                dEvaluation = r.round(20.0/event.getCodesOfStudentsAssigned().size(), 4);                                      //calculating only the scale from the preference
                                dEvaluationPersonal =20;
                                evaluation.addPerscentSatisfiability(dEvaluation);
                                if(iOption == 1)student.addPercertageSat(dEvaluationPersonal);

                                break;
                            } else if (event.getiHourScheduled() + 100 == preferences.getiHour1() || event.getiHourScheduled() + 100 == preferences.getiHour2()) {
                                dEvaluation = r.round(20.0/event.getCodesOfStudentsAssigned().size(), 4);                                      //calculating only the scale from the preference
                                dEvaluationPersonal =20;
                                evaluation.addPerscentSatisfiability(dEvaluation);
                                if(iOption == 1)student.addPercertageSat(dEvaluationPersonal);

                                break;
                            } else if (event.getiHourScheduled() - 100 == preferences.getiHour1() || event.getiHourScheduled() - 100 == preferences.getiHour2()) {
                                dEvaluation = r.round(20.0/event.getCodesOfStudentsAssigned().size() , 4);                                      //calculating only the scale from the preference
                                dEvaluationPersonal =20;
                                evaluation.addPerscentSatisfiability(dEvaluation);
                                if(iOption == 1)student.addPercertageSat(dEvaluationPersonal);

                                break;
                            } else if (event.getiHourScheduled() - 30 == preferences.getiHour1() || event.getiHourScheduled() - 30 == preferences.getiHour2()) {
                                dEvaluation = r.round(20.0/event.getCodesOfStudentsAssigned().size() , 4);                                      //calculating only the scale from the preference
                                dEvaluationPersonal =20;
                                evaluation.addPerscentSatisfiability(dEvaluation);
                                if(iOption == 1)student.addPercertageSat(dEvaluationPersonal);

                                break;
                            } else {
                            }
                            numPeopleWithZeroPercent++;
                            evaluation.setNumPeopleWithZeroPercent(numPeopleWithZeroPercent);
                        }
                    }
                }

            }

            evaluation.addPerscentSatisfiability(dEvaluation/event.getCodesOfStudentsAssigned().size());
            return evaluation;
        }

    
//    public HeuristicEvaluation heuristicEvaluation_percentage(SGT event, double scaleForDay, double scaleForHour, double scaleCloseToHour, Week_Timetable timetable, int iOption){
//
//        //https://stackoverflow.com/questions/2808535/round-a-double-to-2-decimal-places == Rounding doubles
//        //https://www.geeksforgeeks.org/method-within-method-in-java/#:~:text=Java%20does%20not%20support%20%E2%80%9Cdirectly,method%20so%20this%20does%20compile. == method inside a method
//
//
//        //a student is 20% happy if he is assigned to a time that is +- 1 hour or half an hour from what he/she has specified as a preference for time. The day of his preference doesn't match
//        //a student is 40% happy if he is assigned to his time of specified choice
//        //a student is 60% happy if day assigned only matches preference
//        //a student is 75% happy if hour +- 1 hour to preference + day of choice matches
//        // 100% if both preferences for hour and time are fulfilled
//
//
//        myInterface r = new myInterface() {
//            public double round(double value, int places)
//            {
//                if (places < 0) throw new IllegalArgumentException();
//
//                BigDecimal bd = BigDecimal.valueOf(value);
//                bd = bd.setScale(places, RoundingMode.HALF_UP);
//                return bd.doubleValue();            };
//        };
//
//
//        double dEvaluation = 0.0;
//        double dEvaluationPersonal = 0.0;
//        int numPeopleWithZeroPercent = 0;
//        int numPeopleImpossibleHeuristics = 0;
//        int numPeopleFullSatisfiability = 0;
//        HashMap<String, Integer> days = new HashMap<>();
//        days.put("",0);
//        days.put("Monday",1);
//        days.put("Tuesday",2);
//        days.put("Wednesday",3);
//        days.put("Thursday", 4);
//        days.put("Friday", 5);
//
//
//
//
//        HeuristicEvaluation evaluation = new HeuristicEvaluation();
//        evaluation.setSgt(event);
//
//        for(int kings_id : event.getCodesOfStudentsAssigned()){
//            int iCounter = 0;
//             dEvaluation = 0.0;
//             dEvaluationPersonal = 0.0;
//
//            Student student = STUDENTS_TO_BE_ASSINGED_GLOBAL.get(kings_id);
//
//            for(DataSetStudents preferences : student.getPreferences()) {
//                dEvaluationPersonal = 0;
//                if (preferences.getAbbrev().equals(event.getsLect())) {
//
//                    if (event.getsDayOfWeek().equals(preferences.getPrefDay())) {
//                        dEvaluation = r.round(60.0/event.getCodesOfStudentsAssigned().size(),4);                                      //calculating only the scale from the preference
//                        dEvaluationPersonal += 60;
//                        evaluation.addPerscentSatisfiability(dEvaluation);
//
//                        if (event.getiHourScheduled() == preferences.getiHour1() || event.getiHourScheduled() == preferences.getiHour2()) {
//                            dEvaluation = r.round(40.0/event.getCodesOfStudentsAssigned().size(), 4);
//                            dEvaluationPersonal =40;
//                            evaluation.addPerscentSatisfiability(dEvaluation);
//                            if(iOption == 1)if(iOption == 1)student.addPercertageSat(dEvaluationPersonal);
//                            numPeopleFullSatisfiability++;
//
//                            evaluation.setNumPeopleCompleteSatisfiability(numPeopleFullSatisfiability);
//
//                            break;
//                        }
//                        else if (event.getiHourScheduled() + 30 == preferences.getiHour1() || event.getiHourScheduled() + 30 == preferences.getiHour2()) {
//                            dEvaluation = r.round((20.0/*15.0*/) /(event.getCodesOfStudentsAssigned().size()), 4);                                      //calculating only the scale from the preference
//                            dEvaluationPersonal =15;
//                            evaluation.addPerscentSatisfiability(dEvaluation);
//                             if(iOption == 1)student.addPercertageSat(dEvaluationPersonal);
//
//                            break;
//                        } else if (event.getiHourScheduled() + 100 == preferences.getiHour1() || event.getiHourScheduled() + 100 == preferences.getiHour2()) {
//                            dEvaluation = r.round((20.0/*15.0*/) /event.getCodesOfStudentsAssigned().size(),4);                                      //calculating only the scale from the preference
//                            dEvaluationPersonal =15;
//                            evaluation.addPerscentSatisfiability(dEvaluation);
//                             if(iOption == 1)student.addPercertageSat(dEvaluationPersonal);
//
//                            break;
//                        } else if (event.getiHourScheduled() - 100 == preferences.getiHour1() || event.getiHourScheduled() - 100 == preferences.getiHour2()) {
//                            dEvaluation = r.round((20.0/*15.0*/) /event.getCodesOfStudentsAssigned().size(),4);
//                            dEvaluationPersonal =15;
//                            evaluation.addPerscentSatisfiability(dEvaluation);
//                             if(iOption == 1)student.addPercertageSat(dEvaluationPersonal);
//
//                            break;
//                        } else if (event.getiHourScheduled() - 70 == preferences.getiHour1() || event.getiHourScheduled() - 70 == preferences.getiHour2()) {
//                            dEvaluation = r.round((20.0/*15.0*/) /event.getCodesOfStudentsAssigned().size(), 4);
//                            dEvaluationPersonal =15;
//                            evaluation.addPerscentSatisfiability(dEvaluation);
//                             if(iOption == 1)student.addPercertageSat(dEvaluationPersonal);
//
//                            break;
//                        } else if (event.getiHourScheduled() + 70 == preferences.getiHour1() || event.getiHourScheduled() + 70 == preferences.getiHour2()) {
//                            dEvaluation = r.round( (20.0/*15.0*/) /event.getCodesOfStudentsAssigned().size(), 4);                                      //calculating only the scale from the preference
//                            dEvaluationPersonal =15;
//                            evaluation.addPerscentSatisfiability(dEvaluation);
//                             if(iOption == 1)student.addPercertageSat(dEvaluationPersonal);
//
//                            break;
//                        } else if (event.getiHourScheduled() + 100 == preferences.getiHour1() || event.getiHourScheduled() + 100 == preferences.getiHour2()) {
//                            dEvaluation = r.round((20.0/*15.0*/) /event.getCodesOfStudentsAssigned().size(), 4);                                      //calculating only the scale from the preference
//                            dEvaluationPersonal =15;
//                            evaluation.addPerscentSatisfiability(dEvaluation);
//                             if(iOption == 1)student.addPercertageSat(dEvaluationPersonal);
//
//                            break;
//                        } else if (event.getiHourScheduled() - 100 == preferences.getiHour1() || event.getiHourScheduled() - 100 == preferences.getiHour2()) {
//                            dEvaluation = r.round((20.0/*15.0*/) /event.getCodesOfStudentsAssigned().size(), 4);                                      //calculating only the scale from the preference
//                            dEvaluationPersonal =15;
//                            evaluation.addPerscentSatisfiability(dEvaluation);
//                             if(iOption == 1)student.addPercertageSat(dEvaluationPersonal);
//
//                            break;
//                        } else if (event.getiHourScheduled() - 30 == preferences.getiHour1() || event.getiHourScheduled() - 30 == preferences.getiHour2()) {
//                            dEvaluation = r.round((20.0/*15.0*/) /event.getCodesOfStudentsAssigned().size(), 4);                                      //calculating only the scale from the preference
//                            dEvaluationPersonal =15;
//                            evaluation.addPerscentSatisfiability(dEvaluation);
//                             if(iOption == 1)student.addPercertageSat(dEvaluationPersonal);
//
//                            break;
//                        } else {
//                            if(iOption == 1)student.addPercertageSat(dEvaluationPersonal);
//                        }
//
//                    }
//                    else{
//
//
//                        //Check how many peaple have heuristics for a day unsatisfiable
//                        //and how many of them are not possible in the timeline of events occuring, no sgt before the lecture
//
//                        int a = days.get(preferences.getPrefDay());
//                        int b = days.get(findDayOfLecture_By_name(timetable, event.getsLect()));
//                        if( a < b ) {
//                                numPeopleImpossibleHeuristics++;
//                                evaluation.setNumPeopleImpossibleHeuristics(numPeopleImpossibleHeuristics);
//                        }
//                        if (event.getiHourScheduled() == preferences.getiHour1() || event.getiHourScheduled() == preferences.getiHour2()) {
//                            dEvaluation = r.round(40.0/event.getCodesOfStudentsAssigned().size(),4);
//                            dEvaluationPersonal =40;
//                            evaluation.addPerscentSatisfiability(dEvaluation);
//                             if(iOption == 1)student.addPercertageSat(dEvaluationPersonal);
//
//                            break;
//                        } else if (event.getiHourScheduled() + 30 == preferences.getiHour1() || event.getiHourScheduled() + 30 == preferences.getiHour2()) {
//                            dEvaluation = r.round(20.0/event.getCodesOfStudentsAssigned().size(), 4);
//                            dEvaluationPersonal =20/event.getCodesOfStudentsAssigned().size();
//                            evaluation.addPerscentSatisfiability(dEvaluation);
//                             if(iOption == 1)student.addPercertageSat(dEvaluationPersonal);
//
//                            break;
//                        } else if (event.getiHourScheduled() + 100 == preferences.getiHour1() || event.getiHourScheduled() + 100 == preferences.getiHour2()) {
//                            dEvaluation =r.round( 20.0/event.getCodesOfStudentsAssigned().size(), 4 ) ;
//                            dEvaluationPersonal =20;
//                            evaluation.addPerscentSatisfiability(dEvaluation);
//                             if(iOption == 1)student.addPercertageSat(dEvaluationPersonal);
//
//                            break;
//                        } else if (event.getiHourScheduled() - 100 == preferences.getiHour1() || event.getiHourScheduled() - 100 == preferences.getiHour2()) {
//                            dEvaluation =r.round( 20.0/event.getCodesOfStudentsAssigned().size(), 4);
//                            dEvaluationPersonal =20;
//                            evaluation.addPerscentSatisfiability(dEvaluation);
//                             if(iOption == 1)student.addPercertageSat(dEvaluationPersonal);
//
//                            break;
//                        } else if (event.getiHourScheduled() - 70 == preferences.getiHour1() || event.getiHourScheduled() - 70 == preferences.getiHour2()) {
//                            dEvaluation =r.round( 20.0/event.getCodesOfStudentsAssigned().size(), 4);
//                            dEvaluationPersonal =20;
//                            evaluation.addPerscentSatisfiability(dEvaluation);
//                             if(iOption == 1)student.addPercertageSat(dEvaluationPersonal);
//
//                            break;
//                        } else if (event.getiHourScheduled() + 70 == preferences.getiHour1() || event.getiHourScheduled() + 70 == preferences.getiHour2()) {
//                            dEvaluation = r.round( 20.0/event.getCodesOfStudentsAssigned().size(), 4);                                      //calculating only the scale from the preference
//                            dEvaluationPersonal =20;
//                            evaluation.addPerscentSatisfiability(dEvaluation);
//                             if(iOption == 1)student.addPercertageSat(dEvaluationPersonal);
//
//                            break;
//                        } else if (event.getiHourScheduled() + 100 == preferences.getiHour1() || event.getiHourScheduled() + 100 == preferences.getiHour2()) {
//                            dEvaluation = r.round( 20.0/event.getCodesOfStudentsAssigned().size(),4);                                      //calculating only the scale from the preference
//                            dEvaluationPersonal =20;
//                            evaluation.addPerscentSatisfiability(dEvaluation);
//                             if(iOption == 1)student.addPercertageSat(dEvaluationPersonal);
//
//                            break;
//                        } else if (event.getiHourScheduled() - 100 == preferences.getiHour1() || event.getiHourScheduled() - 100 == preferences.getiHour2()) {
//                            dEvaluation = r.round(20.0/event.getCodesOfStudentsAssigned().size(), 4);                                      //calculating only the scale from the preference
//                            dEvaluationPersonal =20;
//                            evaluation.addPerscentSatisfiability(dEvaluation);
//                             if(iOption == 1)student.addPercertageSat(dEvaluationPersonal);
//
//                            break;
//                        } else if (event.getiHourScheduled() - 30 == preferences.getiHour1() || event.getiHourScheduled() - 30 == preferences.getiHour2()) {
//                            dEvaluation = r.round(20.0/event.getCodesOfStudentsAssigned().size(), 4);                                      //calculating only the scale from the preference
//                            dEvaluationPersonal =20;
//                            evaluation.addPerscentSatisfiability(dEvaluation);
//                             if(iOption == 1)student.addPercertageSat(dEvaluationPersonal);
//
//                            break;
//                        } else {
//                        }
//                        numPeopleWithZeroPercent++;
//                        evaluation.addPerscentSatisfiability(dEvaluation);
//                        evaluation.setNumPeopleWithZeroPercent(numPeopleWithZeroPercent);
//                        if(iOption == 1)student.addPercertageSat(dEvaluationPersonal);
//                    }
//                    break;
//
//                }
//            }
//
//        }
//
//        evaluation.addPerscentSatisfiability(dEvaluation/event.getCodesOfStudentsAssigned().size());
//        return evaluation;
//    }

    public HeuristicEvaluation heuristicEvaluation_percentage_newTimeslot(SGT event,String sDay, int iHour, double scaleForDay, double scaleForHour, double scaleCloseToHour, Week_Timetable timetable, int iOption){

        //https://stackoverflow.com/questions/2808535/round-a-double-to-2-decimal-places == Rounding doubles
        //https://www.geeksforgeeks.org/method-within-method-in-java/#:~:text=Java%20does%20not%20support%20%E2%80%9Cdirectly,method%20so%20this%20does%20compile. == method inside a method


        myInterface r = new myInterface() {
            public double round(double value, int places)
            {
                if (places < 0) throw new IllegalArgumentException();

                BigDecimal bd = BigDecimal.valueOf(value);
                bd = bd.setScale(places, RoundingMode.HALF_UP);
                return bd.doubleValue();            };
        };


        double dEvaluation = 0.0;
        double dEvaluationPersonal = 0.0;
        int numPeopleWithZeroPercent = 0;
        int numPeopleImpossibleHeuristics = 0;
        int numPeopleFullSatisfiability = 0;
        HashMap<String, Integer> days = new HashMap<>();
        days.put("",0);
        days.put("Monday",1);
        days.put("Tuesday",2);
        days.put("Wednesday",3);
        days.put("Thursday", 4);
        days.put("Friday", 5);




        HeuristicEvaluation evaluation = new HeuristicEvaluation();
        //evaluation.setSgt(event);

        for(int kings_id : event.getCodesOfStudentsAssigned()){
            int iCounter = 0;
            Student student = STUDENTS_TO_BE_ASSINGED_GLOBAL.get(kings_id);
             dEvaluation = 0.0;
             dEvaluationPersonal = 0.0;

            for(DataSetStudents preferences : student.getPreferences()) {
//                dEvaluationPersonal = 0;
                if (preferences.getAbbrev().equals(event.getsLect())) {

                    if (sDay.equals(preferences.getPrefDay())) {
                        dEvaluation = r.round(60.0/event.getCodesOfStudentsAssigned().size(), 4);                                      //calculating only the scale from the preference
                        dEvaluationPersonal = 60;
                        evaluation.addPerscentSatisfiability(dEvaluation);

                        if (iHour == preferences.getiHour1() || iHour == preferences.getiHour2()) {
                            dEvaluation = r.round(40.0/event.getCodesOfStudentsAssigned().size(), 4);
                            dEvaluationPersonal =40;
                            evaluation.addPerscentSatisfiability(dEvaluation);
                            numPeopleFullSatisfiability++;
                            evaluation.setNumPeopleCompleteSatisfiability(numPeopleFullSatisfiability);

                            if(iOption == 1)student.addPercertageSat(dEvaluationPersonal);

                            break;
                        }
                        else if (iHour + 30 == preferences.getiHour1() || iHour + 30 == preferences.getiHour2()) {
                            dEvaluation = r.round((20.0/*15.0*/) /event.getCodesOfStudentsAssigned().size(), 4);                                      //calculating only the scale from the preference
                            dEvaluationPersonal =15;
                            evaluation.addPerscentSatisfiability(dEvaluation);
                             if(iOption == 1)student.addPercertageSat(dEvaluationPersonal);

                            break;
                        } else if (iHour + 100 == preferences.getiHour1() || iHour + 100 == preferences.getiHour2()) {
                            dEvaluation = r.round((20.0/*15.0*/) /event.getCodesOfStudentsAssigned().size(), 4);                                      //calculating only the scale from the preference
                            dEvaluationPersonal =15;
                            evaluation.addPerscentSatisfiability(dEvaluation);
                             if(iOption == 1)student.addPercertageSat(dEvaluationPersonal);

                            break;
                        } else if (iHour - 100 == preferences.getiHour1() || iHour - 100 == preferences.getiHour2()) {
                            dEvaluation = r.round((20.0/*15.0*/) /event.getCodesOfStudentsAssigned().size(), 4);
                            dEvaluationPersonal =15;
                            evaluation.addPerscentSatisfiability(dEvaluation);
                             if(iOption == 1)student.addPercertageSat(dEvaluationPersonal);

                            break;
                        } else if (iHour - 70 == preferences.getiHour1() || iHour - 70 == preferences.getiHour2()) {
                            dEvaluation = r.round((20.0/*15.0*/) /event.getCodesOfStudentsAssigned().size(),4 );
                            dEvaluationPersonal =15;
                            evaluation.addPerscentSatisfiability(dEvaluation);
                             if(iOption == 1)student.addPercertageSat(dEvaluationPersonal);

                            break;
                        } else if (iHour + 70 == preferences.getiHour1() || iHour + 70 == preferences.getiHour2()) {
                            dEvaluation = r.round((20.0/*15.0*/) /event.getCodesOfStudentsAssigned().size(), 4);                                      //calculating only the scale from the preference
                            dEvaluationPersonal =15;
                            evaluation.addPerscentSatisfiability(dEvaluation);
                             if(iOption == 1)student.addPercertageSat(dEvaluationPersonal);

                            break;
                        } else if (iHour + 100 == preferences.getiHour1() || iHour + 100 == preferences.getiHour2()) {
                            dEvaluation = r.round((20.0/*15.0*/) /event.getCodesOfStudentsAssigned().size(), 4);                                      //calculating only the scale from the preference
                            dEvaluationPersonal =15;
                            evaluation.addPerscentSatisfiability(dEvaluation);
                             if(iOption == 1)student.addPercertageSat(dEvaluationPersonal);

                            break;
                        } else if (iHour - 100 ==  preferences.getiHour1() || iHour - 100 == preferences.getiHour2()) {
                            dEvaluation = r.round((20.0/*15.0*/) /event.getCodesOfStudentsAssigned().size(), 4);                                      //calculating only the scale from the preference
                            dEvaluationPersonal =15;
                            evaluation.addPerscentSatisfiability(dEvaluation);
                             if(iOption == 1)student.addPercertageSat(dEvaluationPersonal);

                            break;
                        } else if (iHour - 30 == preferences.getiHour1() || iHour - 30 == preferences.getiHour2()) {
                            dEvaluation = r.round((20.0/*15.0*/) /event.getCodesOfStudentsAssigned().size(), 4);                                      //calculating only the scale from the preference
                            dEvaluationPersonal =15;
                            evaluation.addPerscentSatisfiability(dEvaluation);
                             if(iOption == 1)student.addPercertageSat(dEvaluationPersonal);

                            break;
                        } else {
                            if(iOption == 1)student.addPercertageSat(dEvaluationPersonal);
                        }

                    }
                    else{


                        //Check how many peaple have heuristics for a day unsatisfiable
                        //and how many of them are not possible in the timeline of events occuring, no sgt before the lecture

                        int a = days.get(preferences.getPrefDay());
                        int b = days.get(findDayOfLecture_By_name(timetable, event.getsLect()));
                        if( a < b ) {
                                numPeopleImpossibleHeuristics++;
                                evaluation.setNumPeopleImpossibleHeuristics(numPeopleImpossibleHeuristics);
                        }
                        if (iHour == preferences.getiHour1() || iHour == preferences.getiHour2()) {
                            dEvaluation = r.round(40.0/event.getCodesOfStudentsAssigned().size(), 4);
                            dEvaluationPersonal =40;
                            evaluation.addPerscentSatisfiability(dEvaluation);
                             if(iOption == 1)student.addPercertageSat(dEvaluationPersonal);

                            break;
                        } else if (iHour + 30 == preferences.getiHour1() || iHour + 30 == preferences.getiHour2()) {
                            dEvaluation = r.round(20.0/event.getCodesOfStudentsAssigned().size(), 4);                                      //calculating only the scale from the preference
                            dEvaluationPersonal =20;
                            evaluation.addPerscentSatisfiability(dEvaluation);
                             if(iOption == 1)student.addPercertageSat(dEvaluationPersonal);

                            break;
                        } else if (iHour + 100 == preferences.getiHour1() || iHour + 100 == preferences.getiHour2()) {
                            dEvaluation = r.round(20.0/event.getCodesOfStudentsAssigned().size(), 4);                                      //calculating only the scale from the preference
                            dEvaluationPersonal =20;
                            evaluation.addPerscentSatisfiability(dEvaluation);
                             if(iOption == 1)student.addPercertageSat(dEvaluationPersonal);

                            break;
                        } else if (iHour - 100 == preferences.getiHour1() || iHour - 100 == preferences.getiHour2()) {
                            dEvaluation = r.round(20.0/event.getCodesOfStudentsAssigned().size(), 4);
                            dEvaluationPersonal =20;
                            evaluation.addPerscentSatisfiability(dEvaluation);
                             if(iOption == 1)student.addPercertageSat(dEvaluationPersonal);

                            break;
                        } else if (iHour - 70 == preferences.getiHour1() || iHour - 70 == preferences.getiHour2()) {
                            dEvaluation = r.round(20.0/event.getCodesOfStudentsAssigned().size(), 4);
                            dEvaluationPersonal =20;
                            evaluation.addPerscentSatisfiability(dEvaluation);
                             if(iOption == 1)student.addPercertageSat(dEvaluationPersonal);

                            break;
                        } else if (iHour + 70 == preferences.getiHour1() || iHour + 70 == preferences.getiHour2()) {
                            dEvaluation = r.round(20.0/event.getCodesOfStudentsAssigned().size(), 4);                                      //calculating only the scale from the preference
                            dEvaluationPersonal =20;
                            evaluation.addPerscentSatisfiability(dEvaluation);
                             if(iOption == 1)student.addPercertageSat(dEvaluationPersonal);

                            break;
                        } else if (iHour + 100 == preferences.getiHour1() || iHour + 100 == preferences.getiHour2()) {
                            dEvaluation = r.round(20.0/event.getCodesOfStudentsAssigned().size(), 4);                                      //calculating only the scale from the preference
                            dEvaluationPersonal =20;
                            evaluation.addPerscentSatisfiability(dEvaluation);
                             if(iOption == 1)student.addPercertageSat(dEvaluationPersonal);

                            break;
                        } else if (iHour - 100 == preferences.getiHour1() || iHour - 100 == preferences.getiHour2()) {
                            dEvaluation = r.round(20.0/event.getCodesOfStudentsAssigned().size() , 4);                                      //calculating only the scale from the preference
                            dEvaluationPersonal =20;
                            evaluation.addPerscentSatisfiability(dEvaluation);
                             if(iOption == 1)student.addPercertageSat(dEvaluationPersonal);

                            break;
                        } else if (iHour - 30 == preferences.getiHour1() || iHour - 30 == preferences.getiHour2()) {
                            dEvaluation = r.round(20.0/event.getCodesOfStudentsAssigned().size() , 4);                                      //calculating only the scale from the preference
                            dEvaluationPersonal =20;
                            evaluation.addPerscentSatisfiability(dEvaluation);
                             if(iOption == 1)student.addPercertageSat(dEvaluationPersonal);

                            break;
                        } else {
                        }
                        numPeopleWithZeroPercent++;
                        evaluation.setNumPeopleWithZeroPercent(numPeopleWithZeroPercent);
                    }
                }
            }

        }

        evaluation.addPerscentSatisfiability(dEvaluation/event.getCodesOfStudentsAssigned().size());
        return evaluation;
    }

    //TRY A DIFF VARIATION OF THE SGT BY TESTING WITH A NEW DAY AND HOUR, depends on the available slots the sgt has
    public HeuristicEvaluation heuristicEvaluation_function_Changed_TimeSlot(SGT event,String sDay, int ihour, int iDate, int iMonth,int iYear, double scaleForDay, double scaleForHour, double scaleCloseToHour){


        myInterface r = new myInterface() {
            public double round(double value, int places)
            {
                if (places < 0) throw new IllegalArgumentException();

                BigDecimal bd = BigDecimal.valueOf(value);
                bd = bd.setScale(places, RoundingMode.HALF_UP);
                return bd.doubleValue();            };
        };

        double dEvaluation = 0.0;
        double dEvaluationRaw = 0.0;
        double dEvaluationScaled = 0.0;
        HeuristicEvaluation evaluation = new HeuristicEvaluation();
        evaluation.setiHour(ihour);
        evaluation.setsDayOfWeek(sDay);
        evaluation.setiDate(iDate);
        evaluation.setiMonth(iMonth);
        evaluation.setiYear(iYear);

        for(int kings_id : event.getCodesOfStudentsAssigned()){
            int iCounter = 0;
            Student student = STUDENTS_TO_BE_ASSINGED_GLOBAL.get(kings_id);
             dEvaluation = 0.0;
             dEvaluationRaw = 0.0;
             dEvaluationScaled = 0.0;

            for(DataSetStudents preferences : student.getPreferences()) {

                if (preferences.getAbbrev().equals(event.getsLect())) {
                    //has entered evaluation
                    if (sDay.equals(preferences.getPrefDay())) {
                        dEvaluation += r.round(preferences.getScale(), 5);                                      //calculating only the scale from the preference
                        dEvaluationScaled += r.round(scaleForDay * (preferences.getScale()), 5);             // calculating the scale predefined scale times the scale from the preference of the student
                        dEvaluationRaw += scaleForDay;                              //only the predefined scale


                    }
                    if (ihour == preferences.getiHour1() || ihour == preferences.getiHour2()) {

                        dEvaluation += r.round(preferences.getScale(), 5);
                        dEvaluationScaled += scaleForHour * (preferences.getScale());
                        dEvaluationRaw += scaleForHour;

                    }

                    else {

                        if (ihour % 100 == 0) {
                            if (ihour + 30 == preferences.getiHour2() || ihour + 30 == preferences.getiHour2()) {
                                dEvaluation += r.round(preferences.getScale(),5);                                      //calculating only the scale from the preference
                                dEvaluationScaled += scaleCloseToHour * (preferences.getScale());             // calculating the scale predefined scale times the scale from the preference of the student
                                dEvaluationRaw += scaleCloseToHour;

                            } else if (ihour + 100 == preferences.getiHour2() || ihour + 100 == preferences.getiHour2()) {
                                dEvaluation += r.round(preferences.getScale(),5);                                      //calculating only the scale from the preference
                                dEvaluationScaled += scaleCloseToHour * (preferences.getScale());             // calculating the scale predefined scale times the scale from the preference of the student
                                dEvaluationRaw += scaleCloseToHour;

                            } else if (ihour - 100 == preferences.getiHour2() || ihour - 100 == preferences.getiHour2()) {
                                dEvaluation += r.round(preferences.getScale(),5);                                      //calculating only the scale from the preference
                                dEvaluationScaled += scaleCloseToHour * (preferences.getScale());             // calculating the scale predefined scale times the scale from the preference of the student
                                dEvaluationRaw += scaleCloseToHour;

                            } else if (ihour -70 == preferences.getiHour2() || ihour -70 == preferences.getiHour2()) {
                                dEvaluation += r.round(preferences.getScale(),5);                                      //calculating only the scale from the preference
                                dEvaluationScaled += scaleCloseToHour * (preferences.getScale());             // calculating the scale predefined scale times the scale from the preference of the student
                                dEvaluationRaw += scaleCloseToHour;

                            }
                            else{}



                        } else {
                            if (ihour + 70 == preferences.getiHour2() || ihour + 70 == preferences.getiHour2()) {
                                dEvaluation += r.round(preferences.getScale(),5);                                      //calculating only the scale from the preference
                                dEvaluationScaled += scaleCloseToHour * (preferences.getScale());             // calculating the scale predefined scale times the scale from the preference of the student
                                dEvaluationRaw += scaleCloseToHour;

                            } else if (ihour + 100 == preferences.getiHour2() || ihour + 100 == preferences.getiHour2()) {
                                dEvaluation += r.round(preferences.getScale(),5);                                      //calculating only the scale from the preference
                                dEvaluationScaled += scaleCloseToHour * (preferences.getScale());             // calculating the scale predefined scale times the scale from the preference of the student
                                dEvaluationRaw += scaleCloseToHour;

                            } else if (ihour - 100 == preferences.getiHour2() || ihour - 100 == preferences.getiHour2()) {
                                dEvaluation += r.round(preferences.getScale(),5);                                      //calculating only the scale from the preference
                                dEvaluationScaled += scaleCloseToHour * (preferences.getScale());             // calculating the scale predefined scale times the scale from the preference of the student
                                dEvaluationRaw += scaleCloseToHour;

                            } else if (ihour -30 == preferences.getiHour2() || ihour - 30 == preferences.getiHour2()) {
                                dEvaluation += r.round(preferences.getScale(),5);                                      //calculating only the scale from the preference
                                dEvaluationScaled += scaleCloseToHour * (preferences.getScale());             // calculating the scale predefined scale times the scale from the preference of the student
                                dEvaluationRaw += scaleCloseToHour;

                            } else {
                            }
                        }

                    }

                    evaluation.addToRawHeuristics(dEvaluationRaw);
                    evaluation.addToHeuristics(dEvaluation);
                    evaluation.addToScaledHeuristics(dEvaluationScaled);
                    break;
                }

            }
        }
        return evaluation;
    }



    public HeuristicEvaluation evaluateSolution(){
        HeuristicEvaluation solution = new HeuristicEvaluation();
        for(SGT tutorial : this.ASSIGNED_SGTS_CURRENTWEEK_GLOBAL){
            solution.mergeHeuristicEvaluations(tutorial.getEvaluation());
        }
        return solution;
    }


    //PUBLIC for now, FUNCTION FOR CALCULATING HEURISTICS,current, for all lectures
    //public ArrayList<HeuristicEvaluation> calculationHeuristicsArray(double scaleForDay, double scaleForHour, double scaleCloseToHour, Week_Timetable timetable){
    public void calculationHeuristicsArray(double scaleForDay, double scaleForHour, double scaleCloseToHour, Week_Timetable timetable){

        ArrayList<HeuristicEvaluation> evaluations = new ArrayList<>();
        ArrayList<HeuristicEvaluation> percentages = new ArrayList<>();
        for(int i =0; i< ASSIGNED_SGTS_CURRENTWEEK_GLOBAL.size(); i++){
            SGT current = ASSIGNED_SGTS_CURRENTWEEK_GLOBAL.get(i);
            HeuristicEvaluation tutorialEval = heuristicEvaluation_function(ASSIGNED_SGTS_CURRENTWEEK_GLOBAL.get(i),scaleForDay,scaleForHour,scaleCloseToHour);
            HeuristicEvaluation tutorialEval_percents = heuristicEvaluation_percentage(ASSIGNED_SGTS_CURRENTWEEK_GLOBAL.get(i),scaleForDay,scaleForHour,scaleCloseToHour, timetable, 1);

            evaluations.add(tutorialEval);
            percentages.add(tutorialEval_percents);
            tutorialEval.mergeHeuristicEvaluations(tutorialEval_percents);
            ASSIGNED_SGTS_CURRENTWEEK_GLOBAL.get(i).setEvaluation(tutorialEval);
        }

        //return evaluations;
    }




    public void dependencies_for_building_graph() throws CloneNotSupportedException {

        HashSet<SGT> dependencies = new HashSet<>();
        ArrayList<SGT> allDepssUnfilterred = new ArrayList<>();             //for testing purposes
        for(SGT sgt : this.ASSIGNED_SGTS_CURRENTWEEK_GLOBAL){
            SGT currentsgt = sgt;
            dependencies.clear();
            for(int iKings_Code : sgt.getCodesOfStudentsAssigned()){ // this line will add all the sgts,I also need to add the lectures here as well
                Student student = this.STUDENTS_TO_BE_ASSINGED_GLOBAL.get(iKings_Code);
                for(SGT sgt_Assigned : student.getCourses()){       // the name here .getCourses is misleading, those are the assigned SGTs
                    dependencies.add((SGT) sgt_Assigned.clone());
                    allDepssUnfilterred.add((SGT) sgt_Assigned.clone());
                    //+ remove itself from the block ??
                }
            }
            sgt.setDependentOn((HashSet<SGT>) dependencies.clone());
        }

        return ;
    }



    public void updateSlotsSGT(Week_Timetable timetable) throws CloneNotSupportedException {
        //trying to update all sgt ...
        //
        for(SGT sgt : this.ASSIGNED_SGTS_CURRENTWEEK_GLOBAL){

            Duplet event = null;
            for(Duplet lectures : timetable.getSgt()){
                if(lectures.getiCode() == sgt.getiCode()){
                    event = lectures;
                    break;
                }
            }

            ArrayList<Timeslot> notAvailable =  notavailableTime_specificDay_fromSGT(sgt, timetable);
            sgt.setNotAvailableSlots(notAvailable);
            sgt.setAvailableSlots(new ArrayList<>());
            for(Hall hall : timetable.getHalls()) {
                    ArrayList<Timeslot> goodTimes = hall.findAvailableSlot_PreferredDay_sgt_remastered(900, 1.0,"", event, notAvailable);
                    sgt.getAvailableSlots().addAll(goodTimes);
            }

    }


        return;
    }


    public void updateAllDependencies(Week_Timetable timetable) throws CloneNotSupportedException {
        dependencies_for_building_graph();
        updateSlotsSGT(timetable);
        addLGTsToStudents(timetable);
    }


    //This function plays the role of the heuristics of reassigning the sgt to an open slot. That is one of the options, the other one is swapping two sgts
    //function gives evaluation in the form of HeuristicEvaluation class for each of the available slot that are redefined after all sgts have been assigned
    //second step is to get the evaluation of the swap between two
    public void searchAmongAvailableSlots_betterHeuristics(SGT sgt) throws InterruptedException {
        ArrayList<HeuristicEvaluation> evaluations = new ArrayList<>();
        HeuristicEvaluation current = heuristicEvaluation_function(sgt, 1.5, 1, 0.5);

        for(Timeslot availableSlots : sgt.getAvailableSlots()){
            evaluations.add( heuristicEvaluation_function_Changed_TimeSlot(sgt,
                                                            availableSlots.getTimePeriod().get(0).getsDay(),
                                                            availableSlots.getTimePeriod().get(0).getiTime(),
                                                            availableSlots.getTimePeriod().get(0).getiDate(),
                                                            availableSlots.getTimePeriod().get(0).getiMonth(),
                                                            availableSlots.getTimePeriod().get(0).getiYear(),
                                                    1.5, 1, 0.5) );
            Thread.sleep(0);
        }

    }

    public void testingTestingTesting(Week_Timetable timetable) throws InterruptedException {
        for(SGT sgt : this.ASSIGNED_SGTS_CURRENTWEEK_GLOBAL){
            neighbourhood_realocatingToAvailableSlot(sgt,timetable);
            //searchAmongAvailableSlots_betterHeuristics(sgt);
            neighbourhood_swappingSGTs(sgt, timetable);
        }
    }

    private int dayAvailableCheck(SGT sgt, String sDay){
        for(CoupledData data : sgt.getPreferredDays().getHeuristics()){
            if(data.getsDay().equals(sDay)){
                return 1;
            }
        }
        return 0;
    }

    //JUST A PROTOTYPE FOR NOW
    public ArrayList<Change> neighbourhood_swappingSGTs(SGT sgt,Week_Timetable timetable){
        ArrayList<HeuristicEvaluation> evaluations = new ArrayList<>();
        ArrayList<Change> swappingTwoSGTs = new ArrayList<>();
        for(SGT sgt_loop : this.ASSIGNED_SGTS_CURRENTWEEK_GLOBAL){
            if(dayAvailableCheck(sgt_loop, sgt.getsDayOfWeek()) == 1 && dayAvailableCheck(sgt, sgt_loop.getsDayOfWeek()) == 1 && checkStudents(sgt, sgt_loop.getsDayOfWeek(), 4, timetable) == 1 && checkStudents(sgt_loop, sgt.getsDayOfWeek(), 4, timetable) == 1 && sgt.getiAdditionalCode() != sgt_loop.getiAdditionalCode()) {
                if(checkStudents_TimeDependency(sgt, sgt_loop) == 0) continue;
                Change step = new Change();
                HeuristicEvaluation calcBenefit1 = (heuristicEvaluation_percentage_newTimeslot(sgt_loop, sgt.getsDayOfWeek(), sgt.getiHourScheduled(), 1.5, 1, 0.5, timetable, 0));
                HeuristicEvaluation calcBenefit2 = (heuristicEvaluation_percentage_newTimeslot(sgt, sgt_loop.getsDayOfWeek(), sgt_loop.getiHourScheduled(),1.5, 1, 0.5, timetable, 0));
                HeuristicEvaluation calcBenefit3 = (heuristicEvaluation_function_Changed_TimeSlot(sgt_loop, sgt.getsDayOfWeek(), sgt.getiHourScheduled(), sgt.getiDayScheduled(), sgt.getiMonthScheduled(), sgt.getiYearScheduled(), 1.5, 1, 0.5));
                HeuristicEvaluation calcBenefit4 = (heuristicEvaluation_function_Changed_TimeSlot(sgt, sgt_loop.getsDayOfWeek(), sgt_loop.getiHourScheduled(), sgt_loop.getiDayScheduled(), sgt_loop.getiMonthScheduled(), sgt_loop.getiYearScheduled(), 1.5, 1, 0.5));

                //    public HeuristicEvaluation heuristicEvaluation_percentage_newTimeslot(SGT event,String sDay, int iHour, double scaleForDay, double scaleForHour, double scaleCloseToHour, Week_Timetable timetable, int iOption){

                calcBenefit3.mergeHeuristicEvaluations(calcBenefit1);
                calcBenefit2.mergeHeuristicEvaluations(calcBenefit4);
                step.setSgtMain(sgt);
                step.setSgtSecondary(sgt_loop);
                step.setEvalMain(calcBenefit2);
                step.setEvalSecondary(calcBenefit3);

                //step.setSgtMain(new SGT(sgt.getsLect(), sgt.getDayAssigned(), sgt.getsLectureHall(), sgt.getiHourScheduled(), sgt.getiHours(), sgt.getiAdditionalCode(), sgt.getCodesOfStudentsAssigned(), sgt.getiDayScheduled(), sgt.getiMonthScheduled(), sgt.getiYearScheduled(), sgt.getEvaluation()));
                //step.setSgtSecondary(new SGT(sgt_loop.getsLect(), sgt_loop.getDayAssigned(), sgt_loop.getsLectureHall(), sgt_loop.getiHourScheduled(), sgt_loop.getiHours(), sgt_loop.getiAdditionalCode(), sgt_loop.getCodesOfStudentsAssigned(), sgt_loop.getiDayScheduled(), sgt_loop.getiMonthScheduled(), sgt_loop.getiYearScheduled(), sgt_loop.getEvaluation()));

                //probably more heuristics willbe added here, I do check for a day, but not for the dependencies of the second sgt
                swappingTwoSGTs.add(step);
            }
        }
        return swappingTwoSGTs;
    }

    //same functionality as dayAvailableCheck
    private int swapIsPossible_check(SGT initiator, SGT swapped, Week_Timetable timetable){


        HashMap<String, Integer> days = new HashMap<>();
        days.put("",0);
        days.put("Monday",1);
        days.put("Tuesday",2);
        days.put("Wednesday",3);
        days.put("Thursday", 4);
        days.put("Friday", 5);

        String sDayOfInitiator = initiator.getsDayOfWeek();
        String sDayOfSwapped = swapped.getsDayOfWeek();
        int dayInitiator_Lecture = days.get(findDayOfLecture_By_name(timetable, initiator.getsLect()));
        int daySwapped_Lecture = days.get(findDayOfLecture_By_name(timetable, swapped.getsLect()));
        int dayInitiator_sgt = days.get(sDayOfInitiator);
        int daySwapped_sgt = days.get(sDayOfSwapped);

        if(dayInitiator_Lecture > daySwapped_sgt){return 0;}
        if(daySwapped_Lecture > dayInitiator_sgt){return 0;}
        return 1;
    }

    public void updateSGT_Time(SGT sgt, String sDay,int iHour,String sHall, Week_Timetable timetable) throws CloneNotSupportedException {

        int iDate = 0;
        int iMonth = 0;//+1
        int iYear = 0;//+1900

        for (int f = 0; f < timetable.getWeekTimet().size(); f++) {                 //add to the Day log, for printing purposes + check
            if (timetable.getWeekTimet().get(f).getSname().toLowerCase(Locale.ROOT).equals(sgt.getsDayOfWeek().toLowerCase(Locale.ROOT))) {
                timetable.getWeekTimet().get(f).v_removeEvent(sgt);
                break;
            }
        }

        for (int f = 0; f < timetable.getWeekTimet().size(); f++) {                 //add to the Day log, for printing purposes + check
            if (timetable.getWeekTimet().get(f).getSname().toLowerCase(Locale.ROOT).equals(sDay.toLowerCase(Locale.ROOT))) {
                 iDate = timetable.getWeekTimet().get(f).getiDate();
                 iMonth = timetable.getWeekTimet().get(f).getiMonth();//+1
                 iYear = timetable.getWeekTimet().get(f).getiYear();//+1900
                timetable.getWeekTimet().get(f).v_assignEvent(iHour, 1.0, sHall, "", sgt.getsLect());
                break;
            }
        }

        //following lines are not needed,the update of the lecture for each student happens automatically with the
        //change of the parameters of the sgt, as students hold it as a reference
//        SGT temp = (SGT) sgt.clone();
//        sgt.setsDayOfWeek(sDay);
//        sgt.setiHourScheduled(iHour);
//        sgt.setsLectureHall(sHall);
//        for(int id : sgt.getCodesOfStudentsAssigned()){
//            Student student = STUDENTS_TO_BE_ASSINGED_GLOBAL.get(id);
//            for(SGT assigned : student.getCourses()){
//                student.getCourses().remove(temp);          //courses here again doesn't mean the lectures, but the small group tutorials
//                student.addToCourses((SGT)sgt.clone());
//            }
//        }


    }


    public void addLecturesToStudents(Week_Timetable timetable){
        Iterator iterator = this.STUDENTS_TO_BE_ASSINGED_GLOBAL.values().iterator();
        for(int i =0; i < this.STUDENTS_TO_BE_ASSINGED_GLOBAL.size(); i++){
            Student student = (Student) iterator.next();
            ArrayList<Integer> lecturecodes = new ArrayList<>(student.getCoursesCodes());
            ArrayList<Duplet> duplets = lecturesOfStudentsFromOnlyCodes(lecturecodes, timetable);
            for(Duplet duplet : duplets){
                student.addToAssignedCourses(duplet);
            }
        }
    }

    public void removeLecturesFromStudents(Week_Timetable timetable){
        Iterator iterator = this.STUDENTS_TO_BE_ASSINGED_GLOBAL.values().iterator();
        for(int i =0; i < this.STUDENTS_TO_BE_ASSINGED_GLOBAL.size(); i++){
            Student student = (Student) iterator.next();
            ArrayList<Integer> lecturecodes = new ArrayList<>(student.getCoursesCodes());
            ArrayList<Duplet> duplets = lecturesOfStudentsFromOnlyCodes(lecturecodes, timetable);
            for(Duplet duplet : duplets){
                student.removeFromAssignedCourses(duplet);
            }
        }
    }

    private void addLGTsToStudents(Week_Timetable timetable) {
        if(!timetable.getSgt().isEmpty()){

            Iterator iterator = this.STUDENTS_TO_BE_ASSINGED_GLOBAL.values().iterator();
            for(int i =0; i < this.STUDENTS_TO_BE_ASSINGED_GLOBAL.size(); i++){
                Student student = (Student) iterator.next();
                student.setAssignedLGT(new ArrayList<>());
            }


        }
        Iterator iterator = this.STUDENTS_TO_BE_ASSINGED_GLOBAL.values().iterator();
        for(int i =0; i < this.STUDENTS_TO_BE_ASSINGED_GLOBAL.size(); i++){
            Student student = (Student) iterator.next();
            ArrayList<Integer> lecturecodes = new ArrayList<>(student.getCoursesCodes());
            ArrayList<Duplet> duplets = LGTsOfStudentsFromOnlyCodes(lecturecodes, timetable);
            for(Duplet duplet : duplets){
                student.getAssignedLGT().add(duplet);
            }
        }
    }


    public void takeASolutionSnap_initial(){

        HeuristicEvaluation snapHeuristics  = evaluateSolution();
        INITIAL_SOLUTION.setassigned_SGTs(this.ASSIGNED_SGTS_CURRENTWEEK_GLOBAL);
        INITIAL_SOLUTION.setUnassignedSGTs(this.NOT_ASSIGNED_SGTS_CURRENTWEEK_GLOBAL);
        INITIAL_SOLUTION.setHeuristicEvaluation(snapHeuristics);

    }


    public void takeASolutionSnap(){

        HeuristicEvaluation snapHeuristics  = evaluateSolution();
        CURRENT_SOLUTION.setassigned_SGTs(this.ASSIGNED_SGTS_CURRENTWEEK_GLOBAL);
        CURRENT_SOLUTION.setUnassignedSGTs(this.NOT_ASSIGNED_SGTS_CURRENTWEEK_GLOBAL);
        CURRENT_SOLUTION.setHeuristicEvaluation(snapHeuristics);

    }


    public Solution takeASolutionSnap_object(){
        Solution ss = new Solution();
        HeuristicEvaluation snapHeuristics  = evaluateSolution();
        ss.setassigned_SGTs(this.ASSIGNED_SGTS_CURRENTWEEK_GLOBAL);
        ss.setUnassignedSGTs(this.NOT_ASSIGNED_SGTS_CURRENTWEEK_GLOBAL);
        ss.setHeuristicEvaluation(snapHeuristics);
        return ss;
    }


    private void swap(SGT sgtInitial, SGT sgtSwapped){
        //update the date
        //update the students -- automatic
        //update the Day -- probably not doable
        if(sgtInitial.getsLect().equals("4CCS1PRP") && sgtSwapped.getsLect().equals("5CCS2ENM")){
            int hello = 0;
        }

        int sgtInitialHourTemp = sgtInitial.getiHourScheduled();
        String sgtInitialSDay = sgtInitial.getDayAssigned();
        int sgtInitialDayTemp = sgtInitial.getiDayScheduled();
        int sgtInitialMonthTemp = sgtInitial.getiMonthScheduled();
        int sgtInitialYearTemp = sgtInitial.getiYearScheduled();
        String sgtInitialSHall = sgtInitial.getsLectureHall();


        sgtInitial.setiHourScheduled(sgtSwapped.getiHourScheduled());
        sgtInitial.setDayAssigned(sgtSwapped.getDayAssigned());
        sgtInitial.setsDayOfWeek(sgtSwapped.getsDayOfWeek());
        sgtInitial.setiDayScheduled(sgtSwapped.getiDayScheduled());
        sgtInitial.setiMonthScheduled(sgtSwapped.getiMonthScheduled());
        sgtInitial.setiYearScheduled(sgtSwapped.getiYearScheduled());
        sgtInitial.setsLectureHall(sgtSwapped.getsLectureHall());
        //sgtInitial.setEvaluation();

        sgtSwapped.setiHourScheduled(sgtInitialHourTemp);
        sgtSwapped.setDayAssigned(sgtInitialSDay);
        sgtSwapped.setsDayOfWeek(sgtInitialSDay);
        sgtSwapped.setiDayScheduled(sgtInitialDayTemp);
        sgtSwapped.setiMonthScheduled(sgtInitialMonthTemp);
        sgtSwapped.setiYearScheduled(sgtInitialYearTemp);
        sgtSwapped.setsLectureHall(sgtInitialSHall);
        //sgtSwapped.setEvaluation();


    }

    private void reassingSGTEvent(Week_Timetable timeable , SGT resgt, String sDay,String sHall,  int iHour,int iDay, int iMonth, int iYear){

        resgt.setiHourScheduled(iHour);
        resgt.setsDayOfWeek(sDay);
        resgt.setiDayScheduled(iDay);
        resgt.setiMonthScheduled(iMonth);
        resgt.setiYearScheduled(iYear);
        for(Hall halls : timeable.getHalls()){
            if(halls.getsAbbrev().equals(resgt.getsLectureHall())){
                halls.setAvToOne(resgt.getiHourScheduled(), (int) (resgt.getiHourScheduled() + 1.0), resgt.getDayAssigned());
            }
            if(halls.getsAbbrev().equals(sHall)){
                halls.setAvToZero(iHour ,(int)( iHour + 1.0), sDay );
            }
        }
        resgt.setsLectureHall(sHall);

    }


    private double calculation_slotChange_stepBenefit(Change stepEval){

        if(stepEval.getSgtMain().getEvaluation().getPercentSatisfiability() >=  this.percentageScale*stepEval.getEvalSecondary().getPercentSatisfiability()){
            //if I do not want the percentage to be the main factor of evaluation I could lower down the percentageScale
        }
        return 0.0;
    }

    private double calculation_slotChange_stepBenefit_simple(Change stepEval){
        //calculation based on the percentage and scaledHeuristics
        if(stepEval.getSgtMain().getEvaluation().getPercentSatisfiability() <  stepEval.getEvalSecondary().getPercentSatisfiability()){
            return stepEval.getEvalSecondary().getdHeuristics() - stepEval.getSgtMain().getEvaluation().getdHeuristics();
        }
        //return stepEval.getEvalSecondary().getdHeuristics() - stepEval.getSgtMain().getEvaluation().getdHeuristics();
        return 0.0;

    }


    private double calculation_swap_stepBenefit_simple(Change stepEval){
        //calculation based on the percentage and scaledHeuristics
        if(stepEval.getSgtMain().getEvaluation().getPercentSatisfiability() + stepEval.getSgtSecondary().getEvaluation().getPercentSatisfiability()
                <  stepEval.getEvalMain().getPercentSatisfiability() + stepEval.getEvalSecondary().getPercentSatisfiability())
        {
            return stepEval.getEvalMain().getdHeuristics() + stepEval.getEvalSecondary().getdHeuristics() - (stepEval.getSgtMain().getEvaluation().getdHeuristics() + stepEval.getSgtSecondary().getEvaluation().getdHeuristics());
        }
        return 0.0;
    }

    private double stepBenefit(Change steptoEvaluate){
            //to evaluate
        if(steptoEvaluate.getSgtSecondary() == null){
            //we are calculating the change to a different time during the week
            //to be more accurate and to cut the memory used the Change class copies the content of the sgt and stores it as a new object
            //will have to calculate from the two HeuristicEvaluations inside it:   inside the sgtmain and the supplied HE with the new day and time
            //so I am left with sgtMain and evalSecondary in the HE class

            //scenarios:
            //1. Beneficial step : all of the heuristics are improved, after calculation the step is positive
            //2. Detrimental step : all the heuristics are worsened, after calculation the step is negative
            //3. Nonbeneficial step: all the heuristics are the same, not a useful step
            //4. Indecisive step: some of the heuristics are improved while others are not, depending on the algorithm used after calculation it is negative/positive/nonbeneficial
            return (double) calculation_slotChange_stepBenefit_simple(steptoEvaluate);
        }
        else{
            return (double) calculation_swap_stepBenefit_simple(steptoEvaluate);
        }
    }


    private ArrayList<Change> completeNeighbourhood(Week_Timetable timetable){
        ArrayList<Change> stepsAvailable = new ArrayList<>();
        for(SGT sgt : this.ASSIGNED_SGTS_CURRENTWEEK_GLOBAL){
            ArrayList<Change> steps1 = neighbourhood_realocatingToAvailableSlot(sgt,timetable);
            ArrayList<Change> steps2 = neighbourhood_swappingSGTs(sgt, timetable);
            stepsAvailable.addAll(steps1);
            stepsAvailable.addAll(steps2);
        }
        return stepsAvailable;
    }

    //build the function to test the heuristics of all available slots
    public ArrayList<Change> neighbourhood_realocatingToAvailableSlot(SGT sgt, Week_Timetable timetable){
        ArrayList<Change> steps = new ArrayList<>();
        for(Timeslot available : sgt.getAvailableSlots()){
            //available.get
            if(checkStudents(sgt, available.getTimePeriod().get(0).getsDay(),4,timetable) != 0) {
                Change change = new Change();
                Timeperiod temp = available.getTimePeriod().get(0);
                HeuristicEvaluation stepEval = heuristicEvaluation_function_Changed_TimeSlot(sgt, temp.getsDay(), temp.getiTime(), temp.getiDate(), temp.getiMonth(), temp.getiYear(), 1.5, 1, 0.5);
                HeuristicEvaluation stepEval_percentage = heuristicEvaluation_percentage_newTimeslot(sgt, temp.getsDay(), temp.getiTime(), 1.5, 1, 0.5, timetable, 0);
                stepEval.setiHour(temp.getiTime());
                stepEval.setsDayOfWeek(temp.getsDay());
                stepEval.setsHall(available.getsHall());
                stepEval.setiDate(temp.getiDate());
                stepEval.setiMonth(temp.getiMonth());
                stepEval.setiYear(temp.getiYear());
                stepEval.mergeHeuristicEvaluations(stepEval_percentage);
                change.setEvalSecondary(stepEval);
                //change.setSgtMain(new SGT(sgt.getsLect(), sgt.getDayAssigned(), sgt.getsLectureHall(), sgt.getiHourScheduled(), sgt.getiHours(), sgt.getiAdditionalCode(), sgt.getCodesOfStudentsAssigned(), sgt.getiDayScheduled(), sgt.getiMonthScheduled(), sgt.getiYearScheduled()));
                change.setSgtMain(sgt);
                //change.setEvalMain(sgt.getEvaluation());
                change.setsDaySecondary(temp.getsDay());
                change.setiHourSecondary(temp.getiTime());
                steps.add(change);
            }
        }
        return steps;
    }

    public void implementChange(Week_Timetable timetable, Change change){
        if(change.getSgtSecondary() == null){
            HeuristicEvaluation evaluation = change.getEvalSecondary();
            reassingSGTEvent(timetable,change.getSgtMain(), evaluation.getsDayOfWeek(), evaluation.getsHall(), evaluation.getiHour(), evaluation.getiDate(), evaluation.getiMonth(), evaluation.getiYear());
        }else{
            swap(change.getSgtMain(), change.getSgtSecondary());
        }
    }


    public Change copyChange(Change iterated) throws CloneNotSupportedException {
        Change toReturn = (Change) iterated.clone();
        if(iterated.getSgtMain() != null){
            SGT sgt = iterated.getSgtMain();
            //change.setSgtMain(new SGT(sgt.getsLect(), sgt.getDayAssigned(), sgt.getsLectureHall(), sgt.getiHourScheduled(), sgt.getiHours(), sgt.getiAdditionalCode(), sgt.getCodesOfStudentsAssigned(), sgt.getiDayScheduled(), sgt.getiMonthScheduled(), sgt.getiYearScheduled()));
            toReturn.setSgtMain(new SGT(sgt.getsLect(), sgt.getDayAssigned(), sgt.getsLectureHall(), sgt.getiHourScheduled(), sgt.getiHours(), sgt.getiAdditionalCode(), sgt.getCodesOfStudentsAssigned(), sgt.getiDayScheduled(), sgt.getiMonthScheduled(), sgt.getiYearScheduled(), (HeuristicEvaluation) sgt.getEvaluation().clone()));
        }
        if(iterated.getSgtSecondary() != null){
            SGT sgt = iterated.getSgtSecondary();
            //change.setSgtMain(new SGT(sgt.getsLect(), sgt.getDayAssigned(), sgt.getsLectureHall(), sgt.getiHourScheduled(), sgt.getiHours(), sgt.getiAdditionalCode(), sgt.getCodesOfStudentsAssigned(), sgt.getiDayScheduled(), sgt.getiMonthScheduled(), sgt.getiYearScheduled()));
            toReturn.setSgtSecondary(new SGT(sgt.getsLect(), sgt.getDayAssigned(), sgt.getsLectureHall(), sgt.getiHourScheduled(), sgt.getiHours(), sgt.getiAdditionalCode(), sgt.getCodesOfStudentsAssigned(), sgt.getiDayScheduled(), sgt.getiMonthScheduled(), sgt.getiYearScheduled(), (HeuristicEvaluation) sgt.getEvaluation().clone()));
        }

        return toReturn;
    }

    private void hillClimbing(Week_Timetable timetable, int iITeration, double scaleForDay, double scaleForHour, double scaleCloseToHour) throws CloneNotSupportedException, InterruptedException {
        //previous to running this algorithm, I have had the SGTs assigned, the dependencies for each one of them built, also their HE(Heursticevailation) attached; all students have their preferences, assigned SGTs and also the lectures(lectures are not used however)
        Solution solutionIterated = new Solution();
        Solution bestSolutionFound = new Solution();
        ArrayList<Change> visited = new ArrayList<>();
        ArrayList<Change> allpossibleSteps = new ArrayList<>();

        takeASolutionSnap_initial();
        this.INITIAL_SOLUTION.normalize();
        System.out.println(this.INITIAL_SOLUTION);
        allpossibleSteps = completeNeighbourhood(timetable);
        for(Change change : allpossibleSteps){
                change.setFinalEffect(stepBenefit(change));
        }
        Collections.sort(allpossibleSteps);

        int iCounter = 0;
        if(!allpossibleSteps.isEmpty())
        while(allpossibleSteps.get(0).getFinalEffect() > 1  ){
            iCounter++;


            //very optimistic algorithm, looking for the local minimum
            Change dataCopy = copyChange(allpossibleSteps.get(0));
//            System.out.println(dataCopy.toString() + " ICOUNTER : " + iCounter);
//            System.out.println();


            implementChange(timetable, allpossibleSteps.get(0));               //later on to be subbed with more complex algo
            updateAllDependencies(timetable);
            visited.add(dataCopy);
            calculationHeuristicsArray(scaleForDay,scaleForHour,scaleCloseToHour, timetable);


            solutionIterated = takeASolutionSnap_object();

            //https://www.w3schools.com/java/ref_string_compareto.asp
            if(bestSolutionFound.compareTo(solutionIterated)  < 0){
                bestSolutionFound = solutionIterated;
                takeASolutionSnap();
//                System.out.println(bestSolutionFound);
            }


            allpossibleSteps = completeNeighbourhood(timetable);
            for(Change change : allpossibleSteps){
                change.setFinalEffect(stepBenefit(change));
            }

            Collections.sort(allpossibleSteps);
            //pick the best choice at each step, test , implement and start over
            //for testing purposes let's implement the first , take a snapshot of the solution and update
            if(allpossibleSteps.isEmpty()) break;

        }
        bestSolutionFound.normalize();
        System.out.println(bestSolutionFound);

        Thread.sleep(0);
    }



    private void hillClimbing_sgt(Week_Timetable timetable, double iScale, int iCondition,int iCondition_UnusedDays, int iNumberOfIterations, int minNumForTutorials, int maxNumForTutorials, double scaleForDay, double scaleForHour, double scaleCloseToHour) throws SQLException, ParseException, CloneNotSupportedException, IOException, InterruptedException {

//        ArrayList<DataSetStudents> data = getHCData(1);
//        ArrayList<DataSetStudents> data2 = getHCData(2);
//        ArrayList<DataSetStudents> data3 = getHCData(3);
//        this.data = data;
//        this.data.addAll(data2);                                                                                     //global data of all the students and their variations of courses and preferences
//        this.data.addAll(data3);


        // Week_Timetable nextWeek = sortOutWeek_nextWeek(grdAlg.getWeek_timetable_ont());
        // Week_Timetable nextnextWeek = sortOutWeek_nextWeek(grdAlg.getWeek_timetable_spare());
        weekNumber++;
        timetable.setiWeekNum(weekNumber);

        /*
        this.ASSIGNED_SGTS_CURRENTWEEK_GLOBAL = new ArrayList<>();
        this.STUDENTS_TO_BE_ASSINGED_GLOBAL = new HashMap<>();
        //the code on the previous lines should be used, but there has to be a way to keep all my unassigned stuff, also the assigend most probably
         */

//        this.ASSIGNED_SGTS_CURRENTWEEK_GLOBAL = new ArrayList<>();
//        this.STUDENTS_TO_BE_ASSINGED_GLOBAL = new HashMap<>();

        extractSGTsFromLGTs(timetable);
        updateTimeline_SGT(timetable);
        heuristics_sgt_preferreddays(timetable, iScale);
        heuristics_sgt_hours(timetable, data ,iScale);
        removeUnusableDays(timetable);

        if(iCondition_UnusedDays != 0) {
            removeHeuristicDaysWithNull(timetable);
        }

        if(iCondition == 1){
            reorderHeuristics_days(timetable);
        }

        timetable.setHalls(grdAlg.hallsAvailability_Tutorials(timetable.getsStartDay(), timetable.getsEndDay()));

        //this algorithm can be run without trying to comply to soft cinstraints
        // apply the same thing for years 1 and 2
        algorithmAssigning_5050filtering(timetable, iNumberOfIterations,minNumForTutorials, maxNumForTutorials , 1);                                  //apply the same thing for years 1 and 2
        algorithmAssigning_5050filtering(timetable, iNumberOfIterations,minNumForTutorials, maxNumForTutorials , 2);                                  //apply the same thing for years 1 and 2
        algorithmAssigning_5050filtering(timetable, iNumberOfIterations,minNumForTutorials, maxNumForTutorials , 3);                                  //apply the same thing for years 1 and 2
        updateAllDependencies(timetable);                         //writes to stidents' assignedSGT
        //take a snap of the current solution/starting point;

        if(!this.ASSIGNED_SGTS_CURRENTWEEK_GLOBAL.isEmpty()){
            calculationHeuristicsArray(scaleForDay, scaleForHour, scaleCloseToHour,timetable);                                              //heuristics calculated for each sgt that has been assigned
            takeASolutionSnap_initial();
            //testingTestingTesting(timetable);
            //run the HC algorithm
            hillClimbing(timetable, 3,scaleForDay, scaleForHour, scaleCloseToHour);

        }
        addLecturesToStudents(timetable);
        printPersonalSchedules(weekNumber,timetable);

    }

    //Function to not allow more than fixed number of tutorials per day
    //mind that this will traverse the lectures as well
    public int checkStudents(SGT sgt, String sDay, int iMaxNumPerDay, Week_Timetable timetable){
       for(int kingscode : sgt.getCodesOfStudentsAssigned()){
           Student student = this.STUDENTS_TO_BE_ASSINGED_GLOBAL.get(kingscode);
           int iCounter = 0;
           int iLectures = 0;
           for(SGT assigned : student.getCourses()){
               if(assigned.getsDayOfWeek().equals(sDay) && !assigned.getsLect().equals(sgt.getsLect())){            //if the checked Day matches and the sgt doesn't count itself
                   iCounter++;
               }
           }

           //the following loop may fail when initially assigning since I only add the lectures after the assigning of the sgts,
           // kind of all update with the dependencies and available slots left for the sgt, function is updateAllDependencies()
           for(Duplet lectures : student.getAssignedSGT()){
               if(lectures.getsDayOfWeek().equals(sDay)){
                   iLectures++;
               }
           }
           //so I will use the preferences of each student to find when the lectures are assigned
           if(student.getAssignedSGT().size() == 0) {
               for (DataSetStudents searchForLectures : student.getPreferences()) {
                   String sLectureDay = findDayOfLecture_By_name(timetable, searchForLectures.getAbbrev());
                   if (sLectureDay.equals(sDay)) {
                       iLectures++;
                   }
               }
           }
           //I can have a max of two lectures per day and a max of 4 events per day lectures + tutorials

           if(iCounter + iLectures >= iMaxNumPerDay) return 0;
           //if(iCounter > iMaxNumPerDay) return 0;     //allows + 1
       }
       return 1;
    }


//    public int checkStudents_TimeDependency(SGT sgtInitial, SGT sgtSecondary){
//        //additional calculation to assure that the two sgts do not overlap an event once they have been swapped
//        for(int kingsCode : sgtInitial.getCodesOfStudentsAssigned()){
//            Student student = this.STUDENTS_TO_BE_ASSINGED_GLOBAL.get(kingsCode);
//            for(SGT assigned : student.getCourses()){
//                if(assigned.getsLect().equals(sgtInitial.getsLect())) continue;
//                if(assigned.getsDayOfWeek().equals(sgtSecondary.getsDayOfWeek())){
//                    if(assigned.getiHourScheduled() >= sgtSecondary.getiHourScheduled() && assigned.getiHourScheduled() < sgtSecondary.getiHourScheduled() + sgtSecondary.getiHours()*100){
//                        //overlap of the event being swapped occurs:
//                        return 0;
//                    }
//                }
//            }
//
//        }
//
//
//        for(int kingsCode : sgtSecondary.getCodesOfStudentsAssigned()){
//            Student student = this.STUDENTS_TO_BE_ASSINGED_GLOBAL.get(kingsCode);
//            for(SGT assigned : student.getCourses()){
//                if(assigned.getsLect().equals(sgtSecondary.getsLect())) continue;
//                if(assigned.getsDayOfWeek().equals(sgtInitial.getsDayOfWeek())){
//                    if(assigned.getiHourScheduled() >= sgtInitial.getiHourScheduled() && assigned.getiHourScheduled() < sgtInitial.getiHourScheduled() + sgtInitial.getiHours()*100){
//                        //overlap of the event being reassigned occurs:
//                        return 0;
//                    }
//                }
//            }
//
//        }
//
//
//        return 1;
//    }

    //updated:
    public int checkStudents_TimeDependency(SGT sgtInitial, SGT sgtSecondary){
        //additional calculation to assure that the two sgts do not overlap an event once they have been swapped
//        if(sgtInitial.getsLect().equals("4CCS1PRP") && sgtSecondary.getsLect().equals("5CCS2ENM")){
//            int hello = 0;
//        }

        if(overlap_lecture(sgtInitial, sgtSecondary.getiHourScheduled(), sgtSecondary.getiHours() ,sgtSecondary.getsDayOfWeek()) == 1) return 0;
        if(overlap_lecture(sgtSecondary, sgtInitial.getiHourScheduled(), sgtInitial.getiHours() ,sgtInitial.getsDayOfWeek()) == 1) return 0;
        //


        for(int kingsCode : sgtInitial.getCodesOfStudentsAssigned()){
            Student student = this.STUDENTS_TO_BE_ASSINGED_GLOBAL.get(kingsCode);
            for(SGT assigned : student.getCourses()){
                if(assigned.getsLect().equals(sgtInitial.getsLect())) continue;
                if(assigned.getsDayOfWeek().equals(sgtSecondary.getsDayOfWeek())){
//                    if(assigned.getiHourScheduled() >= sgtSecondary.getiHourScheduled() && assigned.getiHourScheduled() < sgtSecondary.getiHourScheduled() + sgtSecondary.getiHours()*100){
//                        //overlap of the event being swapped occurs:
//
//                        return 0;
//                    }
                    if(overlaps_time(sgtInitial, assigned) == 1){
                        return 0;
                    }
                }
            }

        }


        for(int kingsCode : sgtSecondary.getCodesOfStudentsAssigned()){
            Student student = this.STUDENTS_TO_BE_ASSINGED_GLOBAL.get(kingsCode);
            for(SGT assigned : student.getCourses()){
                if(assigned.getsLect().equals(sgtSecondary.getsLect())) continue;
                if(assigned.getsDayOfWeek().equals(sgtInitial.getsDayOfWeek())){
//                    if(assigned.getiHourScheduled() >= sgtInitial.getiHourScheduled() && assigned.getiHourScheduled() < sgtInitial.getiHourScheduled() + sgtInitial.getiHours()*100){
//                        //overlap of the event being reassigned occurs:
//                        //added this following line after submitting
//                        if(assigned.getiHourScheduled() + 100*assigned.getiHours() >= sgtInitial.getiHourScheduled() && assigned.getiHourScheduled() < sgtInitial.getiHourScheduled()+ sgtInitial.getiHours()*100 )
//                        return 0;
//                    }
//                    if(assigned.getiHourScheduled() + 100*assigned.getiHours() >= sgtInitial.getiHourScheduled() && assigned.getiHourScheduled() < sgtInitial.getiHourScheduled()+ sgtInitial.getiHours()*100 ){
//                        return 0;
//                    }
                    if(overlaps_time(sgtSecondary, assigned) == 1){
                        return 0;
                    }

                }
            }


        }


        return 1;
    }

    private ArrayList<Integer> codesFromDataSetStudents(ArrayList<DataSetStudents> students){
        ArrayList<Integer> toReturn = new ArrayList<>();
        for(DataSetStudents setStudents : students){
            toReturn.add(setStudents.getKings_id());
        }
        return toReturn;
    }

    public void printPersonalSchedules(int WeekNum, Week_Timetable timetable) throws IOException, CloneNotSupportedException {
        Iterator iterator = this.STUDENTS_TO_BE_ASSINGED_GLOBAL.values().iterator();
        for(int i= 0; i < this.STUDENTS_TO_BE_ASSINGED_GLOBAL.size();i++){
            Student student = (Student) iterator.next();
            student.generate_and_addToFile(WeekNum, timetable);              //ONLY 0 FOR TESTING !!!!!!!!!!!!!!!
        }
    }


    public void clearAllData(){

        this.ASSIGNED_SGTS_CURRENTWEEK_GLOBAL = new ArrayList<>();
        this.STUDENTS_TO_BE_ASSINGED_GLOBAL = new HashMap<>();
        this.ASSIGNED_SGTS_CURRENTWEEK_GLOBAL = new ArrayList<>();
        this.NOT_ASSIGNED_SGTS_CURRENTWEEK_GLOBAL = new ArrayList<>();
        this.NOT_ASSIGNED_STUDENTS_GLOBAL = new HashMap<>();
        this.TIMETABLEONT_GLOBAL = new Week_Timetable();
        this.TIMETABLETWO_GLOBAL = new Week_Timetable();

    }


    public void combinationsResult(ArrayList<DataSetStudents> students, ArrayList<DataSetStudents> toAssign,int size){
        //if the combinations do not take too much time -> calculating 31 million combinations would take at most 6 minutes
        int st2 = 0;
        for (int st = 0; st < students.size(); st++) {
            st2 = st;
            students.get(st).setNumberforCombinations(st);
        }


        ArrayList<DataSetStudents> filtered = new ArrayList<>();
        ArrayList<DataSetStudents[]> filtered2 = generate_myGenerate(st2 + 1, size, students);        //find the combination of students suitable for my needs(lowest/highest number of lecture code dependencies)
        ArrayList<Integer> lowestFound = lectureCodesOfStudents(new ArrayList<>(Arrays.asList(filtered2.get(0))), toAssign);
        ArrayList<Integer> iteration = new ArrayList<>();

        for (int k = 1; k < filtered2.size(); k++) {           //traverse the combinations
            ArrayList<DataSetStudents> combination = new ArrayList<>(Arrays.asList(filtered2.get(k)));

            iteration = lectureCodesOfStudents(combination, toAssign);
            if (iteration.size() < lowestFound.size()) {
                lowestFound = iteration;
                filtered = combination;
            }

        }

        for(Integer id : lowestFound){
            for(DataSetStudents retrieve : toAssign){
                if(retrieve.getKings_id() == id){
                    filtered.add(retrieve);
                }
            }
        }

        students = new ArrayList<>(filtered);

    }

    //develop if more time
    public void pleaseAssignMe(Duplet event, ArrayList<DataSetStudents> students){
        for(SGT alreadyAssignedSGTs : this.ASSIGNED_SGTS_CURRENTWEEK_GLOBAL){
            if(alreadyAssignedSGTs.getsLect().equals(event.getsLect())){
                for(DataSetStudents dataSetStudents : students){
                    Student student = this.STUDENTS_TO_BE_ASSINGED_GLOBAL.get(dataSetStudents.getKings_id());
                    for(SGT dependencies : student.getCourses()){}
                }
            }
        }
    }


    ////===============================================================


    //couldn't implemen maximum number of students in time
    public void algorithmAssigning_5050filtering(Week_Timetable timetable, int iPredefinedIterations, int minNumForTutorials,int maxNumForTutorials,  int iYearOfStudy) throws SQLException, CloneNotSupportedException, ParseException {


        int iLectureAssignedSuccessfully  = 0;
        ArrayList<DataSetStudents> toAssign = getHCData(iYearOfStudy, timetable.getsTableStudents(), timetable.getsTableCoures());
        //I need to pass the students actually I want for the testing, not all of them every time
        ArrayList<DataSetStudents> tempAssign =  new ArrayList<>();         // I need the list of duplets plus all their details for week2s
        ArrayList<Duplet> sgts = new ArrayList<>();

        String courseAbrev = "";

        if(iYearOfStudy == 1){courseAbrev = "4CCS";}
        if(iYearOfStudy == 2){courseAbrev = "5CCS";}
        if(iYearOfStudy == 3){courseAbrev = "6CCS";}



        for(Duplet event : timetable.getSgt()){
            if(event.getsLect().startsWith(courseAbrev)){
                sgts.add(event);
            }
        }

        HashMap<String, ArrayList<Duplet>> sorterOfSGTs = new HashMap<>();

        for(Duplet event : sgts){
            if(event.getsDayOfWeek().equals("Monday")){
                if(sorterOfSGTs.get("Monday") == null){
                    sorterOfSGTs.put("Monday", new ArrayList<>());
                    sorterOfSGTs.get("Monday").add(event);
                    continue;
                }else{
                    sorterOfSGTs.get("Monday").add(event);
                    continue;
                }
            }
            if(event.getsDayOfWeek().equals("Tuesday")){
                if(sorterOfSGTs.get("Tuesday") == null){
                    sorterOfSGTs.put("Tuesday", new ArrayList<>());
                    sorterOfSGTs.get("Tuesday").add(event);
                    continue;
                }else{
                    sorterOfSGTs.get("Tuesday").add(event);
                    continue;
                }
            }
            if(event.getsDayOfWeek().equals("Wednesday")){
                if(sorterOfSGTs.get("Wednesday") == null){
                    sorterOfSGTs.put("Wednesday", new ArrayList<>());
                    sorterOfSGTs.get("Wednesday").add(event);
                    continue;
                }else{
                    sorterOfSGTs.get("Wednesday").add(event);
                    continue;
                }
            }
            if(event.getsDayOfWeek().equals("Thursday")){
                if(sorterOfSGTs.get("Thursday") == null){
                    sorterOfSGTs.put("Thursday", new ArrayList<>());
                    sorterOfSGTs.get("Thursday").add(event);
                    continue;
                }else{
                    sorterOfSGTs.get("Thursday").add(event);
                    continue;
                }
            }
            if(event.getsDayOfWeek().equals("Friday")){
                if(sorterOfSGTs.get("Friday") == null){
                    sorterOfSGTs.put("Friday", new ArrayList<>());
                    sorterOfSGTs.get("Friday").add(event);
                    continue;
                }else{
                    sorterOfSGTs.get("Friday").add(event);
                    continue;
                }
            }
        }

        ArrayList<Duplet> dupletArrayListSorted = new ArrayList<>();
        if(sorterOfSGTs.get("Friday") != null){dupletArrayListSorted.addAll(sorterOfSGTs.get("Friday"));}
        if(sorterOfSGTs.get("Thursday") != null){dupletArrayListSorted.addAll(sorterOfSGTs.get("Thursday"));}
        if(sorterOfSGTs.get("Wednesday") != null){dupletArrayListSorted.addAll(sorterOfSGTs.get("Wednesday"));}
        if(sorterOfSGTs.get("Tuesday") != null){dupletArrayListSorted.addAll(sorterOfSGTs.get("Tuesday"));}
        if(sorterOfSGTs.get("Monday") != null){dupletArrayListSorted.addAll(sorterOfSGTs.get("Monday"));}


        sgts = (ArrayList<Duplet>) dupletArrayListSorted.clone();
        dupletArrayListSorted = null;
        sorterOfSGTs = null;
        int secondaryAdditionalCode = 0;
        for(Duplet event : sgts){ //start assigning each event
            ArrayList<Integer> codesOfLectures = new ArrayList<>();             //I NEED TO FILL THIS ARRAY AS THOSE ARE THE DEPENDENCIES, each event holds the dependencies of the lecture,
            // currently due to the large number of students and variations, each event holds as dependencies all the other lectures in the same year, so I could use this to check for
            secondaryAdditionalCode++;
            Duplet temp = (Duplet) event.clone();
            SGT temp_sgt = new SGT();
            tempAssign = findInData(toAssign, event.getsLect());                    //those are the students to be assigned





            for(int numIterations = 0; numIterations < iPredefinedIterations; numIterations++) {

                int iHallFound = 0;
                temp_sgt = new SGT();
                temp_sgt.setPreferredDays(event.getPreferredDays());
                temp_sgt.setiCode(temp.getiCode());
                temp_sgt.setiHours(1);
                temp_sgt.setsLect(temp.getsLect());
                secondaryAdditionalCode++;
                temp_sgt.setiAdditionalCode(secondaryAdditionalCode);


                for (CoupledData heuristicsData : event.getPreferredDays().getHeuristics()) {

                    if (tempAssign.size() == 0) break;
                    if (iHallFound == 1) break;                                                 //will beginfrom the start every time a suitable hall is found for the current iteration
                    String sDayIteration = heuristicsData.getsDay();
                    ArrayList<DataSetStudents> students = findInDataByDay(tempAssign, event.getsLect(), sDayIteration);


                    for (Hall halls : timetable.getHalls()) {                     //test for each day the availability if not available proceed to assigning toa free slot,if there are available

                        if(students.isEmpty()) break;
                        if(students.size() < minNumForTutorials) break;
                        if(iHallFound == 1) break;
                        if(students.isEmpty()) break;       //add if hall is the last hall and not found then break;

                        if (students.size() > halls.getiCapacity()) {

//                            if (students.size() == halls.getiCapacity() && availableForFiltering(students.size(), halls.getiCapacity()) < 20030010L) { //NEEDS TO BE REVIEWED// SO THAT NO MEMORY OVERFLOW WOULD OCCUR//FILTER ADDED HERE TO CHOSE WHO OF THE STUDENTS WILL GET ASSIGNED, ONLY WORKS ON SPECIFIC SIZES BEFORE EXPONENTIAL GROWTH
                            if (students.size() == halls.getiCapacity() && availableForFiltering(students.size(), halls.getiCapacity()) < 0L) { //NEEDS TO BE REVIEWED// SO THAT NO MEMORY OVERFLOW WOULD OCCUR//FILTER ADDED HERE TO CHOSE WHO OF THE STUDENTS WILL GET ASSIGNED, ONLY WORKS ON SPECIFIC SIZES BEFORE EXPONENTIAL GROWTH

                                int st2 = 0;
                                for (int st = 0; st < students.size(); st++) {
                                    st2 = st;
                                    students.get(st).setNumberforCombinations(st);              //gives each datasetstudent object a unique number
                                }

                                ArrayList<DataSetStudents> filtered = new ArrayList<>();
                                // ArrayList<DataSetStudents[]> filtered2 = generate_myGenerate(st2 + 1, 20, students);        //find the combination of students suitable for my needs(lowest/highest number of lecture code dependencies)
                                ArrayList<Integer> lowestFound = new ArrayList<>();
                                ArrayList<Integer> iteration = new ArrayList<>();

//                            for (int k = 0; k < filtered2.size(); k++) {           //traverse the combinations
//                                ArrayList<DataSetStudents> combination = new ArrayList<>(Arrays.asList(filtered2.get(k)));
//
//                                iteration = lectureCodesOfStudents(combination, toAssign);
//                                if (iteration.size() < lowestFound.size()) {
//                                    lowestFound = iteration;
//                                    filtered = combination;
//                                }//after finding the combination with the lowest or highest number of courses could be tested for both, then assign the students and move them from the global data array to the assigned
//                            }

                                //assign them now to this course:
                                //1. find suitable day
                                //2. find most popular choices of hour among this group
                                //3.


                            } else {//randomly pick among the students to reach the hall's capacity
                                students = randomlyRemoveFromArray(students, halls.getiCapacity());
                            }
                        }
                        else {
                            if(students.size() < minNumForTutorials){
                                students.addAll(randomStudents(tempAssign, minNumForTutorials - students.size()));
                            }//do something else here instead, this check is already at the start of halls iteration
                        }


                        ArrayList<Timeslot> notAvailable = notavailableTime_specificDay(students, timetable, temp_sgt);               //ONLY FOR TESTING
                        //ArrayList<Timeslot> notAvailable_onlyLectures = notavailableTime_specificDay_onlyLectures(students, timetable);               //ONLY FOR TESTING

                        temp_sgt.setNotAvailableSlots(notAvailable);

                        ArrayList<Timeslot> iTimes = halls.findAvailableSlot_PreferredDay_sgt_remastered(900, 1.0, heuristicsData.getsDay(), event, temp_sgt.getNotAvailableSlots());

                        //ArrayList<Timeslot> iTimes_Lectures = halls.findAvailableSlot_PreferredDay_sgt_remastered(900, 1.0, heuristicsData.getsDay(), event, notAvailable_onlyLectures);
                        //temp_sgt.getAvailableSlots().addAll(iTimes);

                        temp_sgt.setAvailableSlots(iTimes);
                        temp_sgt.setCodesOfStudentsAssigned( codesFromDataSetStudents(students) );

                        //temp_sgt.setAvailableSlots_noDependencies(iTimes_Lectures);         //only for testing, changes every time the hall is changed, so not very useful

                        ArrayList<CoupledData> times_coupledData = heuristics_sgt_preferredHours(event, DSCALE_GLOBAL, sDayIteration);
                        for (CoupledData coupledData : times_coupledData) {
                            int iHourFound = i_prefferedTime_byDay_check(temp_sgt, sDayIteration, coupledData.getiHour(), halls.getsAbbrev());
//                                int iHourFound = halls.checkAvailability(sDayIteration, coupledData.getiHour(), 1.0);
                            if(iHallFound == 1) break;

                            if (iHourFound != 0) {
                                int iFinalCheck = halls.checkAvailability(sDayIteration, iHourFound, 1.0);
                                if(iFinalCheck == 0 && coupledData.getiHour() == times_coupledData.get(times_coupledData.size() - 1).getiHour() ){}

                                if (iFinalCheck != 0 && checkStudents(temp_sgt, sDayIteration, 4, timetable) != 0) {

                                    int iAddCode = halls.getiAdditionalCode();
                                    for (int t = 0; t < timetable.getHalls().size(); t++) {
                                        if (timetable.getHalls().get(t).getiAdditionalCode() == iAddCode) {
                                            timetable.getHalls().get(t).setAvToZero(iHourFound, (int) (iHourFound + 1 * 100), sDayIteration);   // ONLY TRIES THE FIRST CHOICE OF HEURISTICS
                                            updateAvailabilitySlotsSGT(temp_sgt, sDayIteration, halls.getsAbbrev(),iHourFound);

                                            for (int f = 0; f < timetable.getWeekTimet().size(); f++) {                 //add to the Day log, for printing purposes + check
                                                if (timetable.getWeekTimet().get(f).getSname().toLowerCase(Locale.ROOT).equals(sDayIteration.toLowerCase(Locale.ROOT))) {
                                                    timetable.getWeekTimet().get(f).v_assignEvent(iHourFound, 1, halls.getsAbbrev(), temp.getsLect(), "event");
                                                    int iDate = timetable.getWeekTimet().get(f).getiDate();
                                                    int iMonth = timetable.getWeekTimet().get(f).getiMonth();//+1
                                                    int iYear = timetable.getWeekTimet().get(f).getiYear();//+1900
                                                    iHallFound = 1;
                                                    temp_sgt.setDayAssigned(sDayIteration);//this line is wrong
                                                    temp_sgt.setiHourScheduled(iHourFound);
                                                    temp_sgt.setiDayScheduled(iDate);
                                                    temp_sgt.setiMonthScheduled(iMonth + 1);
                                                    temp_sgt.setiYearScheduled(iYear); // + 1900
                                                    temp_sgt.setsDayOfWeek(sDayIteration);
                                                    temp_sgt.setsLectureHall(timetable.getHalls().get(t).getsAbbrev());
                                                    temp_sgt.setiSizeOfHall(halls.getiCapacity());
                                                    break;
                                                }
                                            }
                                        }
                                    }


                                    // change this to the size of the array assigning which is of course == capacity of the hall
                                    //the only place where adding to the students' assigned sgts happens
//                                    for (int stdItr = 0; stdItr < students.size(); stdItr++) {
//                                        //increase the number of attending students for this sgt
//                                        tempAssign.remove(students.get(stdItr));
//                                        temp_sgt.setiNumberOfStudentsAttending(stdItr + 1);
//                                        //temp_sgt.addToCodesOfStudentsAssigned(students.get(stdItr).getKings_id());
//                                        this.STUDENTS_TO_BE_ASSINGED_GLOBAL.get(students.get(stdItr).getKings_id()).addToCourses(temp_sgt);                                 //so now when the sgt updates itself, the update would reflect inside the student's array of sgts
//                                        this.STUDENTS_TO_BE_ASSINGED_GLOBAL.get(students.get(stdItr).getKings_id()).increaseAssignedCoursesNumber();
//                                        this.ASSIGNED_STUDENTS_GLOBAL.put((students.get(stdItr).getKings_id()), this.STUDENTS_TO_BE_ASSINGED_GLOBAL.get(students.get(stdItr).getKings_id()));
//
//                                    }

                                    //carry on assigning people until all people are assigned, if there is
                                    //no available spot for the rest of the people/ all of them then
                                    //try at random, and if again, then add to the unsuccessfully assigned lectures

                                    //now remove them from the data so that they are not retrieved again


                                    for(DataSetStudents student : students){
                                        //increase the number of attending students for this sgt

                                        temp_sgt.setiNumberOfStudentsAttending(temp_sgt.getiNumberOfStudentsAttending() +  1);
                                        //temp_sgt.addToCodesOfStudentsAssigned(students.get(my_itr).getKings_id());
                                        this.STUDENTS_TO_BE_ASSINGED_GLOBAL.get(student.getKings_id()).addToCourses(temp_sgt);                                 //so now when the sgt updates itself, the update would reflect inside the student's array of sgts
                                        this.STUDENTS_TO_BE_ASSINGED_GLOBAL.get(student.getKings_id()).increaseAssignedCoursesNumber();
                                        this.ASSIGNED_STUDENTS_GLOBAL.put((student.getKings_id()), this.STUDENTS_TO_BE_ASSINGED_GLOBAL.get(student.getKings_id()));
                                    }
                                    tempAssign.removeAll(students);



                                    updateAvailailitySlotsSGT_AllEvents(temp_sgt);
                                    this.ASSIGNED_SGTS_CURRENTWEEK_GLOBAL.add(temp_sgt);//or a clone, this is inside the first for(!)

                                }

                            }
                        }


                    }//END OF HALLS
                }//END OF COUPLED DATA
            } // END OF FOR LOOP

            if(!tempAssign.isEmpty()){
                //students whose preferences for day were not satisfiable would fall into this if statement
                //since there are only a number of days are alowed for a sgt, depending on the lecture, algoithm
                //will still assign to those days

                int iPredefinedIterations2 = 2*iPredefinedIterations;                   //CHANGE THIS LINE OF CODE
                ArrayList<DataSetStudents> students = new ArrayList<>();

                for(int numIterations = 0; numIterations < iPredefinedIterations2; numIterations++) {

                    temp_sgt = new SGT();
                    temp_sgt.setPreferredDays(event.getPreferredDays());
                    temp_sgt.setiCode(temp.getiCode());
                    temp_sgt.setiHours(1);
                    temp_sgt.setsLect(temp.getsLect());
                    secondaryAdditionalCode++;
                    temp_sgt.setiAdditionalCode(secondaryAdditionalCode);

                    if(tempAssign.isEmpty()) break;

                    int iHallFound = 0;

                    for (CoupledData heuristicsData : event.getPreferredDays().getHeuristics()){
                        if(tempAssign.isEmpty()) break;
                        if(iHallFound == 1) break;
                        String sDayIteration = heuristicsData.getsDay();

                        for (Hall halls : timetable.getHalls()) {                     //test for each day the availability if not available proceed to assigning toa free slot,if there are available
                            //could add the combinations filter here // add if (hallsFound == 1 ) break;

                            if(tempAssign.isEmpty()) break;
                            if(iHallFound == 1) break;

                            students = randomStudents(tempAssign, halls.getiCapacity());
                            if(tempAssign.size() < halls.getiCapacity()){//change this to tempAssign
                                students = new ArrayList<>(tempAssign);
                            }
                            ArrayList<CoupledData> hours = v_hoursHeuristics_RemainingStudents(students, DSCALE_GLOBAL);

                            ArrayList<Timeslot> notAvailable = notavailableTime_specificDay(students, timetable, temp_sgt);               //ONLY FOR TESTING

                            temp_sgt.setNotAvailableSlots(notAvailable);

                            ArrayList<Timeslot> iTimes = halls.findAvailableSlot_PreferredDay_sgt_remastered(900, 1.0, heuristicsData.getsDay(), event, temp_sgt.getNotAvailableSlots());
                            temp_sgt.setAvailableSlots(iTimes);
                            temp_sgt.setCodesOfStudentsAssigned( codesFromDataSetStudents(students) );

                            for( CoupledData coupledData : hours) {

                                if (iHallFound == 1) break;
                                if (tempAssign.isEmpty()) break;

                                int iHourFound = i_prefferedTime_byDay_check(temp_sgt, sDayIteration, coupledData.getiHour(),halls.getsAbbrev());

                                if (iHourFound != 0){

                                    int iFinalCheck = halls.checkAvailability(sDayIteration, iHourFound, 1.0);
                                    if(iFinalCheck == 0 && coupledData.getiHour() == hours.get(hours.size()-1).getiHour()){ break; }
                                    if (iFinalCheck != 0 && checkStudents(temp_sgt, sDayIteration, 4, timetable) != 0) {

                                        int iAddCode = halls.getiAdditionalCode();
                                        for (int t = 0; t < timetable.getHalls().size(); t++) {
                                            if (timetable.getHalls().get(t).getiAdditionalCode() == iAddCode) {
                                                timetable.getHalls().get(t).setAvToZero(iHourFound, (int) (iHourFound + 1 * 100), sDayIteration);   // ONLY TRIES THE FIRST CHOICE OF HEURISTICS
                                                updateAvailabilitySlotsSGT(temp_sgt, sDayIteration, halls.getsAbbrev(),iHourFound);

                                                for (int f = 0; f < timetable.getWeekTimet().size(); f++) {                 //add to the Day log, for printing purposes + check
                                                    if (timetable.getWeekTimet().get(f).getSname().toLowerCase(Locale.ROOT).equals(sDayIteration.toLowerCase(Locale.ROOT))) {
                                                        timetable.getWeekTimet().get(f).v_assignEvent(iHourFound, 1, halls.getsAbbrev(), temp.getsLect(), "event");
                                                        int iDate = timetable.getWeekTimet().get(f).getiDate();
                                                        int iMonth = timetable.getWeekTimet().get(f).getiMonth();//+1
                                                        int iYear = timetable.getWeekTimet().get(f).getiYear();//+1900
                                                        iHallFound = 1;
                                                        temp_sgt.setDayAssigned(sDayIteration);
                                                        temp_sgt.setsDayOfWeek(sDayIteration);
                                                        temp_sgt.setiHourScheduled(iHourFound);
                                                        temp_sgt.setiDayScheduled(iDate);
                                                        temp_sgt.setiMonthScheduled(iMonth + 1);
                                                        temp_sgt.setiYearScheduled(iYear); // + 1900
                                                        temp_sgt.setsLectureHall(timetable.getHalls().get(t).getsAbbrev());
                                                        temp_sgt.setiSizeOfHall(halls.getiCapacity());
                                                        break;
                                                    }
                                                }
                                            }
                                        }


                                        // change this to the size of the array assigning which is of course == capacity of the hall
//                                        for (int my_itr = 0; my_itr < students.size(); my_itr++) {
//                                            //increase the number of attending students for this sgt
//                                            tempAssign.remove(students.get(my_itr));
//                                            temp_sgt.setiNumberOfStudentsAttending(my_itr + 1);
//                                            //temp_sgt.addToCodesOfStudentsAssigned(students.get(my_itr).getKings_id());
//                                            this.STUDENTS_TO_BE_ASSINGED_GLOBAL.get(students.get(my_itr).getKings_id()).addToCourses(temp_sgt);                                 //so now when the sgt updates itself, the update would reflect inside the student's array of sgts
//                                            this.STUDENTS_TO_BE_ASSINGED_GLOBAL.get(students.get(my_itr).getKings_id()).increaseAssignedCoursesNumber();
//                                            this.ASSIGNED_STUDENTS_GLOBAL.put((students.get(my_itr).getKings_id()), this.STUDENTS_TO_BE_ASSINGED_GLOBAL.get(students.get(my_itr).getKings_id()));
//                                        }


                                        for(DataSetStudents student : students){
                                            //increase the number of attending students for this sgt

                                            temp_sgt.setiNumberOfStudentsAttending(temp_sgt.getiNumberOfStudentsAttending() +  1);
                                            //temp_sgt.addToCodesOfStudentsAssigned(students.get(my_itr).getKings_id());
                                            this.STUDENTS_TO_BE_ASSINGED_GLOBAL.get(student.getKings_id()).addToCourses(temp_sgt);                                 //so now when the sgt updates itself, the update would reflect inside the student's array of sgts
                                            this.STUDENTS_TO_BE_ASSINGED_GLOBAL.get(student.getKings_id()).increaseAssignedCoursesNumber();
                                            this.ASSIGNED_STUDENTS_GLOBAL.put((student.getKings_id()), this.STUDENTS_TO_BE_ASSINGED_GLOBAL.get(student.getKings_id()));
                                        }
                                        tempAssign.removeAll(students);

                                        //carry on assigning people until all people are assigned, if there is
                                        //no available spot for the rest of the people/ all of them then
                                        //try at random, and if again, then add to the unsuccessfully assigned lectures

                                        //now remove them from the data so that they are not retrieved again

                                        updateAvailailitySlotsSGT_AllEvents(temp_sgt);
                                        this.ASSIGNED_SGTS_CURRENTWEEK_GLOBAL.add(temp_sgt); //set students to 0 //inside the if

                                    }



                                }
                            }


                        }



                    }



                }


            }
            if(!tempAssign.isEmpty()){
                //add to sgts with no available spot + students who cannot be assigned, might be partially assigned
                for (int my_itr = 0; my_itr < tempAssign.size(); my_itr++) {
                    //increase the number of attending students for this sgt
                    temp_sgt.setiNumberOfStudentsAttending(my_itr + 1);
                    if(this.STUDENTS_TO_BE_ASSINGED_GLOBAL.get(tempAssign.get(my_itr).getKings_id()) != null)
                        this.NOT_ASSIGNED_STUDENTS_GLOBAL.put((tempAssign.get(my_itr).getKings_id()), this.STUDENTS_TO_BE_ASSINGED_GLOBAL.get(tempAssign.get(my_itr).getKings_id()));
                    else
                        this.NOT_ASSIGNED_STUDENTS_GLOBAL.put((tempAssign.get(my_itr).getKings_id()), new Student(tempAssign.get(my_itr).getKings_id(), iYearOfStudy));

                }
                this.NOT_ASSIGNED_SGTS_CURRENTWEEK_GLOBAL.add(temp_sgt);
            }

        }//END OF DUPLET



    }


    ///????????????????????????????????????????????????????????///
    //////////////////////!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    public void generateHCSolution(double iScale, int iterations, int minNumForTutorials,int maxNumForTutorials, double scaleForDay, double scaleForHour, double scaleCloseToHour) throws SQLException, CloneNotSupportedException, IOException, ParseException, InterruptedException {

        hillClimbing_sgt(TIMETABLEONT_GLOBAL, iScale, 1, 0, iterations, minNumForTutorials,maxNumForTutorials, scaleForDay, scaleForHour,scaleCloseToHour);
        hillClimbing_sgt(TIMETABLETWO_GLOBAL, iScale, 1, 0, iterations, minNumForTutorials,maxNumForTutorials, scaleForDay, scaleForHour, scaleCloseToHour);


    }

}
