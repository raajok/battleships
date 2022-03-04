package fi.utu.tech.gui.javafx;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public class ResourceLoader<N extends Parent, C> {

    protected final N root;
    protected final C controller;

    protected Class<?> resourceRoot() {
        return getClass();
    }

    public ResourceLoader(String contentPath) {
        N root_ = null;
        C controller_ = null;
        try {
            // determines where to look for the resources (the root path)
            Class<?> resourceRootClass = resourceRoot();

            FXMLLoader loader = new FXMLLoader(resourceRootClass.getResource(contentPath));
            root_ = loader.load();
            controller_ = loader.getController();
            System.out.println("DEBUG: " + contentPath + " loaded.");
        } catch (Exception e) {
            e.printStackTrace();
            Dialogs.warning(
                    "Internal error",
                    "Could not open FXML file: " + contentPath,
                    "Reason: " + e.getMessage());
            System.exit(1);
        }
        root = root_;
        controller = controller_;
    }

    // finds images both outside and inside jars
    public static String image(String fileName) {
        return ResourceLoader.class.getResource(fileName).toExternalForm();
    }

    // finds stylesheets both outside and inside jars
    public static String stylesheet(String fileName) {
        return ResourceLoader.class.getResource(fileName).toExternalForm();
    }
}
