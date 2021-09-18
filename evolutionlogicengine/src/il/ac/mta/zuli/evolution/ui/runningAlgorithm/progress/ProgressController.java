package il.ac.mta.zuli.evolution.ui.runningAlgorithm.progress;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

public class ProgressController {
    //region FXML
    @FXML
    private Label titleLabel;
    @FXML
    private Label valueLabel;
    @FXML
    private Label slashLabel;
    @FXML
    private Label maxLabel;
    @FXML
    private ProgressBar progressBar;
    //endregion

    public StringProperty valueTextProperty() {
        return valueLabel.textProperty();
    }

    public DoubleProperty progressProperty() {
        return progressBar.progressProperty();
    }

    public void setTitle(String title) {
        titleLabel.setText(title);
    }

    public void setMax(String max) {
        maxLabel.setText(max);
    }

    @FXML
    private void initialize() {
        slashLabel.visibleProperty().bind(maxLabel.textProperty().isNotEmpty());
        maxLabel.visibleProperty().bind(maxLabel.textProperty().isNotEmpty());
        progressBar.visibleProperty().bind(maxLabel.textProperty().isNotEmpty());
    }
}
