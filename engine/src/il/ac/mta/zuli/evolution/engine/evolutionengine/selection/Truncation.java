package il.ac.mta.zuli.evolution.engine.evolutionengine.selection;

import il.ac.mta.zuli.evolution.engine.evolutionengine.Solution;
import il.ac.mta.zuli.evolution.engine.xmlparser.generated.ETTSelection;

import java.util.Collection;

public class Truncation implements Selection {
    private int topPercent;

    public Truncation(ETTSelection ettSelection) throws Exception{
        String configuration = ettSelection.getConfiguration();
        int index = configuration.indexOf('=');
        int num = Integer. parseInt(configuration.substring(index+1));
        setTopPercent(num);
    }

    public int getTopPercent() {
        return topPercent;
    }

    private void setTopPercent(int topPercent) throws Exception {
        if (topPercent >= 1 && topPercent <= 100) {
            this.topPercent = topPercent;
        } else {
            throw new Exception("invalid top percent value");
        }
    }

    //TODO implement selection()
    @Override
    public Collection<Solution> select(Collection<Solution> solutions) {
        return null;
    }


}
