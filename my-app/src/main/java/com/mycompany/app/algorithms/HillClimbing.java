package com.mycompany.app.algorithms;

import com.mycompany.app.timetabling.*;

import java.io.IOException;
import java.sql.*;
import java.text.ParseException;
import java.util.*;

public class HillClimbing {

    Week_Timetable timetableont;
    Week_Timetable timetabletwo;
    Connection connection;
    GreedyAlgorithm grdAlg;
    HashMap<Integer, Student> studentHashMap=new HashMap<>();
    ArrayList<Duplet> assignedSGTS_currentWeek = new ArrayList<>();
    Schedule finalSchedule;

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

    public ArrayList<DataSetStudents> getHCData(int iYear) throws SQLException {
        ArrayList<DataSetStudents> searchData = new ArrayList<>();
        String sql99 = "";
        if(iYear == 3) {
            sql99 = "SELECT c.kings_id, lecture_code,abreviation, prefday, preftime, preftime_secondchoice, scale, privileged " +
                    "FROM ( (select * " +
                    "FROM students_lectures a " +
                    "JOIN courses b ON a.lecture_code = b.inside_code ) AS c " +
                    "JOIN main_preferences_lectures_students m ON m.course_abreviation = c.abreviation " + "AND c.kings_id = m.kings_id_fk " +
                    "JOIN preferences p ON m.kings_id_fk = p.kings_id ) WHERE c.kings_id < 1900000";
        }
        if(iYear == 2) {
            sql99 = "SELECT c.kings_id, lecture_code,abreviation, prefday, preftime, preftime_secondchoice, scale, privileged " +
                    "FROM ( (select * " +
                    "FROM students_lectures a " +
                    "JOIN courses b ON a.lecture_code = b.inside_code ) AS c " +
                    "JOIN main_preferences_lectures_students m ON m.course_abreviation = c.abreviation " + "AND c.kings_id = m.kings_id_fk " +
                    "JOIN preferences p ON m.kings_id_fk = p.kings_id ) WHERE c.kings_id > 1900000 && c.kings_id < 2000000";
        }

        if(iYear == 1){
            sql99 =  "SELECT c.kings_id, lecture_code,abreviation, prefday, preftime, preftime_secondchoice, scale, privileged " +
                    "FROM ( (select * " +
                    "FROM students_lectures a " +
                    "JOIN courses b ON a.lecture_code = b.inside_code ) AS c " +
                    "JOIN main_preferences_lectures_students m ON m.course_abreviation = c.abreviation " + "AND c.kings_id = m.kings_id_fk " +
                    "JOIN preferences p ON m.kings_id_fk = p.kings_id ) WHERE c.kings_id > 2000000";
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
        }

        //start by assigning people with stronger preferences, for example the privileged people
        //or maybe not;


        resultSet.close();
        statement.close();
        return searchData;

    }


    //NOT USED FOR NOW
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

    //NOT USED FOR NOW
    private void extractSGTsFromLGTs(Week_Timetable timetableont){

        for(int z = 0; z < timetableont.getAssignedLectures().size(); z++) {
            int iCounter = 0;
            Duplet temp = timetableont.getAssignedLectures().get(z);
            for (int i = 0; i < timetableont.getAssignedLGT().size(); i++) {
                //if it's inside then we can't assign it a SGT for this week
                if (timetableont.getAssignedLGT().get(i).getsLect().equals(timetableont.getAssignedLectures().get(z).getsLect())) {
                    //do not add it to the sgt list
                    break;
                }
                iCounter++;
            }
            if(iCounter == timetableont.getAssignedLGT().size()){
                //temp.setiHours(1.0);
                timetableont.getSgt().add(temp);
            }
        }
    }

    //NOT USED FOR NOW
    private void updateTimeline_SGT(Week_Timetable timetableont){

        ArrayList<String> empty = new ArrayList<>();
        for(int i =0; i< timetableont.getSgt().size(); i++){
            timetableont.getSgt().get(i).getPreferredDays().setPrefDay(empty);
            timetableont.getSgt().get(i).getPreferredDays().setForsgt_usage(timetableont.getSgt().get(i).getsDayOfWeek());             //turning
            timetableont.getSgt().get(i).getPreferredDays().setNotAvailableBefore((int)(timetableont.getSgt().get(i).getiHourScheduled() + 100*timetableont.getSgt().get(i).getiHours()));
//            timetableont.getSgt().get(i).getPreferredDays().setMandatory();
//            timetableont.getSgt().get(i).getPreferredDays().setiPrefHour();
        }

    }

    //NOT USED FOR NOW
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

    //NOT USED FOR NOW
    public Week_Timetable sortOutWeek_nextWeek(Week_Timetable toClone) throws SQLException, CloneNotSupportedException, ParseException, IOException {
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

            grdAlg.generateGreedySolution2(grdAlg.sCoursesTable, grdAlg.sStudentsTable,grdAlg.iLocalMin, grdAlg.iLocalMax ,grdAlg.prefdDays, timetabletwo);
            timetable = grdAlg.getWeek_timetable_ont();
        }

        return timetable;
    }


    //THIS COULD BE USEFUL
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
                    + " AND HOUR < " + Integer.toString((int) (temp.getiHourScheduled() + 100*temp.getiHours()));
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


///============================================================================

    //updates the preferred days based on the students' choices, writes directly to the PreferredDay class inside the Duplet class
    //information and update is passed in the form of CoupledData class concerning each day
    public void statistics_sgt_days(Week_Timetable week_timetable, double iScale) throws SQLException {
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

    //updates the days heuristics inside PreferredDays, removes the days of the week prior to the lecture. Those are unavailable for heuristics
    public void removeUnusableDays(Week_Timetable timetable){

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

    //if called and used, reorders the days of preference, moves the day of the lecture as last heuristics option
    private void reorderHeuristics_days(Week_Timetable timetable){
        //reorder heuristics to allow one day to be given between the lecture and the sgt
        //cleans the heuristics first, removes any day which has 0 people voted for it
        for(Duplet event : timetable.getSgt()){
            for(int i =0; i<event.getPreferredDays().getHeuristics().size(); i++){
                if(event.getPreferredDays().getHeuristics().get(i).getHeuristics() == 0.0){
                    event.getPreferredDays().getHeuristics().remove(i);
                }
            }
        }

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

    private void statistics_sgt_hours(Week_Timetable week_timetable, ArrayList<DataSetStudents> data, double dFactor){

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

    private void hillClimbing_sgt(Week_Timetable timetable, double iScale, int iCondition) throws SQLException, ParseException, CloneNotSupportedException, IOException {


        ArrayList<DataSetStudents> toPrint = getHCData(1);
        ArrayList<Duplet> assignedSGTs = new ArrayList<>();
        //info on who to assign to sgt(s) first would come from a Filter
        //

        ArrayList<DataSetStudents> data = getHCData(1);


       // Week_Timetable nextWeek = sortOutWeek_nextWeek(grdAlg.getWeek_timetable_ont());
       // Week_Timetable nextnextWeek = sortOutWeek_nextWeek(grdAlg.getWeek_timetable_spare());
        extractSGTsFromLGTs(timetable);
        updateTimeline_SGT(timetable);
        statistics_sgt_days(timetable, iScale);
        removeUnusableDays(timetable);
        statistics_sgt_hours(timetable, data ,iScale);
        if(iCondition == 1){
            reorderHeuristics_days(timetable);
        }

        //after these series of actions I can start sorting out the students individually
        //heuristics would allow me to
        if(timetable.getSgt().size() != 0) {
            Duplet temp = timetable.getSgt().get(0);
            ArrayList<DataSetStudents> toAssign = findInDataByDay(data,temp.getsLect(), "Monday");

            System.out.println();
        }

        algorithmAssigning_year2_5050filtering(timetable);
    }

    //////////////////////!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!


    public void algorithmAssigning_year2_5050filtering(Week_Timetable timetable) throws SQLException, CloneNotSupportedException {

        //find halls
        //findInData
        //findInDataByDay
        ArrayList<DataSetStudents> toAssign = getHCData(2);
        ArrayList<DataSetStudents> tempAssign = getHCData(2);
        // I need the list of duplets plus all their details for week2
        ArrayList<Duplet> sgts = new ArrayList<>();
        for(Duplet event : timetable.getSgt()){
            if(event.getsLect().startsWith("5CCS")){
                sgts.add(event);
            }
        }


        for(Duplet event : sgts){
            //start assigning each event
            //those are the students to be assigned
            tempAssign = findInData(toAssign, event.getsLect());
            Duplet temp = (Duplet) event.clone();
            //test for each day the availability
            for(Hall halls : timetable.getHalls()){
                //test for each day the availability
                //of the halls
                int iTime = halls.findAvailableSlot_PreferredDay_sgt(900, 1, event.getPreferredDays().getHeuristics().get(0).getsDay(),
                                                                        event.getiDayScheduled(), event.getiMonthScheduled(), event.getiYearScheduled(),
                                                                        (int)(event.getiHourScheduled() + event.getiHours()*100), event);

                ArrayList<DataSetStudents> students = findInDataByDay(tempAssign, event.getsLect(), event.getPreferredDays().getHeuristics().get(0).getsDay());
                ArrayList<DataSetStudents> assigned_students = new ArrayList<>();

                if(iTime != 0) {
                    //assign the event to n number of students


                    //update temp :
                    temp.setiHours(1);
                    temp.setiHourScheduled(iTime);
                    temp.setDayAssigned(event.getPreferredDays().getHeuristics().get(0).getsDay());
                    int iAddCode = halls.getiAdditionalCode();
                    for(int t =0; t < timetable.getHalls().size(); t++) {
                        if (timetable.getHalls().get(t).getiAdditionalCode() == iAddCode) {
                            timetable.getHalls().get(t).setAvToZero(iTime, (int) (iTime + 1 * 100), event.getPreferredDays().getHeuristics().get(0).getsDay());   //SET AVTOZERO

                            for (int f = 0; f < timetable.getWeekTimet().size(); f++) {                 //add to the Day log, for printing purposes + check
                                if (timetable.getWeekTimet().get(f).getSname().toLowerCase(Locale.ROOT).equals(event.getPreferredDays().getHeuristics().get(0).getsDay().toLowerCase(Locale.ROOT))) {
                                    timetable.getWeekTimet().get(f).v_assignEvent(iTime, 1, halls.getsAbbrev(), temp.getsLect(), "event");
                                    int iDate = timetable.getWeekTimet().get(f).getiDate();
                                    int iMonth = timetable.getWeekTimet().get(f).getiMonth();//+1
                                    int iYear = timetable.getWeekTimet().get(f).getiYear();//+1900

                                    temp.setDayAssigned(event.getPreferredDays().getHeuristics().get(0).getsDay());
                                    temp.setiHourScheduled(iTime);
                                    temp.setiDayScheduled(iDate);
                                    temp.setiMonthScheduled(iMonth + 1);
                                    temp.setiYearScheduled(iYear);
                                    temp.setsDayOfWeek(event.getPreferredDays().getHeuristics().get(0).getsDay());
                                    temp.setsLectureHall(timetable.getHalls().get(t).getsAbbrev());

                                }

                            }
                        }
                    }


                    if (assigned_students.size() >= halls.getiCapacity()) {         //REMEMBER TO CHANGE THIS TO >=

                        for (int i = 0; i < halls.getiCapacity(); i++) {
                            //
                            studentHashMap.put(students.get(i).getKings_id(), new Student(students.get(i).getKings_id(), 4));
                            studentHashMap.get(students.get(i).getKings_id()).addToPrefs(students.get(i));
                            studentHashMap.get(students.get(i).getKings_id()).assignSGT(temp);

                        }
                    }

                    else{
                        for (int i = 0; i < assigned_students.size(); i++) {
                            //
                            studentHashMap.put(students.get(i).getKigoogngs_id(), new Student(students.get(i).getKings_id(), 4));
                            studentHashMap.get(students.get(i).getKings_id()).addToPrefs(students.get(i));
                            studentHashMap.get(students.get(i).getKings_id()).assignSGT(temp);

                        }
                    }

                }
                else{
                    //FIND AVAILABLE SPOT
                }

            }
        }



    }



    ///????????????????????????????????????????????????????????///
    //////////////////////!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    public int generateHCSolution(double iScale) throws SQLException, CloneNotSupportedException, IOException, ParseException {

        hillClimbing_sgt(timetabletwo, iScale, 1);
        if(1 == 1){             //if solution found return 0
            return 0;
        }
        return 1;
    }

}
