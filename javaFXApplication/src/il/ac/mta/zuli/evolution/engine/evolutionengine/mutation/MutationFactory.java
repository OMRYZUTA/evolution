package il.ac.mta.zuli.evolution.engine.evolutionengine.mutation;

import il.ac.mta.zuli.evolution.engine.exceptions.ValidationException;
import il.ac.mta.zuli.evolution.engine.timetable.TimeTable;
import il.ac.mta.zuli.evolution.engine.xmlparser.generated.ex2.ETTMutation;
import org.jetbrains.annotations.NotNull;

public class MutationFactory {
    public static Mutation createMutation(ETTMutation ettMutation, TimeTable timeTable) {
        Mutation mutation;
        switch (ettMutation.getName().toLowerCase()) {
            case "sizer":
                mutation = createSizerMutation(ettMutation, timeTable);
                break;
            case "flipping":
                mutation = creatFlippingMutation(ettMutation, timeTable);
                break;

            default:
                throw new ValidationException(ettMutation.getName() + " is invalid mutation name ");
        }

        return mutation;
    }

    @NotNull
    private static Mutation createSizerMutation(ETTMutation ettMutation, TimeTable timeTable) {
//        <ETT-Mutation name="Sizer" probability="0.3" configuration="TotalTupples=7,Component=D"/>
        int totalTuples = extractTotalTuplesFromString(ettMutation);

        if (totalTuples > timeTable.getHours() * timeTable.getDays()) {
            throw new ValidationException("positive-total-tuples must be < (days * hours), invalid value: " + totalTuples);
        }

        if (totalTuples < (-1) * timeTable.getHours() * timeTable.getDays()) {
            throw new ValidationException("negative-total-tuples must be > -(days * hours), invalid value: " + totalTuples);
        }

        return new Sizer(ettMutation.getProbability(), totalTuples, timeTable);
    }

    @NotNull
    private static Mutation creatFlippingMutation(ETTMutation ettMutation, TimeTable timeTable) {
        Mutation mutation;
        int maxTuples = extractMaxTuplesFromString(ettMutation);

        if (maxTuples < 0) {
            throw new ValidationException("max tuples must be >=0, invalid value: " + maxTuples);
        }

        ComponentName component = extractComponentNameFromString(ettMutation);
        mutation = new Flipping(ettMutation.getProbability(), maxTuples, component, timeTable);

        return mutation;
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

        return Integer.parseInt(maxTuplesStr);
    }

    private static int extractTotalTuplesFromString(ETTMutation ettMutation) {
        //<ETT-Mutation name="Sizer" probability="0.3" configuration="TotalTupples=7"/>
        int totalTuplesIndex = (ettMutation.getConfiguration()).indexOf("TotalTupples=") + "TotalTupples=".length();
        String totalTuplesStr = (ettMutation.getConfiguration()).substring(totalTuplesIndex);

        return Integer.parseInt(totalTuplesStr);
    }
}

