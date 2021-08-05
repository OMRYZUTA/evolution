package il.ac.mta.zuli.evolution.engine;

import il.ac.mta.zuli.evolution.engine.evolutionengine.Solution;
import il.ac.mta.zuli.evolution.engine.rules.Rule;
import il.ac.mta.zuli.evolution.engine.timetable.SchoolClass;
import il.ac.mta.zuli.evolution.engine.timetable.Subject;
import il.ac.mta.zuli.evolution.engine.timetable.Teacher;
import il.ac.mta.zuli.evolution.engine.timetable.TimeTable;

import java.time.DayOfWeek;
import java.util.*;
import java.util.stream.Collectors;

import static il.ac.mta.zuli.evolution.engine.utils.generateRandomNum;

public class TimeTableSolution implements Solution {
    private List<Quintet> solution;
    private final int solutionSize; //number of quintets
    private double totalFitnessScore;
    private final Map<Rule, Double> fitnessScorePerRule;
    TimeTable timeTable;

    public TimeTableSolution(TimeTable timeTable) {
        fitnessScorePerRule = new HashMap<>();
        this.timeTable = timeTable;
        solutionSize = generateRandomNum(1, calculateTotalRequiredHours());
        totalFitnessScore = 0;
        randomlyGenerateSolutionQuintets();
    }

    public TimeTableSolution(List<Quintet> quintets, TimeTable timeTable) {
        this.timeTable =timeTable;
        fitnessScorePerRule = new HashMap<>();
        solutionSize = quintets.size();
        totalFitnessScore = 0;
        solution = new ArrayList<>();
        this.solution.addAll(quintets);
    }

    //#region getters
    public List<Quintet> getSolutionQuintets() {
        return Collections.unmodifiableList(solution);
    }

    public int getSolutionSize() {
        return solutionSize;
    }

    @Override
    public double getTotalFitnessScore() {
        return totalFitnessScore;
    }

    public Map<Rule, Double> getFitnessScorePerRule() {
        return Collections.unmodifiableMap(fitnessScorePerRule);
    }

    //TODO might return an empty list - need to check size of list returned when using this method
    public List<Quintet> getSubSolutionForClass(int schoolClassID) {
        List<Quintet> solutionForClass = solution.stream()
                .filter(p -> p.getSchoolClassID() == schoolClassID).collect(Collectors.toList());

        return Collections.unmodifiableList(solutionForClass);
    }

    //TODO might return an empty list - need to check size of list returned when using this method
    public List<Quintet> getSubSolutionForTeacher(int teacherID) {
        List<Quintet> solutionForTeacher = solution.stream()
                .filter(p -> p.getTeacher().getId() == teacherID).collect(Collectors.toList());

        return Collections.unmodifiableList(solutionForTeacher);
    }

    //#endregion

    //#region setters and quintet-generation methods
    private void randomlyGenerateSolutionQuintets() {
        Set<Quintet> solutionSet = generateQuintets(solutionSize);
        solution = new ArrayList<Quintet>(solutionSet.size());
        solution.addAll(solutionSet);
    }



    private Set<Quintet> generateQuintets(int num) {
        Set<Quintet> newSet = new HashSet<>();
        //initially creating the solution in order to prevent duplicate quintets

        while (newSet.size() < num) {
            newSet.add(generateRandomQuintet());
        }

        return newSet;
    }

    private Quintet generateRandomQuintet() {
        DayOfWeek randomDay = generateRandomDay();
        int randomHour = new Random().nextInt(timeTable.getHours());

        //randomly generate class
        int randomClassID = generateRandomNum(1, timeTable.getSchoolClasses().size());
        SchoolClass randomSchoolClass = timeTable.getSchoolClasses().get(randomClassID);

        //randomly generate subject - randomly but only from class-subjects
        List<Integer> classRequiredSubjectsIDs = randomSchoolClass.getRequiredSubjectsIDs();
        int randomIndex = new Random().nextInt(classRequiredSubjectsIDs.size());
        Subject randomSubject = timeTable.getSubjects().get(classRequiredSubjectsIDs.get(randomIndex));

        //randomly generate teacher - randomly but only from teachers that teach the random subject
        List<Integer> TeachersIDs = timeTable.getTeachersThatTeachSubject(randomSubject.getId());
        int randomTeachersIndex = new Random().nextInt(TeachersIDs.size());
        Teacher randomTeacher = timeTable.getTeachers().get(TeachersIDs.get(randomTeachersIndex));

        return new Quintet(randomDay, randomHour, randomTeacher, randomSchoolClass, randomSubject);
    }

    private DayOfWeek generateRandomDay() {
        DayOfWeek[] enumValues = DayOfWeek.values();
        int randIndex = new Random().nextInt(timeTable.getDays());
        return enumValues[randIndex];
    }
    //#endregion

    public void addScoreToRule(Rule rule, double score) {
        fitnessScorePerRule.put(rule, score);
    }

    public void calculateTotalScore() {

        double softRuleSum = 0, hardRuleSum = 0;
        int numOfSoftRules = 0, numOfHardRules = 0;

        for (Map.Entry<Rule, Double> entry : fitnessScorePerRule.entrySet()) {
            if (entry.getKey().isHardRule()) {
                numOfHardRules++;
                hardRuleSum += entry.getValue();
            } else {
                numOfSoftRules++;
                softRuleSum += entry.getValue();
            }
        }

        double softRuleAvg = softRuleSum / numOfSoftRules;
        double hardRuleAvg = hardRuleSum / numOfHardRules;
        double hardRuleWeightedScore = (hardRuleAvg * timeTable.getHardRulesWeight()) / 100;
        double softRuleWeightedScore = (softRuleAvg * (100 - timeTable.getHardRulesWeight())) / 100;

        totalFitnessScore = hardRuleWeightedScore + softRuleWeightedScore;
    }

    private int calculateTotalRequiredHours() {
        int totalRequiredHours = 0;
        Map<Integer, SchoolClass> classes = timeTable.getSchoolClasses();

        for (SchoolClass c : classes.values()) {
            totalRequiredHours += c.getTotalRequiredHours();
        }

        return totalRequiredHours;
    }

    @Override
    public int compareTo(Solution other) {

        return ((Double) totalFitnessScore).compareTo(other.getTotalFitnessScore());
    }

    @Override
    public String toString() {
        return "TimeTableSolution=" + System.lineSeparator() +
                ", totalFitnessScore=" + totalFitnessScore + System.lineSeparator() +
                solution + "*****";
        //", solutionSize=" + solutionSize +
        //+", fitnessScorePerRole=" + fitnessScorePerRule;
    }


}
