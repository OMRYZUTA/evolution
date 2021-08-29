package il.ac.mta.zuli.evolution.engine.evolutionengine;

import il.ac.mta.zuli.evolution.engine.evolutionengine.mutation.Mutation;
import il.ac.mta.zuli.evolution.engine.exceptions.EmptyCollectionException;
import il.ac.mta.zuli.evolution.engine.rules.Rule;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

public class EvolutionEngine<T extends Solution> {
    private final EngineSettings<T> engineSettings;
    private Set<Rule> rules;
    private final int numOfElitism;


    public EvolutionEngine(@NotNull EngineSettings<T> engineSettings, @NotNull Set<Rule> rules) {
        this.engineSettings = engineSettings;
        setRules(rules);
        this.numOfElitism = engineSettings.getEliteNumber();
    }

    public void setRules(@NotNull Set<Rule> rules) {
        if (rules.size() == 0) {
            throw new EmptyCollectionException("Empty rules list in evolution engine");
        }

        this.rules = rules;
    }

    public List<T> execute(@NotNull List<T> generation) {
        if (generation.size() == 0) {
            throw new EmptyCollectionException("The generation is empty (no solutions to work with).");
        }

        evaluateSolutions(generation);
        //extract elitism from generation
        List<T> eliteSolutions = generation.stream()
                .sorted(Collections.reverseOrder()).
                        limit(numOfElitism).collect(Collectors.toList());
        List<T> parents = selectParentsFrom(generation);
        List<T> newGeneration = crossoverNewGeneration(parents);//makes sure that new generation < populationSize- elitism
        List<T> newGenerationAfterMutation = mutateGeneration(newGeneration);
        newGenerationAfterMutation.addAll(eliteSolutions);//elite solutions weren't mutated.
        //final fitnessEvaluation for new generation
        evaluateSolutions(newGenerationAfterMutation);
        return newGenerationAfterMutation;
    }

    private List<T> selectParentsFrom(@NotNull List<T> generation) {
        return (engineSettings.getSelection()).select(generation);
    }

    @NotNull
    private List<T> mutateGeneration(List<T> newGeneration) {
        // mutate certain quintets
        List<Mutation<T>> mutationList = engineSettings.getMutations();
        List<T> newGenerationAfterMutation = new ArrayList<>();

        for (T solution : newGeneration) {
            T tempSolution = solution; //handling solutions with 0 quintets inside mutate()

            for (Mutation<T> mutation : mutationList) {
                tempSolution = mutation.mutate(tempSolution);
            }

            newGenerationAfterMutation.add(tempSolution);
        }

        return newGenerationAfterMutation;
    }

    @NotNull
    private List<T> crossoverNewGeneration(List<T> parents) {
        // crossover to create next generation (repeat crossover until we receive generation big enough)
        List<T> newGeneration = new ArrayList<>();
        int populationSize = engineSettings.getInitialPopulationSize();
        while (newGeneration.size() < (populationSize - numOfElitism)) {
            newGeneration.addAll(engineSettings.getCrossover().crossover(parents));
        }
        if (newGeneration.size() > (populationSize - numOfElitism)) {
            //keeps the best solution, remove the worst.
            newGeneration = removeExtraSolutionsFromGeneration(newGeneration, (populationSize - numOfElitism));
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

