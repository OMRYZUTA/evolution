package il.ac.mta.zuli.evolution.engine.evolutionengine;

import il.ac.mta.zuli.evolution.engine.rules.Rule;

import java.util.List;
import java.util.Set;

public class EvolutionEngine<T extends Solution> {
    private final EngineSettings engineSettings;
    private List<T> generation;
    private final Set<Rule> rules;

    public EvolutionEngine(List<T> initialPopulation, EngineSettings engineSettings, Set<Rule> rules) {
        this.engineSettings = engineSettings;
        this.generation = initialPopulation;
        this.rules = rules;
    }

    public void execute() {
        for (T solution : generation) {
            fitnessEvaluation(solution);
        }

        System.out.println("in evolutionEngine execute()");
        System.out.println("gngeration size: " + generation.size());
        System.out.println(generation);
        generation = (engineSettings.getSelection()).select(generation);
        System.out.println("*****" + System.lineSeparator() + "after selection size: " + generation.size());
        System.out.println(generation);
    }

    //getting fitness score per rule for every solution
    private void fitnessEvaluation(T solution) {
        for (Rule rule : rules) {
            rule.fitnessEvaluation(solution);
        }
        solution.calculateTotalScore();
    }


    @Override
    public String toString() {
        return "EvolutionEngine{" +
                "engineSettings=" + engineSettings +
                ", generation=" + generation +
                ", rules=" + rules +
                '}';
    }
}

