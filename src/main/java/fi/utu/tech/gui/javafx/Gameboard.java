package fi.utu.tech.gui.javafx;

import java.util.LinkedList;
import java.util.Deque;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

import javafx.beans.property.SimpleBooleanProperty;

public class Gameboard {
		
		private int boardX;
		private int boardY;
		// Field status array
		// 0: empty, not hit
		// 1: not empty, not hit
		// -1: not empty, hit
		// 2: empty, hit
		private int[][] fieldStatus;
		private int HitsRemaining;
		private Deque<Ship> ships = new LinkedList<Ship>();
		private Map<XY, Ship> shipsMapping = new HashMap<XY, Ship>();
		
		private SimpleBooleanProperty ready = new SimpleBooleanProperty(false);

		public Gameboard(int boardSize, int[] shipCounts) {
			// makes board and initializes the field status of all coordinates to 0 (empty and not hit)
			this.boardX = boardSize;
			this.boardY = boardSize;
			this.HitsRemaining = countHitsRemaining(shipCounts);
			
			fieldStatus = new int[this.boardX][this.boardY];
			for(int[] row : fieldStatus) {
				Arrays.fill(row, 0);
			}	
		}
		
		public void shipCheck() {
			int ones = 0;
			
			for(int i = 0; i<boardX; i++) {
				for(int j = 0; j<boardY; j++) {
					if(fieldStatus[i][j] == 1) {
						ones++;
					}
				}
			}
			
			if(ones == HitsRemaining) {
				ready.setValue(true);
			}
			else {
				ready.setValue(false);
			}
		}
		
		public int countHitsRemaining(int[] shipCounts) {
			// adds up all the "hitpoints" of all the ships, example ship has the length of 2 so it has 2 hitpoints
			int hits;
			hits = shipCounts[ShipType.CARRIER.ordinal()]*5+shipCounts[ShipType.BATTLESHIP.ordinal()]*4+shipCounts[ShipType.CRUISER.ordinal()]*3+
					shipCounts[ShipType.SUBMARINE.ordinal()]*3+shipCounts[ShipType.DESTROYER.ordinal()]*2;
			return hits;
		}
		
		public boolean isFree(int x, int y) {
			// Return false if x or y is out of bounds.
			if (x < 0 || y < 0 || x >= fieldStatus[0].length || y >= fieldStatus[1].length) { return false; }
			
			// If location [x],[y] is free, return true, otherwise false.
			if (fieldStatus[x][y] == 0) { return true; } else {	return false; }
		}

		
		public boolean setShip(Ship ship, XY coord) {
			// setting field status to 1 (not empty, not hit) at the coordinates that the ship occupies
			
			int x; int y;
			
			Queue<XY> coords = new LinkedList<XY>();
			switch(ship.getOrientation()) {
			case RIGHT:
				for (int i = 0; i<ship.getSize(); i++) {
					x = coord.getX()+i;
					y = coord.getY();
					if (isFree(x,y)) { coords.add(new XY(x,y)); } else { return false; }
				}
			case LEFT:
				for (int i = 0; i<ship.getSize(); i++) {
					x = coord.getX()-i;
					y = coord.getY();
					if (isFree(x,y)) { coords.add(new XY(x,y)); } else { return false; }
				}
			case UP:
				for (int i = 0; i<ship.getSize(); i++) {
					x = coord.getX();
					y = coord.getY()-i;
					if (isFree(x,y)) { coords.add(new XY(x,y)); } else { return false; }
				}
			case DOWN:
				for (int i = 0; i<ship.getSize(); i++) {
					x = coord.getX();
					y = coord.getY()+i;
					if (isFree(x,y)) { coords.add(new XY(x,y)); } else { return false; }
				}
			}
			for (XY xy: coords) {
				fieldStatus[xy.getX()][xy.getY()] = 1;
				shipsMapping.put(xy, ship);				
			}
			return true;
		}
		
		public Boolean isShootable(XY coord) {
			// checks if the coordinate is shootable, aka it hasnt been shot yet
			if (fieldStatus[coord.getX()][coord.getY()] == 0 || fieldStatus[coord.getX()][coord.getY()] == 1) {
				return true;
			}
			return false; 
		}
		
		public Boolean isHitSuccessful(XY coord) {
			if (fieldStatus[coord.getX()][coord.getY()] == 1) {
				return true;
			}
			return false;
		}
		
		public void setHit(XY coord) {
			// sets the field status to hit (-1 or 2) and reduces the nHitsRemaining if the hit was successful
			if (isHitSuccessful(coord)) {
				Ship ship = getShipFrom(coord);
				if (ship != null) { ship.hit(); }
				fieldStatus[coord.getX()][coord.getY()] = -1;
				HitsRemaining--;
			}
			else {
				fieldStatus[coord.getX()][coord.getY()] = 2;
			}
		}
		
		public void reset() {
			// sets field status values to 0
			for(int[] row : fieldStatus) {
				Arrays.fill(row, 0);
			}	
		}
		
		public int getnHitsRemaining() {
			return HitsRemaining;
		}
		
		public void setnHitsRemaining(int nHitsRemaining) {
			this.HitsRemaining = nHitsRemaining;
		}
		
		
		public boolean moveShip(Ship ship, XY toLocation) {
			// checks if the place where the ship is being placed is free
			try {
			switch(ship.getOrientation()) {
			case RIGHT:
				for (int i = 0; i<ship.getSize(); i++) {
					if (isFree(toLocation.getX()+i,toLocation.getY())) {
						return true;
					}
				}
			case LEFT:
				for (int i = 0; i<ship.getSize(); i++) {
					if (isFree(toLocation.getX()-i,toLocation.getY())) {
						return true;
					}
				}
			case UP:
				for (int i = 0; i<ship.getSize(); i++) {
					if (isFree(toLocation.getX(),toLocation.getY()+i)) {
						return true;
					}
				}
			case DOWN:
				for (int i = 0; i<ship.getSize(); i++) {
					if (isFree(toLocation.getX(),toLocation.getY()-i)) {
						return true;
					}
				}
			}
			return false;
			}
			catch(NullPointerException e) {
				return false;
			}	
		}
		
		public SimpleBooleanProperty readyProperty() {
				return this.ready;
			
		}

		public boolean addShip(Ship ship) {
			// Return true if successful, false if not.
			if (setShip(ship, ship.getLocation())) {
				ships.push(ship);
				return true;
			} else {
				return false;
			}
		}
		
		public int[][] getBoard() {
			return this.fieldStatus;
		}

		public Collection<Ship> getShips() {
			return ships;
		}
		
		public String toString() {
			StringBuilder sb = new StringBuilder();		
			sb.append("  0123456789\n");
			for (int y = 0; y < this.fieldStatus.length; y++) {
				sb.append(y + " ");
				for (int x = 0; x < this.fieldStatus.length; x++) {
					sb.append(this.fieldStatus[x][y]);
				}
				sb.append("\n");
			}
			return sb.toString();
		}
		
		public Ship getShipFrom(XY coord) {
			return shipsMapping.getOrDefault(coord, null);
		}

		public void removeShip(Ship ship) {
			XY coord = ship.getLocation();
			switch(ship.getOrientation()) {
			case RIGHT:
				for (int i = 0; i<ship.getSize(); i++) {
					fieldStatus[coord.getX()+i][coord.getY()] = 0;
					shipsMapping.remove(new XY(coord.getX()+i,coord.getY()));
				} break;
			case LEFT:
				for (int i = 0; i<ship.getSize(); i++) {
					fieldStatus[coord.getX()-i][coord.getY()] = 0;
					shipsMapping.remove(new XY(coord.getX()-i,coord.getY()));
				} break;
			case UP:
				for (int i = 0; i<ship.getSize(); i++) {
					fieldStatus[coord.getX()][coord.getY()-i] = 0;
					shipsMapping.remove(new XY(coord.getX(),coord.getY()-i));
				} break;
			case DOWN:
				for (int i = 0; i<ship.getSize(); i++) {
					fieldStatus[coord.getX()][coord.getY()+i] = 0;
					shipsMapping.remove(new XY(coord.getX(),coord.getY()+i));
				} break;
			}
			ships.remove(ship);
		}
		
		public void removeLastShip() {
			this.removeShip(ships.pop());
		}
}

