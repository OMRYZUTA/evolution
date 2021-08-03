package il.ac.mta.zuli.evolution.engine.evolutionengine.selection;

import il.ac.mta.zuli.evolution.engine.TimeTableSolution;
import il.ac.mta.zuli.evolution.engine.xmlparser.generated.ETTSelection;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Truncation implements Selection<TimeTableSolution> {
    private int topPercent;

    public Truncation(ETTSelection ettSelection) {
        String configuration = ettSelection.getConfiguration();
        int index = configuration.indexOf('=');
        int num = Integer.parseInt(configuration.substring(index + 1));
        setTopPercent(num);
    }

    public int getTopPercent() {
        return topPercent;
    }

    private void setTopPercent(int topPercent) {
        if (topPercent >= 1 && topPercent <= 100) {
            this.topPercent = topPercent;
        } else {
            throw new RuntimeException("invalid top percent value");
        }
    }

    @Override
    public String toString() {
        return "Selection: " + this.getClass().getSimpleName() +
                "topPercent=" + topPercent;
    }

    @Override
    public List<TimeTableSolution> select(List<TimeTableSolution> solutions) {
        Collections.sort(solutions); //sorting by fitnessScore
        Collections.reverse(solutions); //in descending order

        int topFitnessSolutions = (topPercent * solutions.size()) / 100;

        return solutions.stream().limit(topFitnessSolutions).collect(Collectors.toList());
    }

    public String getConfiguration() {
        return String.format("TopPercent = %d", topPercent);
    }

}
