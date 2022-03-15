package fi.utu.tech.gui.javafx;

public enum Player {
	
	// enum fields
    PLAYER1("Player1"),
    PLAYER2("Player2");
	
	// constructor
    private Player(final String str) {
        this.str = str;
    }
 
    // internal state
    private String str;
	
	@Override
	public String toString() {
		return str;
	}
	
	private static Player[] vals = values();
	
    public Player next()
    {
        return vals[(this.ordinal()+1) % vals.length];
    }
}
