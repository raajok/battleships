package fi.utu.tech.gui.javafx;

import java.util.Collection;

import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * The main class of the Battleship game model.
 * 
 * It controls the game play and creates the player's boards when newGame-method is called.
 * 
 */

public class BattleshipGame {	
	private Gameboard[] boards = new Gameboard[2];
	private SimpleStringProperty[] playerNamesProperty = {new SimpleStringProperty(),
														  new SimpleStringProperty()};
	private ObjectProperty<Player> playerInTurn = new SimpleObjectProperty<Player>();
	private SimpleStringProperty playerInTurnNameProperty = new SimpleStringProperty();
	private SimpleIntegerProperty playerInTurnValueProperty = new SimpleIntegerProperty();
	private SimpleBooleanProperty gameReady = new SimpleBooleanProperty(false);
	private SimpleBooleanProperty settingsReady = new SimpleBooleanProperty(false);
	private SimpleIntegerProperty[] shipCountProperties = {new SimpleIntegerProperty(0),
	                                              		   new SimpleIntegerProperty(0),
	                                              		   new SimpleIntegerProperty(0),
	                                              		   new SimpleIntegerProperty(0),
	                                              		   new SimpleIntegerProperty(0)};
	private SimpleIntegerProperty boardSizeProperty = new SimpleIntegerProperty(10);
	private SimpleIntegerProperty shipSums = new SimpleIntegerProperty(0);
	private SimpleBooleanProperty turnIsOver = new SimpleBooleanProperty(false);
	private SimpleBooleanProperty requestTurnChange = new SimpleBooleanProperty(false);
	private SimpleBooleanProperty awaiting = new SimpleBooleanProperty(false);
	private DoubleProperty effectVol = new SimpleDoubleProperty(.5);
	private Runnable onGameEndAction;
	
	/**
	 * This is a constructor for the BattleshipGame class.
	 * It initializes the shipSums property to the sum of the shipCountProperties for each ship type,
	 * multiplied by the appropriate number of squares for that ship type.
	 * 
	 * It also initializes the settingsReady property to true if the board size is large enough to
	 * accommodate all the ships and if there is at least one ship.
	 * 
	 * Finally, it sets up a listener for the playerInTurn property so that when it changes,
	 * the playerInTurnNameProperty and playerInTurnValueProperty are updated accordingly.
	 * 
	 */
	
	public BattleshipGame() {
		shipSums.bind(shipCountProperties[ShipType.CARRIER.ordinal()].multiply(5)
				.add(shipCountProperties[ShipType.BATTLESHIP.ordinal()].multiply(4)
				.add(shipCountProperties[ShipType.CRUISER.ordinal()].multiply(3)
				.add(shipCountProperties[ShipType.SUBMARINE.ordinal()].multiply(3)
				.add(shipCountProperties[ShipType.DESTROYER.ordinal()].multiply(2)
						)))));
		settingsReady.bind(
			 Bindings.when(
					boardSizeProperty.multiply(boardSizeProperty).greaterThanOrEqualTo(shipSums.multiply(2))
					.and(shipSums.greaterThan(0)))
			 		.then(true)
			 		.otherwise(false));

		playerInTurn.addListener((obj, oldVal, newVal) -> {
			playerInTurnNameProperty.setValue(playerNamesProperty[newVal.ordinal()].getValue());
			playerInTurnValueProperty.set(newVal.ordinal());			
		});
	}
	
	/**
	 * The newGame function is used to start a new game.
	 * This function creates a new Gameboard object for each player with a given name and ship counts.
	 * 
	 * The boards[].readyProperty indicates a boolean state when a board is ready to start a new game.
	 * Both boards ready properties are bound to gameReady property.
	 * Likewise, the gameReady property indicates a boolean state when the game is ready to start,
	 * but it is true only when both boards are ready to start.
	 * Before binding happens, the gameReady property unbinds itself from the old bindings (if any).
	 * This way we can have only one listener on the game readiness state instead of two listeners for each board separately.
	 *
	 * Finally it sets our current playerInTurn variable as Player 1, who is a starting player.
	 * 
	 **/
	
	public void newGame() {
		
		
		// Create and initialize the boards
		int[]shipCounts = {shipCountProperties[0].get(),
						shipCountProperties[1].get(),
						shipCountProperties[2].get(),
						shipCountProperties[3].get(),
						shipCountProperties[4].get()				
		};
	
		// Create and initialize the boards
		this.boards[Player.PLAYER1.ordinal()] = new Gameboard(
                                                           boardSizeProperty.get(),
                                                           shipCounts);
		this.boards[Player.PLAYER2.ordinal()] = new Gameboard(
                                                          boardSizeProperty.get(),
                                                          shipCounts);
		
		this.playerInTurn.set(Player.PLAYER1); // Player 1 will start the game
		//this.shipCounts = shipCounts;
		 
		// Create bindings for when boards are ready
		if (this.gameReady.isBound()) this.gameReady.unbind(); // Remove old bindings
		this.gameReady.bind(
			 Bindings.when(
					boards[Player.PLAYER1.ordinal()].readyProperty()
					.and(
			 		boards[Player.PLAYER2.ordinal()].readyProperty()))
			 		.then(true)
			 		.otherwise(false));
	}
	
	/**
	 * The shoot function takes a coordinate and checks if the location is shootable.
	 * If it is, it calls the opponent board's setHit method. Then checks if all ships have been sunk.
	 * It is indicated by opponent board's getHitsRemaining value.
	 * If all ships have been sunk, the game is over and the onGameScene is shown to announce the winner.
	 * 
	 * If the hit was a miss (value at the location of coord = 0) then it switches turns.
	 * Finally the value at the location of coord is returned.
	 * 
	 * @param coord Used to Determine the location of the opponent's board that is to be shot at.
	 * @return A value indicating the result of the shot.
	 * 
	 */
	
	public int shoot(XY coord) {
		Gameboard opponentBoard = boards[getOpponent().ordinal()];
		int valueAtLocation = opponentBoard.getBoard()[coord.getX()][coord.getY()];
		if (opponentBoard.isShootable(coord)) {
			opponentBoard.setHit(coord);
			// Is game over?
			if (opponentBoard.getnHitsRemaining() <= 0) {
				// player "playerInTurn" has won.
				// Switch to "Start Menu"-scene and announce the winner.
				onGameEndAction.run();
			}
			// If game continues and no hit, switch turns.
			// Otherwise allow to shoot again.
			if (valueAtLocation != 1) {
				requestTurnChange.set(true);
				switchTurn();
			}
		}
		return valueAtLocation;
	}
	
	public Boolean isShootable(XY coord) {
		return boards[getOpponent().ordinal()].isShootable(coord);
	}
	
	public void switchTurn() {
		playerInTurn.set(getOpponent());
	}
	
	public Player getOpponent() {
		return getOpponent(this.playerInTurn.get()); 
	}
	
	public Player getOpponent(Player player) {
		return player.next(); 
	}
	
	public boolean addShip(Ship ship) {
		return addShip(ship, this.playerInTurn.get());
	}
	
	public boolean addShip(Ship ship, Player player) {
		return boards[player.ordinal()].addShip(ship);
	}
	
	public Ship createShip(ShipType shipType, XY location, Orientation orientation) {
		return shipType.instantiate(location, orientation);
	}
	
	public SimpleBooleanProperty gameReadyProperty() {
		return this.gameReady;
	}
	
	public SimpleBooleanProperty settingsReadyProperty() {
		return this.settingsReady;
	}
	
	public SimpleIntegerProperty[] shipCountProperties() {
		return shipCountProperties;
	}

	public SimpleIntegerProperty boardSizeProperty() {
		return boardSizeProperty;
	}
	
	public SimpleStringProperty[] getPlayerNamesProperty() {
		return playerNamesProperty;
	}
	
	public Gameboard getBoard() {
		return boards[playerInTurn.get().ordinal()];
	}
	
	public Gameboard getOpponentBoard() {
		return boards[getOpponent().ordinal()];
	}
	
	public Player getPlayerInTurn() {
		return playerInTurn.get();
	}
	
	public ObjectProperty<Player> playerInTurnProperty() {
		return playerInTurn;
	}
	
	public SimpleStringProperty playerInTurnNameProperty() {
		return playerInTurnNameProperty;
	}
		
	public SimpleIntegerProperty playerInTurnValueProperty() {
		return playerInTurnValueProperty;
	}
	
	public Collection<Ship> getShips(Player player) {
		return boards[player.ordinal()].getShips();
	}
	
	public Ship getShipFrom(Player player, XY coord) {
		return boards[player.ordinal()].getShipFrom(coord);
	}
	
	public SimpleBooleanProperty turnIsOverProperty() {
		return turnIsOver;
	}
	
	public SimpleBooleanProperty awaitingProperty() {
		return awaiting;
	}
	
	public SimpleBooleanProperty requestTurnChangeProperty() {
		return requestTurnChange;
	}
	
	public void setOnGameEndAction(Runnable onGameEndAction) {
		this.onGameEndAction = onGameEndAction;
	}
	
	public void removeShip(Ship ship) {
		removeShip(ship, playerInTurn.get());
	}
	
	public void removeShip(Ship ship, Player player) {
		boards[player.ordinal()].removeShip(ship);
	}
	
	public void removeShips(Collection<Ship> ships) {
		removeShips(ships, playerInTurn.get());
	}
	
	public void removeShips(Collection<Ship> ships, Player player) {
		for (Ship ship: ships) {
			removeShip(ship, player);
		}
	}
	
	public void removeAllShips(Player player) {
		removeShips(boards[player.ordinal()].getShips());
	}
	
	public void removeAllShips() {
		removeShips(boards[playerInTurn.get().ordinal()].getShips());
	}
	
	public DoubleProperty effectVolProperty() {
		return effectVol;
	}
}
