package il.ac.mta.zuli.evolution.engine.rules;

import il.ac.mta.zuli.evolution.engine.Quintet;
import il.ac.mta.zuli.evolution.engine.TimeTableSolution;
import il.ac.mta.zuli.evolution.engine.evolutionengine.Solution;
import il.ac.mta.zuli.evolution.engine.exceptions.EmptyCollectionException;
import il.ac.mta.zuli.evolution.engine.timetable.Requirement;
import il.ac.mta.zuli.evolution.engine.timetable.SchoolClass;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class Satisfactory extends Rule {
    //satisfactory rule - each class gets the exact number of hours-per-subject (the class' requirements are met)
    private Map<Integer, SchoolClass> schoolClasses;
    private final String ruleName;
    public Satisfactory(@NotNull String ruleType, @NotNull List<SchoolClass> schoolClasses) {
        super(ruleType);
        this.ruleName="Satisfactory";
        setSchoolClasses(schoolClasses);
    }

    private void setSchoolClasses(List<SchoolClass> schoolClasses) {
        if (schoolClasses.size() == 0) {
            throw new EmptyCollectionException("Empty school-classes-list in satisfactory-rule");
        }
        Map<Integer, SchoolClass> integerSchoolClassMap = new HashMap<>();
        for (SchoolClass schoolClass: schoolClasses) {
            integerSchoolClassMap.put(schoolClass.getId(),schoolClass);
        }

        this.schoolClasses = integerSchoolClassMap;
    }

    @Override
    public void fitnessEvaluation(@NotNull Solution solution) {
        if (!(solution instanceof TimeTableSolution)) {
            throw new RuntimeException("solution must be TimeTableSolution");
        }

        TimeTableSolution timeTableSolution = (TimeTableSolution) solution;

        double score = 0;
        double[] classScores = new double[schoolClasses.size()]; //classID will be used as index (a sort of bucket)

        if (timeTableSolution.getSolutionSize() > 0) {
            for (SchoolClass schoolClass : schoolClasses.values()) {
                int classID = schoolClass.getId();
                classScores[classID - 1] = classFitnessEvaluation(classID, timeTableSolution.getSubSolutionForClass(classID));
            }

            score = Arrays.stream(classScores).average().getAsDouble();
        }

        timeTableSolution.addScoreToRule(this, score);
    }

    private double classFitnessEvaluation(int classID, List<Quintet> subSolutionForClass) {
        double score = 0;

        if (subSolutionForClass.size() > 0) {
            List<Requirement> classRequirements = schoolClasses.get(classID).getRequirements();
            int numOfRequiredSubjects = classRequirements.size();
            Map<Integer, Integer> hoursPerSubjectCounter = initializeCounterMap(classRequirements);
            countHoursInSubSolution(subSolutionForClass, hoursPerSubjectCounter);
            int satisfactorySubjects = countSatisfactorySubjects(classRequirements, hoursPerSubjectCounter);

            score = (100 * satisfactorySubjects) / (double) numOfRequiredSubjects;
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
        //we only call this function after we check (subSolutionForClass.size() > 0)
        for (Quintet quintet : subSolutionForClass) {
            subjectID = quintet.getSubject().getId();
            hoursPerSubjectCounter.merge(subjectID, 1, Integer::sum); //hours for subject ++
        }
    }

    @NotNull
    private Map<Integer, Integer> initializeCounterMap(List<Requirement> classRequirements) {
        //Map<subjectID, hoursInSolution>
        Map<Integer, Integer> hoursPerSubjectCounter = new HashMap<>();

        if (classRequirements.size() > 0) {
            for (Requirement requirement : classRequirements) {
                hoursPerSubjectCounter.put(requirement.getSubject().getId(), 0);
            }
        }

        return hoursPerSubjectCounter;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getClass());
    }

    @Override
    public boolean equals(Object obj) {
        return this.getClass() == obj.getClass();
    }
}

