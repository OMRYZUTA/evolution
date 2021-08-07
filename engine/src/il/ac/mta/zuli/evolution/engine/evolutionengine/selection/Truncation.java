package il.ac.mta.zuli.evolution.engine.evolutionengine.selection;

import il.ac.mta.zuli.evolution.engine.evolutionengine.Solution;
import il.ac.mta.zuli.evolution.engine.exceptions.ValidationException;
import il.ac.mta.zuli.evolution.engine.xmlparser.generated.ETTSelection;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Truncation<S extends Solution> implements Selection<S> {
    private int topPercent;

    public Truncation(ETTSelection ettSelection) {
        parseConfiguration(ettSelection);
    }

    private void parseConfiguration(ETTSelection ettSelection) {
        String configuration = ettSelection.getConfiguration();
        if(configuration.length()==0){
            throw new ValidationException("empty configuartion ");
        }
        int index = configuration.indexOf('=');
        if(index ==-1){
            throw new ValidationException("missing \'=\' ");
        }
        int num = Integer.parseInt(configuration.substring(index + 1));
        setTopPercent(num);
    }

    @Override
    public List<S> select(List<S> solutions) {
        Collections.sort(solutions); //sorting by fitnessScore
        Collections.reverse(solutions); //in descending order

        int topFitnessSolutions = (int) Math.ceil(((double) topPercent * solutions.size()) / 100);

        return solutions.stream().limit(topFitnessSolutions).collect(Collectors.toList());
    }


    public String getConfiguration() {
        return String.format("TopPercent = %d", topPercent);
    }

    @Override
    public String toString() {
        return "Selection: " + this.getClass().getSimpleName() +
                "topPercent=" + topPercent;
    }

    private void setTopPercent(int topPercent) {
        if (topPercent >= 1 && topPercent <= 100) {
            this.topPercent = topPercent;
        } else {
            throw new RuntimeException("invalid top percent value :"+topPercent+"need to be between 1 - 100");
        }
    }
}
