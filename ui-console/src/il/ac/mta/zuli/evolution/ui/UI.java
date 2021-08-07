package il.ac.mta.zuli.evolution.ui;

import il.ac.mta.zuli.evolution.dto.*;
import il.ac.mta.zuli.evolution.engine.Engine;
import il.ac.mta.zuli.evolution.engine.TimeTableEngine;
import il.ac.mta.zuli.evolution.engine.events.ErrorEvent;
import il.ac.mta.zuli.evolution.engine.events.LoadedEvent;
import il.ac.mta.zuli.evolution.engine.events.OnStrideEvent;

import java.time.DayOfWeek;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

public class UI {
    private final Engine engine;
    private final Scanner scanner;
    private int stride;
    private int numOfGenerations;
    private boolean fileLoaded;
    private boolean evolutionAlgorithmCompleted;

    public UI() {
        engine = new TimeTableEngine();
        scanner = new Scanner(System.in);
        stride = 0;
        numOfGenerations = 0;
        fileLoaded = false;
        evolutionAlgorithmCompleted = false;
        addListenersToEngineEvents();
    }

    public void operateMenu() {
        boolean stayInSystem = true;

        // TODO: make sure engine never throws exceptions. It should catch them, and raise an "error" event
        while (stayInSystem) {
            printMainMenu();
            MenuOptions selectedOption = getSelectedOption(scanner.nextLine());
            switch (selectedOption) {
                case LoadFile:
                    loadFile();
                    break;
                case ShowDetails:
                    if (fileLoaded) {
                        showDetails();
                    } else {
                        System.out.println("Option unavailable until a file is loaded to the system");
                        System.out.println("***");
                    }
                    break;
                case RunAlgorithm:
                    if (fileLoaded) {
                        runAlgorithm();
                    } else {
                        System.out.println("Option unavailable until a file is loaded to the system");
                        System.out.println("***");
                    }
                    break;
                case ShowSolution:
                    if (evolutionAlgorithmCompleted) {
                        showSolution();
                    } else {
                        System.out.println("Option unavailable until a file is loaded to the system, and algorithm completed running");
                        System.out.println("***");
                    }
                    break;
                case ShowProgress:
                    if (evolutionAlgorithmCompleted) {
                        showProgress();
                    } else {
                        System.out.println("Option unavailable until a file is loaded to the system, and algorithm completed running");
                        System.out.println("***");
                    }
                    break;
                case Exit:
                    stayInSystem = false;
                    break;
                case None:
                default:
                    System.out.println("Invalid option entered, please try again");
                    System.out.println("***");
                    break;
            }
        }
    }

    private MenuOptions getSelectedOption(String input) {
        MenuOptions result = MenuOptions.None;
        try {
            int intInput = Integer.parseInt(input, 10);
            result = MenuOptions.values()[intInput];
        } catch (Exception e) {
            // swallow parsing exceptions
        }

        return result;
    }

    private void loadFile() {
        System.out.println(System.lineSeparator() + "To load XML file, enter absolute file path:");
        //TODO: restore later
        String path = "C:\\Users\\fifil\\source\\repos\\evolution\\engine\\src\\resources\\EX1-small.xml"; //scanner.nextLine();
        if (!checkExtension(path, ".xml")) {
            System.out.println("Invalid file path. Must be an xml file");
            System.out.println("***");
            return;
        }

        engine.loadXML(path); //C:\Users\fifil\source\repos\evolution\engine\src\resources\EX1-small.xml
    }

    private boolean checkExtension(String path, String expectedExt) {
        if (!path.contains(".")) {
            return false;
        }

        String ext = path.substring(path.lastIndexOf("."));
        return expectedExt.equalsIgnoreCase(ext);
    }

    private void showDetails() {
        DescriptorDTO descriptorDTO = engine.getSystemDetails();
        TimeTableDTO timeTableDTO = descriptorDTO.getTimeTable();
        EngineSettingsDTO engineSettingsDTO = descriptorDTO.getEngineSettings();
        System.out.println("Showing timetable and algorithm settings");
        printTimeTable(timeTableDTO);
        printEngineSetting(engineSettingsDTO);

        System.out.println("***");
    }

    private void runAlgorithm() {
        System.out.println("To run the evolution algorithm");
        int numOfGenerations = 0;

        //TODO continue from here - doesn't work well
        if (!getNumOfGenerationsInput()) {
            return;
        }

        if (!getGenerationStride()) {
            return;
        }

        //TODO later change from hardcoded
        engine.executeEvolutionAlgorithm(100, 50);
    }

    private boolean getNumOfGenerationsInput() {
        boolean result = false;
        System.out.println("Enter the number of generations (greater than 100): ");

        try {
            numOfGenerations = Integer.parseInt(scanner.nextLine());
        } catch (Exception E) {
            // swallow parsing exceptions
        }

        if (numOfGenerations < 100) {
            numOfGenerations = 0;
            System.out.println("Invalid number of generations entered, must be an integer greater than 100");
        } else {
            result = true;
        }

        return result;
    }

    private boolean getGenerationStride() {
        boolean result = false;
        System.out.println("Enter the generation stride you'd like to see while the algorithm runs (a positive integer)");

        try {
            stride = Integer.parseInt(scanner.nextLine());
        } catch (Exception E) {
            // swallow parsing exceptions
        }

        if (stride <= 0) {
            stride = 0;
            System.out.println("Invalid stride value entered, the value must be a positive integer");
        } else {
            result = true;
        }

        return result;
    }

    private void showSolution() {
        printShowSolutionMenu();

        int displayOption = 0;
        try {
            displayOption = Integer.parseInt(scanner.nextLine(), 10);
        } catch (Exception e) {
            //swallow exception
        }

        TimeTableSolutionDTO bestSolution = engine.getBestSolution();

        switch (displayOption) {
            case 1:
                printRawQuintets(bestSolution);
                break;
            case 2:
                printTeacherDHMatrix(bestSolution);
                break;
            case 3:
                printClassDHMatrix(bestSolution);
                break;
            case 0:
            default:
                System.out.println("Invalid option entered");
        }
    }

    private void printRawQuintets(TimeTableSolutionDTO solution) {
        for (QuintetDTO q : solution.getSolutionQuintets()) {
            System.out.printf("<%d, %d, %d, %d, %d>%n",
                    q.getDay().getValue(),
                    q.getHour(),
                    q.getSchoolClass().getId(),
                    q.getTeacher().getId(),
                    q.getSubject().getId());
        }
    }

    private void printTeacherDHMatrix(TimeTableSolutionDTO solution) {
        Map<TeacherDTO, List<QuintetDTO>> teacherSolutions = solution.getSolutionQuintets().stream()
                .collect(Collectors.groupingBy(QuintetDTO::getTeacher));

        List<TeacherDTO> sortedTeachers = teacherSolutions.keySet().stream()
                .sorted(Comparator.comparingInt(TeacherDTO::getId))
                .collect(Collectors.toList());

        for (TeacherDTO t : sortedTeachers) {
            System.out.println(t.getName());
            printMatrix(teacherSolutions.get(t),
                    q -> String.format("%2s, %2s", q.getSchoolClass().getId(), q.getSubject().getId()));
            System.out.println("***");
        }
    }

    private void printClassDHMatrix(TimeTableSolutionDTO solution) {
        Map<SchoolClassDTO, List<QuintetDTO>> classSolutions = solution.getSolutionQuintets().stream()
                .collect(Collectors.groupingBy(QuintetDTO::getSchoolClass));

        List<SchoolClassDTO> sortedClasses = classSolutions.keySet().stream()
                .sorted(Comparator.comparingInt(SchoolClassDTO::getId))
                .collect(Collectors.toList());

        for (SchoolClassDTO t : sortedClasses) {
            System.out.println(t.getName());
            printMatrix(classSolutions.get(t),
                    q -> String.format("%2s, %2s", q.getTeacher().getId(), q.getSubject().getId()));
            System.out.println("***");
        }
    }

    private void printMatrix(Collection<QuintetDTO> quintets, QuintetFormatter qf) {
        int hours = engine.getSystemDetails().getTimeTable().getHours();
        int days = engine.getSystemDetails().getTimeTable().getDays();
        List<QuintetDTO>[][] matrix = populateMatrix(quintets, hours, days);

        printHeaderLine(days);
        printSeparatorLine(days);

        for (int h = 0; h < hours; h++) {
            boolean moreToPrint = true;
            int cellIterations = 0;
            while (moreToPrint) {
                StringBuilder sb = new StringBuilder();
                Formatter formatter = new Formatter(sb); // formatting into the StringBuilder
                moreToPrint = false;
                formatter.format(" %2d ", h);
                for (int d = 0; d < days; d++) {
                    List<QuintetDTO> cell = matrix[h][d];
                    if (cell.size() <= cellIterations) {
                        formatter.format("|%8s", " ");
                    } else {
                        QuintetDTO q = cell.get(cellIterations);
                        formatter.format("| %s ", qf.format(q));
                        if (cell.size() > cellIterations + 1) {
                            moreToPrint = true;     // Need to print another line,
                        }
                    }
                }

                formatter.format("|");
                System.out.println(sb);
                if (moreToPrint) {
                    cellIterations++; // Advance to next row
                } else {
                    printSeparatorLine(days);
                }
            }
        }
    }

    private List<QuintetDTO>[][] populateMatrix(Collection<QuintetDTO> quintets, int hours, int days) {
        List<QuintetDTO>[][] matrix = (List<QuintetDTO>[][]) new List[hours][days];

        for (int h = 0; h < hours; h++) {
            for (int d = 0; d < days; d++) {
                matrix[h][d] = new ArrayList<>();
            }
        }

        for (QuintetDTO q : quintets) {
            matrix[q.getHour()][q.getDay().getValue() - 1].add(q);
        }

        return matrix;
    }

    private void showProgress() {
    }

    private void printEngineSetting(EngineSettingsDTO engineSettingsDTO) {
        System.out.println(String.format("population size : %d", engineSettingsDTO.getInitialPopulationSize()));
        SelectionDTO selectionDTO = engineSettingsDTO.getSelection();
        CrossoverDTO crossoverDTO = engineSettingsDTO.getCrossover();
        List<MutationDTO> mutationDTOList = engineSettingsDTO.getMutations();

        System.out.println("*** Engine Settings ***");
        System.out.println("Population size: " + engineSettingsDTO.getInitialPopulationSize());
        System.out.println(selectionDTO);
        System.out.println(crossoverDTO);
        printList(mutationDTOList);
    }

    private void printTimeTable(TimeTableDTO timeTableDTO) {
        Set<RuleDTO> rules = timeTableDTO.getRules();

        System.out.println("*** Time Table ***");
        System.out.println("Subjects");
        printMap(timeTableDTO.getSubjects());
        System.out.println("Teachers");
        printTeachers(timeTableDTO.getTeachers());
        System.out.println("Classes");
        printSchoolClasses(timeTableDTO.getSchoolClasses());
        System.out.println("Rules");
        printSet(rules);
    }

    private void printTeachers(Map<Integer, TeacherDTO> teachers) {
        for (TeacherDTO teacher : teachers.values()) {
            System.out.print(teacher + ", teaches subjects: ");
            for (SubjectDTO subject : teacher.getSubjects().values()) {
                System.out.print(subject + " ");
            }
            System.out.println();
        }
    }

    private void printSchoolClasses(Map<Integer, SchoolClassDTO> schoolClasses) {
        for (SchoolClassDTO schoolClass : schoolClasses.values()) {
            System.out.print(schoolClass + ", requirements: ");

            for (RequirementDTO requirement : schoolClass.getRequirements()) {
                System.out.print(requirement + " ");
            }

            System.out.println();
        }
    }

    private void addListenersToEngineEvents() {
        engine.addListener("loaded", e -> {
            UI.this.fileLoaded = true;
            LoadedEvent loadedEvent = (LoadedEvent) e;
            System.out.println("file was successfully loaded from " + loadedEvent.getPath());
            System.out.println("***");
        });
        engine.addListener("error", e -> {
            ErrorEvent errorEvent = (ErrorEvent) e;
            System.out.println("failed loading file: " + errorEvent.getError().getMessage());
            System.out.println("***");
        });
        engine.addListener("stride", e -> {
            OnStrideEvent strideEvent = (OnStrideEvent) e;
            UI.this.showStrideProgress(strideEvent.getGenerationStrideScoreDTO());
        });
        engine.addListener("completed", e -> {
            UI.this.evolutionAlgorithmCompleted = true;
            System.out.println(e.getMessage());
            System.out.println("***");
        });
    }

    private void showStrideProgress(GenerationStrideScoreDTO generationStrideScoreDTO) {
        System.out.println("generation: " + generationStrideScoreDTO.getGenerationNum()
                + " best score: " + generationStrideScoreDTO.getBestScore());
    }

    private void printHeaderLine(int days) {
        StringBuilder sb = new StringBuilder("    "); //4 spaces
        Formatter formatter = new Formatter(sb);
        for (int d = 0; d < days; d++) {
            DayOfWeek day = DayOfWeek.of(d + 1);

            // 2 spaces [SHORT is 3 letters per day] 3 spaces
            formatter.format("|  %s   ", day.getDisplayName(TextStyle.SHORT, Locale.getDefault()));
        }

        sb.append("|");
        System.out.println(sb);
    }

    private void printSeparatorLine(int days) {
        System.out.println(String.join("", Collections.nCopies(5 + 9 * days, "-")));
    }

    private void printList(List<?> values) {
        for (Object object : values) {
            System.out.println(object);
        }
    }

    private void printSet(Set<RuleDTO> rules) {
        for (RuleDTO rule : rules) {
            System.out.println(rule);
        }
    }

    private <K, V> void printMap(Map<K, V> map) {
        for (V value : map.values()) {
            System.out.println(value);
        }
    }

    // Optional for raw print in #4
    private void printMinQuintets(List<QuintetDTO> quintets) {
        for (QuintetDTO q : quintets) {
            System.out.printf("<%s, %d, %s, %s, %s>%n",
                    q.getDay(),
                    q.getHour(),
                    q.getSchoolClass().getName(),
                    q.getTeacher().getName(),
                    q.getSubject().getName());
        }
    }

    private void printMainMenu() {
        /*String noFileLoaded = fileLoaded ? "" : " <Unavailable - no file loaded>";
        String algoIncomplete = evolutionAlgorithmCompleted ? "" : " <Unavailable - algorithm run was not completed>";*/
        System.out.println("To select from the menu, please enter the option number:");
        System.out.println(MenuOptions.LoadFile.ordinal() + ". Load xml file");
        System.out.println(MenuOptions.ShowDetails.ordinal() + ". Show timetable and algorithm settings");// + noFileLoaded);
        System.out.println(MenuOptions.RunAlgorithm.ordinal() + ". Run evolution algorithm");// + noFileLoaded);
        System.out.println(MenuOptions.ShowSolution.ordinal() + ". Show best solution");// + algoIncomplete);
        System.out.println(MenuOptions.ShowProgress.ordinal() + ". Show algorithm progress per stride");// + algoIncomplete);
        System.out.println(MenuOptions.Exit.ordinal() + ". Exit system");
    }

    private void printShowSolutionMenu() {
        System.out.println("Enter an option number for your preferred display of the best solution:");
        System.out.println("1. A sorted list of the solution quintets <D,H,C,T,S>");
        System.out.println("2. A separate D*H timetable for every teacher");
        System.out.println("3. A separate D*H timetable for every class");
    }

    //    @Override
//    public void actionPerformed(ActionEvent e) {
//        switch (e.getID()) {
//            case 1:
//                System.out.println(e.getActionCommand());
//                break;
//            case 3:
//                GenerationStrideScoreDTO generationStrideScoreDTO = ((OnStrideEvent) e).getGenerationStrideScoreDTO();
//                showStrideProgress(generationStrideScoreDTO);
//                break;
//            default:
//                throw new IllegalStateException("Unexpected value: " + e.getID());
//        }
//    }
}
