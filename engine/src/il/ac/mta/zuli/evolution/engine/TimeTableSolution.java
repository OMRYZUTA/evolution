package il.ac.mta.zuli.evolution.engine;

import il.ac.mta.zuli.evolution.engine.evolutionengine.Solution;
import il.ac.mta.zuli.evolution.engine.rules.Rule;
import il.ac.mta.zuli.evolution.engine.timetable.SchoolClass;
import il.ac.mta.zuli.evolution.engine.timetable.Subject;
import il.ac.mta.zuli.evolution.engine.timetable.Teacher;
import il.ac.mta.zuli.evolution.engine.timetable.TimeTable;

import java.time.DayOfWeek;
import java.util.*;

public class TimeTableSolution implements Solution {
    //private final Collection<Quintet> solution; //TODO start with hashSet but end with ArrayList
    private final List<Quintet> solution;
    private final int solutionSize; //number of quintets
    private final int totalFitnessScore;
    private final Map<Rule, Integer> fitnessScorePerRole;
    TimeTable timeTable;

    public TimeTableSolution(TimeTable timeTable) {
        fitnessScorePerRole = new HashMap<>();
        this.timeTable = timeTable;
        solutionSize = generateRandomNumOfQuintets(1, calculateTotalRequiredHours());
        totalFitnessScore = 0;
        Set<Quintet> tempSolution = generateQuintets(solutionSize);
        //TODO change set to eventually be an arrayList in order of days-hours
        //solution = Arrays.asList(new Quintet[solutionSize]);
        solution = new ArrayList<Quintet>(tempSolution.size());
        System.out.println("in timeTableSolution Ctor");
        System.out.println("empty array? " + solution);
        solution.addAll(tempSolution);
        System.out.println("full array? " + solution);
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

    public Map<Rule, Integer> getFitnessScorePerRole() {
        return Collections.unmodifiableMap(fitnessScorePerRole);
    }

    @Override
    public String toString() {
        return "TimeTableSolution=" + solution + System.lineSeparator() +
                ", solutionSize=" + solutionSize +
                ", totalFitnessScore=" + totalFitnessScore + System.lineSeparator() +
                ", fitnessScorePerRole=" + fitnessScorePerRole;
    }

    private int generateRandomNumOfQuintets(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min) + min;
    }

    Set<Quintet> generateQuintets(int num) {
        Set<Quintet> newSet = new HashSet<>();
        //I think the collection of quintets should be a set so that we don't have duplicate quintets

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
        int randomClassID = new Random().nextInt(timeTable.getSchoolClasses().size());
        SchoolClass randomSchoolClass = timeTable.getSchoolClasses().get(randomClassID);

        //randomly generate subject - option 1 randomly but only from class-subjects
        List<Subject> classRequiredSubjects = randomSchoolClass.getRequiredSubjects();
        int randomSubjectID = new Random().nextInt(classRequiredSubjects.size());
        Subject randomSubject = classRequiredSubjects.get(randomSubjectID + 1);

        //randomly generate subject - option 2 totally random from entire subject list
        /*int randomSubjectID = new Random().nextInt(timeTable.getSubjects().size()-1);
        Subject randomSubject = timeTable.getSubjects().get(randomSubjectID+1);*/

        //randomly generate teacher - option 1 totally random
       /* int randomTeacherID = new Random().nextInt(timeTable.getTeachers().size() - 1);
        Teacher randomTeacher = timeTable.getTeachers().get(randomTeacherID + 1);*/
        //randomly generate teacher - option 2 - check if teacher teaches subjects
        List<Teacher> teachersOfSubject = timeTable.getTeachersThatTeachSubject(randomSubject.getId());
        int randomTeacherID = new Random().nextInt(teachersOfSubject.size());
        Teacher randomTeacher = teachersOfSubject.get(randomTeacherID + 1);
        System.out.println("in generateRandomQuintet " + randomDay + " " + randomHour + System.lineSeparator()
                + randomTeacher + System.lineSeparator()
                + randomSchoolClass + System.lineSeparator()
                + randomSubject + System.lineSeparator() + "***********");
        return new Quintet(randomDay, randomHour, randomTeacher, randomSchoolClass, randomSubject);
    }

    private DayOfWeek generateRandomDay() {
        DayOfWeek[] enumValues = DayOfWeek.values();
        int daysInWeek = enumValues.length;
        int randIndex = new Random().nextInt(daysInWeek);
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


}
