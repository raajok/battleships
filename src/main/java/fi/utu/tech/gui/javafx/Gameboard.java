package fi.utu.tech.gui.javafx;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

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
		private Collection<Ship> ships = new ArrayList<Ship>();
		
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
			if((fieldStatus[x][y] == 0) &&
				(x == boardX-1 || fieldStatus[x+1][y] == 0) &&
				(x == 0 || fieldStatus[x-1][y] == 0) &&
				(y == boardY-1 || fieldStatus[x][y+1] == 0) &&
				(y == 0 || fieldStatus[x][y-1] == 0)) {
				return true;
			}
			else {
				return false;
			}
		}

		
		public void setShip(Ship ship, XY coord) {
			// setting field status to 1 (not empty, not hit) at the coordinates that the ship occupies
			switch(ship.getOrientation()) {
			case RIGHT:
				for (int i = 0; i<ship.getSize(); i++) {
					fieldStatus[coord.getX()+i][coord.getY()] = 1;
				}
			case LEFT:
				for (int i = 0; i<ship.getSize(); i++) {
					fieldStatus[coord.getX()-i][coord.getY()] = 1;
				}
			case UP:
				for (int i = 0; i<ship.getSize(); i++) {
					fieldStatus[coord.getX()][coord.getY()+i] = 1;
				}
			case DOWN:
				for (int i = 0; i<ship.getSize(); i++) {
					fieldStatus[coord.getX()][coord.getY()-i] = 1;
				}
			}
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
			ships.add(ship);
			return true;
		}
		
		public int[][] getBoard() {
			return this.fieldStatus;
		}

		public Collection getShips() {
			return ships;
		}
		
}

