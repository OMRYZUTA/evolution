package il.ac.mta.zuli.evolution.engine.rules;

import il.ac.mta.zuli.evolution.engine.Quintet;
import il.ac.mta.zuli.evolution.engine.TimeTableSolution;
import il.ac.mta.zuli.evolution.engine.evolutionengine.Solution;
import il.ac.mta.zuli.evolution.engine.timetable.Subject;

import java.util.Collection;
import java.util.Map;

//teachers only teach subjects from their subject-collection
public class Knowledgeable extends Rule {
    public Knowledgeable(String ruleType) {
        super(ruleType);
    }

    @Override
    public int fitnessEvaluation(Solution solution) {
        int qualifiedQuintets = 0;
        Collection<Quintet> solutionQuintets = ((TimeTableSolution) solution).getSolution();
        Map<Integer, Subject> teaches;

        for (Quintet quintet : solutionQuintets) {
            teaches = (quintet.getTeacher()).getSubjects();

            if (teaches.containsKey(quintet.getSubject().getId())) {
                qualifiedQuintets++;
            } else {
                if (this.isHardRule()) {
                    return 0;
                }
            }
        }

        //TODO fitnessEvaluation returns double or int?
        return ((100 * qualifiedQuintets) / (((TimeTableSolution) solution).getSolutionSize()));
    }
}
