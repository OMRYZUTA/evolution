package il.ac.mta.zuli.evolution.engine.rules;

import il.ac.mta.zuli.evolution.engine.Quintet;
import il.ac.mta.zuli.evolution.engine.TimeTableSolution;
import il.ac.mta.zuli.evolution.engine.evolutionengine.Solution;
import il.ac.mta.zuli.evolution.engine.exceptions.ValidationException;
import il.ac.mta.zuli.evolution.engine.timetable.SchoolClass;
import il.ac.mta.zuli.evolution.engine.timetable.Subject;
import org.jetbrains.annotations.NotNull;

import java.time.DayOfWeek;
import java.util.*;
import java.util.stream.Collectors;

public class Sequentiality extends Rule {
    private int totalHours;
    private final List<SchoolClass> schoolClasses;
    private final String ruleName;
    public Sequentiality(@NotNull String ruleType, int totalHours, List<SchoolClass> schoolClasses) {
        super(ruleType);
        this.ruleName="Sequentiality";
        setTotalHours(totalHours);
        this.schoolClasses = schoolClasses;

    }

    @Override
    public void fitnessEvaluation(Solution solution) {
        if (!(solution instanceof TimeTableSolution)) {
            throw new RuntimeException("solution must be TimeTableSolution");
        }

        TimeTableSolution timeTableSolution = (TimeTableSolution) solution;
        double score = 0;

        if (timeTableSolution.getSolutionSize() > 0) {
            score = calculateScoreSequentiality(timeTableSolution);
        }

        timeTableSolution.addScoreToRule(this, score);
    }

    @Override
    public String getParams() {
        return String.format("totalHours=%d", totalHours);
    }

    private double calculateScoreSequentiality(TimeTableSolution timeTableSolution) {

        Map<SchoolClass, List<Quintet>> classSubSolutions = timeTableSolution.getSolutionQuintets().stream()
                .collect(Collectors.groupingBy(Quintet::getSchoolClass));

        List<Double> classScores = new ArrayList();

        for (SchoolClass schoolClass : schoolClasses) {
            calculateClassScore(classSubSolutions, classScores, schoolClass);
        }

        int satisfactoryClasses = (int) classScores.stream().filter(s -> s == 100.0).count();

        double score;
        int numOfClasses = schoolClasses.size();
        if (this.isHardRule() && (satisfactoryClasses < numOfClasses)) {
            score = 0.0;
        } else {
            score = classScores.stream().reduce(0.0, (sum, element) -> sum + element);
            score = score / numOfClasses;
        }

        return score;
    }

    private void calculateClassScore(Map<SchoolClass, List<Quintet>> classSubSolutions, List<Double> classScores, SchoolClass schoolClass) {
        List<Quintet> classSolution = classSubSolutions.get(schoolClass);

        if (classSolution != null) {
            Map<DayOfWeek, List<Quintet>> daysSubSolutions = classSolution.stream()
                    .collect(Collectors.groupingBy(Quintet::getDay));

            int satisfactoryDays = 0;

            for (Map.Entry<DayOfWeek, List<Quintet>> entry : daysSubSolutions.entrySet()) {

                if (dayIsSatisfactory(entry.getValue())) {
                    satisfactoryDays++;
                }
            }

            classScores.add((double) (satisfactoryDays * 100) / daysSubSolutions.size());
        } else {
            classScores.add(100.0);
        }
    }

    private boolean dayIsSatisfactory(List<Quintet> dayQuintets) {
        List<Quintet> daySortedByHours = dayQuintets.stream()
                .sorted(Comparator.comparingInt(Quintet::getHour))
                .collect(Collectors.toList());

        int currentSequence = 0;
        Subject prevSubject = null;

        for (Quintet quintet : daySortedByHours) {
            if (prevSubject != quintet.getSubject()) {
                currentSequence = 1;
                prevSubject = quintet.getSubject();
            } else {
                currentSequence++;
                if (currentSequence > totalHours) {
                    return false;
                }
            }
        }

        return true;
    }

    private void setTotalHours(int totalHours) {
        if (totalHours > 0) {
            this.totalHours = totalHours;
        } else {
            throw new ValidationException("Total hours of Sequential Rule must be a positive integer, but received: " + totalHours);
        }

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
