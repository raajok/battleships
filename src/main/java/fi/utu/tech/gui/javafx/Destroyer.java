package fi.utu.tech.gui.javafx;

import java.io.FileNotFoundException;

public class Destroyer extends Ship {

	public Destroyer() throws FileNotFoundException {
		super(2,"Destroyer_placeholder.png"); // Destroyer has a length of 2
	}
	
	@Override
	ShipType getType() {
		return ShipType.Destroyer;
	}

}
