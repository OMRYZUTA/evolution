package il.ac.mta.zuli.evolution.engine;

import il.ac.mta.zuli.evolution.engine.evolutionengine.Solution;
import il.ac.mta.zuli.evolution.engine.exceptions.ValidationException;
import il.ac.mta.zuli.evolution.engine.rules.Rule;
import il.ac.mta.zuli.evolution.engine.timetable.SchoolClass;
import il.ac.mta.zuli.evolution.engine.timetable.Subject;
import il.ac.mta.zuli.evolution.engine.timetable.Teacher;
import il.ac.mta.zuli.evolution.engine.timetable.TimeTable;
import org.jetbrains.annotations.NotNull;

import java.time.DayOfWeek;
import java.util.*;
import java.util.stream.Collectors;

import static il.ac.mta.zuli.evolution.engine.utils.generateRandomNum;
import static il.ac.mta.zuli.evolution.engine.utils.generateRandomNumZeroBase;

public class TimeTableSolution implements Solution {
    private List<Quintet> solutionQuintets;
    private final int solutionSize; //number of quintets
    private double totalFitnessScore;
    private final Map<Rule, Double> fitnessScorePerRule;
    TimeTable timeTable;

    public TimeTableSolution(@NotNull TimeTable timeTable) {
        fitnessScorePerRule = new HashMap<>();
        this.timeTable = timeTable;
        solutionSize = generateRandomNum(1, calculateTotalRequiredHours());
        totalFitnessScore = 0;
        randomlyGenerateSolutionQuintets();
    }

    //for testing purposes
    public TimeTableSolution(List<Quintet> solutionQuintets,
                             int solutionSize, //number of quintets
                             TimeTable timeTable) {
        this.solutionQuintets =solutionQuintets;
        this.fitnessScorePerRule = new HashMap<>();
        this.timeTable = timeTable;
        this.solutionSize = solutionSize;
        totalFitnessScore = 0;
    }

    public TimeTableSolution(@NotNull List<Quintet> quintets, @NotNull TimeTable timeTable) {
        this.timeTable = timeTable;
        fitnessScorePerRule = new HashMap<>();
        solutionSize = quintets.size();
        totalFitnessScore = 0;
        solutionQuintets = new ArrayList<>();
        this.solutionQuintets.addAll(quintets);
    }

    //#region getters
    public List<Quintet> getSolutionQuintets() {
        return Collections.unmodifiableList(solutionQuintets);
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
        List<Quintet> solutionForClass = solutionQuintets.stream()
                .filter(p -> p.getSchoolClassID() == schoolClassID).collect(Collectors.toList());

        return Collections.unmodifiableList(solutionForClass);
    }

    //TODO might return an empty list - need to check size of list returned when using this method
    public List<Quintet> getSubSolutionForTeacher(int teacherID) {
        List<Quintet> solutionForTeacher = solutionQuintets.stream()
                .filter(p -> p.getTeacher().getId() == teacherID).collect(Collectors.toList());

        return Collections.unmodifiableList(solutionForTeacher);
    }

    //#endregion

    //#region setters and quintet-generation methods
    private void randomlyGenerateSolutionQuintets() {
        Set<Quintet> solutionSet = generateQuintets(solutionSize);
        solutionQuintets = new ArrayList<Quintet>(solutionSet.size());
        solutionQuintets.addAll(solutionSet);
    }

    private Set<Quintet> generateQuintets(int num) {
        Set<Quintet> newSet = new HashSet<>(); //initially creating the solution as set in order to prevent duplicate quintets

        while (newSet.size() < num) {
            newSet.add(generateRandomQuintet());
        }

        return newSet;
    }

    public Quintet generateRandomQuintet() {
        DayOfWeek randomDay = generateRandomDay();
        int randomHour = generateRandomNumZeroBase(timeTable.getHours());

        SchoolClass randomSchoolClass = generateRandomClass();

        Subject randomSubject = generateRandomSubject(randomSchoolClass);


        Teacher randomTeacher = generateRandomTeacher(randomSubject);

        return new Quintet(randomDay, randomHour, randomTeacher, randomSchoolClass, randomSubject);
    }

    private Teacher generateRandomTeacher(Subject randomSubject) {
        //randomly generate teacher - randomly but only from teachers that teach the random subject
        List<Integer> TeachersIDs = timeTable.getTeachersThatTeachSubject(randomSubject.getId());
        int randomTeachersIndex = generateRandomNumZeroBase(TeachersIDs.size());
        Teacher randomTeacher = timeTable.getTeachers().get(TeachersIDs.get(randomTeachersIndex));
        return randomTeacher;
    }

    private Subject generateRandomSubject(SchoolClass randomSchoolClass) {
        //randomly generate subject - randomly but only from class-subjects
        List<Integer> classRequiredSubjectsIDs = randomSchoolClass.getRequiredSubjectsIDs();
        int randomIndex = generateRandomNumZeroBase(classRequiredSubjectsIDs.size());
        Subject randomSubject = timeTable.getSubjects().get(classRequiredSubjectsIDs.get(randomIndex));
        return randomSubject;
    }

    private SchoolClass generateRandomClass() {
        int randomClassID = generateRandomNum(1, timeTable.getSchoolClasses().size());
        SchoolClass randomSchoolClass = timeTable.getSchoolClasses().get(randomClassID);
        return randomSchoolClass;
    }


    private DayOfWeek generateRandomDay() {
        DayOfWeek[] enumValues = DayOfWeek.values();
        int randIndex = new Random().nextInt(timeTable.getDays());
        return enumValues[randIndex];
    }
    //#endregion

    //not in place - creates new TimeTableSolution
    public void sortQuintetsInSolution(@NotNull Comparator<Quintet> quintetComparator) {
        List<Quintet> quintets = this.getSolutionQuintets();
        List<Quintet> sortedQuintets = quintets.stream().sorted(quintetComparator).collect(Collectors.toList());
        this.solutionQuintets = sortedQuintets;
    }

    public void addScoreToRule(@NotNull Rule rule, double score) {
        if (score < 0) {
            throw new ValidationException("score can't be negative number");
        }
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

        double softRuleAvg = 0;
        double hardRuleAvg = 0;

        if (numOfSoftRules != 0) {
            softRuleAvg = softRuleSum / numOfSoftRules;
        }
        if (numOfHardRules != 0) {
            hardRuleAvg = hardRuleSum / numOfHardRules;
        }

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

    public void printSolutionQuintets() {
        for (Quintet q : getSolutionQuintets()) {
            System.out.println(q);
        }
    }

    @Override
    public String toString() {
        return "TimeTableSolution=" + System.lineSeparator() +
                ", totalFitnessScore=" + totalFitnessScore + System.lineSeparator() +
                solutionQuintets + "*****";
        //", solutionSize=" + solutionSize +
        //+", fitnessScorePerRole=" + fitnessScorePerRule;
    }


}
