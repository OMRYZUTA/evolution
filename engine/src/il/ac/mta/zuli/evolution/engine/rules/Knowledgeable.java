package il.ac.mta.zuli.evolution.engine.rules;

import il.ac.mta.zuli.evolution.engine.Quintet;
import il.ac.mta.zuli.evolution.engine.TimeTableSolution;
import il.ac.mta.zuli.evolution.engine.evolutionengine.Solution;
import il.ac.mta.zuli.evolution.engine.timetable.Subject;

import java.util.List;
import java.util.Map;


//rule meaning: teachers only teach subjects from their subject-collection
public class Knowledgeable extends Rule {
    public Knowledgeable(String ruleType) {
        super(ruleType);
    }

    @Override
    public void fitnessEvaluation(Solution solution) {
        if (!(solution instanceof TimeTableSolution)) {
            throw new RuntimeException("solution must be TimeTableSolution");
        }

        TimeTableSolution timeTableSolution = (TimeTableSolution) solution;
        double score = INVALIDSCORE;
        int qualifiedQuintets = 0;
        List<Quintet> solutionQuintets = timeTableSolution.getSolutionQuintets();
        Map<Integer, Subject> subjectsTeaches;

        for (Quintet quintet : solutionQuintets) {
            subjectsTeaches = (quintet.getTeacher()).getSubjects();

            if (subjectsTeaches.containsKey(quintet.getSubject().getId())) {
                qualifiedQuintets++;
            }
        }


        score = (100 * qualifiedQuintets) / (double) (timeTableSolution.getSolutionSize());


        timeTableSolution.addScoreToRule(this, score);
    }
}
