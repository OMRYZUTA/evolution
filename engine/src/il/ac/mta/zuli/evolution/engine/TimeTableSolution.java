package il.ac.mta.zuli.evolution.engine;

import il.ac.mta.zuli.evolution.engine.evolutionengine.Solution;
import il.ac.mta.zuli.evolution.engine.timetable.SchoolClass;
import il.ac.mta.zuli.evolution.engine.timetable.Subject;
import il.ac.mta.zuli.evolution.engine.timetable.Teacher;
import il.ac.mta.zuli.evolution.engine.timetable.TimeTable;

import java.time.DayOfWeek;
import java.util.*;

public class TimeTableSolution implements Solution {
    private final List<Quintet> solution;
    private final int solutionSize; //number of quintets
    private final int totalFitnessScore;
    private  Map<String, Integer> fitnessScorePerRule;
    TimeTable timeTable;

    public TimeTableSolution(TimeTable timeTable) {
        fitnessScorePerRule = new HashMap<>();
        this.timeTable = timeTable;
        solutionSize = generateRandomNum(1, calculateTotalRequiredHours());
        totalFitnessScore = 0;
        Set<Quintet> solutionSet = generateQuintets(solutionSize);
        solution = new ArrayList<Quintet>(solutionSet.size());
        solution.addAll(solutionSet);
    }

    public Collection<Quintet> getSolution() {
        return Collections.unmodifiableCollection(solution);
    }

    public int getSolutionSize() {
        return solutionSize;
    }

    public int getTotalFitnessScore() {
        return totalFitnessScore;
    }

    public Map<String, Integer> getFitnessScorePerRule() {
        return Collections.unmodifiableMap(fitnessScorePerRule);
    }

    @Override
    public String toString() {
        return "TimeTableSolution=" + solution + System.lineSeparator() +
                ", solutionSize=" + solutionSize +
                ", totalFitnessScore=" + totalFitnessScore + System.lineSeparator() +
                ", fitnessScorePerRole=" + fitnessScorePerRule;
    }

    private int generateRandomNum(int min, int max) {
        int result;
        if (max - min == 0) {
            result = 1;
        } else {
            Random random = new Random();

            result = random.nextInt(max - min) + min;
        }
        return result;
    }

    private Set<Quintet> generateQuintets(int num) {
        Set<Quintet> newSet = new HashSet<>();
        //I think the collection of quintets should be a set so that we don't have duplicate quintets
        System.out.println("num of quintet: " + num);
        while (newSet.size() < num) {
            newSet.add(generateRandomQuintet());
        }

        return newSet;
    }

    private Quintet generateRandomQuintet() {
        //TODO figure out random bounds

        DayOfWeek randomDay = generateRandomDay();
        int randomHour = new Random().nextInt(timeTable.getHours());

        //randomly generate class
        int randomClassID = generateRandomNum(1, timeTable.getSchoolClasses().size());
        SchoolClass randomSchoolClass = timeTable.getSchoolClasses().get(randomClassID);
        System.out.println("random class" + randomClassID);
        //randomly generate subject - option 1 randomly but only from class-subjects
        List<Integer> classRequiredSubjectsIDs = randomSchoolClass.getRequiredSubjectsIDs();
        int randomIndex = new Random().nextInt(classRequiredSubjectsIDs.size());

        Subject randomSubject = timeTable.getSubjects().get(classRequiredSubjectsIDs.get(randomIndex));

        //randomly generate teacher - option 2 - check if teacher teaches subjects
        List<Integer> TeachersIDs = timeTable.getTeachersThatTeachSubject(randomSubject.getId());
        int randomTeachersIndex = new Random().nextInt(TeachersIDs.size());

        Teacher randomTeacher = timeTable.getTeachers().get(TeachersIDs.get(randomTeachersIndex));


        System.out.println("in generateRandomQuintet " + randomDay + " " + randomHour + System.lineSeparator()
                + randomTeacher + System.lineSeparator()
                + randomSchoolClass + System.lineSeparator()
                + randomSubject + System.lineSeparator() + "***********");
        return new Quintet(randomDay, randomHour, randomTeacher, randomSchoolClass, randomSubject);
    }

    private DayOfWeek generateRandomDay() {
        DayOfWeek[] enumValues = DayOfWeek.values();
        int randIndex = new Random().nextInt(timeTable.getDays());
        return enumValues[randIndex];
    }

    private int calculateTotalRequiredHours() {
        int totalRequiredHours = 0;
        Map<Integer, SchoolClass> classes = timeTable.getSchoolClasses();

        for (SchoolClass c : classes.values()) {
            totalRequiredHours += c.getTotalRequiredHours();
        }

        return totalRequiredHours;
    }
    public void addScoreToRule(String ruleName,int score){
        fitnessScorePerRule.put(ruleName,score);
    }
}
