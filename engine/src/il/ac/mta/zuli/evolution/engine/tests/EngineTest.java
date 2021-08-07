package il.ac.mta.zuli.evolution.engine.tests;

import il.ac.mta.zuli.evolution.engine.Descriptor;
import il.ac.mta.zuli.evolution.engine.Engine;
import il.ac.mta.zuli.evolution.engine.TimeTableEngine;
import il.ac.mta.zuli.evolution.engine.TimeTableSolution;
import il.ac.mta.zuli.evolution.engine.evolutionengine.EvolutionEngine;
import il.ac.mta.zuli.evolution.engine.evolutionengine.crossover.Crossover;
import il.ac.mta.zuli.evolution.engine.evolutionengine.mutation.Mutation;
import il.ac.mta.zuli.evolution.engine.evolutionengine.selection.Selection;
import il.ac.mta.zuli.evolution.engine.rules.Satisfactory;
import il.ac.mta.zuli.evolution.engine.timetable.SchoolClass;
import il.ac.mta.zuli.evolution.engine.xmlparser.XMLParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
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
        } catch (Exception e) {
            System.out.println(e);
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
    void timeTableSolutionQuintetsNumIsDayMultipliedByHours() {
        TimeTableSolution timeTableSolution = new TimeTableSolution(descriptor.getTimeTable());
        int totalRequiredHours = 0;
        Map<Integer, SchoolClass> classes = descriptor.getTimeTable().getSchoolClasses();
        for (SchoolClass c : classes.values()) {
            totalRequiredHours += c.getTotalRequiredHours();
        }
        int quintetNum = timeTableSolution.getSolutionSize();
        assertTrue(quintetNum <= totalRequiredHours);

    }

    @Test
    void selectReturnsLessSolutions() {
        Selection<TimeTableSolution> selection = descriptor.getEngineSettings().getSelection();
        assertTrue(selection.select(initialPopulation).size() <= initialPopulation.size());
    }

    @Test
    void crossOverReturnsDifferentPopulation() {
        Crossover<TimeTableSolution> crossover = descriptor.getEngineSettings().getCrossover();
        assertFalse(crossover.crossover(initialPopulation).equals(initialPopulation));
    }

    @Test
    void mutateReturnsDifferentPopulation() {
        List<Mutation<TimeTableSolution>> mutations = descriptor.getEngineSettings().getMutations();
        List<TimeTableSolution> solutionsAfterMutations = new ArrayList<>();
        for (TimeTableSolution solution : initialPopulation) {
            for (Mutation<TimeTableSolution> mutaiton : mutations) {
                mutaiton.mutate(solution);
            }
        }
        assertFalse(solutionsAfterMutations.equals(initialPopulation));
    }

}