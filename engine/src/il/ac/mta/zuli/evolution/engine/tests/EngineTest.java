package il.ac.mta.zuli.evolution.engine.tests;

import il.ac.mta.zuli.evolution.engine.Descriptor;
import il.ac.mta.zuli.evolution.engine.Engine;
import il.ac.mta.zuli.evolution.engine.TimeTableEngine;
import il.ac.mta.zuli.evolution.engine.TimeTableSolution;
import il.ac.mta.zuli.evolution.engine.evolutionengine.EvolutionEngine;
import il.ac.mta.zuli.evolution.engine.evolutionengine.selection.Selection;
import il.ac.mta.zuli.evolution.engine.rules.Satisfactory;
import il.ac.mta.zuli.evolution.engine.xmlparser.XMLParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;


class EngineTest {
    Engine engine = new TimeTableEngine();
    Satisfactory satisfactoryRule;
    XMLParser xmlParser = new XMLParser();
    Descriptor descriptor;
    EvolutionEngine evolutionEngine;
    List<TimeTableSolution> initialPopulation;
    @BeforeEach
    void setUp() {
        try {
            descriptor = xmlParser.unmarshall("src/resources/EX1-small.xml");
            satisfactoryRule = new Satisfactory("soft", descriptor.getTimeTable().getSchoolClasses());
            initialPopulation = new ArrayList<>();
            int initialPopulationSize = descriptor.getEngineSettings().getInitialPopulationSize();
            for (int i = 0; i < initialPopulationSize; i++) {
                initialPopulation.add(new TimeTableSolution(descriptor.getTimeTable()));
            }
            evolutionEngine = new EvolutionEngine(descriptor.getEngineSettings(), descriptor.getTimeTable().getRules());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Test
    void load() {
        engine = new TimeTableEngine();
        engine.loadXML("src/resources/EX1-small.xml");

        assertTrue(engine.isXMLLoaded());
    }

    @Test
    void fitnessEvaluation() {
        TimeTableSolution timeTableSolution = new TimeTableSolution(descriptor.getTimeTable());
        satisfactoryRule.fitnessEvaluation(timeTableSolution);
    }

    @Test
    void selectReturnsTopPercentSolutions(){
        Selection<TimeTableSolution> selection = descriptor.getEngineSettings().getSelection();

        //evolutionEngine.execute();
    }
}