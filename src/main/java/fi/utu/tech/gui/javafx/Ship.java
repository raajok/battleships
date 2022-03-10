package fi.utu.tech.gui.javafx;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.scene.image.Image;

abstract class Ship {
	private final int size;
	private int orientation; // 0 = right, 1 = down, 2 = left, 3 = up
	private final Image shipImage;
	
	public Ship(int size, String imageSrc) throws FileNotFoundException {
		this.size = size;
		this.orientation = 0;
		this.shipImage = new Image(new FileInputStream(imageSrc));
	}
	
	public int getOrientation() {
		return orientation;
	}
	
	public void setOrientation(int orientation) {
		this.orientation = orientation;
	}
	
	public int getSize() {
		return size;
	}
	
	abstract ShipType getType();

	public Image getShipImage() {
		return shipImage;
	}

}
