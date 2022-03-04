package fi.utu.tech.gui.javafx;

import java.io.File;
import java.util.Optional;
import java.util.function.Consumer;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.FileChooser;

public class Dialogs {

    /**
     * Warning dialog.
     *
     * @param title
     * @param header
     * @param content
     */
    public static void warning(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Confirmation dialog.
     *
     * @param title
     * @param header
     * @param content
     * @return
     */
    public static boolean confirmation(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        Optional<ButtonType> choice = alert.showAndWait();

        return choice.get() == ButtonType.OK;
    }

    /**
     * Save/load file dialog for a single file. Propagates the file name (if not null) to the
     * handler.
     *
     * @param title
     * @param type
     * @param ext
     * @param saveDialog
     * @param handler
     */
    public static void singleFileDialog(
            String title, String type, String ext, boolean saveDialog, Consumer<File> handler) {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters()
                .add(new FileChooser.ExtensionFilter(type + " (." + ext + ")", "*." + ext));
        chooser.setTitle(title);
        File selected;
        if (saveDialog) {
            selected = chooser.showSaveDialog(null);
            if (selected == null) return;
            String file = selected.getAbsolutePath();
            if (!file.endsWith("." + ext)) file += "." + ext;
            selected = new File(file);
        } else {
            selected = chooser.showOpenDialog(null);
        }
        if (selected == null) return;
        handler.accept(selected);
    }
}
