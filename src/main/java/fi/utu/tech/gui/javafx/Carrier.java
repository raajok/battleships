package fi.utu.tech.gui.javafx;

public class Carrier extends Ship {

	public Carrier(XY location, Orientation orientation) {
		super(5, location, orientation); // Carrier has a length of 5
	}

	@Override
	ShipType getType() {
		return ShipType.CARRIER;
	}
}
