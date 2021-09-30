package il.ac.mta.zuli.evolution.engine.evolutionengine.crossover;

import il.ac.mta.zuli.evolution.Constants;
import il.ac.mta.zuli.evolution.engine.evolutionengine.Solution;
import il.ac.mta.zuli.evolution.engine.exceptions.ValidationException;
import il.ac.mta.zuli.evolution.engine.timetable.TimeTable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class CrossoverFactory {

    public static <T extends Solution> Crossover<T> createCrossoverFromMap(
            Map<String, Object> crossoverMap,
            TimeTable timeTable) {

        Map<String, Supplier<Crossover<T>>> crossoverBuilder = new HashMap<>();

        crossoverBuilder.put(Constants.DAY_TIME_ORIENTED, () -> {
            int cuttingPoints;

            try {
                cuttingPoints = Integer.parseInt((String) crossoverMap.get(Constants.CUTTING_POINTS));
            } catch (Throwable e) {
                throw new ValidationException("Invalid Crossover parameter." + e.getMessage());
            }

            return new DayTimeOriented<T>(cuttingPoints, timeTable);
        });

        crossoverBuilder.put(Constants.ASPECT_ORIENTED, () -> {
            int cuttingPoints;

            try {
                cuttingPoints = Integer.parseInt((String) crossoverMap.get(Constants.CUTTING_POINTS));
            } catch (Throwable e) {
                throw new ValidationException("Invalid Crossover parameter." + e.getMessage());
            }

            Orientation orientation = parseOrientationFromConfiguration(crossoverMap);

            return new AspectOriented<T>(cuttingPoints, orientation, timeTable);
        });

        try {
            String crossoverType = (String) crossoverMap.get(Constants.NAME);
            return crossoverBuilder.get(crossoverType).get();
        } catch (Throwable e) {
            throw new ValidationException("Invalid Crossover type. " + e.getMessage());
        }
    }

    private static Orientation parseOrientationFromConfiguration(Map<String, Object> crossoverMap) {
        Orientation orientation;

        try {
            String OrientationStr = (String) crossoverMap.get(Constants.ORIENTATION);
            orientation = Orientation.valueOf(OrientationStr);
        } catch (Throwable e) {
            throw new ValidationException("invalid orientation, must be a either TEACHER or CLASS");
        }

        return orientation;
    }
}
