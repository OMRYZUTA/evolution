package il.ac.mta.zuli.evolution.engine.rules;

import il.ac.mta.zuli.evolution.engine.Quintet;
import il.ac.mta.zuli.evolution.engine.TimeTableSolution;
import il.ac.mta.zuli.evolution.engine.evolutionengine.Solution;
import il.ac.mta.zuli.evolution.engine.timetable.Requirement;
import il.ac.mta.zuli.evolution.engine.timetable.SchoolClass;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Satisfactory extends Rule {
    //satisfactory rule - each class gets the exact number of hours-per-subject (the class' requirements are met)
    Map<Integer, SchoolClass> schoolClasses;

    public Satisfactory(String ruleType, Map<Integer, SchoolClass> schoolClasses) { //TODO return not null
        super(ruleType);
        this.schoolClasses = schoolClasses;
    }

    /*@Override
    public void fitnessEvaluation(Solution solution) {
        if (!(solution instanceof TimeTableSolution)) {
            throw new RuntimeException("solution must be TimeTableSolution");
        }

        TimeTableSolution timeTableSolution = (TimeTableSolution) solution;
        double score = INVALIDSCORE;
        Map<Integer, Integer> sumPerSubject;
        // map< classID,Map<subjectID, HourCounter>>
        Map<Integer, Map<Integer, Integer>> subjectBucketPerClass = new HashMap<>();
        for (SchoolClass schoolClass : schoolClasses.values()) {
            sumPerSubject = new HashMap<>();
            List<Integer> subjectIDs = schoolClass.getRequiredSubjectsIDs();
            for (Integer subjectID : subjectIDs) {
                sumPerSubject.put(subjectID, 0);
            }
            subjectBucketPerClass.put(schoolClass.getId(), sumPerSubject);
        }


        List<Quintet> solutionQuintets = timeTableSolution.getSolution();
        for (Quintet quintet : solutionQuintets) {
            sumPerSubject = subjectBucketPerClass.get(quintet.getSchoolClassID());
            sumPerSubject.put(quintet.getSubject().getId(), sumPerSubject.get(quintet.getSubject().getId()) + 1);
        }
        System.out.println(subjectBucketPerClass);
        Map<Integer, Double> classIDPerScore = new HashMap<>();
        int totalScore = 0;
        for (SchoolClass schoolClass : schoolClasses.values()) {
            totalScore = 0;
            for (Requirement requirement : schoolClass.getRequirements()) {
                int actualSubjectHours = subjectBucketPerClass.get(schoolClass.getId()).get(requirement.getSubject().getId());
                int expectedSubjectHours = requirement.getHours();
                if (actualSubjectHours == expectedSubjectHours) {
                    totalScore += 100;
                }
            }
            classIDPerScore.put(schoolClass.getId(), ((double) totalScore) / schoolClass.getRequirements().size());
        }
        double classesTotal = 0;
        for (double classScore : classIDPerScore.values()) {
            classesTotal += classScore;
        }
        score = classesTotal / schoolClasses.size();
        System.out.println("score: " + score);
        timeTableSolution.addScoreToRule(this, score);
    }*/

    @Override
    public void fitnessEvaluation(Solution solution) {
        if (!(solution instanceof TimeTableSolution)) {
            throw new RuntimeException("solution must be TimeTableSolution");
        }

        TimeTableSolution timeTableSolution = (TimeTableSolution) solution;

        double[] classScores = new double[schoolClasses.size()]; //classID will be used as index (sort of a bucket)

        for (SchoolClass schoolClass : schoolClasses.values()) {
            int classID = schoolClass.getId();
            classScores[classID - 1] = classFitnessEvaluation(classID, timeTableSolution.getSubSolutionForClass(classID));
        }

        double score = 0;

        if (this.isHardRule()) {
            if (!Arrays.stream(classScores).anyMatch(n -> n == HARDRULEFAILURE)) {
                score = HARDRULEFAILURE;
            }
        } else {
            score = Arrays.stream(classScores).average().getAsDouble();
        }

        timeTableSolution.addScoreToRule(this, score);

    }

    private double classFitnessEvaluation(int classID, List<Quintet> subSolutionForClass) {
        List<Requirement> classRequirements = schoolClasses.get(classID).getRequirements();
        int numOfRequiredSubjects = classRequirements.size();

        Map<Integer, Integer> hoursPerSubjectCounter = initializeCounterMap(classRequirements);

        countHoursInSubSolution(subSolutionForClass, hoursPerSubjectCounter);

        int satisfactorySubjects = countSatisfactorySubjects(classRequirements, hoursPerSubjectCounter);

        double score = 0;

        if (this.isHardRule()) {
            //if there exists a subject that didn't meet the required hours
            if (satisfactorySubjects != numOfRequiredSubjects)
                score = HARDRULEFAILURE;
        } else {
            score = (100 * satisfactorySubjects) / numOfRequiredSubjects;
        }

        return score;
    }

    private int countSatisfactorySubjects(List<Requirement> classRequirements, Map<Integer, Integer> hoursPerSubjectCounter) {
        int satisfactorySubjects = 0;
        int subjectID;

        for (Requirement requirement : classRequirements) {
            subjectID = requirement.getSubject().getId();

            if (requirement.isRequirementMet(hoursPerSubjectCounter.get(subjectID))) {
                satisfactorySubjects++;
            }
        }

        return satisfactorySubjects;
    }

    private void countHoursInSubSolution(List<Quintet> subSolutionForClass, Map<Integer, Integer> hoursPerSubjectCounter) {
        int subjectID;

        for (Quintet quintet : subSolutionForClass) {
            subjectID = quintet.getSubject().getId();
            hoursPerSubjectCounter.merge(subjectID, 1, Integer::sum); //hours for subject ++
        }
    }

    @NotNull
    private Map<Integer, Integer> initializeCounterMap(List<Requirement> classRequirements) {
        //Map<subjectID, hoursInSolution>
        Map<Integer, Integer> hoursPerSubjectCounter = new HashMap<>();

        for (Requirement requirement : classRequirements) {
            hoursPerSubjectCounter.put(requirement.getSubject().getId(), 0);
        }

        return hoursPerSubjectCounter;
    }
}

