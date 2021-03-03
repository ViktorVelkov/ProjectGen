package com.mycompany.app.inserts.data;

import java.sql.*;
import java.util.*;

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
            String sq221 = "TRUNCATE TABLE IF EXISTS students_lectures";
            statement.executeUpdate(sq221);
            statement.close();
        }


        public void populateTable(String sCourses, String sStudents) throws SQLException {

            String sql11 = "SELECT inside_code, elective_subject, year FROM " + sCourses + " WHERE ABREVIATION LIKE '4CCS%'";
            String sql33 = "SELECT inside_code, elective_subject, year FROM " + sCourses + " WHERE ABREVIATION LIKE '5CCS%'";
            String sql99 = "SELECT inside_code, elective_subject, year FROM " + sCourses + " WHERE ABREVIATION LIKE '6CCS%'";
            String sql80 = "INSERT INTO students_lectures VALUES (?, ? ,?)";
            String sql90 = "SELECT kings_id FROM " + sStudents + " WHERE kings_id like '18%'";
            String sql901 = "SELECT kings_id FROM " + sStudents + " WHERE kings_id like '19%'";
            String sql71 = "SELECT kings_id FROM " + sStudents + " WHERE kings_id like '20%'";          //these are hard coded, in the future, those numbers will change

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
                for (int i = 0; i < 8 - iSize; i++) {
                    //pick at random
                    int iRandom = (int) (Math.random() * (electableEvents.size()));         //there is a flaw with this, it cannot guarantee that every random number is different
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
                for (int i = 0; i < 8 - iSize; i++) {
                    //pick at random
                    int iRandom = (int) (Math.random() * (electableEvents.size()));         //there is a flaw with this, it cannot guarantee that every random number is different
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

    public void finalTable(int min,int max, String sCourses, String sStudents) throws SQLException {
        //use method populate table here
        //populateTable(sCourses, sStudents);
        ArrayList<TwoInts> values = new ArrayList<>();
        HashMap<Integer,Integer> map = new HashMap<>();
        String sql11 = "SELECT lecture_code FROM students_lectures";
        Statement statement = connection.createStatement();
        ResultSet rst = statement.executeQuery(sql11);

        while(rst.next()){
            int temp = rst.getInt(1);
            if(map.containsKey(rst.getInt(1))){
                map.put(rst.getInt(1), map.get(rst.getInt(1) ) + 1);
            }
            else{
                map.put(rst.getInt(1), 1);
            }
        }
        //now from that data on courses and participants decide on what to do:
        //we have a max and min quota, if someone is above the quota or below it,
        //they get transferred to another course
//LEAVING IT FOR NOW
        for(Map.Entry<Integer, Integer> mapElement : map.entrySet()){
            if(mapElement. getValue() > min && mapElement.getValue() <= max){
                values.add(new TwoInts(mapElement.getKey(), mapElement.getValue()));
            }
        }

        if(values.size() < 8){

            //not enough lectures here
            //what do I do??? -> I try to add values so that I have at least 8 attendable courses
//            Iterator iterator = map.entrySet().iterator();
//            while(iterator.hasNext()){
//                if(iterator.next())
//            }

        }


        for(TwoInts tp : values){
            //courses that have too few students
            //assign students to other events/courses
            for(int i = 0; i < tp.getiAttending(); i++){
                String sql123 = "SELECT kings_id FROM students_lectures WHERE lecture_code = " + Integer.toString(tp.getiCode());

                ResultSet rS5 = statement.executeQuery(sql123);
                Iterator iterator = map.entrySet().iterator();

                while(rS5.next()){
                    String sql09 = "UPDATE students_lectures SET lecture_code = ? WHERE lecture_code = ? AND kings_id = " + Integer.toString(rS5.getInt(1));
                    PreparedStatement statement1 = connection.prepareStatement(sql09);
                    statement1.setInt(1,tp.getiCode());         //fix this one
                    statement1.setInt(2, tp.getiCode());
                }
            }

        }
        System.out.println(values);
    }
}
