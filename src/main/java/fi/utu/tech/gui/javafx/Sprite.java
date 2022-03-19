package fi.utu.tech.gui.javafx;

import javafx.animation.AnimationTimer;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Sprite extends AnimationTimer {

    private final ImageView imageView; //Image view that will display our sprite

    private final int totalFrames; //Total number of frames in the sequence
    private final float fps; //frames per second I.E. 24
    private final int cols; //Number of columns on the sprite sheet
    private int frameWidth; //Width of an individual frame
    private int frameHeight; //Height of an individual frame
    private final int repeat;
    private int repeatCounter;
    private int currentFrame = 0;
    private long lastFrame = 0;

    
    public Sprite(ImageView imageView, Image image, int columns, int rows, int frameWidth,
    		int frameHeight, float framesPerSecond, int repeat) {
    	
        this.imageView = imageView;
        imageView.setImage(image);
        imageView.setVisible(false);
        cols = columns;
        this.totalFrames = columns * rows;
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;
        fps = framesPerSecond;
        this.repeat = repeat; /* -1 = infinite repeat*/
        this.repeatCounter = repeat;
        showFrame(currentFrame);
    }
    
    @Override
    public void start() {
        lastFrame = System.nanoTime();
        imageView.setVisible(true);
    	super.start();
    }

    @Override
    public void handle(long now) {
    	if (repeatCounter == 0) {
            this.repeatCounter = repeat;
    		this.stop();
            imageView.setVisible(false);
    		return;
    	}
    	
    	//Determine how many frames we need to advance to maintain frame rate independence
        int frameJump = (int) Math.floor((now - lastFrame) / (1000000000 / fps));
        
        if (frameJump >= 1) {
            lastFrame = now;
            currentFrame += frameJump;
            repeatCounter -= currentFrame / totalFrames;
            currentFrame %= totalFrames;
            showFrame(currentFrame);
        }
    }
    
    public void showFrame(int frameNumber) {
    	final int col = frameNumber % cols;
    	final int row = frameNumber / cols;
    	imageView.setViewport(new Rectangle2D(col * frameWidth, row * frameHeight, frameWidth, frameHeight));
    }
    
    public void showNextFrame() {
    	currentFrame++;
        currentFrame %= totalFrames;
    	showFrame(currentFrame);
    }
    
    public void scaleBy(double scaleFactor) {
    	imageView.setScaleX(scaleFactor);
    	imageView.setScaleY(scaleFactor);
    	frameWidth *= frameWidth;
    	frameHeight *= frameHeight;
    }
}