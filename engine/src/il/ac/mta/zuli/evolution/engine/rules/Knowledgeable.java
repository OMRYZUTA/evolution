package il.ac.mta.zuli.evolution.engine.rules;

import il.ac.mta.zuli.evolution.engine.Quintet;
import il.ac.mta.zuli.evolution.engine.TimeTableSolution;
import il.ac.mta.zuli.evolution.engine.evolutionengine.Solution;
import il.ac.mta.zuli.evolution.engine.timetable.Subject;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;


//rule meaning: teachers only teach subjects from their subject-collection
public class Knowledgeable extends Rule {
    public Knowledgeable(@NotNull String ruleType) {
        super(ruleType);
    }

    @Override
    public void fitnessEvaluation(@NotNull Solution solution) {
        if (!(solution instanceof TimeTableSolution)) {
            throw new RuntimeException("solution must be TimeTableSolution");
        }

        TimeTableSolution timeTableSolution = (TimeTableSolution) solution;
        double score = 0;

        if (timeTableSolution.getSolutionSize() > 0) {
            score = calculateScoreBySubjectsFitToTeachers(timeTableSolution);
        }

        timeTableSolution.addScoreToRule(this, score);
    }

    private double calculateScoreBySubjectsFitToTeachers(TimeTableSolution timeTableSolution) {
        double score = 0;
        int qualifiedQuintets = 0;
        List<Quintet> solutionQuintets = timeTableSolution.getSolutionQuintets();
        Map<Integer, Subject> subjectsTeaches;

        for (Quintet quintet : solutionQuintets) {
            subjectsTeaches = (quintet.getTeacher()).getSubjects();

            if (subjectsTeaches.containsKey(quintet.getSubject().getId())) {
                qualifiedQuintets++;
            } else {
                break;
            }
        }

        int numOfQuintets = timeTableSolution.getSolutionSize();
        score = (100 * qualifiedQuintets) / (double) numOfQuintets;

        if (this.isHardRule() && qualifiedQuintets < numOfQuintets) {
            score = 0;
        }

        return score;
    }
}
