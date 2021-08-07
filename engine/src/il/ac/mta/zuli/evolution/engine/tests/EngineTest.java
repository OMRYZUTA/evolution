package il.ac.mta.zuli.evolution.engine.tests;

import il.ac.mta.zuli.evolution.engine.*;
import il.ac.mta.zuli.evolution.engine.evolutionengine.EngineSettings;
import il.ac.mta.zuli.evolution.engine.evolutionengine.EvolutionEngine;
import il.ac.mta.zuli.evolution.engine.evolutionengine.crossover.Crossover;
import il.ac.mta.zuli.evolution.engine.evolutionengine.mutation.Mutation;
import il.ac.mta.zuli.evolution.engine.evolutionengine.selection.Selection;
import il.ac.mta.zuli.evolution.engine.rules.Knowledgeable;
import il.ac.mta.zuli.evolution.engine.rules.Satisfactory;
import il.ac.mta.zuli.evolution.engine.timetable.Requirement;
import il.ac.mta.zuli.evolution.engine.timetable.SchoolClass;
import il.ac.mta.zuli.evolution.engine.timetable.Subject;
import il.ac.mta.zuli.evolution.engine.timetable.Teacher;
import il.ac.mta.zuli.evolution.engine.xmlparser.XMLParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


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
    void crossOverReturnsSameSizeOfPopulation() {
        Crossover<TimeTableSolution> crossover = descriptor.getEngineSettings().getCrossover();
        assertEquals(crossover.crossover(initialPopulation).size(),initialPopulation.size());
    }

    @Test
    void mutateReturnsDifferentPopulation() {
        List<Mutation<TimeTableSolution>> mutations = descriptor.getEngineSettings().getMutations();
        List<TimeTableSolution> solutionsAfterMutations = new ArrayList<>();
        for (TimeTableSolution solution : initialPopulation) {
            TimeTableSolution tempSolution =solution;
            for (Mutation<TimeTableSolution> mutaiton : mutations) {
                tempSolution =mutaiton.mutate(tempSolution);
            }
            solutionsAfterMutations.add(tempSolution);
        }
        assertFalse(solutionsAfterMutations.equals(initialPopulation));
    }
    @Test
    void mutateReturnsSameAmountOfPopulation() {
        List<Mutation<TimeTableSolution>> mutations = descriptor.getEngineSettings().getMutations();
        List<TimeTableSolution> solutionsAfterMutations = new ArrayList<>();
        for (TimeTableSolution solution : initialPopulation) {
            TimeTableSolution tempSolution =solution;
            for (Mutation<TimeTableSolution> mutaiton : mutations) {
                 tempSolution =mutaiton.mutate(tempSolution);
            }
            solutionsAfterMutations.add(tempSolution);
        }
        assertEquals(solutionsAfterMutations.size(),initialPopulation.size());
    }
    @Test
    void engineSettingsConstructorNotAcceptingNulls(){
        try {
            EngineSettings engineSettings = new EngineSettings(null, null);
        }catch(IllegalArgumentException e){
            assertTrue(e.getMessage().length()>5);
        }
    }

    @Test
    void evolutionEngineExecuteReturnsDifferentPopulation(){
        assertNotEquals(initialPopulation,evolutionEngine.execute(initialPopulation));
    }
    @Test
    void evolutionEngineExecuteReturnsSameSizeOfPopulation(){
        assertEquals(initialPopulation.size(),evolutionEngine.execute(initialPopulation).size());
    }
//    DayOfWeek randomDay = generateRandomDay();
//    int randomHour = generateRandomNumZeroBase(timeTable.getHours());
//
//    SchoolClass randomSchoolClass = generateRandomClass();
//
//    Subject randomSubject = generateRandomSubject(randomSchoolClass);
//
//
//    Teacher randomTeacher = generateRandomTeacher(randomSubject);
//
//        return new Quintet(randomDay, randomHour, randomTeacher, randomSchoolClass, randomSubject);

    @Test
    void knowledgeableGiveSolutionFullScore(){
        List<Quintet> quintets = new ArrayList<>();
            int dayIndex = 1;
            DayOfWeek day =DayOfWeek.of(dayIndex);
            int hour = 0;
            SchoolClass schoolClass = descriptor.getTimeTable().getSchoolClasses().get(1);
            Requirement requirement = schoolClass.getRequirements().get(0);
            int teachingHours =requirement.getHours();
            Subject subject = requirement.getSubject();
        Teacher teacher = descriptor.getTimeTable().getTeachers().
                get(descriptor.getTimeTable().getTeachersThatTeachSubject(subject.getId()).get(0));
        for (int i = 0; i < teachingHours; i++) {
            if(hour>=descriptor.getTimeTable().getHours()){
                hour=0;
                dayIndex++;
                day = DayOfWeek.of(dayIndex);
            }
            quintets.add(new Quintet(day,hour,teacher,schoolClass,subject));
        }
        TimeTableSolution solution = new TimeTableSolution(quintets, quintets.size(), descriptor.getTimeTable());
        Knowledgeable knowledgeable = new Knowledgeable("soft");
        knowledgeable.fitnessEvaluation(solution);

        assertEquals(solution.getFitnessScorePerRule().get(knowledgeable),100.0);
    }
}