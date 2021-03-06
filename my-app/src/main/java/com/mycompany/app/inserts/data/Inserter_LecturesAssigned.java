package com.mycompany.app.inserts.data;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

/*
* Description:
* This class creates and populates the table students_lectures. The table is the connection between
* the king's id of a student and the courses he/she attends
* */


public class Inserter_LecturesAssigned {

    private Connection connection;

    public Inserter_LecturesAssigned(Connection connection) {
            this.connection = connection;
        }

        public void createTableReference() throws SQLException {

            Statement statement = connection.createStatement();
            String sql112 ="CREATE TABLE `students_choice_of_lectures` ( " +
                    "  `kings_id` int unsigned NOT NULL, " +
                    "  `lecture_code` int unsigned NOT NULL, " +
                    "  PRIMARY KEY (`kings_id`,`lecture_code`), " +
                    "  KEY `lecture_code` (`lecture_code`), " +
                    "  CONSTRAINT `students_choice_of_lectures_ibfk_1` FOREIGN KEY (`kings_id`) REFERENCES `students` (`KINGS_ID`), " +
                    "  CONSTRAINT `students_choice_of_lectures_ibfk_2` FOREIGN KEY (`lecture_code`) REFERENCES `courses` (`inside_code`) " +
                    ")";

            dropTableReference();
            statement.executeUpdate(sql112);
            statement.close();

        }


        public void dropTableReference() throws SQLException {
            Statement statement= connection.createStatement();
            String sq221 = "DROP TABLE IF EXISTS students_lectures";
            statement.executeUpdate(sq221);
            statement.close();
        }

        public void createTable() throws SQLException {
                Statement statement = connection.createStatement();
                String sql112 = "CREATE TABLE `students_lectures` ( " +
                        "  `kings_id` int unsigned NOT NULL, " +
                        "  `lecture_code` int unsigned NOT NULL," +
                        "  `electable` int unsigned NOT NULL, " +
                        "  `semester` int unsigned NOT NULL, " +
                        "  PRIMARY KEY (`kings_id`,`lecture_code`), " +
                        "  KEY `lecture_code` (`lecture_code`), " +
                        "  CONSTRAINT `students_lectures_ibfk_1` FOREIGN KEY (`kings_id`) REFERENCES `students` (`KINGS_ID`), " +
                        "  CONSTRAINT `students_lectures_ibfk_2` FOREIGN KEY (`lecture_code`) REFERENCES `courses` (`inside_code`) " +
                        ")";

                dropTable();
                statement.executeUpdate(sql112);

                statement.close();

            }

        public void dropTable() throws SQLException {
                Statement statement= connection.createStatement();
                String sq221 = "DROP TABLE IF EXISTS students_lectures";
                statement.executeUpdate(sq221);
                statement.close();
        }


        public void truncateTable() throws SQLException {
            Statement statement= connection.createStatement();
            String sq221 = "TRUNCATE TABLE students_lectures";
            statement.executeUpdate(sq221);
            statement.close();
        }


        public void populateTable(String sCourses, String sStudents, int iSemester) throws SQLException {

            String sql11 = "SELECT inside_code, elective_subject, year FROM " + sCourses + " WHERE ABREVIATION LIKE '4CCS%' AND DESCRIPTION LIKE 'SEM" + Integer.toString(iSemester) + "%" + "'";
            String sql33 = "SELECT inside_code, elective_subject, year FROM " + sCourses + " WHERE ABREVIATION LIKE '5CCS%' AND DESCRIPTION LIKE 'SEM" + Integer.toString(iSemester) + "%" + "'";
            String sql99 = "SELECT inside_code, elective_subject, year FROM " + sCourses + " WHERE ABREVIATION LIKE '6CCS%' AND DESCRIPTION LIKE 'SEM" + Integer.toString(iSemester) + "%" + "'";
            String sql80 = "INSERT INTO students_lectures VALUES (?, ? ,?, ?)";
            String sql90 = "SELECT kings_id FROM " + sStudents + " WHERE kings_id like '20%'";
            String sql901 = "SELECT kings_id FROM " + sStudents + " WHERE kings_id like '19%'";
            String sql71 = "SELECT kings_id FROM " + sStudents + " WHERE kings_id like '18%'";          //these are hard coded, in the future, those numbers will change

            Statement statement = connection.createStatement();
            Statement statement2 = connection.createStatement();

            PreparedStatement preparedStatement = connection.prepareStatement(sql80);
            ResultSet resSet = statement2.executeQuery(sql90);

            ResultSet rst = statement.executeQuery(sql11);

            //there would be three types of students 18, 19 and 20
            // for the 18 everything is mandatory, no choice on courses

            //allocated for the year one students :


            while (resSet.next()) {

                while (rst.next()) {

                    preparedStatement.setInt(1, resSet.getInt(1));
                    preparedStatement.setInt(2, rst.getInt(1));
                    preparedStatement.setInt(3, rst.getInt(2));
                    preparedStatement.setInt(4, iSemester);

                    preparedStatement.executeUpdate();
                }
                rst.beforeFirst();

            }


            resSet.close();
            rst.close();
            //allocated for the year two students :

            ResultSet rS1 = statement.executeQuery(sql901);
            ResultSet rS2 = statement2.executeQuery(sql33);
            ArrayList<Integer> electableEvents = new ArrayList<>();
            ArrayList<Integer> electableEvents_copy = new ArrayList<>();
            ArrayList<Integer> mandatoryEvents = new ArrayList<>();
            ArrayList<Integer> mandatoryEvents_copy = new ArrayList<>();

            while (rS2.next()) {
                if (rS2.getInt(2) == 0) {
                    electableEvents.add(rS2.getInt(1));
                } else {
                    mandatoryEvents.add(rS2.getInt(1));
                }

            }

            int iSize = mandatoryEvents.size();

            electableEvents_copy = new ArrayList<>(electableEvents);
            mandatoryEvents_copy = new ArrayList<>(mandatoryEvents);


            while (rS1.next()) {
                //assign the ones which are mandatory first ?
                for (int i = 0; i < 4 - iSize; i++) {
                    //pick at random
                    int iRandom = (int) (Math.random() * (electableEvents.size()));
                    mandatoryEvents.add(electableEvents.get(iRandom));
                    //to fix the problem let's remove that ones selected from the electbleEvents
                    electableEvents.remove(iRandom);
                }

                for (int i = 0; i < mandatoryEvents.size(); i++) {
                    preparedStatement.setInt(1, rS1.getInt(1));
                    preparedStatement.setInt(2, mandatoryEvents.get(i));
                    preparedStatement.setInt(3, 1);
                    preparedStatement.executeUpdate();
                }

                //now reset:
                electableEvents = new ArrayList<>(electableEvents_copy);
                mandatoryEvents = new ArrayList<>(mandatoryEvents_copy);
            }

            rS1.close();
            rS2.close();

            //allocate for year three students

            ResultSet rS3 = statement.executeQuery(sql99);              //courses
            ResultSet rS4 = statement2.executeQuery(sql71);             //students
            electableEvents = new ArrayList<>();
            mandatoryEvents = new ArrayList<>();


            while(rS3.next()){
                if (rS3.getInt(2) == 0) {
                    electableEvents.add(rS3.getInt(1));
                } else {
                    mandatoryEvents.add(rS3.getInt(1));
                }
            }

//
//            for(int i = 0; i < electableEvents.size(); i++){
//                electableEvents_copy.add(electableEvents.get(i));
//            }
//
//            for(int i = 0; i < mandatoryEvents.size(); i++){
//                mandatoryEvents_copy.add(mandatoryEvents.get(i));
//            }

            electableEvents_copy = new ArrayList<>(electableEvents);
            mandatoryEvents_copy = new ArrayList<>(mandatoryEvents);

            while (rS4.next()) {
                //assign the ones which are mandatory first ?
                for (int i = 0; i < 4 - iSize; i++) {
                    //pick at random
                    int iRandom = (int) (Math.random() * (electableEvents.size()));
                    mandatoryEvents.add(electableEvents.get(iRandom));
                    //to fix the problem let's remove that ones selected from the electbleEvents
                    electableEvents.remove(iRandom);
                }

                for (int i = 0; i < mandatoryEvents.size(); i++) {
                    preparedStatement.setInt(1, rS4.getInt(1));
                    preparedStatement.setInt(2, mandatoryEvents.get(i));
                    preparedStatement.setInt(3, 1);
                    preparedStatement.executeUpdate();
                }

                //now reset:
                electableEvents = new ArrayList<>(electableEvents_copy);
                mandatoryEvents = new ArrayList<>(mandatoryEvents_copy);
            }


            rS3.close();
            rS4.close();
            statement.close();
            statement2.close();
            preparedStatement.close();
        }


        public int checkStudentsIrregularity(String sStudentsTable, String sCourses) throws SQLException {

            String sql11 = "SELECT kings_id, lecture_code FROM students_choice_of_lectures";
            Statement set = connection.createStatement();
            ResultSet rst = set.executeQuery(sql11);
            HashMap<Integer, Integer> map = new HashMap<>();

            int iResult = 1;
            while(rst.next()){
                rst.getInt(1);
                rst.getInt(2);

                    map.put(rst.getInt(1), rst.getInt(2));
            }

            map.forEach((k,v) -> map.put(k, 0));
            rst.beforeFirst();

            while(rst.next()){
                rst.getInt(1);
                rst.getInt(2);

                map.put(rst.getInt(1), map.get(rst.getInt(1)).intValue() + 1);
            }


            for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
                    if(entry.getValue() > 8){
                        return 1;
                    }
            }

            return 0;
        }



    public ArrayList<TwoInts> finalTable(int min,int max, String sCourses, String sStudents) throws SQLException {


        ArrayList<TwoInts> values = new ArrayList<>();
        ArrayList<TwoInts> values22 = new ArrayList<>();
        ArrayList<TwoInts> values33 = new ArrayList<>();
        ArrayList<Integer> initial_values = new ArrayList<>();
        ArrayList<Integer> initial_values_attendance = new ArrayList<>();
        HashMap<Integer,Integer> map = new HashMap<>();
        HashMap<Integer,Integer> map33 = new HashMap<>();
        String sql11 = "SELECT lecture_code FROM students_lectures WHERE kings_id < 2000000"; //hard coded again, exclude the first year courses
        String sql22 = "SELECT lecture_code FROM students_lectures WHERE kings_id < 2000000 AND kings_id > 1800000";
        String sql12 = "SELECT lecture_code FROM students_lectures WHERE kings_id > 1900000"; //hard coded again, include the first year courses

        Statement statement = connection.createStatement();
        Statement statement22 = connection.createStatement();
        ResultSet rst = statement.executeQuery(sql11);
        ResultSet rst2 = statement22.executeQuery(sql12);

        while(rst.next()){
            int temp = rst.getInt(1);
            if(map.containsKey(rst.getInt(1))){
                map.put(rst.getInt(1), map.get(rst.getInt(1) ) + 1);
            }
            else{
                map.put(rst.getInt(1), 1);
            }

        }

        while(rst2.next()){
            int temp = rst2.getInt(1);
            if(map33.containsKey(rst2.getInt(1))){
                map33.put(rst2.getInt(1), map33.get(rst2.getInt(1) ) + 1);
            }
            else{
                map33.put(rst2.getInt(1), 1);
            }

        }


        //Array 'values' corresponds to courses which have enough capacity in them
        //to be held. Those remaining, who don't will have the students reassigned

        for(Map.Entry<Integer, Integer> mapElement : map.entrySet()){
            if(mapElement. getValue() >= min && mapElement.getValue() <= max){
                values.add(new TwoInts(mapElement.getKey(), mapElement.getValue()));
                map.remove(mapElement);
            }
            else{
                initial_values.add(mapElement.getKey());
                initial_values_attendance.add(mapElement.getValue());
            }
        }


        for(Map.Entry<Integer, Integer> mapElement : map33.entrySet()){
                values33.add(new TwoInts(mapElement.getKey(), mapElement.getValue()));
        }


        //these are the the courses codes which don't have enough people in them
        //or have too many
        //I reassign the students in the students_lectures table to different courses
        ArrayList<Integer> choices = new ArrayList<>();
        ArrayList<Integer> joinRandomly = new ArrayList<>();
        ArrayList<Integer> notAssigned = new ArrayList<>();

        if(values.size() < 8) {
            //too many courses, too few students
        }

        for(int i = 0; i < initial_values.size(); i++) {
            String sql13 = "SELECT kings_id FROM students_lectures WHERE lecture_code = " + initial_values.get(i);

            ResultSet resultSet = statement.executeQuery(sql13);
            while (resultSet.next()) {
                String sql44 = "SELECT lecture_code FROM students_lectures WHERE kings_id = " + resultSet.getInt(1)
                                + " AND lecture_code != " + initial_values.get(i);
                ResultSet resultSet1 = statement22.executeQuery(sql44);
                while(resultSet1.next()){
                    choices.add(resultSet1.getInt(1));
                }
                resultSet1.close();
                //now the process of reassigning

                for(int k = 0; k < values.size(); k++){
                    int iterator = 0;
                    for(int x = 0; x < choices.size(); x++){
                        if(values.get(k).getiCode() != choices.get(x) && (values.get(k).getiAttending() + 1 <= max)){
                            iterator++;
                        }
                        else {
                            break;
                        }
                    }
                    if(iterator == choices.size()){
                        //means not a chosen course by the student already
                        joinRandomly.add(values.get(k).getiCode());

                    }
                }

                if( !joinRandomly.isEmpty() ) {
                    int iRandom = (int) (Math.random() * (choices.size() - 1));
                    Statement statement77 = connection.createStatement();
                    String sql43 = "UPDATE students_lectures SET lecture_code = " + joinRandomly.get(iRandom)
                                 + " WHERE lecture_code = " + initial_values.get(i) + " AND kings_id = " + resultSet.getInt(1);
                    statement77.executeUpdate(sql43);
                    statement77.close();

                    for(int s =0; s < values.size(); s++) {
                        if(joinRandomly.get(iRandom) == values.get(s).getiCode())
                        values.set(s, new TwoInts(values.get(s).getiCode(), values.get(s).getiAttending())); //fix this to s
                    }
                }else{
                    notAssigned.add(initial_values.get(i));
                }
                joinRandomly.clear();
                choices.clear();
            }

        }
        if(!notAssigned.isEmpty()){
            //there is some reassigning left to do
            if(notAssigned.size() == 1){
                //assign to different courses anyway, overflowing the limit by a little

            }
            else{
                //combine them to create a course that fits the limits

            }
        }

        for(TwoInts v1: values){
            values33.add(v1);
        }
        return values33;
        //System.out.println(values);
    }


    public ArrayList<TwoInts> finalTable2(int min,int max, String sCourses, String sStudents) throws SQLException {


        ArrayList<TwoInts> values = new ArrayList<>();
        ArrayList<TwoInts> values22 = new ArrayList<>();
        ArrayList<TwoInts> values33 = new ArrayList<>();
        ArrayList<TwoInts> values_final = new ArrayList<>();
        ArrayList<Integer> initial_values = new ArrayList<>();
        ArrayList<Integer> initial_values22 = new ArrayList<>();
        ArrayList<Integer> initial_values33 = new ArrayList<>();
        ArrayList<Integer> initial_values_attendance = new ArrayList<>();
        HashMap<Integer,Integer> map = new HashMap<>();
        HashMap<Integer,Integer> map22 = new HashMap<>();
        HashMap<Integer,Integer> map33 = new HashMap<>();
        String sql11 = "SELECT lecture_code FROM students_lectures WHERE kings_id < 1900000"; //hard coded again, exclude the first year courses
        String sql22 = "SELECT lecture_code FROM students_lectures WHERE kings_id < 2000000 AND kings_id > 1900000";
        String sql33 = "SELECT lecture_code FROM students_lectures WHERE kings_id > 2000000"; //hard coded again, include the first year courses

        Statement statement = connection.createStatement();
        Statement statement22 = connection.createStatement();
        Statement statement33 = connection.createStatement();

        ResultSet rst = statement.executeQuery(sql11);
        ResultSet rst2 = statement22.executeQuery(sql22);
        ResultSet rst3 = statement33.executeQuery(sql33);

        while(rst.next()){
            int temp = rst.getInt(1);
            if(map.containsKey(rst.getInt(1))){
                map.put(rst.getInt(1), map.get(rst.getInt(1) ) + 1);
            }
            else{
                map.put(rst.getInt(1), 1);
            }

        }

        if(map.size() < 4){
            //too few courses, abort
        }

        while(rst2.next()){
            int temp = rst2.getInt(1);
            if(map22.containsKey(rst2.getInt(1))){
                map22.put(rst2.getInt(1), map22.get(rst2.getInt(1) ) + 1);
            }
            else{
                map22.put(rst2.getInt(1), 1);
            }

        }

        if(map22.size() < 4){
            //too few courses, abort
        }

        while(rst3.next()){
            int temp = rst3.getInt(1);
            if(map33.containsKey(rst3.getInt(1))){
                map33.put(rst3.getInt(1), map33.get(rst3.getInt(1) ) + 1);
            }
            else{
                map33.put(rst3.getInt(1), 1);
            }

        }


        if(map33.size() < 4){
            //too few courses, abort
        }
        //Array 'values' corresponds to courses which have enough capacity in them
        //to be held. Those remaining, who don't will have the students reassigned

        for(Map.Entry<Integer, Integer> mapElement : map.entrySet()){
            if(mapElement. getValue() >= min && mapElement.getValue() <= max){
                values.add(new TwoInts(mapElement.getKey(), mapElement.getValue()));
            }
            else{
                initial_values.add(mapElement.getKey());
                initial_values_attendance.add(mapElement.getValue());
            }
        }

        if(values.size() < 4){
            //not enough students for 4 core courses
            //do something to combine them, maybe, ot leave as it is
        }

        values = reassignCourses(values, initial_values, max,min);



        for(Map.Entry<Integer, Integer> mapElement : map22.entrySet()){
            if(mapElement. getValue() >= min && mapElement.getValue() <= max){
                values22.add(new TwoInts(mapElement.getKey(), mapElement.getValue()));
            }
            else{
                initial_values22.add(mapElement.getKey());
            }
        }

        values22 = reassignCourses(values22, initial_values, max,min);

        if(values22.size() < 4){
            //not enough for 4 + 4 core courses, years 2 and 3
            //do something to combine them, maybe, ot leave as it is
        }


        for(Map.Entry<Integer, Integer> mapElement : map33.entrySet()){
            values33.add(new TwoInts(mapElement.getKey(), mapElement.getValue()));
        }

//        values33 = reassignCourses(values33, initial_values, max,min);            //unnecessary


        //these are the the courses codes which don't have enough people in them
        //or have too many
        //I reassign the students in the students_lectures table to different courses
        ArrayList<Integer> choices = new ArrayList<>();
        ArrayList<Integer> joinRandomly = new ArrayList<>();
        ArrayList<Integer> notAssigned = new ArrayList<>();


//        for(int i = 0; i < initial_values.size(); i++) {
//            String sql13 = "SELECT kings_id FROM students_lectures WHERE lecture_code = " + initial_values.get(i);
//
//            ResultSet resultSet = statement.executeQuery(sql13);
//            while (resultSet.next()) {
//                String sql44 = "SELECT lecture_code FROM students_lectures WHERE kings_id = " + resultSet.getInt(1)
//                        + " AND lecture_code != " + initial_values.get(i);
//                ResultSet resultSet1 = statement22.executeQuery(sql44);
//                while(resultSet1.next()){
//                    choices.add(resultSet1.getInt(1));
//                }
//                resultSet1.close();
//                //now the process of reassigning
//
//                for(int k = 0; k < values.size(); k++){
//                    int iterator = 0;
//                    for(int x = 0; x < choices.size(); x++){
//                        if(values.get(k).getiCode() != choices.get(x) && (values.get(k).getiAttending() + 1 <= max)){
//                            iterator++;
//                        }
//                        else {
//                            break;
//                        }
//                    }
//                    if(iterator == choices.size()){
//                        //means not a chosen course by the student already
//                        joinRandomly.add(values.get(k).getiCode());
//
//                    }
//                }
//
//                if( !joinRandomly.isEmpty() ) {
//                    int iRandom = (int) (Math.random() * (choices.size() - 1));
//                    Statement statement77 = connection.createStatement();
//                    String sql43 = "UPDATE students_lectures SET lecture_code = " + joinRandomly.get(iRandom)
//                            + " WHERE lecture_code = " + initial_values.get(i) + " AND kings_id = " + resultSet.getInt(1);
//                    statement77.executeUpdate(sql43);
//                    statement77.close();
//
//                    for(int s =0; s < values.size(); s++) {
//                        if(joinRandomly.get(iRandom) == values.get(s).getiCode())
//                            values.set(s, new TwoInts(values.get(s).getiCode(), values.get(s).getiAttending()));
//                    }
//                }else{
//                    notAssigned.add(initial_values.get(i));
//                }
//                joinRandomly.clear();
//                choices.clear();
//            }
//
//        }
        if(!notAssigned.isEmpty()){
            //there is some reassigning left to do
            if(notAssigned.size() == 1){
                //assign to different courses anyway, overflowing the limit by a little

            }
            else{
                //combine them to create a course that fits the limits

            }
        }

        for(TwoInts v1: values){
            values_final.add(v1);
        }
        for(TwoInts v1: values22){
            values_final.add(v1);
        }
        for(TwoInts v1: values33){
            values_final.add(v1);
        }

        return values_final;
        //System.out.println(values);
    }


    private ArrayList<TwoInts> reassignCourses(ArrayList<TwoInts> values,ArrayList<Integer> initial_values,int min,int max) throws SQLException {
        ArrayList<Integer> choices = new ArrayList<>();
        ArrayList<Integer> joinRandomly = new ArrayList<>();
        ArrayList<Integer> notAssigned = new ArrayList<>();
        for(int i = 0; i < initial_values.size(); i++) {
            String sql13 = "SELECT kings_id FROM students_lectures WHERE lecture_code = " + initial_values.get(i);
            Statement statement = connection.createStatement();
            Statement statement22 = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql13);
            while (resultSet.next()) {
                String sql44 = "SELECT lecture_code FROM students_lectures WHERE kings_id = " + resultSet.getInt(1)
                        + " AND lecture_code != " + initial_values.get(i);
                ResultSet resultSet1 = statement22.executeQuery(sql44);
                while(resultSet1.next()){
                    choices.add(resultSet1.getInt(1));
                }
                resultSet1.close();
                //now the process of reassigning

                for(int k = 0; k < values.size(); k++){
                    int iterator = 0;
                    for(int x = 0; x < choices.size(); x++){
                        if(values.get(k).getiCode() != choices.get(x) && (values.get(k).getiAttending() + 1 <= max)){
                            iterator++;
                        }
                        else {
                            break;
                        }
                    }
                    if(iterator == choices.size()){
                        //means not a chosen course by the student already
                        joinRandomly.add(values.get(k).getiCode());

                    }
                }

                if( !joinRandomly.isEmpty() ) {
                    int iRandom = (int) (Math.random() * (choices.size() - 1));
                    Statement statement77 = connection.createStatement();
                    String sql43 = "UPDATE students_lectures SET lecture_code = " + joinRandomly.get(iRandom)
                            + " WHERE lecture_code = " + initial_values.get(i) + " AND kings_id = " + resultSet.getInt(1);
                    statement77.executeUpdate(sql43);
                    statement77.close();

                    for(int s =0; s < values.size(); s++) {
                        if(joinRandomly.get(iRandom) == values.get(s).getiCode())
                            values.set(s, new TwoInts(values.get(s).getiCode(), values.get(s).getiAttending()));
                    }
                }else{
                    notAssigned.add(initial_values.get(i));
                }
                joinRandomly.clear();
                choices.clear();
            }

        }
        return values;
    }


    public ArrayList<TwoInts> events() throws SQLException {
        ArrayList<TwoInts> events = new ArrayList<>();
        String sql99 = "SELECT DISTINCT lecture_code FROM students_lectures";
        String sql89 = "SELECT COUNT(*) FROM students_lectures WHERE lecture_code = ? ";

        Statement statement = connection.createStatement();
        PreparedStatement statement66 = connection.prepareStatement(sql89);

        ResultSet resultSet = statement.executeQuery(sql99);
        while (resultSet.next()){
            statement66.setInt(1, resultSet.getInt(1));
            ResultSet resultSet66 = statement66.executeQuery();
            while(resultSet66.next()){
                events.add(new TwoInts(resultSet.getInt(1), resultSet66.getInt(1)));
            }
        }
        statement66.close();
        statement.close();

        return events;
    }

}
