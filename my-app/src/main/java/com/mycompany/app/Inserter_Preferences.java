package com.mycompany.app;

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

    void v_insertStatement(int iPrev, int iKingsID, int iPrivileged) throws SQLException {
        String sql2 = "INSERT INTO CURICULUM.PREFERENCES VALUES (?,?,?)";
        PreparedStatement preparedStmt = connection.prepareStatement(sql2);
        preparedStmt.setInt(1, iPrev);
        preparedStmt.setInt(2, iKingsID);
        preparedStmt.setInt(3, iPrivileged);

        preparedStmt.execute();

    }

    void v_truncatePreferencesTable() throws SQLException {
        String sql2 = "TRUNCATE TABLE CURICULUM.PREFERENCES";
        PreparedStatement preparedStmt = connection.prepareStatement(sql2);
        preparedStmt.execute();

    }

    void v_populate() throws SQLException {
        Statement stmt = connection.createStatement();
        String sql2 = "SELECT kings_id " +
                      "FROM students";
        ResultSet rs2 = stmt.executeQuery(sql2);
        v_truncatePreferencesTable();

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


    void v_populate_teachers_preferences(int iChoiceOnDays) throws SQLException {
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
                String sWeek[] = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
                sWeekDay = sWeek[iRand1];
            }
            else {
                int iRand2 = (int) (Math.random() * (5 - 1 + 0) + 0);
                String sWeek[] = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
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
                System.out.println(rs2.getInt("teachers_id") + " " + rs2.getString("course") + " "+ sWeekDay + " " +iRandom + " " + iRandom2);
            prs.execute();
            prs.close();
        }
        rs2.close();

    }

    void v_truncate_teachers_preferences() throws SQLException{

        Statement stmt = connection.createStatement();
        String sql = "TRUNCATE CURICULUM.main_preferences_lectures_teachers";
        ResultSet rst = stmt.executeQuery(sql);
        rst.close();

    }

    void v_populate_students_preferences(int iChoiceOnDays, int iSemester, int iAcademicYear) throws SQLException {

        String sCourses = "";
        String sDescription = "SEM" + Integer.toString(iSemester);
        String sql31 = "SELECT kings_id " +
                     "FROM CURICULUM.students " +
                     "WHERE year_od_study = " + Integer.toString(iAcademicYear);

        String sql2 = "SELECT abreviation " +
                      "FROM CURICULUM.s_courses " +
                      "WHERE abreviation LIKE ? " +
                      "AND description LIKE ?";

        ArrayList<String> mylist = new ArrayList<>(4);
        mylist.add("Monday");
        mylist.add("Tuesday");
        mylist.add("Wednesday");
        mylist.add("Thursday");
        mylist.add("Friday");

        if (iChoiceOnDays == 0) {

            if(iAcademicYear == 1){
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
            System.out.println();
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
                        prs2.setInt(5, iRandom*100 + 30 + 300); }
                    else{
                        prs2.setInt(4, iRandom*100 );
                        prs2.setInt(5, iRandom*100 + 300);
                    }
                    prs2.execute();
                    prs2.close();
                }
                rst.beforeFirst();

            }

            for(int i = 0; i < 10; i++){

                System.out.println();
            }

        }
        else {

            mylist.add("Saturday");
            //Still not developed, possibility to add Saturday as a school day

            }
        }


    }


