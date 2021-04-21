package com.mycompany.app.timetabling;

import java.util.ArrayList;

public class Solution implements Comparable<Solution> {

    private ArrayList<SGT> ASSIGNED_SGTS_CURRENTWEEK_SOLUTION = new ArrayList<SGT>();
    private ArrayList<SGT> UNASSIGNED_SGTS_CURRENTWEEK_SOLUTION = new ArrayList<SGT>();
    private HeuristicEvaluation heuristicEvaluation = new HeuristicEvaluation();
    public void setassigned_SGTs(ArrayList<SGT> solution){
       //strip the sgts down of their dependencies, would take too much data to hold all the repetitions of information once the HC algorithm is run
        for(SGT sgt : solution){
           this.ASSIGNED_SGTS_CURRENTWEEK_SOLUTION.add(new SGT(sgt.getsLect(), sgt.getDayAssigned(), sgt.getsLectureHall(), sgt.getiHourScheduled(), sgt.getiHours(), sgt.getiAdditionalCode(), sgt.getCodesOfStudentsAssigned(), sgt.getiDayScheduled(), sgt.getiMonthScheduled(), sgt.getiYearScheduled()));
       }
    }

    public ArrayList<SGT> getAssignedSGTs(){
        return this.ASSIGNED_SGTS_CURRENTWEEK_SOLUTION;
    }

    public ArrayList<SGT> getUnassignedSGTs() {
        return UNASSIGNED_SGTS_CURRENTWEEK_SOLUTION;
    }

    public void setUnassignedSGTs(ArrayList<SGT> UNASSIGNED_SGTS_CURRENTWEEK_SOLUTION) {
        this.UNASSIGNED_SGTS_CURRENTWEEK_SOLUTION = UNASSIGNED_SGTS_CURRENTWEEK_SOLUTION;
    }

    public HeuristicEvaluation getHeuristicEvaluation() {
        return heuristicEvaluation;
    }

    public void setHeuristicEvaluation(HeuristicEvaluation heuristicEvaluation) { this.heuristicEvaluation = heuristicEvaluation; }

    @Override
    public String toString() {
        return "Solution{" +
                " heuristicEvaluation=" + heuristicEvaluation +
                '}';
    }

    @Override
    public int compareTo(Solution o) {
        return (int)((this.heuristicEvaluation.getPercentSatisfiability() - o.getHeuristicEvaluation().getPercentSatisfiability())*10000);
    }
}
