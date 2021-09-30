package il.ac.mta.zuli.evolution.engine.rules;

import il.ac.mta.zuli.evolution.engine.Quintet;
import il.ac.mta.zuli.evolution.engine.TimeTableSolution;
import il.ac.mta.zuli.evolution.engine.evolutionengine.Solution;
import il.ac.mta.zuli.evolution.engine.timetable.Teacher;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class WorkingHoursPreference extends Rule {
    private final List<Teacher> teachers;
    private final String ruleName;
    public WorkingHoursPreference(@NotNull String ruleType, List<Teacher> teachers) {
        super(ruleType);
        this.teachers = teachers;
        this.ruleName="WorkingHoursPreference";
    }

    @Override
    public void fitnessEvaluation(Solution solution) {
        if (!(solution instanceof TimeTableSolution)) {
            throw new RuntimeException("solution must be TimeTableSolution");
        }

        TimeTableSolution timeTableSolution = (TimeTableSolution) solution;
        double score = 0;

        if (timeTableSolution.getSolutionSize() > 0) {
            score = calculateScoreWorkingHours(timeTableSolution);
        }

        timeTableSolution.addScoreToRule(this, score);

    }

    private double calculateScoreWorkingHours(TimeTableSolution timeTableSolution) {
        Map<Teacher, List<Quintet>> teacherSubSolutions = timeTableSolution.getSolutionQuintets().stream()
                .collect(Collectors.groupingBy(Quintet::getTeacher));
        int teachersWithExactHours = 0;

        for (Teacher teacher : teachers) {
            List<Quintet> teacherSolution = teacherSubSolutions.get(teacher);
            if (teacherSolution != null) {
                if (teacherSolution.size() == teacher.getWorkingHours()) {
                    teachersWithExactHours++;
                }
            } else if (teacher.getWorkingHours() == 0) {
                teachersWithExactHours++;
            }
        }

        double score;
        if (this.isHardRule() && teachersWithExactHours < teachers.size()) {
            score = 0;
        } else {
            score = (teachersWithExactHours * 100.0) / (double) teachers.size();
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
