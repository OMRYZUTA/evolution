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

    public void execute() {
        //calculate fitness for every solution in generation and save score to solution
        for (T solution : generation) {
            fitnessEvaluationPerSolution(solution);
        }

        // select topPercent of solutions, according to fitness, in order to create next generation
        List<T> parents = (engineSettings.getSelection()).select(generation);

        engineSettings.getCrossover().crossover(parents);

    }

    //getting fitness score per rule for every solution
    private void fitnessEvaluationPerSolution(T solution) {
        for (Rule rule : rules) {
            rule.fitnessEvaluation(solution);
        }
        solution.calculateTotalScore();
    }

    private void crossover() {

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

