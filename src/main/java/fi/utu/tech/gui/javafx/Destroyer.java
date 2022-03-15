package fi.utu.tech.gui.javafx;

public class Destroyer extends Ship {

	public Destroyer(XY location, Orientation orientation) {
		super(2,location, orientation); // Destroyer has a length of 2
	}
	
	@Override
	ShipType getType() {
		return ShipType.DESTROYER;
	}

}
