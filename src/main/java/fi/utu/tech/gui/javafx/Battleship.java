package fi.utu.tech.gui.javafx;

public class Battleship extends Ship {

	public Battleship(XY location, Orientation orientation) {
		super(4, location, orientation); // Battleship has a length of 4
	}

	@Override
	ShipType getType() {
		return ShipType.BATTLESHIP;
	}
}
