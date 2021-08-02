package il.ac.mta.zuli.evolution.engine.rules;

import il.ac.mta.zuli.evolution.engine.Quintet;
import il.ac.mta.zuli.evolution.engine.evolutionengine.Solution;
import il.ac.mta.zuli.evolution.engine.timetable.SchoolClass;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

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
    public void fitnessEvaluation(Solution solution) {
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
        Collection<Quintet> solutionQuintets = solution.getSolution();

        ArrayList<ArrayList<Quintet>> lists = new ArrayList<ArrayList<Quintet>>(
                (Collection<ArrayList<Quintet>>) solutionQuintets.parallelStream()
                        .collect(Collectors.groupingBy(Quintet::getSchoolClassID)));

        System.out.println("in satisfactory rule: " + lists);

        //Comparator c = (Computer c1, Computer c2) -> c1.getAge().compareTo(c2.getAge());


        return 0;
    }
}
