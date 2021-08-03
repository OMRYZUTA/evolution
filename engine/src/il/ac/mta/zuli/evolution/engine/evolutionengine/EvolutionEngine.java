package il.ac.mta.zuli.evolution.engine.evolutionengine;

import il.ac.mta.zuli.evolution.engine.rules.Rule;

import java.util.List;
import java.util.Set;

public class EvolutionEngine<T extends Solution> {
    private final EngineSettings engineSettings;
    private final List<T> generation;
    private final Set<Rule> rules;

    public EvolutionEngine(List<T> initialPopulation, EngineSettings engineSettings, Set<Rule> rules) {
        this.engineSettings = engineSettings;
        this.generation = initialPopulation;
        this.rules = rules;
    }


    //getting fitness score per role for every solution
    private void fitnessEvaluation(T solution) {
        for (Rule rule : rules) {
            rule.fitnessEvaluation(solution);
        }
        solution.calculateTotalScore();
    }


    public void execute() {
        for (T solution : generation) {
            fitnessEvaluation(solution);
            select();
        }
    }

    private void select() {

    }
}

