package il.ac.mta.zuli.evolution.ui.solution;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.RowConstraints;

import java.time.DayOfWeek;

public class DisplaySolutionUtils {
    public static GridPane createGrid(int days, int hours) {
        GridPane gridPane = new GridPane();
        gridPane.setGridLinesVisible(true);
        gridPane.prefHeight(Region.USE_COMPUTED_SIZE);
        gridPane.prefWidth(Region.USE_COMPUTED_SIZE);

        // +1 for header-row and header-columns
        int rowCount = hours + 1;
        int columnCount = days + 1;

        RowConstraints rc = new RowConstraints();
        rc.setPercentHeight(100d / rowCount);

        for (int i = 0; i < rowCount; i++) {
            if (i > 0) {
                Label label = new Label(String.valueOf(i - 1));
                gridPane.add(label, 0, i);
                GridPane.setMargin(label, new Insets(5));
            }

            gridPane.getRowConstraints().add(rc);
        }

        ColumnConstraints cc = new ColumnConstraints();
        cc.setPercentWidth(100d / columnCount);
        cc.setMinWidth(Region.USE_COMPUTED_SIZE);
        for (int i = 0; i < columnCount; i++) {
            if (i > 0) {
                Label label = new Label(DayOfWeek.of(i).toString());
                gridPane.add(label, i, 0);
                GridPane.setMargin(label, new Insets(5));
            }

            gridPane.getColumnConstraints().add(cc);
        }

        return gridPane;
    }
}
