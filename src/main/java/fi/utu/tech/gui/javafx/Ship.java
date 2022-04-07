package fi.utu.tech.gui.javafx;

/**
 * The java class Ship is an abstract class that contains fields and methods for a ship object.
 * A ship has a size, an orientation, a location, and a number of hits remaining until it is sunk.
 * The class also has methods for getting and setting the orientation, getting the size, getting the type of ship,
 * checking if the ship has sunk, hitting the ship, and getting and setting the location.
 * 
 * @author j-code
 *
 */

abstract class Ship {
	private final int size;
	private Orientation orientation; // 0 = right, 1 = down, 2 = left, 3 = up
	private XY location;
	private int nHitsRemaining;
	
	public Ship(int size, XY location, Orientation orientation) {
		this.size = size;
		this.orientation = orientation;
		this.location = location;
		// Initialize ship's hits remaining until sunk
		nHitsRemaining = size;
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

	public boolean hasSunk() {
		if (this.nHitsRemaining <= 0) { return true; } else { return false; }
	}

	void hit() {
		nHitsRemaining--;
	}
	
	public XY getLocation() {
		return location;
	}
	
	public void setLocation(XY location) {
		this.location = location;
	}
	
	@Override
	public String toString() {
		return getType().toString() + " at " + getLocation() + " pointing " + getOrientation();
	}

}
