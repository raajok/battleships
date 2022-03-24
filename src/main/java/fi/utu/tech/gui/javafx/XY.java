package fi.utu.tech.gui.javafx;

public class XY {
	private final int x;
	private final int y;
	
	public XY(int x, int y) {
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
	public String toString() {
		return String.format("%d,%d", this.x, this.y);
	}
}
