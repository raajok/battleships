package fi.utu.tech.gui.javafx;

public enum Orientation {
    // enum fields
    RIGHT(270),
    DOWN(0),
    LEFT(90),
    UP(180);
	
	// Internal state
	private int orientation;
	
	private Orientation(final int o) {
		this.orientation = o;
	}
	
	public int getDegrees() {
		return orientation;
	}
	
}