package fi.utu.tech.gui.javafx;

import javafx.animation.AnimationTimer;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * A sprite class is an animated graphic which works by showing one frame at a time.
 * 
 * Note: a thread can be set to run after the animation finishes. This is done via setRunAfter-method.
 *
 */

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
    private SimpleBooleanProperty isPlaying = new SimpleBooleanProperty(false);
    private Thread runAfterThread;
    
    /**
     * This is the constructor for a Sprite object.
     * A Sprite is a 2D image that consists of multiple frames (images) played in succession.
     * The constructor takes in parameters for the image to be used, the number of columns and rows in the Sprite,
     * the width and height of each frame, the frames per second, and how many times to repeat the animation.
     * The imageView is set to be invisible until the start method is called.
     * Finally, it calls the showFrame() method to display the sprite's (first) current frame.
     * 
     * @param imageView The view that will contain the sprite
     * @param image The image that the sprite will use
     * @param columns The number of columns in the sprite's image
     * @param rows The number of rows in the sprite's image
     * @param frameWidth The width of each frame in the sprite's image
     * @param frameHeight The height of each frame in the sprite's image
     * @param framesPerSecond The number of frames that should be played per second
     * @param repeat The number of times that the sprite should be played (-1 means infinite)
     * 
     */
    
    public Sprite(Image image, int columns, int rows, int frameWidth,
    		int frameHeight, float framesPerSecond, int repeat) {
    	
        imageView = new ImageView();
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
    	isPlaying.set(true);
        lastFrame = System.nanoTime();
        imageView.setVisible(true);
    	super.start();
    }
    
    /**
     * The handle method is called by the JavaFX animation timer.
     * It is responsible for playing the animation, making sure it plays at the correct speed,
     * and stopping the animation when it is finished.
     * 
     * @param now Current time when the method is called.
     * 
     */

    @Override
    public void handle(long now) {
    	if (repeatCounter == 0) {
            this.repeatCounter = repeat;
            imageView.setVisible(false);
            this.isPlaying.set(false);
    		this.stop();
    		if (this.runAfterThread instanceof Thread) { runAfterThread.start();}
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
    
    public SimpleBooleanProperty isPlayingProperty() {
		return isPlaying;
	}
    
    public void setRunAfter(Thread thread) {
    	this.runAfterThread = thread;
    }
    
    public ImageView getImageView() {
		return imageView;
	}
}