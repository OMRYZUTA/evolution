package il.ac.mta.zuli.evolution.ui;

import il.ac.mta.zuli.evolution.engine.Engine;
import il.ac.mta.zuli.evolution.engine.MyUtils;
import il.ac.mta.zuli.evolution.engine.TimeTableEngine;
import il.ac.mta.zuli.evolution.engine.events.ErrorEvent;
import il.ac.mta.zuli.evolution.engine.events.ErrorType;
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

    // just for debugging - Types of solutions reports
    public enum ReportType {
        TEACHER_VIEW,
        CLASS_VIEW,
        FULL_VIEW
    }

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
                        showBestSolution();
                    } else {
                        System.out.println("Option unavailable until a file is loaded to the system, and algorithm completed running");
                        System.out.println("***");
                    }
                    break;
                case ShowProgress:
                    if (evolutionAlgorithmCompleted) {
                        showProgressHistory();
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

    private void loadFile() {
        System.out.println("To load XML file, enter file path:");

        String path = scanner.nextLine();
        //String path = "C:\\Users\\fifil\\source\\repos\\evolution\\engine\\src\\resources\\EX1-small.xml"; //scanner.nextLine();
        if (!checkExtension(path, ".xml")) {
            System.out.println("Invalid file path. Must be an xml file");
            System.out.println("***");
            return;
        }

        engine.loadXML(path); //C:\Users\fifil\source\repos\evolution\engine\src\resources\EX1-small.xml
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
        if (evolutionAlgorithmCompleted) {
            System.out.println("The evolution-algorithm has already completed its course. " + System.lineSeparator() +
                    "If you choose to re-run it, the information from the previous run will be lost." + System.lineSeparator() +
                    "Would you like to re-run the algorithm? (Enter Y/N)");
            String answer = scanner.nextLine();
            if (!answer.equalsIgnoreCase("y")) {
                return;
            }
        }

        System.out.println("To run the evolution algorithm");

        if (!getNumOfGenerationsInput()) {
            return;
        }

        if (!getGenerationStrideInput()) {
            return;
        }

        System.out.println("Would you like to stop the algorithm before running through all generations when attaining a certain score? (enter Y/N)");
        String answer = scanner.nextLine();
        if (answer.equalsIgnoreCase("y")) {
            System.out.println("Enter a score to use as stopping-point (can be Integer or floating-point number)");
            double stopScore = 0;

            try {
                stopScore = Double.parseDouble(scanner.nextLine());
            } catch (Throwable e) {
                System.out.println("Invalid input. Value must be number");
                return;
            }
            System.out.println("Running algorithm until reaching score " + stopScore +
                    " or completing all generations" + System.lineSeparator() + "whichever comes first");
            engine.executeEvolutionAlgorithmWithFitnessStop(numOfGenerations, stride, stopScore);
        } else {
            System.out.println("Running algorithm until completing all generations.");
            engine.executeEvolutionAlgorithm(numOfGenerations, stride);
        }
    }

    private void showBestSolution() {
        printShowSolutionMenu();

        int displayOption = 0;
        try {
            displayOption = Integer.parseInt(scanner.nextLine(), 10);
        } catch (Throwable e) {
            //swallow exception
        }

        TimeTableSolutionDTO bestSolution = engine.getBestSolution();


        System.out.println("***The best solution***");
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
                return;
        }

        printSolutionExtraDetails(bestSolution);
    }

    private void showProgressHistory() {
        List<GenerationProgressDTO> list = engine.getEvolutionProgress();
        System.out.println("Best score for first to last generations with the given stride: " + stride);

        if (list.size() > 0) {
            for (GenerationProgressDTO gp : list) {
                System.out.println(gp);
            }
        }

    }

    private void addListenersToEngineEvents() {
        engine.addListener("loaded", e -> {
            UI.this.fileLoaded = true;
            LoadedEvent loadedEvent = (LoadedEvent) e;
            System.out.println("File was successfully loaded from " + loadedEvent.getPath());
            System.out.println("***");
        });
        engine.addListener("error", e -> {
            ErrorEvent errorEvent = (ErrorEvent) e;
            StringBuilder sb = new StringBuilder();
            Throwable root = MyUtils.findThrowableRootCause(errorEvent.getError());
            Throwable currError = errorEvent.getError();

            while (!currError.equals(root)) {
                sb.append(currError.getMessage() + System.lineSeparator());
                currError = currError.getCause();
            }

            sb.append(root.getMessage());
            System.out.println(e.getMessage() + ". " + sb);

            if (errorEvent.getType() == ErrorType.LoadError) {
                if (UI.this.fileLoaded) {
                    System.out.println("Reverted to last file that was successfully loaded.");
                } else {
                    System.out.println("There is no file loaded to the system.");
                }
            }

            System.out.println("***");
        });
        engine.addListener("stride", e -> {
            OnStrideEvent strideEvent = (OnStrideEvent) e;
            UI.this.showStrideProgressDuringRun(strideEvent.getGenerationStrideScoreDTO());
        });
        engine.addListener("completed", e -> {
            UI.this.evolutionAlgorithmCompleted = true;
            System.out.println(e.getMessage());
            System.out.println("***");
        });
    }

    private MenuOptions getSelectedOption(String input) {
        MenuOptions result = MenuOptions.None;
        try {
            int intInput = Integer.parseInt(input, 10);
            result = MenuOptions.values()[intInput];
        } catch (Throwable e) {
            // swallow parsing exceptions
        }

        return result;
    }

    private boolean checkExtension(String path, String expectedExt) {
        if (!path.contains(".")) {
            return false;
        }

        String ext = path.substring(path.lastIndexOf("."));
        return expectedExt.equalsIgnoreCase(ext);
    }

    private boolean getNumOfGenerationsInput() {
        boolean result = false;
        System.out.println("Enter the number of generations (greater than 100): ");

        try {
            numOfGenerations = Integer.parseInt(scanner.nextLine(), 10);
        } catch (Throwable E) {
            System.out.println("Invalid number of generations entered, must be an integer greater than 100");
            return false;
        }

        if (numOfGenerations < 100) {
            numOfGenerations = 0;
            System.out.println("Invalid number of generations entered, must be an integer greater than 100");
        } else {
            result = true;
        }

        return result;
    }

    private boolean getGenerationStrideInput() {
        boolean result = false;
        System.out.println("Enter the generation stride you'd like to see while the algorithm runs (a positive integer)");

        try {
            stride = Integer.parseInt(scanner.nextLine(), 10);
        } catch (Throwable E) {
            System.out.println("Invalid stride value entered, the value must be a positive integer");
            return false;
        }

        if (stride <= 0) {
            stride = 0;
            System.out.println("Invalid stride value entered, the value must be a positive integer");
        } else {
            result = true;
        }

        return result;
    }

    private void showStrideProgressDuringRun(GenerationStrideScoreDTO generationStrideScoreDTO) {
        System.out.println("generation: " + generationStrideScoreDTO.getGenerationNum()
                + ", top score: " + generationStrideScoreDTO.getBestScore());
    }

    private void printSolutionExtraDetails(TimeTableSolutionDTO solution) {
        System.out.println("Score: " + solution.getTotalFitnessScore());
        System.out.printf("Hard rules average score: %.1f %n", solution.getHardRulesAvg());
        System.out.printf("Soft rules average score: %.1f %n", solution.getSoftRulesAvg());
        System.out.println("The rules applied and the fitness score evaluated for this solution:");
        Map<RuleDTO, Double> fitnessScorePerRule = solution.getFitnessScorePerRule();
        for (Map.Entry<RuleDTO, Double> entry : fitnessScorePerRule.entrySet()) {
            RuleDTO rule = entry.getKey();
            System.out.println(rule.getName() + "," + rule.getType() + ", " + entry.getValue());
        }

        System.out.println("***");
    }

    private void printRawQuintets(TimeTableSolutionDTO solution) {
        List<QuintetDTO> sortedQuintets = solution.getSolutionQuintets().stream()
                .sorted(QuintetDTO.getRawComparator())
                .collect(Collectors.toList());

        for (QuintetDTO q : sortedQuintets) {
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
            System.out.println(t.getId() + " " + t.getName());
            if (teacherSolutions.get(t).size() > 0) {
                printMatrix(teacherSolutions.get(t),
                        q -> String.format("%2s, %2s", q.getSchoolClass().getId(), q.getSubject().getId()));
            } else {
                System.out.println(t.getName() + " is not scheduled in the timetable.");
            }

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
            System.out.println(t.getId() + " " + t.getName());

            if (classSolutions.get(t).size() > 0) {
                printMatrix(classSolutions.get(t),
                        q -> String.format("%2s, %2s", q.getTeacher().getId(), q.getSubject().getId()));
            } else {
                System.out.println(t.getName() + " is not scheduled in the timetable.");
            }
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

    private void printEngineSetting(EngineSettingsDTO engineSettingsDTO) {
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

    private void printMainMenu() {
        /*String noFileLoaded = fileLoaded ? "" : " <Unavailable - no file loaded>";
        String algoIncomplete = evolutionAlgorithmCompleted ? "" : " <Unavailable - algorithm run was not completed>";*/

        System.out.println("To select from the menu, please enter the option number: ");
//                + System.lineSeparator() + "Please note, in order to select 2 or 3, you first need to select 1"
//                + System.lineSeparator() + "and in order to select 4 or 5, you first need to select 3");
        System.out.println(MenuOptions.LoadFile.ordinal() + ". Load xml file");
        System.out.println(MenuOptions.ShowDetails.ordinal() + ". Show timetable and algorithm settings");// + noFileLoaded);
        System.out.println(MenuOptions.RunAlgorithm.ordinal() + ". Run evolution algorithm");// + noFileLoaded);
        System.out.println(MenuOptions.ShowSolution.ordinal() + ". Show best solution");// + algoIncomplete);
        System.out.println(MenuOptions.ShowProgress.ordinal() + ". Show algorithm progress per stride");// + algoIncomplete);
        System.out.println(MenuOptions.Exit.ordinal() + ". Exit system");
    }

    private void printShowSolutionMenu() {
        System.out.println("Enter an option number for your preferred display of the best solution:");
        System.out.println("1. A sorted list of the solution quintets <D,H,C,T,S> (raw)");
        System.out.println("2. A separate D*H timetable for every teacher (showing <C,S>)");
        System.out.println("3. A separate D*H timetable for every class (showing <T,S>)");
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

    /**
     * Print a solution in the form of matrix, where the columns are days
     * and the rows are hours.
     * Print the report from different point of views according to the reportType parameter.
     *
     * @param solution - a list of Quintets to be printed
     *                 report type - teacher view, class view or full view
     */
    protected void printSolutionQuintets(List<QuintetDTO> solution, ReportType reportType) {
        int timeTableHours = engine.getSystemDetails().getTimeTable().getHours();
        int timeTableDays = engine.getSystemDetails().getTimeTable().getDays();
        // --- for testing ----
        //int timeTableHours = 6; //engine.getSystemDetails().getTimeTable().getHours();
        //int timeTableDays = 6; //engine.getSystemDetails().getTimeTable().getDays();
        ArrayList<Integer> testArray = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4, 5));

        // This array will be set according to the report type. Either teachers IDs, class IDs
        // or just {0} for a full solution printout.
        ArrayList<Integer> listToIterateOver = new ArrayList<Integer>();
        listToIterateOver.add(new Integer(0));

        List<QuintetDTO>[][] solutionAsMatrix;
        solutionAsMatrix = new List[timeTableHours][timeTableDays];   // The rows are hours and the columns are days
        // Each cell is a quintet list, because several
        // quintets may be assigned to one hour.
        // Preset the matrix to nulls.
        for (int i = 0; i < timeTableHours; i++) {

            for (int j = 0; j < timeTableDays; j++) {
                solutionAsMatrix[i][j] = null;
            }
        }

        // Insert each Quintet into its place in the matrix
        QuintetDTO quintet = null;
        int qhour, qday;
        Iterator<QuintetDTO> iter = solution.iterator();
        while (iter.hasNext()) {
            quintet = iter.next();
            if (quintet != null) {
                qhour = quintet.getHour();
                qday = quintet.getDay().getValue() - 1; // Subtract 1, because days start at 1.
                if (qhour >= timeTableHours || qday >= timeTableDays)
                    continue;   // Ignore quintets with hor or day out of scope
                if (solutionAsMatrix[qhour][qday] == null)
                    solutionAsMatrix[qhour][qday] = new ArrayList<QuintetDTO>();
                // Add the quintet to the hour and day's list
                solutionAsMatrix[qhour][qday].add(quintet);
            }
        }

        // Build the listToIterateOver according to report type (it was initially set for a full solution printout).
        String header = "--- Full TimeTable ---\n";
        if (reportType == ReportType.TEACHER_VIEW) {
            // -- for testing --- listToIterateOver = testArray; //new ArrayList<Integer>(engine.getSystemDetails().getTimeTable().getTeachers().keySet());
            listToIterateOver = new ArrayList<Integer>(engine.getSystemDetails().getTimeTable().getTeachers().keySet());
            Collections.sort(listToIterateOver);
            header = "\n--- Teacher View for: ";
        } else if (reportType == ReportType.CLASS_VIEW) {
            // -- for testing --- listToIterateOver = testArray;  //new ArrayList<Integer>(engine.getSystemDetails().getTimeTable().getSchoolClasses().keySet());
            listToIterateOver = new ArrayList<Integer>(engine.getSystemDetails().getTimeTable().getSchoolClasses().keySet());
            Collections.sort(listToIterateOver);
            header = "\n--- Class View for: ";
        }

        // Print a title and a header
        System.out.println("          solution's Time Table\n" +
                "          =====================\n");

        Formatter frmt = new Formatter();

        final String[] dayNames = {"Sun", "Mon", "Tue", "Wed", "Thr", "Fri"};
        String daysLine = "    ";
        for (int i = 0; i < timeTableDays; i++) {
            daysLine = daysLine + frmt.format("| %-11s ", dayNames[i]);
            frmt = new Formatter();
        }
        daysLine = daysLine + "|";
        String line;
        // Loop printing a time table for each element in the listToIterateOver.
        for (int loop = 0; loop < listToIterateOver.size(); loop++) {
            // Print a header
            System.out.println(header + listToIterateOver.get(loop) + "\n");

            System.out.println(daysLine);
            // Print Separation line - repeated hyphens.
            System.out.println(String.join("", Collections.nCopies(5 + 14 * timeTableDays, "-")));

            boolean moreToPrint = true;    // True, if ,more quintet in this cell
            int cellIterations = 0;
            List<QuintetDTO> oneCellList = null;
            QuintetDTO quintetToPrint = null;
            for (int i = 0; i < timeTableHours; i++) {
                moreToPrint = true;
                cellIterations = 0;     // Count of items for this cell
                while (moreToPrint) {
                    line = "";
                    moreToPrint = false;
                    // Add the hour
                    frmt = new Formatter();
                    line = line + frmt.format(" %2d ", i);

                    for (int j = 0; j < timeTableDays; j++) {
                        oneCellList = solutionAsMatrix[i][j];
                        // Check whether this cell is to be printed.
                        if (oneCellList == null || oneCellList.size() <= cellIterations ||
                                (reportType == ReportType.TEACHER_VIEW &&       // If not currently printed teacher
                                        oneCellList.get(cellIterations).getTeacher().getId() != listToIterateOver.get(loop)) ||
                                (reportType == ReportType.CLASS_VIEW &&     // If not currently printed class
                                        oneCellList.get(cellIterations).getSchoolClass().getId() != listToIterateOver.get(loop))) {
                            // Print an empty cell
                            frmt = new Formatter();
                            // Print an empty cell
                            line = line + frmt.format("|      %-6s ", "-");
                        } else {
                            quintetToPrint = oneCellList.get(cellIterations);
                            frmt = new Formatter();
                            line = line + frmt.format("| %3.3s,%3.3s,%3.3s ",
                                    quintetToPrint.getTeacher().getId(), quintetToPrint.getSubject().getId(),
                                    quintetToPrint.getSchoolClass().getId());
                            if (oneCellList.size() > cellIterations + 1)
                                moreToPrint = true;     // Need to print another line,
                            // because there are more quintets for this pair of (hour, day).
                        }
                    }
                    // Add the last cell separator and end of line and print the entire line
                    System.out.println(line + "|");
                    if (moreToPrint)
                        cellIterations++;   // Advance to next row
                    else
                        // Print Separation line - repeated hyphens.
                        System.out.println(String.join("", Collections.nCopies(5 + 14 * timeTableDays, "-")));
                }
            }
        }
    }
}
