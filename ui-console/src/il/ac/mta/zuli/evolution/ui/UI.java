package il.ac.mta.zuli.evolution.ui;

import il.ac.mta.zuli.evolution.dto.*;
import il.ac.mta.zuli.evolution.engine.Engine;
import il.ac.mta.zuli.evolution.engine.TimeTableEngine;
import il.ac.mta.zuli.evolution.engine.events.ErrorEvent;
import il.ac.mta.zuli.evolution.engine.events.LoadedEvent;
import il.ac.mta.zuli.evolution.engine.events.OnStrideEvent;

import java.util.*;
import java.util.stream.Collectors;

public class UI {
    private final Engine engine;
    private final Scanner scanner;
    private boolean fileLoaded;
    private boolean evolutionAlgorithmCompleted;

    public UI() {
        engine = new TimeTableEngine();
        scanner = new Scanner(System.in);
        fileLoaded = false;
        evolutionAlgorithmCompleted = false;

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
        //TODO get parameters for evolution algorithm (and validate in engine)
        engine.executeEvolutionAlgorithm(100, 50);
    }

    private void showSolution() {
        printShowSolutionMenu();

        int displayOption = 0;
        try {
            displayOption = Integer.parseInt(scanner.nextLine(), 10);
        } catch (Exception e) {
            //swallow exception
        }

        TimeTableSolutionDTO bestSolution = null;

        switch (displayOption) {
            case 1:
                bestSolution = engine.getBestSolutionRaw();
                printRawQuintets(bestSolution.getSolutionQuintets());
                System.out.println("***");
                break;
            case 2:
                bestSolution = engine.getBestSolutionTeacherOriented();
                printTeacherDHMatrix(bestSolution);
                break;
            case 3:
                bestSolution = engine.getBestSolutionTeacherOriented();
                printClassDHMatrix(bestSolution);
                break;
            case 0:
            default:
                System.out.println("Invalid option entered");
        }


    }

    private void printRawQuintets(List<QuintetDTO> quintets) {
        for (QuintetDTO q : quintets) {
            System.out.printf("<%d, %d, %d, %d, %d>%n",
                    q.getDay().getValue(),
                    q.getHour(),
                    q.getSchoolClass().getId(),
                    q.getTeacher().getId(),
                    q.getSubject().getId());
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

    private void printTeacherDHMatrix(TimeTableSolutionDTO solution) {
        Map<TeacherDTO, List<QuintetDTO>> teacherSolutions = solution.getSolutionQuintets().stream()
                .collect(Collectors.groupingBy(QuintetDTO::getTeacher));

        List<TeacherDTO> sortedTeachers = teacherSolutions.keySet().stream()
                .sorted(Comparator.comparingInt(TeacherDTO::getId))
                .collect(Collectors.toList());

        for (TeacherDTO t : sortedTeachers) {
            System.out.println(t.getName());
            for (QuintetDTO q : teacherSolutions.get(t)) {
                System.out.println(q);
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
            System.out.println(t.getName());
            for (QuintetDTO q : classSolutions.get(t)) {
                System.out.println(q);
            }
            System.out.println("***");
        }
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

    private void showStrideProgress(GenerationStrideScoreDTO generationStrideScoreDTO) {
        System.out.println("generation: " + generationStrideScoreDTO.getGenerationNum()
                + " best score: " + generationStrideScoreDTO.getBestScore());
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

    /**
     * Print a solution in the form of matrix, where the columns are days from Mon to Fri
     * and the rows are hours from 8 to 14.
     *
     * @param solution a list of Quintets to be printed
     */
    protected void printSolutionQuintets(List<QuintetDTO> solution) {
        int timeTableHours = engine.getSystemDetails().getTimeTable().getHours();
        int timeTableDays = engine.getSystemDetails().getTimeTable().getDays();

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

        // Print a title and a header
        System.out.println("          solution's Time Table\n" +
                "          =====================\n");

        Formatter frmt = new Formatter();

        final String[] dayNames = {"Sun", "Mon", "Tue", "Wed", "Thr", "Fri"};
        String line = "     ";
        for (int i = 0; i < timeTableDays; i++) {
            line = line + frmt.format("|    %-17s ", dayNames[i]);
            frmt = new Formatter();
        }

        System.out.println(line + "|\n");

        // Print Separation line - 132 hyphens.
        System.out.println(String.join("", Collections.nCopies(144, "-")));

        // Start printing the cells in the matrix spaced evenly.
        boolean moreToPrint = true;    // True, if ,more quintet in this cell
        List<QuintetDTO> oneCellList = null;
        QuintetDTO quintetToPrint = null;
        for (int i = 0; i < timeTableHours; i++) {
            moreToPrint = true;
            while (moreToPrint) {
                line = "";
                moreToPrint = false;
                // Add the hour
                frmt = new Formatter();
                line = line + frmt.format(" %3d ", i);

                for (int j = 0; j < timeTableDays; j++) {
                    oneCellList = solutionAsMatrix[i][j];
                    if (oneCellList == null || oneCellList.isEmpty()) {
                        frmt = new Formatter();
                        // Print an empty cell
                        line = line + frmt.format("|      %-15s ", "-");
                    } else {
                        quintetToPrint = oneCellList.get(0);
                        frmt = new Formatter();
                        line = line + frmt.format("| %6.6s,%6.6s,%6.6s ",
                                quintetToPrint.getTeacher().getName(), quintetToPrint.getSubject().getName(),
                                quintetToPrint.getSchoolClass().getName());
                        // Remove the printed quintet
                        oneCellList.remove(quintetToPrint);
                        if (!oneCellList.isEmpty())
                            moreToPrint = true;     // Need to print another line,
                        // because there are more quintet for this pair of (hour, day).
                    }
                }
                // Add the last cell separator and end of line and print the entire line
                System.out.println(line + "|\n");
                if (!moreToPrint)
                    // Print Separation line - 130 hyphens.
                    System.out.println(String.join("", Collections.nCopies(144, "-")));
            }
        }
    }
}
