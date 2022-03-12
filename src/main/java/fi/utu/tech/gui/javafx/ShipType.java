package fi.utu.tech.gui.javafx;

public enum ShipType {
	CARRIER {
		@Override
		public Ship instantiate(XY location, Orientation orientation) {
			return new Carrier(location, orientation);
		}		
	},
	BATTLESHIP {
		@Override
		public Ship instantiate(XY location, Orientation orientation) {
			return new Battleship(location, orientation);
		}		
	},
	CRUISER {
		@Override
		public Ship instantiate(XY location, Orientation orientation) {
			return new Cruiser(location, orientation);
		}		
	},
	SUBMARINE {
		@Override
		public Ship instantiate(XY location, Orientation orientation) {
			return new Submarine(location, orientation);
		}		
	},
	DESTROYER {
		@Override
		public Ship instantiate(XY location, Orientation orientation) {
			return new Destroyer(location, orientation);
		}		
	};
	
	public abstract Ship instantiate(XY location, Orientation orientation);
}