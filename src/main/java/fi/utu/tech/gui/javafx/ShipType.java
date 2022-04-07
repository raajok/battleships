package fi.utu.tech.gui.javafx;

/**
 * This is a java enum representing the different types of ships in the game.
 * 
 * @author j-code
 *
 */

public enum ShipType {
	CARRIER("lentotukialus") {
		@Override
		public Ship instantiate(XY location, Orientation orientation) {
			return new Carrier(location, orientation);
		}		
	},
	BATTLESHIP("taistelulaiva") {
		@Override
		public Ship instantiate(XY location, Orientation orientation) {
			return new Battleship(location, orientation);
		}		
	},
	CRUISER("risteilijä") {
		@Override
		public Ship instantiate(XY location, Orientation orientation) {
			return new Cruiser(location, orientation);
		}		
	},
	SUBMARINE("sukellusvene") {
		@Override
		public Ship instantiate(XY location, Orientation orientation) {
			return new Submarine(location, orientation);
		}		
	},
	DESTROYER("hävittäjä") {
		@Override
		public Ship instantiate(XY location, Orientation orientation) {
			return new Destroyer(location, orientation);
		}		
	};
	
	private String name;
	
	public abstract Ship instantiate(XY location, Orientation orientation);
	private ShipType(final String str) {
		this.name = str;
	}

	@Override
	public String toString() {
		return this.name;
	}
}