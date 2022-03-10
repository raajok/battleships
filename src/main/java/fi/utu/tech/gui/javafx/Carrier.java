package fi.utu.tech.gui.javafx;

import java.io.FileNotFoundException;

public class Carrier extends Ship {

	public Carrier() throws FileNotFoundException {
		super(5,"Carrier_placeholder.png"); // Carrier has a length of 5
	}

	@Override
	ShipType getType() {
		return ShipType.Carrier;
	}
}
