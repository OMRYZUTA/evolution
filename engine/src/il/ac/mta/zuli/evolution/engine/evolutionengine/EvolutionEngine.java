package il.ac.mta.zuli.evolution.engine.evolutionengine;

import il.ac.mta.zuli.evolution.engine.evolutionengine.mutation.Mutation;
import il.ac.mta.zuli.evolution.engine.exceptions.EmptyCollectionException;
import il.ac.mta.zuli.evolution.engine.rules.Rule;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class EvolutionEngine<T extends Solution> {
    private  EngineSettings<T> engineSettings;



    public void setRules(Set<Rule> rules) {
        if(rules.size()==0){
            throw  new EmptyCollectionException("invalid rules list in evolution engine");
        }
        this.rules = rules;
    }

    private  Set<Rule> rules;


    public EvolutionEngine(@NotNull EngineSettings<T> engineSettings,@NotNull Set<Rule> rules) {
        this.engineSettings = engineSettings;
        setRules(rules);
    }

    public List<T> execute(@NotNull List<T> generation) {
        if(generation.size()==0){
            throw  new EmptyCollectionException("solution generation is empty. in evolution engine");
        }
        evaluateSolutions((List<T>) generation);

        List<T> parents = selectParentsFrom(generation);

        List<T> newGeneration = CrossoverNewGeneration(parents);

        List<T> newGenerationAfterMutation = mutateGeneration(newGeneration);

        //final fitnessEvaluation for new generation
        evaluateSolutions(newGenerationAfterMutation);

        return newGenerationAfterMutation;
    }

    private List<T> selectParentsFrom(@NotNull List<T> generation) {
        List<T> parents = (engineSettings.getSelection()).select(generation);
        return parents;
    }

    @NotNull
    private List<T> mutateGeneration(List<T> newGeneration) {
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
        return newGenerationAfterMutation;
    }

    @NotNull
    private List<T> CrossoverNewGeneration(List<T> parents) {
        // crossover to create next generation (repeat crossover until we receive generation big enough)
        List<T> newGeneration = new ArrayList<>();
        int populationSize = engineSettings.getInitialPopulationSize();

        while (newGeneration.size() < populationSize) {
            newGeneration.addAll(engineSettings.getCrossover().crossover(parents));
        }

        if (newGeneration.size() > populationSize) {
            newGeneration = removeExtraSolutionsFromGeneration(newGeneration, populationSize);
        }
        return newGeneration;
    }

    private void evaluateSolutions(@NotNull List<T> generation) {
        // calculate fitness for every solution in generation and save score to solution
        for (T solution : generation) {
            fitnessEvaluationPerSolution(solution);
        }
    }

    @NotNull
    private List<T> removeExtraSolutionsFromGeneration(List<T> newGeneration, int populationSize) {
        evaluateSolutions(newGeneration);

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

