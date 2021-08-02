package il.ac.mta.zuli.evolution.engine.rules;

import il.ac.mta.zuli.evolution.engine.Quintet;
import il.ac.mta.zuli.evolution.engine.TimeTableSolution;
import il.ac.mta.zuli.evolution.engine.evolutionengine.Solution;
import il.ac.mta.zuli.evolution.engine.timetable.Requirement;
import il.ac.mta.zuli.evolution.engine.timetable.SchoolClass;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Satisfactory extends Rule {
    //satisfactory rule - each class gets the exact number of hours-per-subject (the class' requirements are met)
    Map<Integer, SchoolClass> schoolClasses;


    public Satisfactory( String ruleType,  Map<Integer, SchoolClass> schoolClasses) { //TODO return not null
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
        double score = INVALIDSCORE;
        Map <Integer,Integer> sumPerSubject;
        // map< classID,Map<subjectID, HourCounter>>
        Map< Integer,Map<Integer, Integer>> subjectBucketPerClass = new HashMap<>();
        for (SchoolClass schoolClass: schoolClasses.values()) {
            sumPerSubject = new HashMap<>();
            List<Integer> subjectIDs = schoolClass.getRequiredSubjectsIDs();
            for (Integer subjectID:subjectIDs) {
                sumPerSubject.put(subjectID,0);
            }
            subjectBucketPerClass.put(schoolClass.getId(),sumPerSubject);
        }


        List<Quintet> solutionQuintets = timeTableSolution.getSolution();
        for (Quintet quintet: solutionQuintets) {
            sumPerSubject =subjectBucketPerClass.get(quintet.getSchoolClassID());
            sumPerSubject.put(quintet.getSubject().getId(),sumPerSubject.get(quintet.getSubject().getId())+1);
        }
        System.out.println(subjectBucketPerClass);
        Map<Integer,Double> classIDPerScore= new HashMap<>();
        int totalScore = 0;
        for (SchoolClass schoolClass: schoolClasses.values()) {
            totalScore =0;
            for (Requirement requirement: schoolClass.getRequirements() ) {
                int actualSubjectHours =subjectBucketPerClass.get(schoolClass.getId()).get(requirement.getSubject().getId());
                int expectedSubjectHours = requirement.getHours();
                if(actualSubjectHours ==expectedSubjectHours){
                    totalScore+=100;
                }
            }
            classIDPerScore.put(schoolClass.getId(), ((double) totalScore)/ schoolClass.getRequirements().size());
        }
        double classesTotal =0;
        for (double classScore:classIDPerScore.values()) {
            classesTotal += classScore;
        }
        score = classesTotal/ schoolClasses.size();
        System.out.println("score: "+score);
        timeTableSolution.addScoreToRule(this, score);
    }
}
