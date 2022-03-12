package fi.utu.tech.gui.javafx;

public class Submarine extends Ship {

	public Submarine(XY location, Orientation orientation) {
		super(3, location, orientation); // Submarine has a length of 3
	}
	
	@Override
	ShipType getType() {
		return ShipType.SUBMARINE;
	}

}
