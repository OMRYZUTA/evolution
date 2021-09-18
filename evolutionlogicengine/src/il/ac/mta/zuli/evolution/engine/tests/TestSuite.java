//package il.ac.mta.zuli.evolution.engine.tests;
//
//
//import il.ac.mta.zuli.evolution.engine.Descriptor;
//import il.ac.mta.zuli.evolution.engine.Quintet;
//import il.ac.mta.zuli.evolution.engine.TimeTableSolution;
//import il.ac.mta.zuli.evolution.engine.evolutionengine.EngineSettings;
//import il.ac.mta.zuli.evolution.engine.evolutionengine.EvolutionEngine;
//import il.ac.mta.zuli.evolution.engine.evolutionengine.crossover.CrossoverInterface;
//import il.ac.mta.zuli.evolution.engine.evolutionengine.mutation.Mutation;
//import il.ac.mta.zuli.evolution.engine.evolutionengine.mutation.Sizer;
//import il.ac.mta.zuli.evolution.engine.evolutionengine.selection.Selection;
//import il.ac.mta.zuli.evolution.engine.rules.*;
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
//class TestSuite {
//
//
//    final XMLParser xmlParser = new XMLParser();
//    Descriptor descriptor;
//    EvolutionEngine evolutionEngine;
//    List<TimeTableSolution> initialPopulation;
//    private int generationCounter = 0;
//
//    @BeforeEach
//    void setUp() {
//        try {
//            descriptor = xmlParser.unmarshall("src/resources/ex1/EX1-big.xml");
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
//    @Test
//    void testSetUp() {
//        assertTrue(descriptor != null);
//        assertTrue(initialPopulation.size() == descriptor.getPopulationSize());
//        assertTrue(evolutionEngine != null);
//    }
//
//
////    #region xmlParser
//
//    @Test
//    void loadBig() {
//        try {
//            descriptor = xmlParser.unmarshall("src/resources/ex1/EX1-big.xml");
//
//            assertTrue(descriptor != null);
//        } catch (Throwable e) {
//
//        }
//    }
//
//    @Test
//    void loadSmall() {
//        try {
//            descriptor = xmlParser.unmarshall("src/resources/ex1/EX1-small.xml");
//
//            assertTrue(descriptor != null);
//        } catch (Throwable e) {
//
//        }
//    }
//
//    @Test
//    void loadError3Dot2XMLFILE() {
//        try {
//            descriptor = null;
//            descriptor = xmlParser.unmarshall("src/resources/ex1/EX1-error-teache-id-2-missing.xml");
//
//
//        } catch (Throwable e) {
//            assertTrue(descriptor == null);
//        }
//    }
//
//    @Test
//    void nonExistingSubjectThrowsException() {
//        try {
//            descriptor = null;
//            descriptor = xmlParser.unmarshall("src/resources/ex1/EX1-error-non-existing-subject-in-teacher.xml");
//
//
//        } catch (Throwable e) {
//            assertTrue(descriptor == null);
//        }
//    }
//
//    @Test
//    void tooManyHoursThrowsException() {
//        try {
//            descriptor = null;
//            descriptor = xmlParser.unmarshall("src/resources/ex1/EX1-error-to-many-hours.xml");
//
//
//        } catch (Throwable e) {
//            assertTrue(descriptor == null);
//        }
//    }
//
//
//    //#endregion xmlParser
////
////#region evolution
//    @Test
//    void selectReturnsLessSolutions() {
//        Selection<TimeTableSolution> selection = descriptor.getEngineSettings().getSelection();
//        assertTrue(selection.select(initialPopulation).size() <= initialPopulation.size());
//    }
//
//
//    @Test
//    void crossOverReturnsDifferentPopulation() {
//        CrossoverInterface crossover = descriptor.getEngineSettings().getCrossover();
//        assertNotEquals(crossover.crossover(initialPopulation), initialPopulation);
//    }
//
//    @Test
//    void crossOverReturnsSameSizeOfPopulation() {
//        CrossoverInterface crossover = descriptor.getEngineSettings().getCrossover();
//        assertEquals(crossover.crossover(initialPopulation).size(), initialPopulation.size());
//    }
//
//
//    @Test
//    void mutateReturnsDifferentPopulation() {
//        List<Mutation<TimeTableSolution>> mutations = getMutations();
//        List<TimeTableSolution> solutionsAfterMutations = new ArrayList<>();
//        for (TimeTableSolution solution : initialPopulation) {
//            TimeTableSolution tempSolution = solution;
//            for (Mutation<TimeTableSolution> mutaiton : mutations) {
//                tempSolution = mutaiton.mutate(tempSolution);
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
//            TimeTableSolution tempSolution = solution;
//            for (Mutation<TimeTableSolution> mutaiton : mutations) {
//                tempSolution = mutaiton.mutate(tempSolution);
//            }
//            solutionsAfterMutations.add(tempSolution);
//        }
//        assertEquals(solutionsAfterMutations.size(), initialPopulation.size());
//    }
//
//    @Test
//    void evolutionEngineExecuteReturnsSameSizeOfPopulation() {
//        assertEquals(initialPopulation.size(), evolutionEngine.execute(initialPopulation).size());
//    }
//
//    @Test
//    void sizerAddQuintetsFromSolution() throws Throwable {
//        List<Quintet> quintets = new ArrayList<>();
//        int dayIndex = 1;
//        DayOfWeek day = DayOfWeek.of(dayIndex);
//        int hour = 0;
//        descriptor = xmlParser.unmarshall("src/resources/ex1/EX1-smallForSizer.xml");
//
//        SchoolClass schoolClass = descriptor.getTimeTable().getSchoolClasses().get(1);
//
//        for (Requirement requirement : schoolClass.getRequirements()) {
//            for (int i = 0; i < requirement.getHours(); i += 3) {
//                Subject subject = requirement.getSubject();
//                Teacher teacher = descriptor.getTimeTable()
//                        .getTeachers().get(descriptor.getTimeTable().getTeachersThatTeachSubject(subject.getId()).get(0));
//                quintets.add(new Quintet(day, hour, teacher, schoolClass, subject));
//            }
//        }
//
//        Map<Integer, SchoolClass> schoolClasses = new HashMap<>();
//        schoolClasses.put(1, schoolClass);
//        TimeTableSolution solution = new TimeTableSolution(quintets, quintets.size(), descriptor.getTimeTable());
//
//        List<Mutation<TimeTableSolution>> mutations = getMutations();
//        Sizer mutation = (Sizer) mutations.get(0);
//        TimeTableSolution mutatedSolution = (TimeTableSolution) mutation.mutate(solution);
//        assertTrue(solution.getSolutionSize() < mutatedSolution.getSolutionSize());
//
//    }
//
//    @Test
//    void sizerRemoveQuintetsFromSolution() throws Throwable {
//        List<Quintet> quintets = new ArrayList<>();
//        int dayIndex = 1;
//        DayOfWeek day = DayOfWeek.of(dayIndex);
//        int hour = 0;
//        descriptor = xmlParser.unmarshall("src/resources/ex1/EX1-smallForSizer.xml");
//        SchoolClass schoolClass = descriptor.getTimeTable().getSchoolClasses().get(1);
//
//        createQuintetList(quintets, day, hour, schoolClass);
//
//        Map<Integer, SchoolClass> schoolClasses = new HashMap<>();
//        schoolClasses.put(1, schoolClass);
//        TimeTableSolution solution = new TimeTableSolution(quintets, quintets.size(), descriptor.getTimeTable());
//
//        List<Mutation<TimeTableSolution>> mutations = getMutations();
//        Sizer mutation = (Sizer) mutations.get(1);
//        TimeTableSolution mutatedSolution = (TimeTableSolution) mutation.mutate(solution);
//        assertTrue(solution.getSolutionSize() > mutatedSolution.getSolutionSize());
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
//
//
//    //#endregion evolution
////
////
////#region rules
//    @Test
//    void knowledgeableGivesEmptySolutionFullScore() {
//        List<Quintet> quintets = new ArrayList<>();
//        int dayIndex = 1;
//        DayOfWeek day = DayOfWeek.of(dayIndex);
//        int hour = 0;
//        SchoolClass schoolClass = descriptor.getTimeTable().getSchoolClasses().get(1);
//        Requirement requirement = schoolClass.getRequirements().get(0);
//        int teachingHours = requirement.getHours();
//        Subject subject = requirement.getSubject();
//        Teacher teacher = descriptor.getTimeTable().getTeachers().
//                get(descriptor.getTimeTable().getTeachersThatTeachSubject(subject.getId()).get(0));
//        TimeTableSolution solution = new TimeTableSolution(quintets, 0, descriptor.getTimeTable());
//        Knowledgeable knowledgeable = new Knowledgeable("soft");
//        knowledgeable.fitnessEvaluation(solution);
//        solution.calculateTotalScore();
//        assertEquals(0.0, solution.getFitnessScorePerRule().get(knowledgeable));
//        assertTrue(solution.getTotalFitnessScore() == 0.0);
//    }
//
//
//    @Test
//    void knowledgeableGiveSolutionFullScore() {
//        List<Quintet> quintets = new ArrayList<>();
//        int dayIndex = 1;
//        DayOfWeek day = DayOfWeek.of(dayIndex);
//        int hour = 0;
//        SchoolClass schoolClass = descriptor.getTimeTable().getSchoolClasses().get(1);
//        Requirement requirement = schoolClass.getRequirements().get(0);
//        int teachingHours = requirement.getHours();
//        Subject subject = requirement.getSubject();
//        Teacher teacher = descriptor.getTimeTable().getTeachers().
//                get(descriptor.getTimeTable().getTeachersThatTeachSubject(subject.getId()).get(0));
//        for (int i = 0; i < teachingHours; i++) {
//            if (hour >= descriptor.getTimeTable().getHours()) {
//                dayIndex++;
//                day = DayOfWeek.of(dayIndex);
//            }
//            quintets.add(new Quintet(day, hour, teacher, schoolClass, subject));
//        }
//        TimeTableSolution solution = new TimeTableSolution(quintets, quintets.size(), descriptor.getTimeTable());
//        Knowledgeable knowledgeable = new Knowledgeable("soft");
//        knowledgeable.fitnessEvaluation(solution);
//        solution.calculateTotalScore();
//        assertEquals(solution.getFitnessScorePerRule().get(knowledgeable), 100.0);
//        assertTrue(solution.getTotalFitnessScore() > 0.0);
//    }
//
//    @Test
//    void satisfactoryGivesSolutionFullScore() {
//        List<Quintet> quintets = new ArrayList<>();
//        int dayIndex = 1;
//        DayOfWeek day = DayOfWeek.of(dayIndex);
//        int hour = 0;
//
//        SchoolClass schoolClass = descriptor.getTimeTable().getSchoolClasses().get(1);
//
//        createQuintetList(quintets, day, hour, schoolClass);
//
//        Map<Integer, SchoolClass> schoolClasses = new HashMap<>();
//        schoolClasses.put(1, schoolClass);
//        TimeTableSolution solution = new TimeTableSolution(quintets, quintets.size(), descriptor.getTimeTable());
//        Satisfactory satisfactory = new Satisfactory("soft", schoolClasses);
//        satisfactory.fitnessEvaluation(solution);
//        solution.calculateTotalScore();
//        assertEquals(solution.getFitnessScorePerRule().get(satisfactory), 100.0);
//    }
//
//    @Test
//    void satisfactoryGivesSolutionZeroScore() {
//        List<Quintet> quintets = new ArrayList<>();
//        SchoolClass schoolClass = descriptor.getTimeTable().getSchoolClasses().get(1);
//        Map<Integer, SchoolClass> schoolClasses = new HashMap<>();
//
//        schoolClasses.put(1, schoolClass);
//        TimeTableSolution solution = new TimeTableSolution(quintets, 0, descriptor.getTimeTable());
//        Satisfactory satisfactory = new Satisfactory("soft", schoolClasses);
//        satisfactory.fitnessEvaluation(solution);
//        solution.calculateTotalScore();
//        assertEquals(0.0, solution.getFitnessScorePerRule().get(satisfactory));
//        assertTrue(solution.getTotalFitnessScore() == 0.0);
//    }
//
//    @Test
//    void singularityGivesSolutionZeroScore() {
//        List<Quintet> quintets = new ArrayList<>();
//        int dayIndex = 1;
//        DayOfWeek day = DayOfWeek.of(dayIndex);
//        int hour = 0;
//        SchoolClass schoolClass = descriptor.getTimeTable().getSchoolClasses().get(1);
//        Subject subject = descriptor.getTimeTable().getSubjects().get(1);
//        Teacher teacher = descriptor.getTimeTable().getTeachers().
//                get(descriptor.getTimeTable().getTeachersThatTeachSubject(subject.getId()).get(0));
//        quintets.add(new Quintet(day, hour, teacher, schoolClass, subject));
//        quintets.add(new Quintet(day, hour, teacher, schoolClass, subject));
//        TimeTableSolution solution = new TimeTableSolution(quintets, quintets.size(), descriptor.getTimeTable());
//        Singularity singularity = new Singularity("soft");
//        singularity.fitnessEvaluation(solution);
//        solution.calculateTotalScore();
//        assertEquals(50.0, solution.getFitnessScorePerRule().get(singularity));
//        assertTrue(solution.getTotalFitnessScore() > 0.0);
//    }
//
//    @Test
//    void singularityGivesSolutionFullScore() {
//        List<Quintet> quintets = new ArrayList<>();
//
//        SchoolClass schoolClass = descriptor.getTimeTable().getSchoolClasses().get(1);
//
//
//        Map<Integer, SchoolClass> schoolClasses = new HashMap<>();
//        schoolClasses.put(1, schoolClass);
//        TimeTableSolution solution = new TimeTableSolution(quintets, 0, descriptor.getTimeTable());
//        Singularity singularity = new Singularity("soft");
//        singularity.fitnessEvaluation(solution);
//        solution.calculateTotalScore();
//        assertEquals(0.0, solution.getFitnessScorePerRule().get(singularity));
//        assertTrue(solution.getTotalFitnessScore() == 0.0);
//    }
//
//    @Test
//    void teacherIsHumanGivesSolutionFullScore() {
//        List<Quintet> quintets = new ArrayList<>();
//        int dayIndex = 1;
//        DayOfWeek day = DayOfWeek.of(dayIndex);
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
//    @Test
//    void dayoffTeacherGiveSolutionFullscore() throws Throwable {
//        descriptor = xmlParser.unmarshall("src/resources/EX2-smallForDayOffTeacher.xml");
//        List<Quintet> quintets = new ArrayList<>();
//        int dayIndex = 1;
//        DayOfWeek day = DayOfWeek.of(dayIndex);
//        int hour = 0;
//        SchoolClass schoolClass = descriptor.getTimeTable().getSchoolClasses().get(1);
//
//        for (Requirement requirement : schoolClass.getRequirements()) {
//            for (int i = 0; i < requirement.getHours(); i++) {
//                Subject subject = requirement.getSubject();
//                Teacher teacher = descriptor.getTimeTable()
//                        .getTeachers().get(descriptor.getTimeTable().getTeachersThatTeachSubject(subject.getId()).get(0));
//                if (hour >= descriptor.getTimeTable().getHours()) {
//                    hour = 0;
//                    dayIndex++;
//                    day = DayOfWeek.of(dayIndex);
//                }
//                quintets.add(new Quintet(day, hour, teacher, schoolClass, subject));
//                hour++;
//            }
//        }
//        TimeTableSolution solution = new TimeTableSolution(quintets, quintets.size(), descriptor.getTimeTable());
//
//        DayOffTeacher dayOffTeacherRule = new DayOffTeacher("soft",descriptor.getTimeTableDays(),new ArrayList<>(descriptor.getTimeTable().getTeachers().values()));
//        dayOffTeacherRule.fitnessEvaluation(solution);
//        solution.calculateTotalScore();
//        assertEquals(100.0,solution.getTotalFitnessScore());
//    }
//
//    @Test
//    void dayoffTeacherGiveSolutionZeroscore() throws Throwable {
//        descriptor = xmlParser.unmarshall("src/resources/EX2-smallForDayOffTeacher.xml");
//        List<Quintet> quintets = new ArrayList<>();
//        int dayIndex = 1;
//        DayOfWeek day = DayOfWeek.of(dayIndex);
//        int hour = 0;
//        SchoolClass schoolClass = descriptor.getTimeTable().getSchoolClasses().get(2);
//
//        for (Requirement requirement : schoolClass.getRequirements()) {
//            for (int i = 0; i < requirement.getHours(); i+=5) {
//                Subject subject = requirement.getSubject();
//                Teacher teacher = descriptor.getTimeTable()
//                        .getTeachers().get(descriptor.getTimeTable().getTeachersThatTeachSubject(subject.getId()).get(0));
//                if (hour >= descriptor.getTimeTable().getHours()) {
//                    hour = 0;
//                    dayIndex++;
//                    day = DayOfWeek.of(dayIndex);
//                }
//                quintets.add(new Quintet(day, hour, teacher, schoolClass, subject));
//                hour++;
//            }
//        }
//        TimeTableSolution solution = new TimeTableSolution(quintets, quintets.size(), descriptor.getTimeTable());
//
//        DayOffTeacher dayOffTeacherRule = new DayOffTeacher("Hard",descriptor.getTimeTableDays(),new ArrayList<>(descriptor.getTimeTable().getTeachers().values()));
//        dayOffTeacherRule.fitnessEvaluation(solution);
//        solution.calculateTotalScore();
//        assertEquals(0.0,solution.getTotalFitnessScore());
//    }
//
//
//    @Test
//    void allRulesGiveSolutionFullScore() throws Throwable {
//        descriptor = xmlParser.unmarshall("src/resources/ex1/EX1-smaller2.xml");
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
//                if (hour >= descriptor.getTimeTable().getHours()) {
//                    hour = 0;
//                    dayIndex++;
//                    day = DayOfWeek.of(dayIndex);
//                }
//                quintets.add(new Quintet(day, hour, teacher, schoolClass, subject));
//                hour++;
//            }
//        }
//        schoolClasses.put(1, schoolClass);
//        TimeTableSolution solution = new TimeTableSolution(quintets, quintets.size(), descriptor.getTimeTable());
//
//        Satisfactory satisfactory = new Satisfactory("soft", schoolClasses);
//        satisfactory.fitnessEvaluation(solution);
//        TeacherIsHuman teacherIsHuman = new TeacherIsHuman("hard");
//        teacherIsHuman.fitnessEvaluation(solution);
//        Singularity singularity = new Singularity("soft");
//        singularity.fitnessEvaluation(solution);
//        Knowledgeable knowledgeable = new Knowledgeable("soft");
//        knowledgeable.fitnessEvaluation(solution);
//
//        solution.calculateTotalScore();
//        assertEquals(100.0, solution.getFitnessScorePerRule().get(knowledgeable));
//        assertEquals(100.0, solution.getFitnessScorePerRule().get(singularity));
//        assertEquals(100.0, solution.getFitnessScorePerRule().get(teacherIsHuman));
//        assertEquals(100.0, solution.getFitnessScorePerRule().get(satisfactory));
//        assertEquals(100.0, solution.getTotalFitnessScore());
//    }
//
//    //#endregion rules
//    @Test
//    void engineSettingsConstructorNotAcceptingNulls() {
//        try {
//            EngineSettings engineSettings = new EngineSettings(null, null);
//        } catch (IllegalArgumentException e) {
//            assertTrue(e.getMessage().length() > 5);
//        }
//    }
//
//
//    @Test
//    void evolutionEngineExecuteReturnsDifferentPopulation() {
//        assertNotEquals(initialPopulation, evolutionEngine.execute(initialPopulation));
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
//}