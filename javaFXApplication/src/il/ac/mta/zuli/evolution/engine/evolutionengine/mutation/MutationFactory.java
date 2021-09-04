package il.ac.mta.zuli.evolution.engine.evolutionengine.mutation;

import il.ac.mta.zuli.evolution.engine.exceptions.ValidationException;
import il.ac.mta.zuli.evolution.engine.timetable.TimeTable;
import il.ac.mta.zuli.evolution.engine.xmlparser.generated.ex2.ETTMutation;
import org.jetbrains.annotations.NotNull;

public class MutationFactory {
    public static Mutation createMutation(ETTMutation ettMutation, TimeTable timeTable) {
        double probability = ettMutation.getProbability();
        switch (ettMutation.getName().toLowerCase()) {
            case "sizer":
                int totalTuples = extractTotalTuplesFromString(ettMutation);
                return createSizerMutation(timeTable, probability, totalTuples);
            case "flipping":
                int maxTuples = extractMaxTuplesFromString(ettMutation);
                ComponentName component = extractComponentNameFromString(ettMutation);
                return creatFlippingMutation(timeTable, probability, maxTuples, component);
            default:
                throw new ValidationException(ettMutation.getName() + " is invalid mutation name ");
        }
    }

    public static Mutation createMutationFromInput(
            String mutationType,
            TimeTable timeTable,
            double probability,
            int tuples,
            ComponentName component) {
        switch (mutationType) {
            case "sizer":
                return createSizerMutation(timeTable, probability, tuples);
            case "flipping":
                return creatFlippingMutation(timeTable, probability, tuples, component);
            default:
                throw new ValidationException(mutationType + " is invalid mutation name ");
        }
    }

    @NotNull
    private static Mutation createSizerMutation(TimeTable timeTable, double probability, int totalTuples) {
//        <ETT-Mutation name="Sizer" probability="0.3" configuration="TotalTupples=7,Component=D"/>
        if (totalTuples > timeTable.getHours() * timeTable.getDays()) {
            throw new ValidationException("positive-total-tuples must be < (days * hours), invalid value: " + totalTuples);
        }

        if (totalTuples < (-1) * timeTable.getHours() * timeTable.getDays()) {
            throw new ValidationException("negative-total-tuples must be > -(days * hours), invalid value: " + totalTuples);
        }

        return new Sizer(probability, totalTuples, timeTable);
    }

    @NotNull
    private static Mutation creatFlippingMutation(TimeTable timeTable, double probability, int maxTuples, ComponentName component) {
        if (maxTuples < 0) {
            throw new ValidationException("max tuples must be >=0, invalid value: " + maxTuples);
        }

        return new Flipping(probability, maxTuples, component, timeTable);
    }

    @NotNull
    private static ComponentName extractComponentNameFromString(ETTMutation ettMutation) {
        int componentIndex = (ettMutation.getConfiguration()).indexOf("Component=") + "Component=".length();
        String componentStr = (ettMutation.getConfiguration()).substring(componentIndex, componentIndex + 1);

        return ComponentName.valueOf(componentStr.toUpperCase());
    }

    private static int extractMaxTuplesFromString(ETTMutation ettMutation) {
        //this is how it's spelled in the xml: MaxTupples=3
        int maxTuplesIndex = (ettMutation.getConfiguration()).indexOf("MaxTupples=") + "MaxTupples=".length();
        int commaIndex = ettMutation.getConfiguration().indexOf(",");
        String maxTuplesStr = (ettMutation.getConfiguration()).substring(maxTuplesIndex, commaIndex);

        return Integer.parseInt(maxTuplesStr, 10);
    }

    private static int extractTotalTuplesFromString(ETTMutation ettMutation) {
        //<ETT-Mutation name="Sizer" probability="0.3" configuration="TotalTupples=7"/>
        int totalTuplesIndex = (ettMutation.getConfiguration()).indexOf("TotalTupples=") + "TotalTupples=".length();
        String totalTuplesStr = (ettMutation.getConfiguration()).substring(totalTuplesIndex);

        return Integer.parseInt(totalTuplesStr, 10);
    }
}

