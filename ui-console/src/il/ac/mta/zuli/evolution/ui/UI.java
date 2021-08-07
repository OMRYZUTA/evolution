package il.ac.mta.zuli.evolution.ui;

import il.ac.mta.zuli.evolution.dto.*;
import il.ac.mta.zuli.evolution.engine.Engine;
import il.ac.mta.zuli.evolution.engine.TimeTableEngine;
import il.ac.mta.zuli.evolution.engine.events.OnStrideEvent;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

public class UI implements ActionListener {
    Engine engine;

    public void printInitialMenuOptions() {
        System.out.println("Please enter the option number to select from the menu:" + System.lineSeparator() +
                "1. Load xml file" + System.lineSeparator() +
                "2.");
        //TODO continue
    }

    public void operateMenu() {
        try {
            engine = new TimeTableEngine();
            engine.addHandler(this);
            engine.loadXML("engine/src/resources/EX1-small.xml");

           // showSystemDetails();

            //TODO get parameters for evolution algorithm (and validate in engine)
            engine.executeEvolutionAlgorithm(1000, 20);
            TimeTableSolutionDTO solution = engine.getBestSolutionRaw();
//            printSolution(solution.getSolutionQuintets());
        } catch (Exception e) {
            System.out.println(e);
            System.out.println(e.getMessage() + e.getStackTrace());
            //TODO handleException
        }
    }

    private void showSystemDetails() {
        DescriptorDTO descriptorDTO = engine.getSystemDetails();
        TimeTableDTO timeTableDTO = descriptorDTO.getTimeTable();
        EngineSettingsDTO engineSettingsDTO = descriptorDTO.getEngineSettings();

        printTimeTable(timeTableDTO);
        printEngineSetting(engineSettingsDTO);
        System.out.println("*********************************");
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

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getID()) {
            case 1:
                System.out.println(e.getActionCommand());
                break;
            case 3:
                GenerationStrideScoreDTO generationStrideScoreDTO = ((OnStrideEvent) e).getGenerationStrideScoreDTO();
                showStrideProgress(generationStrideScoreDTO);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + e.getID());
        }
    }

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
    protected void printSolution(List<QuintetDTO> solution) {
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
