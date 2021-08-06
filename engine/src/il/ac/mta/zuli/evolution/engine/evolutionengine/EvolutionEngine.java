package il.ac.mta.zuli.evolution.engine.evolutionengine;

import il.ac.mta.zuli.evolution.engine.evolutionengine.mutation.Mutation;
import il.ac.mta.zuli.evolution.engine.rules.Rule;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class EvolutionEngine<T extends Solution> {
    private final EngineSettings<T> engineSettings;
    private final Set<Rule> rules;

    public EvolutionEngine(EngineSettings<T> engineSettings, Set<Rule> rules) {
        this.engineSettings = engineSettings;
        this.rules = rules;
    }

    public List<T> execute(List<T> generation) {
        // A. calculate fitness for every solution in generation and save score to solution
        for (T solution : generation) {
            fitnessEvaluationPerSolution(solution);
        }

        // B. select topPercent of solutions, according to fitness, in order to create next generation
        List<T> parents = (engineSettings.getSelection()).select(generation);

        // C. crossover to create next generation (repeat crossover until we receive generation big enough)
        List<T> newGeneration = new ArrayList<>();
        int populationSize = engineSettings.getInitialPopulationSize();

        while (newGeneration.size() < populationSize) {
            newGeneration.addAll(engineSettings.getCrossover().crossover(parents));
        }

        if (newGeneration.size() > populationSize) {
            newGeneration = removeExtraSolutionsFromGeneration(newGeneration, populationSize);
        }

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

        //final fitnessEvaluation for new generation
        for (T solution : newGenerationAfterMutation) {
            fitnessEvaluationPerSolution(solution);
        }

        return newGenerationAfterMutation;
    }

    @NotNull
    private List<T> removeExtraSolutionsFromGeneration(List<T> newGeneration, int populationSize) {
        for (T solution : newGeneration) {
            fitnessEvaluationPerSolution(solution);
        }

        //timetableSolution compareTo based on totalFitness (ascending)
        newGeneration = newGeneration.stream()
                .sorted(Collections.reverseOrder()).limit(populationSize).collect(Collectors.toList());

        return newGeneration;
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

