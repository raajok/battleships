package fi.utu.tech.gui.javafx;

abstract class Ship {
	private final int size;
	private Orientation orientation; // 0 = right, 1 = down, 2 = left, 3 = up
	private XY location;
	
	public Ship(int size, XY location, Orientation orientation) {
		this.size = size;
		this.orientation = orientation;
		this.location = location;
	}
	
	public Orientation getOrientation() {
		return orientation;
	}
	
	public void setOrientation(Orientation orientation) {
		this.orientation = orientation;
	}
	
	public int getSize() {
		return size;
	}
	
	abstract ShipType getType();
	
	public XY getLocation() {
		return location;
	}
	
	public void setLocation(XY location) {
		this.location = location;
	}

}
