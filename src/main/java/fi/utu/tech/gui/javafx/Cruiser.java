package fi.utu.tech.gui.javafx;

import java.io.FileNotFoundException;

public class Cruiser extends Ship {

	public Cruiser() throws FileNotFoundException {
		super(3,"Cruiser_placeholder.png"); // Cruiser has a length of 3
	}
	
	@Override
	ShipType getType() {
		return ShipType.Cruiser;
	}

}
