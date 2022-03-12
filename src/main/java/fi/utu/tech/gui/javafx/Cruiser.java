package fi.utu.tech.gui.javafx;

public class Cruiser extends Ship {

	public Cruiser(XY location, Orientation orientation) {
		super(3, location, orientation); // Cruiser has a length of 3
	}
	
	@Override
	ShipType getType() {
		return ShipType.CRUISER;
	}

}
