package fi.utu.tech.gui.javafx;

import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class OtherWindow extends BorderPane {

    public OtherWindow() {
        Circle c = new Circle(200, Color.NAVY);
        setCenter(c);
        setLeft(new ImageView(ResourceLoader.image("hmm.png")));
    }
}
