//package il.ac.mta.zuli.evolution.engine.tests;
//
//import il.ac.mta.zuli.evolution.dto.GenerationProgressDTO;
//import il.ac.mta.zuli.evolution.dto.TimeTableSolutionDTO;
//import il.ac.mta.zuli.evolution.engine.*;
//import il.ac.mta.zuli.evolution.engine.events.OnStrideEvent;
//import il.ac.mta.zuli.evolution.engine.evolutionengine.EngineSettings;
//import il.ac.mta.zuli.evolution.engine.evolutionengine.EvolutionEngine;
//import il.ac.mta.zuli.evolution.engine.evolutionengine.crossover.Crossover;
//import il.ac.mta.zuli.evolution.engine.evolutionengine.mutation.Mutation;
//import il.ac.mta.zuli.evolution.engine.evolutionengine.mutation.Sizer;
//import il.ac.mta.zuli.evolution.engine.evolutionengine.selection.Selection;
//import il.ac.mta.zuli.evolution.engine.rules.Knowledgeable;
//import il.ac.mta.zuli.evolution.engine.rules.Satisfactory;
//import il.ac.mta.zuli.evolution.engine.rules.Singularity;
//import il.ac.mta.zuli.evolution.engine.rules.TeacherIsHuman;
//import il.ac.mta.zuli.evolution.engine.timetable.Requirement;
//import il.ac.mta.zuli.evolution.engine.timetable.SchoolClass;
//import il.ac.mta.zuli.evolution.engine.timetable.Subject;
//import il.ac.mta.zuli.evolution.engine.timetable.Teacher;
//import il.ac.mta.zuli.evolution.engine.xmlparser.XMLParser;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.time.DayOfWeek;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//
//class EngineTest {
//    boolean evolutionAlgorithmCompleted =false;
//    Engine engine = new TimeTableEngine();
//    Satisfactory satisfactoryRule;
//    final XMLParser xmlParser = new XMLParser();
//    Descriptor descriptor;
//    EvolutionEngine evolutionEngine;
//    List<TimeTableSolution> initialPopulation;
//    private int generationCounter=0;
//
//    @BeforeEach
//    void setUp() {
//        try {
//            descriptor = xmlParser.unmarshall("src/resources/EX1-big.xml");
//            satisfactoryRule = new Satisfactory("soft", descriptor.getTimeTable().getSchoolClasses());
//            initialPopulation = new ArrayList<>();
//            int initialPopulationSize = descriptor.getEngineSettings().getInitialPopulationSize();
//            for (int i = 0; i < initialPopulationSize; i++) {
//                initialPopulation.add(new TimeTableSolution(descriptor.getTimeTable()));
//            }
//            evolutionEngine = new EvolutionEngine(descriptor.getEngineSettings(), descriptor.getTimeTable().getRules());
//        } catch (Exception e) {
//
//        } catch (Throwable throwable) {
//            throwable.printStackTrace();
//        }
//    }
//
//    //#region TimeTable engine
//    @Test
//    void loadBig() {
//        engine = new TimeTableEngine();
//        engine.loadXML("src/resources/EX1-big.xml");
//
//        assertTrue(engine.isXMLLoaded());
//    }
//    @Test
//    void loadSmall() {
//        engine = new TimeTableEngine();
//        engine.loadXML("src/resources/EX1-small.xml");
//
//        assertTrue(engine.isXMLLoaded());
//    }
//    @Test
//    void loadError3Dot2XMLFILE() {
//        engine = new TimeTableEngine();
//        engine.loadXML("src/resources/EX1-error-teache-id-2-missing.xml");
//
//        assertFalse(engine.isXMLLoaded());
//    }
//    @Test
//    void loadError3Dot4XMLFILE() {
//        engine = new TimeTableEngine();
//        engine.loadXML("src/resources/EX1-error-non-existing-subject-in-teacher.xml");
//
//        assertFalse(engine.isXMLLoaded());
//    }
//    @Test
//    void loadError3Dot6XMLFILE() {
//        engine = new TimeTableEngine();
//        engine.loadXML("src/resources/EX1-error-to-many-hours.xml");
//
//        assertFalse(engine.isXMLLoaded());
//    }
//    @Test
//    void getEvolutionProgressReturnListWithTheRightSize(){
//        engine.loadXML("src/resources/EX1-small.xml");
//        engine.executeEvolutionAlgorithm(10,1);
//        List<GenerationProgressDTO> progressDTOS = engine.getEvolutionProgress();
//        assertEquals(10,progressDTOS.size());
//    }
//@Test
//void getBestSolutionReturnsSolutionBetterThan50(){
//    engine.loadXML("src/resources/EX1-small.xml");
//        engine.executeEvolutionAlgorithm(50,10);
//    TimeTableSolutionDTO solutionDTO = engine.getBestSolution();
//    System.out.println(solutionDTO.getTotalFitnessScore());
//    assertTrue(solutionDTO.getTotalFitnessScore()>50.0);
//}
//@Test
//void stopOnFittnes(){
//    engine.addListener("completed", e -> {
//        this.evolutionAlgorithmCompleted = true;
//    });
//    engine.addListener("stride", e -> {
//        OnStrideEvent strideEvent = (OnStrideEvent) e;
//        this.generationCounter++;
//    });
//        engine.loadXML("src/resources/EX1-small.xml");
//    engine.executeEvolutionAlgorithmWithFitnessStop(120, 1, 80);
//        TimeTableSolutionDTO solutionDTO =engine.getBestSolution();
//        assertTrue(solutionDTO.getTotalFitnessScore()>80);
//        assertTrue(evolutionAlgorithmCompleted);
//        assertTrue(generationCounter<120);
//}
//    //#endregion engine
//
////#region evolution
//    @Test
//    void selectReturnsLessSolutions() {
//        Selection<TimeTableSolution> selection = descriptor.getEngineSettings().getSelection();
//        assertTrue(selection.select(initialPopulation).size() <= initialPopulation.size());
//    }
//
//    @Test
//    void crossOverReturnsDifferentPopulation() {
//        Crossover<TimeTableSolution> crossover = descriptor.getEngineSettings().getCrossover();
//        assertNotEquals(crossover.crossover(initialPopulation), initialPopulation);
//    }
//    @Test
//    void crossOverReturnsSameSizeOfPopulation() {
//        Crossover<TimeTableSolution> crossover = descriptor.getEngineSettings().getCrossover();
//        assertEquals(crossover.crossover(initialPopulation).size(),initialPopulation.size());
//    }
//
//    @Test
//    void mutateReturnsDifferentPopulation() {
//        List<Mutation<TimeTableSolution>> mutations = getMutations();
//        List<TimeTableSolution> solutionsAfterMutations = new ArrayList<>();
//        for (TimeTableSolution solution : initialPopulation) {
//            TimeTableSolution tempSolution =solution;
//            for (Mutation<TimeTableSolution> mutaiton : mutations) {
//                tempSolution =mutaiton.mutate(tempSolution);
//            }
//            solutionsAfterMutations.add(tempSolution);
//        }
//        assertNotEquals(solutionsAfterMutations, initialPopulation);
//    }
//
//    private List getMutations() {
//        return descriptor.getEngineSettings().getMutations();
//    }
//
//    @Test
//    void mutateReturnsSameAmountOfPopulation() {
//        List<Mutation<TimeTableSolution>> mutations = getMutations();
//        List<TimeTableSolution> solutionsAfterMutations = new ArrayList<>();
//        for (TimeTableSolution solution : initialPopulation) {
//            TimeTableSolution tempSolution =solution;
//            for (Mutation<TimeTableSolution> mutaiton : mutations) {
//                 tempSolution =mutaiton.mutate(tempSolution);
//            }
//            solutionsAfterMutations.add(tempSolution);
//        }
//        assertEquals(solutionsAfterMutations.size(),initialPopulation.size());
//    }
//    @Test
//    void evolutionEngineExecuteReturnsSameSizeOfPopulation(){
//        assertEquals(initialPopulation.size(),evolutionEngine.execute(initialPopulation).size());
//    }
//
//    @Test
//    void sizerAddQuintetsFromSolution() throws Throwable {
//        List<Quintet> quintets = new ArrayList<>();
//        int dayIndex = 1;
//        DayOfWeek day =DayOfWeek.of(dayIndex);
//        int hour = 0;
//        descriptor = xmlParser.unmarshall("src/resources/EX1-smallForSizer.xml");
//
//        SchoolClass schoolClass = descriptor.getTimeTable().getSchoolClasses().get(1);
//
//        for (Requirement requirement: schoolClass.getRequirements()) {
//            for (int i = 0; i < requirement.getHours(); i+=3) {
//                Subject subject = requirement.getSubject();
//                Teacher teacher = descriptor.getTimeTable()
//                        .getTeachers().get(descriptor.getTimeTable().getTeachersThatTeachSubject(subject.getId()).get(0));
//                quintets.add(new Quintet(day,hour,teacher,schoolClass,subject));
//            }
//        }
//
//        Map <Integer,SchoolClass> schoolClasses = new HashMap<>();
//        schoolClasses.put(1,schoolClass);
//        TimeTableSolution solution = new TimeTableSolution(quintets, quintets.size(), descriptor.getTimeTable());
//
//        List<Mutation<TimeTableSolution>> mutations = getMutations();
//        Sizer mutation =(Sizer) mutations.get(0);
//        TimeTableSolution mutatedSolution= (TimeTableSolution) mutation.mutate(solution);
//        assertTrue(solution.getSolutionSize()<mutatedSolution.getSolutionSize());
//
//    }
//
//    @Test
//    void sizerRemoveQuintetsFromSolution() throws Throwable {
//        List<Quintet> quintets = new ArrayList<>();
//        int dayIndex = 1;
//        DayOfWeek day =DayOfWeek.of(dayIndex);
//        int hour = 0;
//        descriptor = xmlParser.unmarshall("src/resources/EX1-smallForSizer.xml");
//        SchoolClass schoolClass = descriptor.getTimeTable().getSchoolClasses().get(1);
//
//        createQuintetList(quintets, day, hour, schoolClass);
//
//        Map <Integer,SchoolClass> schoolClasses = new HashMap<>();
//        schoolClasses.put(1,schoolClass);
//        TimeTableSolution solution = new TimeTableSolution(quintets, quintets.size(), descriptor.getTimeTable());
//
//        List<Mutation<TimeTableSolution>> mutations = getMutations();
//        Sizer mutation =(Sizer) mutations.get(1);
//        TimeTableSolution mutatedSolution= (TimeTableSolution) mutation.mutate(solution);
//        assertTrue(solution.getSolutionSize()>mutatedSolution.getSolutionSize());
//
//    }
//
//    private void createQuintetList(List<Quintet> quintets, DayOfWeek day, int hour, SchoolClass schoolClass) {
//        for (Requirement requirement : schoolClass.getRequirements()) {
//            for (int i = 0; i < requirement.getHours(); i++) {
//                Subject subject = requirement.getSubject();
//                Teacher teacher = descriptor.getTimeTable()
//                        .getTeachers().get(descriptor.getTimeTable().getTeachersThatTeachSubject(subject.getId()).get(0));
//                quintets.add(new Quintet(day, hour, teacher, schoolClass, subject));
//            }
//        }
//    }
////    @Test
////    void sizerAddQuintetsFromSolution() throws Throwable {
////        descriptor = xmlParser.unmarshall("src/resources/EX1-smallForSizer.xml");
////        List<Mutation<TimeTableSolution>> mutations = descriptor.getEngineSettings().getMutations();
////        Sizer mutation =(Sizer) mutations.get(0);
////        TimeTableSolution solution = new TimeTableSolution(descriptor.getTimeTable());
////        TimeTableSolution mutatedSolution= (TimeTableSolution) mutation.mutate(solution);
////        assertTrue(solution.getSolutionSize()<mutatedSolution.getSolutionSize());
////
////    }
//    //#endregion evolution
//
//
////#region rules
//@Test
//void knowledgeableGivesEmptySolutionFullScore(){
//    List<Quintet> quintets = new ArrayList<>();
//    int dayIndex = 1;
//    DayOfWeek day =DayOfWeek.of(dayIndex);
//    int hour = 0;
//    SchoolClass schoolClass = descriptor.getTimeTable().getSchoolClasses().get(1);
//    Requirement requirement = schoolClass.getRequirements().get(0);
//    int teachingHours =requirement.getHours();
//    Subject subject = requirement.getSubject();
//    Teacher teacher = descriptor.getTimeTable().getTeachers().
//            get(descriptor.getTimeTable().getTeachersThatTeachSubject(subject.getId()).get(0));
//    TimeTableSolution solution = new TimeTableSolution(quintets, 0, descriptor.getTimeTable());
//    Knowledgeable knowledgeable = new Knowledgeable("soft");
//    knowledgeable.fitnessEvaluation(solution);
//    solution.calculateTotalScore();
//    assertEquals(0.0,solution.getFitnessScorePerRule().get(knowledgeable));
//    assertTrue(solution.getTotalFitnessScore()==0.0);
//}
//
//    @Test
//    void knowledgeableGiveSolutionFullScore(){
//        List<Quintet> quintets = new ArrayList<>();
//            int dayIndex = 1;
//            DayOfWeek day =DayOfWeek.of(dayIndex);
//            int hour = 0;
//            SchoolClass schoolClass = descriptor.getTimeTable().getSchoolClasses().get(1);
//            Requirement requirement = schoolClass.getRequirements().get(0);
//            int teachingHours =requirement.getHours();
//            Subject subject = requirement.getSubject();
//        Teacher teacher = descriptor.getTimeTable().getTeachers().
//                get(descriptor.getTimeTable().getTeachersThatTeachSubject(subject.getId()).get(0));
//        for (int i = 0; i < teachingHours; i++) {
//            if(hour>=descriptor.getTimeTable().getHours()){
//                dayIndex++;
//                day = DayOfWeek.of(dayIndex);
//            }
//            quintets.add(new Quintet(day,hour,teacher,schoolClass,subject));
//        }
//        TimeTableSolution solution = new TimeTableSolution(quintets, quintets.size(), descriptor.getTimeTable());
//        Knowledgeable knowledgeable = new Knowledgeable("soft");
//        knowledgeable.fitnessEvaluation(solution);
//        solution.calculateTotalScore();
//        assertEquals(solution.getFitnessScorePerRule().get(knowledgeable),100.0);
//        assertTrue(solution.getTotalFitnessScore()>0.0);
//    }
//    @Test
//    void satisfactoryGivesSolutionFullScore(){
//        List<Quintet> quintets = new ArrayList<>();
//        int dayIndex = 1;
//        DayOfWeek day =DayOfWeek.of(dayIndex);
//        int hour = 0;
//
//        SchoolClass schoolClass = descriptor.getTimeTable().getSchoolClasses().get(1);
//
//        createQuintetList(quintets, day, hour, schoolClass);
//
//        Map <Integer,SchoolClass> schoolClasses = new HashMap<>();
//            schoolClasses.put(1,schoolClass);
//        TimeTableSolution solution = new TimeTableSolution(quintets, quintets.size(), descriptor.getTimeTable());
//        Satisfactory satisfactory = new Satisfactory("soft",schoolClasses);
//        satisfactory.fitnessEvaluation(solution);
//        solution.calculateTotalScore();
//        assertEquals(solution.getFitnessScorePerRule().get(satisfactory),100.0);
//        //assertEquals(solution.getTotalFitnessScore(),100.0);
//    }
//    @Test
//    void satisfactoryGivesSolutionZeroScore(){
//        List<Quintet> quintets = new ArrayList<>();
//        SchoolClass schoolClass = descriptor.getTimeTable().getSchoolClasses().get(1);
//        Map <Integer,SchoolClass> schoolClasses = new HashMap<>();
//
//        schoolClasses.put(1,schoolClass);
//        TimeTableSolution solution = new TimeTableSolution(quintets, 0, descriptor.getTimeTable());
//        Satisfactory satisfactory = new Satisfactory("soft",schoolClasses);
//        satisfactory.fitnessEvaluation(solution);
//        solution.calculateTotalScore();
//        assertEquals(0.0, solution.getFitnessScorePerRule().get(satisfactory));
//        assertTrue(solution.getTotalFitnessScore()==0.0);
//    }
//    @Test
//    void singularityGivesSolutionZeroScore(){
//        List<Quintet> quintets = new ArrayList<>();
//        int dayIndex = 1;
//        DayOfWeek day =DayOfWeek.of(dayIndex);
//        int hour = 0;
//        SchoolClass schoolClass = descriptor.getTimeTable().getSchoolClasses().get(1);
//        Subject subject = descriptor.getTimeTable().getSubjects().get(1);
//        Teacher teacher = descriptor.getTimeTable().getTeachers().
//                get(descriptor.getTimeTable().getTeachersThatTeachSubject(subject.getId()).get(0));
//        quintets.add(new Quintet(day,hour,teacher,schoolClass,subject));
//        quintets.add(new Quintet(day,hour,teacher,schoolClass,subject));
//        TimeTableSolution solution = new TimeTableSolution(quintets, quintets.size(), descriptor.getTimeTable());
//        Singularity singularity = new Singularity("soft");
//        singularity.fitnessEvaluation(solution);
//        solution.calculateTotalScore();
//        assertEquals(50.0, solution.getFitnessScorePerRule().get(singularity));
//        assertTrue(solution.getTotalFitnessScore()>0.0);
//    }
//    @Test
//    void singularityGivesSolutionFullScore(){
//        List<Quintet> quintets = new ArrayList<>();
//
//        SchoolClass schoolClass = descriptor.getTimeTable().getSchoolClasses().get(1);
//
//
//
//        Map <Integer,SchoolClass> schoolClasses = new HashMap<>();
//        schoolClasses.put(1,schoolClass);
//        TimeTableSolution solution = new TimeTableSolution(quintets, 0, descriptor.getTimeTable());
//        Singularity singularity = new Singularity("soft");
//        singularity.fitnessEvaluation(solution);
//        solution.calculateTotalScore();
//        assertEquals(0.0, solution.getFitnessScorePerRule().get(singularity));
//        assertTrue(solution.getTotalFitnessScore()==0.0);
//    }
//    @Test
//    void teacherIsHumanGivesSolutionFullScore(){
//        List<Quintet> quintets = new ArrayList<>();
//        int dayIndex = 1;
//        DayOfWeek day =DayOfWeek.of(dayIndex);
//        int hour = 0;
//        SchoolClass schoolClass = descriptor.getTimeTable().getSchoolClasses().get(1);
//
//
//        Map<Integer, SchoolClass> schoolClasses = new HashMap<>();
//        schoolClasses.put(1, schoolClass);
//        TimeTableSolution solution = new TimeTableSolution(quintets, 0, descriptor.getTimeTable());
//        TeacherIsHuman teacherIsHuman = new TeacherIsHuman("soft");
//        teacherIsHuman.fitnessEvaluation(solution);
//        solution.calculateTotalScore();
//        assertEquals(0.0, solution.getFitnessScorePerRule().get(teacherIsHuman));
//        assertTrue(solution.getTotalFitnessScore() == 0.0);
//    }
//
//    @Test
//    void allRulesGiveSolutionFullScore() throws Throwable {
//        descriptor = xmlParser.unmarshall("src/resources/EX1-smaller2.xml");
//        List<Quintet> quintets = new ArrayList<>();
//        int dayIndex = 1;
//        DayOfWeek day = DayOfWeek.of(dayIndex);
//        int hour = 0;
//        Map<Integer, SchoolClass> schoolClasses = new HashMap<>();
//        SchoolClass schoolClass = descriptor.getTimeTable().getSchoolClasses().get(1);
//
//        for (Requirement requirement : schoolClass.getRequirements()) {
//            for (int i = 0; i < requirement.getHours(); i++) {
//                Subject subject = requirement.getSubject();
//                Teacher teacher = descriptor.getTimeTable()
//                        .getTeachers().get(descriptor.getTimeTable().getTeachersThatTeachSubject(subject.getId()).get(0));
//                if(hour>=descriptor.getTimeTable().getHours()){
//                    hour=0;
//                    dayIndex++;
//                    day = DayOfWeek.of(dayIndex);
//                }
//                quintets.add(new Quintet(day,hour,teacher,schoolClass,subject));
//                hour++;
//            }
//        }
//        schoolClasses.put(1,schoolClass);
//        TimeTableSolution solution = new TimeTableSolution(quintets, quintets.size(), descriptor.getTimeTable());
//
//        Satisfactory satisfactory = new Satisfactory("soft",schoolClasses);
//        satisfactory.fitnessEvaluation(solution);
//        TeacherIsHuman teacherIsHuman = new TeacherIsHuman("hard");
//        teacherIsHuman.fitnessEvaluation(solution);
//        Singularity singularity = new Singularity("soft");
//        singularity.fitnessEvaluation(solution);
//        Knowledgeable knowledgeable = new Knowledgeable("soft");
//        knowledgeable.fitnessEvaluation(solution);
//
//        solution.calculateTotalScore();
//        assertEquals(100.0,solution.getFitnessScorePerRule().get(knowledgeable));
//        assertEquals(100.0, solution.getFitnessScorePerRule().get(singularity));
//        assertEquals(100.0, solution.getFitnessScorePerRule().get(teacherIsHuman));
//        assertEquals(100.0,solution.getFitnessScorePerRule().get(satisfactory));
//        assertEquals(100.0,solution.getTotalFitnessScore());
//    }
//    //#endregion rules
//    @Test
//    void engineSettingsConstructorNotAcceptingNulls(){
//        try {
//            EngineSettings engineSettings = new EngineSettings(null, null);
//        }catch(IllegalArgumentException e){
//            assertTrue(e.getMessage().length()>5);
//        }
//    }
//
//    @Test
//    void evolutionEngineExecuteReturnsDifferentPopulation(){
//        assertNotEquals(initialPopulation,evolutionEngine.execute(initialPopulation));
//    }
//
//    @Test
//    void timeTableSolutionQuintetsNumIsLessThanTotalRequiredHours() {
//        TimeTableSolution timeTableSolution = new TimeTableSolution(descriptor.getTimeTable());
//        int totalRequiredHours = 0;
//        Map<Integer, SchoolClass> classes = descriptor.getTimeTable().getSchoolClasses();
//        for (SchoolClass c : classes.values()) {
//            totalRequiredHours += c.getTotalRequiredHours();
//        }
//        int quintetNum = timeTableSolution.getSolutionSize();
//        assertTrue(quintetNum <= totalRequiredHours);
//
//    }
//
//}