package il.ac.mta.zuli.evolution.engine.evolutionengine;

import il.ac.mta.zuli.evolution.engine.evolutionengine.mutation.Mutation;
import il.ac.mta.zuli.evolution.engine.rules.Rule;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class EvolutionEngine<T extends Solution> {
    private final EngineSettings<T> engineSettings;
    private final Set<Rule> rules;

    public EvolutionEngine(EngineSettings<T> engineSettings, Set<Rule> rules) {
        this.engineSettings = engineSettings;
        this.rules = rules;
    }

    public List<T> execute(List<T> generation, boolean finalGenerationFlag) {
        // A. calculate fitness for every solution in generation and save score to solution
        for (T solution : generation) {
            fitnessEvaluationPerSolution(solution);
        }

        // B. select topPercent of solutions, according to fitness, in order to create next generation
        List<T> parents = (engineSettings.getSelection()).select(generation);

        // C. crossover to create next generation
        List<T> newGeneration = engineSettings.getCrossover().crossover(parents);

        // D. mutate certain quintets
        List<Mutation<T>> mutationList = engineSettings.getMutations();
        List<T> newGenerationAfterMutation = new ArrayList<>();

        for (T solution : newGeneration) {
            T tempSolution = solution;
            for (Mutation<T> mutation : mutationList) {
                tempSolution = mutation.mutate(tempSolution);
            }
            newGenerationAfterMutation.add(tempSolution);
        }

        /*if (finalGenerationFlag) {
            for (T solution : newGenerationAfterMutation) {
                fitnessEvaluationPerSolution(solution);
            }
        }*/

        return newGenerationAfterMutation;
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
                ", rules=" + rules +
                '}';
    }
}

