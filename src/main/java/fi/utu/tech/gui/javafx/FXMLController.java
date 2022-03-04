package fi.utu.tech.gui.javafx;

import java.util.function.Supplier;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class FXMLController {

    private final IntegerProperty clicks = new SimpleIntegerProperty(0);

    private Supplier<Parent> supplier;

    @FXML private Label clicksLabel;

    protected void updateClicks() {
        if (!clicksLabel.textProperty().isBound())
            clicksLabel.textProperty().bind(clicks.asString().concat(" clicks."));

        clicks.setValue(clicks.getValue() + 1);
    }

    protected void setLabel(String text) {
        if (!clicksLabel.textProperty().isBound()) clicksLabel.setText(text);
    }

    @FXML
    protected void handleDialogButton(ActionEvent event) {
        updateClicks();
        Dialogs.warning("Example dialog", "Content header", "Content");
    }

    @FXML
    protected void handleExitButton(ActionEvent event) {
        System.out.println("Closing app.");
        Platform.exit();
    }

    @FXML
    protected void handleWindowButton(ActionEvent event) {
        updateClicks();
        Scene other = new Scene(supplier.get());
        Stage otherStage = new Stage();
        otherStage.setScene(other);
        otherStage.show();
    }

    protected void setWindowFactory(Supplier<Parent> supplier) {
        this.supplier = supplier;
    }

    public void initialize() {
        System.out.println("2-A");
    }
}
