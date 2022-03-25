package fi.utu.tech.gui.javafx;

import javafx.scene.Group;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;

public class Cross extends Group {
	private Line line1;
	private Line line2;
	
	public Cross(Double width, Double height, Paint color) {
		// Line 1 from top left to bottom right
		line1 = new Line(0,0,width,height);
		line1.setStrokeWidth(2);
		line1.setStroke(color);
		
		// Line 2 from bottom left to Top right
		line2 = new Line(0,height,width,0);
		line2.setStrokeWidth(2);
		line2.setStroke(color);
		
		this.getChildren().addAll(line1, line2);
	}
}
