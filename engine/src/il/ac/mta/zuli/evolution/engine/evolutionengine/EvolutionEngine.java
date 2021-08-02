package il.ac.mta.zuli.evolution.engine.evolutionengine;

import il.ac.mta.zuli.evolution.engine.rules.Rule;

import java.util.List;

public class EvolutionEngine {
    private EngineSettings engineSettings;
    private List<Solution> generation;
    List<Rule> rules;
    public EvolutionEngine(EngineSettings engineSettings, List<Solution> initialPopulation, List<Rule> rules) {
        this.engineSettings = engineSettings;
        this.generation = initialPopulation;
        this.rules = rules;
    }

    private void fitnessEvaluation(Solution solution){
        for (Rule rule:rules) {
            rule.fitnessEvaluation(solution);
        }
    }


}

