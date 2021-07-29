package il.ac.mta.zuli.evolution.engine.evolutionengine;

import il.ac.mta.zuli.evolution.engine.data.EngineSettings;

import java.util.ArrayList;
import java.util.List;

public class EvolutionEngine {
    private EngineSettings engineSettings;

    //a solution is a collection of quintets
    //a generation is a collection of solutions
    //a collection of certain generations = collection of collections of solutions

    List<Operator> operatorsList = new ArrayList<Operator>(); //{SelectionOperator, CrossOverOperator, MutationOperator, FitnessOperator};


    //first generation of solutions - numOfSolutions = initialPopulationSize
    //generateSolutions(initialPopulationSize);


}

