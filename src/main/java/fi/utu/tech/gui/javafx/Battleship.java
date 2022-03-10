package fi.utu.tech.gui.javafx;

import java.io.FileNotFoundException;

public class Battleship extends Ship {

	public Battleship() throws FileNotFoundException {
		super(4,"Battleship_placeholder.png"); // Battleship has a length of 4
	}

	@Override
	ShipType getType() {
		return ShipType.Battleship;
	}
}
