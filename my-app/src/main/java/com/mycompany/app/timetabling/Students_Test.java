package com.mycompany.app.timetabling;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

public class Students_Test implements Comparable<Students_Test> {
    private int iCode1;
    private int iCode2;
    private int iCode3;
    private int iCode4;
    private Connection connection;

    public Students_Test(int iCode1, int iCode2, int iCode3, int iCode4) {
        this.iCode1 = iCode1;
        this.iCode2 = iCode2;
        this.iCode3 = iCode3;
        this.iCode4 = iCode4;
    }

    public Students_Test(Connection connection) {

        this.connection = connection;
    }

    public void test() throws SQLException {
        String sql11 = "SELECT kings_id, lecture_code FROM students_lectures WHERE kings_id= ?";
        String sql10 = "SELECT kings_id FROM students WHERE kings_id < 1900000";
        HashMap<Students_Test, Integer>map = new HashMap<>();


        Statement state = connection.createStatement();
        PreparedStatement statement = connection.prepareStatement(sql11);
        ResultSet resultSet = state.executeQuery(sql10);
        while(resultSet.next()){
            statement.setInt(1,resultSet.getInt(1));
            ResultSet resultSet1 = statement.executeQuery();
            while (resultSet1.next()){
                int iCode11 = resultSet1.getInt(2);
                resultSet1.next();
                int iCode22 = resultSet1.getInt(2);
                resultSet1.next();
                int iCode33 = resultSet1.getInt(2);
                resultSet1.next();
                int iCode44 = resultSet1.getInt(2);

                Students_Test studet = new Students_Test(iCode11, iCode22,iCode33,iCode44);
                boolean b = map.containsKey(studet);
                int flag = 0;
                int iCounter = 0;
                    Iterator itr = map.keySet().iterator();
                    while(itr.hasNext()){

                        Students_Test testero = (Students_Test) itr.next();
                        if(testero.compareTo(studet) == 0){
                            flag = 1;
                            break;
                        }
                        iCounter++;
                }

                    if(flag == 1){
                        Iterator itr2 = map.keySet().iterator();
                        for(int z=0; z <iCounter; z++){
                            Students_Test stru = (Students_Test) itr2.next();
                            if(z == iCounter - 1){
                                map.put(stru, map.get(stru) + 1);
                            }

                        }
                    }
                    else {
                        map.put(studet, 0);
                    }
            }
        }
        System.out.println(Arrays.asList(map));
    }

    @Override
    public String toString() {
        return "Students_Test{" +
                "iCode1=" + iCode1 +
                ", iCode2=" + iCode2 +
                ", iCode3=" + iCode3 +
                ", iCode4=" + iCode4 +
                '}';
    }

    @Override
    public int compareTo(Students_Test o) {
        int ifive = 0;

        int ione = (this.iCode1 - o.iCode1 );
        int itwo = (this.iCode2 - o.iCode2 );
        int ithree = (this.iCode3 - o.iCode3 );
        int ifour = (this.iCode4 - o.iCode4 );

        if(ione != 0) ifive = 1;
        if(itwo != 0) ifive = 1;
        if(ithree != 0) ifive = 1;
        if(ifour != 0) ifive = 1;

        return ifive;
    }
}
