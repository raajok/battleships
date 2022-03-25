package fi.utu.tech.gui.javafx;

public class XY {
	private final int x;
	private final int y;
	
	public XY(int x, int y) {
		if (x < 0 || x >= 15) { throw new IllegalArgumentException("Variable x is out of bounds (from 0 to 14) with value: " + x); }
		if (y < 0 || y >= 15) { throw new IllegalArgumentException("Variable y is out of bounds (from 0 to 14) with value: " + y); }
		this.x = x;
		this.y = y;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	@Override
    public boolean equals(Object comparable) {
        // if at same location, then they are equal
        if (this == comparable) {
            return true;
        }

        // if comparable is not XY type, then they are not equal
        if (!(comparable instanceof XY)) {
            return false;
        }

        // muunnetaan olio Kirja-olioksi
        XY comparableXY = (XY) comparable;

        // if types and values are same, they are equal
        if (this.x == comparableXY.x && this.y == comparableXY.y) {
            return true;
        }

        // Otherwise, they are not same
        return false;
    }
	
	@Override
	public int hashCode() {
		// Note: variables x and y has to inside bounds from 0 to 14
		return x % 15 + y * 15;
	}
	
	@Override
	public String toString() {
		return String.format("%d,%d", this.x, this.y);
	}
}
