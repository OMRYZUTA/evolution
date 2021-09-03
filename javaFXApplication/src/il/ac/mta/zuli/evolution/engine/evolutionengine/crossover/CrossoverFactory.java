package il.ac.mta.zuli.evolution.engine.evolutionengine.crossover;

import il.ac.mta.zuli.evolution.engine.TimeTableSolution;
import il.ac.mta.zuli.evolution.engine.exceptions.ValidationException;
import il.ac.mta.zuli.evolution.engine.timetable.TimeTable;
import il.ac.mta.zuli.evolution.engine.xmlparser.generated.ex2.ETTCrossover;

public class CrossoverFactory {
    public static CrossoverInterface createCrossover(ETTCrossover ettCrossover, TimeTable timeTable) {

        switch (ettCrossover.getName().toLowerCase()) {
            case "daytimeoriented":
                return new DayTimeOriented<TimeTableSolution>(ettCrossover.getCuttingPoints(), timeTable);
            case "aspectoriented":
                Orientation orientation = parseOrientationFromConfiguration(ettCrossover.getConfiguration());
                //parseOrientationFromConfiguration method checked for valid orientation
                return new AspectOriented<TimeTableSolution>(ettCrossover.getCuttingPoints(), orientation, timeTable);
            default:
                throw new ValidationException("Invalid crossover type for ex. 2");
        }
    }

    public static Crossover createCrossoverFromInput(
            String crossoverType,
            int numOfCuttingPoints,
            Orientation orientation,
            TimeTable timeTable) {
        switch (crossoverType.toLowerCase()) {
            case "daytimeoriented":
                return new DayTimeOriented<TimeTableSolution>(numOfCuttingPoints, timeTable);
            case "aspectoriented":
                return new AspectOriented<TimeTableSolution>(numOfCuttingPoints, orientation, timeTable);
            default:
                throw new ValidationException("Invalid crossover type for ex. 2");
        }
    }

    private static Orientation parseOrientationFromConfiguration(String configuration) {
//        <ETT-Crossover name="AspectOriented" cutting-points="1" configuration="Orientation=CLASS"/>
        int orientationIndex = configuration.indexOf("Orientation=") + "Orientation=".length();
        String OrientationStr = configuration.substring(orientationIndex);
        Orientation orientation;

        try {
            orientation = Orientation.valueOf(OrientationStr);
        } catch (Throwable e) {
            throw new ValidationException("invalid orientation, must be a either TEACHER or CLASS");
        }
        return orientation;
    }
}
