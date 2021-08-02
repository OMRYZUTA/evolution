package il.ac.mta.zuli.evolution.engine.rules;

import il.ac.mta.zuli.evolution.engine.Quintet;
import il.ac.mta.zuli.evolution.engine.TimeTableSolution;
import il.ac.mta.zuli.evolution.engine.evolutionengine.Solution;
import il.ac.mta.zuli.evolution.engine.timetable.SchoolClass;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Satisfactory extends Rule {
    //satisfactory rule - each class gets the exact number of hours-per-subject (the class' requirements are met)
    Map<Integer, SchoolClass> schoolClasses;

    public Satisfactory(String ruleType, Map<Integer, SchoolClass> schoolClasses) {
        super(ruleType);
        this.schoolClasses = schoolClasses; //is a reference enough or do I need to copy it properly?
        // will it be used as readonly?
    }

    @Override
    public void fitnessEvaluation(Solution solution) {
        if (!(solution instanceof TimeTableSolution)) {
            throw new RuntimeException("solution must be TimeTableSolution");
        }

        TimeTableSolution timeTableSolution = (TimeTableSolution) solution;
        int score = INVALIDSCORE;

        //TODO implement
        //iterate throw SchoolClass map, for each class-sc:
        //solution.getSolutionforClass(SchoolClass sc)
        //sum up hours per subject
        //compare to requirements

        //or maybe as stream - first sort the Quintets according to class, then separate to collection-per-class
        //then sum up hours-per-subject-per-class in the Solution
        //and compare to the requirements

        //in order to check every class gets the requiredHours-per Subject:
        //1.to get solution only per class
        //2.

        /*solution.getSolution().stream()
                .sorted(
                        (Quintet q1, Quintet q2) -> Integer.compare(q1.getSchoolClass().getId(), q2.getSchoolClass().getId()).*/
        List<Quintet> solutionQuintets = timeTableSolution.getSolution();

        ArrayList<ArrayList<Quintet>> solutionsPerClass = new ArrayList<ArrayList<Quintet>>(
                (Collection<ArrayList<Quintet>>) solutionQuintets.parallelStream()
                        .collect(Collectors.groupingBy(Quintet::getSchoolClassID)));

        System.out.println(solutionsPerClass.get(0));

        timeTableSolution.addScoreToRule(this, score);
    }
}
