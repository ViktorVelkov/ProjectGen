package com.mycompany.app.inserts.data;

import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;


public class Inserter_Preferences {

    private Connection connection;

    public Inserter_Preferences(Connection connection) {
        this.connection = connection;
    }



    public void v_createTablePreferences() throws SQLException {
        String sql99 = "DROP TABLE IF EXISTS PREFERENCES";
        String sql87 = "CREATE TABLE PREFERENCES(" +
                        "SCALE INT(1), " +
                        "KINGS_ID INT(10), " +
                        "privileged INT(1)," +
                        "PRIMARY KEY(KINGS_ID))";
        Statement statement = connection.createStatement();
        statement.executeUpdate(sql99);
        statement.executeUpdate(sql87);
        statement.close();
    }



    // method used indirectly
    private void v_insertStatement(int iPrev, int iKingsID, int iPrivileged) throws SQLException {
        String sql2 = "INSERT INTO CURICULUM.PREFERENCES VALUES (?,?,?)";
        PreparedStatement preparedStmt = connection.prepareStatement(sql2);
        preparedStmt.setInt(1, iPrev);
        preparedStmt.setInt(2, iKingsID);
        preparedStmt.setInt(3, iPrivileged);

        preparedStmt.execute();

    }

    private void v_truncatePreferencesTable() throws SQLException {
        String sql2 = "TRUNCATE TABLE CURICULUM.PREFERENCES";
        PreparedStatement preparedStmt = connection.prepareStatement(sql2);
        preparedStmt.execute();

    }

    public void v_populate_preferences_initial() throws SQLException {


        v_truncatePreferencesTable();
        Statement stmt = connection.createStatement();
        String sql2 = "SELECT kings_id " +
                      "FROM students";
        ResultSet rs2 = stmt.executeQuery(sql2);

        int counterA = 0;
        while (rs2.next()) {
            int random_int = (int) (Math.random() * (10 - 1 + 1) + 1);
            counterA++;

            int iTmp = rs2.getInt("kings_id");
            if (counterA % 10 == 0) {
                v_insertStatement(random_int, iTmp, 1);
            } else {
                v_insertStatement(random_int, iTmp, 0);
            }
        }
        rs2.close();
    }

    void v_create_teachersp_table() throws SQLException {
        String sql99 = "DROP TABLE IF EXISTS main_preferences_lectures_teachers";
        String sql87 = "CREATE TABLE `main_preferences_lectures_teachers` (  " +
                "  `teacher_id` int DEFAULT NULL,  " +
                "  `course_abreviation` varchar(10) DEFAULT NULL,  " +
                "  `prefday` varchar(9) DEFAULT NULL,  " +
                "  `preftime_start` int DEFAULT NULL,  " +
                "  `preftime_end` int DEFAULT NULL,  " +
                "  UNIQUE KEY `course_abreviation` (`course_abreviation`,`teacher_id`),  " +
                "  KEY `teacher_id` (`teacher_id`),  " +
                "  CONSTRAINT `main_preferences_lectures_teachers_ibfk_1` FOREIGN KEY (`course_abreviation`) REFERENCES `s_courses` (`abreviation`),  " +
                "  CONSTRAINT `main_preferences_lectures_teachers_ibfk_2` FOREIGN KEY (`teacher_id`) REFERENCES `staff` (`teachers_id`)  " +
                ") ";
        Statement statement = connection.createStatement();
        statement.executeUpdate(sql99);
        statement.executeUpdate(sql87);
        statement.close();

    }

    void v_create_studentsp_table() throws SQLException {
        String sql99 = "DROP TABLE IF EXISTS main_preferences_lectures_students";
        String sql87 = "CREATE TABLE `main_preferences_lectures_students` (" +
                "  `kings_id_fk` int unsigned DEFAULT NULL," +
                "  `course_abreviation` varchar(10) DEFAULT NULL," +
                "  `prefday` varchar(9) DEFAULT NULL," +
                "  `preftime` int DEFAULT NULL, " +
                "  `preftime_secondchoice` int DEFAULT NULL, " +
                "  UNIQUE KEY `kings_id_fk` (`kings_id_fk`,`course_abreviation`)," +
                "  CONSTRAINT `main_preferences_lectures_students_ibfk_2` FOREIGN KEY (`kings_id_fk`) REFERENCES `students` (`KINGS_ID`) " +
                ") ";
        Statement statement = connection.createStatement();
        statement.executeUpdate(sql99);
        statement.executeUpdate(sql87);
        statement.close();

    }

    void v_populate_teachers_preferences_initial(int iChoiceOnDays) throws SQLException {
        String sWeekDay;


        Statement stmt = connection.createStatement();
        String sql1 = "SELECT teachers_id, course " +
                      "FROM curiculum.staff";
        ResultSet rs2 = stmt.executeQuery(sql1);

        while(rs2.next()) {

            int iRandom = (int) (Math.random() * (15 - 9 + 0) + 9);
            int iRandom2 = (int) (Math.random() * (15 - 9 + 0) + 9);

            if (iChoiceOnDays == 0) {
                int iRand1 = (int) (Math.random() * (4 - 1 + 0) + 0);
                String sWeek[] = {"Monday", "Tuesday", "Wednesday", "Thursday"};
                sWeekDay = sWeek[iRand1];
            }
            else {
                int iRand2 = (int) (Math.random() * (5 - 1 + 0) + 0);
                String sWeek[] = {"Monday", "Tuesday", "Wednesday", "Thursday","Friday"};
                sWeekDay = sWeek[iRand2];
            }

            String sql23 = "INSERT INTO main_preferences_lectures_teachers VALUES(?,?,?,?,?)";
            PreparedStatement prs = connection.prepareStatement(sql23);
            prs.setInt(1, rs2.getInt("teachers_id"));
            prs.setString(2, rs2.getString("course"));
            prs.setString(3, sWeekDay);
                if(iRandom2 % 2 == 1){
                    prs.setInt(4, iRandom*100 + 30);
                    prs.setInt(5, iRandom*100 + 30 + 300); }
                else{
                    prs.setInt(4, iRandom*100 );
                    prs.setInt(5, iRandom*100 + 300);

                }
            prs.execute();
            prs.close();
        }
        rs2.close();

    }

    void v_truncate_teachers_preferences_initial() throws SQLException{

        Statement stmt = connection.createStatement();
        String sql = "TRUNCATE CURICULUM.main_preferences_lectures_teachers";
        ResultSet rst = stmt.executeQuery(sql);
        rst.close();

    }
    void v_truncate_students_preferences() throws SQLException{

        Statement stmt = connection.createStatement();
        String sql = "TRUNCATE CURICULUM.main_preferences_lectures_students";
        stmt.executeUpdate(sql);
        stmt.close();

    }

    /*
    * This method inserts preferences for the lectures' days
    * */
    void v_populate_students_preferences_initial(int iOption, int iChoiceOnDays, int iSemester, int iAcademicYear, String sTableStudents, String sTableCourses) throws SQLException {

        String sCourses = "";
        String sDescription = "SEM" + Integer.toString(iSemester);
        String sql31 = "SELECT kings_id " +
                     "FROM  " + sTableStudents + " " +
                     "WHERE year_od_study = " + Integer.toString(iAcademicYear);

        String sql2 = "SELECT abreviation " +
                      "FROM " + sTableCourses + " " +
                      "WHERE abreviation LIKE ? " +
                      "AND description LIKE ?";

        ArrayList<String> mylist = new ArrayList<>(4);
        mylist.add("Monday");
        mylist.add("Tuesday");
        mylist.add("Wednesday");
        mylist.add("Thursday");
        if(iOption != 0){
            mylist.add("Friday");
        }



        if (iChoiceOnDays == 0) {

            if(iAcademicYear == 1){//can be switched to else if
                sCourses = "4CCS";
            }
            if(iAcademicYear == 2){
                sCourses = "5CCS";
            }
            if(iAcademicYear == 3){
                sCourses = "6CCS";
            }
            Statement stmt = connection.createStatement();
            PreparedStatement prst = connection.prepareStatement(sql2);
            prst.setString(1, sCourses + "%" );
            prst.setString(2, sDescription + "%");
            ResultSet rst = prst.executeQuery();
            ResultSet resSet = stmt.executeQuery(sql31);

            while(resSet.next()) {

                ArrayList<String> onelist = new ArrayList<>(mylist);

                while (rst.next()) {
                    String sDay = "";
                    int iRandom = (int) (Math.random() * (15 - 9 + 0) + 9);
                    int iRandom2 = (int) (Math.random() * (15 - 9 + 0) + 9);
                    int iRand2 = (int)(Math.random()*((onelist.size() - 1)-0 +1) + 0);

                    sDay = onelist.get(iRand2);
                    onelist.remove(iRand2);

                    String sql23 = "INSERT INTO main_preferences_lectures_students VALUES(?,?,?,?,?)";
                    PreparedStatement prs2 = connection.prepareStatement(sql23);
                    prs2.setInt(1,resSet.getInt("kings_id"));
                    prs2.setString(2,rst.getString("abreviation"));
                    prs2.setString(3, sDay);
                    if(iRandom2 % 2 == 1){
                        prs2.setInt(4, iRandom*100 + 30);
                        iRandom = (int) (Math.random() * (15 - 9 + 0) + 9);
                        iRand2 = (int)(Math.random()*((10 - 1)-0 +1) + 0);
                        if(iRand2 % 2 == 1){
                            prs2.setInt(5, iRandom*100);
                        }
                        else{
                            prs2.setInt(5, iRandom*100 + 30);
                        }
                    }
                    else{
                        prs2.setInt(4, iRandom*100 );

                        iRandom = (int) (Math.random() * (15 - 9 + 0) + 9);
                        iRand2 = (int)(Math.random()*((10 - 1)-0 +1) + 0);
                        if(iRand2 % 2 == 1){
                            prs2.setInt(5, iRandom*100);
                        }
                        else{
                            prs2.setInt(5, iRandom*100 + 30);
                        }                    }
                    prs2.execute();
                    prs2.close();
                }
                rst.beforeFirst();

            }

        }
        else {

            mylist.add("Saturday");
            //Still not developed, possibility to add Saturday as a school day

            }
        }






    void v_populate_students_preferences_initial2(ArrayList<TwoInts> events, int iOption, int iChoiceOnDays, int iSemester, int iAcademicYear, String sTableStudents, String sTableCourses) throws SQLException {
            //events array is all the lectures which are going to be assigned
            //I need the preferreces for those

        String sCourses = "";
        String sDescription = "SEM" + Integer.toString(iSemester);
        String sql31 = "SELECT kings_id " +
                "FROM  " + sTableStudents + " " +
                "WHERE year_od_study = " + Integer.toString(iAcademicYear);

        String sql44 = "SELECT c.abreviation " +
                        "FROM  (select a.abreviation from courses a join students_lectures b on a.inside_code = b.lecture_code WHERE b.kings_id = ? AND b.semester = " + Integer.toString(iSemester) + " ) as c";


        ArrayList<String> mylist = new ArrayList<>(4);
        mylist.add("Monday");
        mylist.add("Tuesday");
        mylist.add("Wednesday");
        mylist.add("Thursday");
        if(iOption != 0){
            mylist.add("Friday");
        }



        if (iChoiceOnDays == 0) {

            if(iAcademicYear == 1){//can be switched to else if
                sCourses = "4CCS";
            }
            if(iAcademicYear == 2){
                sCourses = "5CCS";
            }
            if(iAcademicYear == 3){
                sCourses = "6CCS";
            }
            Statement stmt = connection.createStatement();
            PreparedStatement prst = connection.prepareStatement(sql44);
            ResultSet resSet = stmt.executeQuery(sql31);


            while(resSet.next()) {

                ArrayList<String> onelist = new ArrayList<>(mylist);
                prst.setInt(1, resSet.getInt(1));
                ResultSet rst = prst.executeQuery();

                while (rst.next()) {
                    String sDay = "";
                    int iRandom = (int) (Math.random() * (15 - 9 + 0) + 9);
                    int iRandom2 = (int) (Math.random() * (15 - 9 + 0) + 9);
                    int iRand2 = (int)(Math.random()*((onelist.size() - 1)-0 +1) + 0);

                    sDay = onelist.get(iRand2);         //the way I have implemented this, is that each student can pick just one day for a lecture/event,and that day cannot be then picked for another lecture
                                                        //maybe I can change that, and allow for two different lectures to be picked on the same day
                    //onelist.remove(iRand2);

                    String sql23 = "INSERT INTO main_preferences_lectures_students VALUES(?,?,?,?,?)";
                    PreparedStatement prs2 = connection.prepareStatement(sql23);
                    prs2.setInt(1,resSet.getInt("kings_id"));
                    prs2.setString(2,rst.getString("abreviation"));
                    prs2.setString(3, sDay);
                    if(iRandom2 % 2 == 1){
                        //this will implement the hours preference for each lecture/event
                        prs2.setInt(4, iRandom*100 + 30);
                        iRandom = (int) (Math.random() * (15 - 9 + 0) + 9);
                        iRand2 = (int)(Math.random()*((10 - 1)-0 +1) + 0);
                        if(iRand2 % 2 == 1){
                            prs2.setInt(5, iRandom*100);
                        }
                        else{
                            prs2.setInt(5, iRandom*100 + 30);
                        }
                    }
                    else{
                        prs2.setInt(4, iRandom*100 );

                        iRandom = (int) (Math.random() * (15 - 9 + 0) + 9);
                        iRand2 = (int)(Math.random()*((10 - 1)-0 +1) + 0);
                        if(iRand2 % 2 == 1){
                            prs2.setInt(5, iRandom*100);
                        }
                        else{
                            prs2.setInt(5, iRandom*100 + 30);
                        }                    }
                    prs2.execute();
                    prs2.close();
                }
                rst.beforeFirst();

            }

        }
        else {

            mylist.add("Saturday");
            //Still not developed, possibility to add Saturday as a school day

        }}

    }


