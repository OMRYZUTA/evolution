package il.ac.mta.zuli.evolution.engine.evolutionengine.crossover;

import il.ac.mta.zuli.evolution.engine.exceptions.ValidationException;
import il.ac.mta.zuli.evolution.engine.timetable.TimeTable;
import il.ac.mta.zuli.evolution.engine.xmlparser.generated.ex2.ETTCrossover;

public class CrossoverFactory {
    public static Crossover createCrossover(ETTCrossover ettCrossover, TimeTable timeTable) {
        switch (ettCrossover.getName().toLowerCase()) {
            case "daytimeoriented":
                return new DayTimeOriented(ettCrossover.getCuttingPoints(), timeTable);
            default:
                throw new ValidationException("Invalid crossover type for ex. 1");
        }
    }
}
