package il.ac.mta.zuli.evolution.engine.rules;

import il.ac.mta.zuli.evolution.engine.TimeTableSolution;
import il.ac.mta.zuli.evolution.engine.timetable.SchoolClass;

import java.util.Map;

public class Satisfactory extends Rule {
    Map<Integer, SchoolClass> schoolClasses;

    /*public Satisfactory(String ruleType) {
        super(ruleType);
    }*/

    public Satisfactory(String ruleType, Map<Integer, SchoolClass> schoolClasses) {
        super(ruleType);
        this.schoolClasses = schoolClasses; //is a reference enough or do I need to copy it properly?
        // will it be used as readonly?
    }

    @Override
    public int fitnessEvaluation(TimeTableSolution solution) {
        //TODO implement
        //iterate throw SchoolClass map, for each class-sc:
        //solution.getSolutionforClass(SchoolClass sc)
        //sum up hours per subject
        //compare to requirements

        //or maybe as stream - first sort the Quintets according to class, then separate to collection-per-class
        //then sum up hours-per-subject-per-class in the Solution
        //and compare to the requirements
        return 0;
    }
}
