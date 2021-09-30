package il.ac.mta.zuli.evolution.engine.rules;

import il.ac.mta.zuli.evolution.engine.Quintet;
import il.ac.mta.zuli.evolution.engine.TimeTableSolution;
import il.ac.mta.zuli.evolution.engine.evolutionengine.Solution;
import il.ac.mta.zuli.evolution.engine.timetable.Teacher;
import org.jetbrains.annotations.NotNull;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class DayOffTeacher extends Rule {
    private final int days;
    private final List<Teacher> teachers;
    private final String ruleName;
    public DayOffTeacher(@NotNull String ruleType, int days, List<Teacher> teachers) {
        super(ruleType);
        this.ruleName="DayOffTeacher";
        this.days = days;
        this.teachers = teachers;
    }

    @Override
    public void fitnessEvaluation(Solution solution) {
        if (!(solution instanceof TimeTableSolution)) {
            throw new RuntimeException("solution must be TimeTableSolution");
        }

        TimeTableSolution timeTableSolution = (TimeTableSolution) solution;
        double score = 0;

        if (timeTableSolution.getSolutionSize() > 0) {
            score = calculateScoreDayOff(timeTableSolution);
        }

        timeTableSolution.addScoreToRule(this, score);

    }

    private double calculateScoreDayOff(TimeTableSolution timeTableSolution) {
        Map<Teacher, List<Quintet>> teacherSubSolutions = timeTableSolution.getSolutionQuintets().stream()
                .collect(Collectors.groupingBy(Quintet::getTeacher));
        int teachersWithDayOff = 0;

        for (Teacher teacher : teachers) {
            List<Quintet> teacherSolution = teacherSubSolutions.get(teacher);
            if (teacherSolution != null) {
                Map<DayOfWeek, List<Quintet>> daysSubSolutions = teacherSolution.stream()
                        .collect(Collectors.groupingBy(Quintet::getDay));

                if (daysSubSolutions.size() < days) {
                    teachersWithDayOff++;
                }
            } else {
                teachersWithDayOff++;
            }
        }
        double score;
        if (this.isHardRule() && teachersWithDayOff < teachers.size()) {
            score = 0;
        } else {
             score = (teachersWithDayOff * 100.0) / (double) teachers.size();
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
