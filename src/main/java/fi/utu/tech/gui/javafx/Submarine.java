package fi.utu.tech.gui.javafx;

import java.io.FileNotFoundException;

public class Submarine extends Ship {

	public Submarine() throws FileNotFoundException {
		super(3,"Submarine_placeholder.png"); // Submarine has a length of 3
	}
	
	@Override
	ShipType getType() {
		return ShipType.Submarine;
	}

}
