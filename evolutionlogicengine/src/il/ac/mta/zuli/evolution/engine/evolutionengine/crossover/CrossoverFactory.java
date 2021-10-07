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

        int cuttingPoints;
        try {
            cuttingPoints = (int) Math.ceil((double) crossoverMap.get(Constants.CUTTING_POINTS));
        } catch (Throwable e) {
            throw new ValidationException("Invalid Crossover parameter." + e.getMessage());
        }

        Map<String, Supplier<Crossover<T>>> crossoverBuilder = new HashMap<>();

        crossoverBuilder.put(Constants.DAY_TIME_ORIENTED, () -> new DayTimeOriented<T>(cuttingPoints, timeTable));

        crossoverBuilder.put(Constants.ASPECT_ORIENTED, () -> {
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
