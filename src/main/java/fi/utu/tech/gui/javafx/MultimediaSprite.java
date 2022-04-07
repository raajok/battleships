package fi.utu.tech.gui.javafx;

import javafx.scene.image.Image;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class MultimediaSprite extends Sprite {
	private MediaPlayer player;

	public MultimediaSprite(Image image, Integer columns, Integer rows, Integer frameWidth, Integer frameHeight,
			Integer framesPerSecond, Integer repeat, Media media) {
		super(image, columns, rows, frameWidth, frameHeight, framesPerSecond, repeat);
		this.player = new MediaPlayer(media);
		this.player.volumeProperty().bind(MainApp.getGame().effectVolProperty());
	}
	
	@Override
	public void start() {
		player.play();
		super.start();
	}

}
