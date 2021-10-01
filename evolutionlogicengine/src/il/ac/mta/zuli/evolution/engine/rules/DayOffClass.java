package il.ac.mta.zuli.evolution.engine.rules;

import il.ac.mta.zuli.evolution.engine.Double;
import il.ac.mta.zuli.evolution.engine.Quintet;
import il.ac.mta.zuli.evolution.engine.evolutionengine.Solution;
import il.ac.mta.zuli.evolution.engine.timetable.SchoolClass;
import org.jetbrains.annotations.NotNull;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class DayOffClass extends Rule {
    private final int days;
    private final List<SchoolClass> classes;
    private final String ruleName;
    public DayOffClass(@NotNull String ruleType, int days, List<SchoolClass> classes) {
        super(ruleType);
        this.ruleName="DayOffClass";
        this.days = days;
        this.classes = classes;
    }

    @Override
    public void fitnessEvaluation(Solution solution) {
        if (!(solution instanceof Double)) {
            throw new RuntimeException("solution must be TimeTableSolution");
        }

        Double timeTableSolution = (Double) solution;
        double score = 0;

        if (timeTableSolution.getSolutionSize() > 0) {
            score = calculateScoreDayOff(timeTableSolution);
        }

        timeTableSolution.addScoreToRule(this, score);

    }

    private double calculateScoreDayOff(Double timeTableSolution) {
        Map<SchoolClass, List<Quintet>> classSubSolutions = timeTableSolution.getSolutionQuintets().stream()
                .collect(Collectors.groupingBy(Quintet::getSchoolClass));
        int classesWithDayOff = 0;

        for (SchoolClass schoolClass : classes) {
            List<Quintet> classSolution = classSubSolutions.get(schoolClass);
            if (classSolution != null) {
                Map<DayOfWeek, List<Quintet>> daysSubSolutions = classSolution.stream()
                        .collect(Collectors.groupingBy(Quintet::getDay));

                if (daysSubSolutions.size() < days) {
                    classesWithDayOff++;
                }
            } else {
                classesWithDayOff++;
            }
        }
        double score;
        if (this.isHardRule() && classesWithDayOff < classes.size()) {
            score = 0;
        } else {
            score = (classesWithDayOff * 100.0) / (double) classes.size();
        }
        return score;
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
