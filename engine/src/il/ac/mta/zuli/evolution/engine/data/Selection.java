package il.ac.mta.zuli.evolution.engine.data;

import il.ac.mta.zuli.evolution.engine.data.generated.ETTSelection;
import org.jetbrains.annotations.NotNull;

public class Selection {
    private SelectionType type;
    private Configuration configuration;

    public Selection(@NotNull ETTSelection ettSelection) {
        setType(ettSelection.getType());
    }

    public SelectionType getType() {
        return type;
    }

    private void setType(String type) {
        if (type.equalsIgnoreCase("truncation")) {
            this.type = SelectionType.TRUNCATION;
        } else if (type.equalsIgnoreCase("•roulettewheel")) {
            this.type = SelectionType.ROULETTEWHEEL;
        } else {
            //TODO throw exception
            System.out.println("selection type not applicable");
        }
    }

    @Override
    public String toString() {
        return "Selection{" +
                "type=" + type +
                '}';
    }
}
