package fi.utu.tech.gui.javafx;

import javafx.scene.image.Image;
import javafx.scene.media.AudioClip;

public class MultimediaSprite extends Sprite {
	private AudioClip audioClip;

	public MultimediaSprite(Image image, Integer columns, Integer rows, Integer frameWidth, Integer frameHeight,
			Integer framesPerSecond, Integer repeat, AudioClip audioClip) {
		super(image, columns, rows, frameWidth, frameHeight, framesPerSecond, repeat);
		this.audioClip = audioClip;
		double vol = this.audioClip.getVolume();
		this.audioClip.setVolume(0);
		this.audioClip.play();
		this.audioClip.setVolume(vol);
	}
	
	@Override
	public void start() {
		audioClip.play();
		super.start();
	}

}
