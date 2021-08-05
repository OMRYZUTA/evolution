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
        // A. calculate fitness for every solution in generation and save score to solution
        for (T solution : generation) {
            fitnessEvaluationPerSolution(solution);
        }

        // B. select topPercent of solutions, according to fitness, in order to create next generation
        List<T> parents = (engineSettings.getSelection()).select(generation);

        // C. crossover to create next generation
        List<T> newGeneration = engineSettings.getCrossover().crossover(parents);

        // B. mutate certain quintets
        //go throw list of mutations
        //TODO - very important we need to make sure the solution does not contain duplicate quintets after the mutation phase
        // a solution is like a set of quintets - can use quintets equals() for the check
    }

    //getting fitness score per rule for every solution
    private void fitnessEvaluationPerSolution(T solution) {
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

