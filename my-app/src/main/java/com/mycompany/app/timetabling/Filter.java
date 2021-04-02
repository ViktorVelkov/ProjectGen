package com.mycompany.app.timetabling;

import java.sql.Connection;

public class Filter {

    private Connection connection;

    public Filter(Connection connection) {
        this.connection = connection;
    }
    public Filter() { }

    public void assign(Week_Timetable first, Week_Timetable following, int AssigningCondition){

        if(AssigningCondition == 1){
               //assign all lgts to the first week
                for(int k = 0; k < first.getLectures().size(); k++){
                    // update the lecture's Duration by 1 hour and switch variable
                        double iHours = first.getLectures().get(k).getiHours();
                        first.getLectures().get(k).setiHours(iHours + 1);
                        first.getLectures().get(k).setLgtAssigned(1);
                }
        }

        else{
            //assign all lgts to the second week
                //I am not sure if there are lectur LLGTts in the following week
                //
                //assign all lgts to the following week
                for(int k = 0; k < following.getLectures().size(); k++){
                    // update the lecture's Duration by 1 hour and switch variable
                        double iHours = following.getLectures().get(k).getiHours();
                        following.getLectures().get(k).setiHours(iHours + 1);
                        following.getLectures().get(k).setLgtAssigned(1);

                }


        }


    }

}
