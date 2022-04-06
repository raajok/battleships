package fi.utu.tech.gui.javafx;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class SoundBoxController {
    private MediaPlayer player;
    private Timer hideSoundBoxTimer = new Timer();
    private Boolean mouseOn = false;
    private List<Media> playlist = new LinkedList<Media>();
    private Iterator<Media> playlistIterator;
    private Image playIconWhite = new Image(ResourceLoader.image("play icon.png"));
    private Image playIconGray = new Image(ResourceLoader.image("play icon gray.png"));
    private Image pauseIconWhite = new Image(ResourceLoader.image("pause icon.png"));
    private Image pauseIconGray = new Image(ResourceLoader.image("pause icon gray.png"));
    private Image nextIconWhite = new Image(ResourceLoader.image("next icon.png"));
    private Image nextIconGray = new Image(ResourceLoader.image("next icon gray.png"));
    
    private class HideSoundBoxTimerTask extends TimerTask {

		@Override
		public void run() {
			// Hide sound box
			if (mouseOn) { hideSoundBoxTimer.schedule(new HideSoundBoxTimerTask(), 2000); }
			else {
				soundBox.setVisible(false);
			}
		}
   
    };
    
    private void loadNextSong() {
    	if (!playlistIterator.hasNext()) playlistIterator = playlist.iterator();
    	player = new MediaPlayer(playlistIterator.next());
    }
    
    public SoundBoxController() {
    	// Load playlist
    	playlist.add(new Media(ResourceLoader.image("Battle Theme 1.mp3")));
    	playlist.add(new Media(ResourceLoader.image("Battle Theme 2.mp3")));
    	playlist.add(new Media(ResourceLoader.image("Battle Theme 3.mp3")));
    	playlist.add(new Media(ResourceLoader.image("Battle Theme 4.mp3")));
    	playlist.add(new Media(ResourceLoader.image("Fanfare.mp3")));
    	playlistIterator = playlist.iterator();
	}
    
    public void init() {
    	soundBox.setVisible(false);
    	soundBox.visibleProperty().addListener((obj, oldVal, newVal) -> {
    		if (newVal.booleanValue()) {
    			hideSoundBoxTimer.schedule(new HideSoundBoxTimerTask(), 500);    			
    		}
    	});

    	loadNextSong();
    	player.play();
    	
		// Bindings for sound volumes
		player.volumeProperty().bind(musicSlider.valueProperty());
		MainApp.getGame().effectVolProperty().bind(effectSlider.valueProperty());
    }


	@FXML
	private Slider musicSlider;

	@FXML
	private Slider effectSlider;
	
	@FXML
	private ImageView playBtn;
	
	@FXML
	private ImageView pauseBtn;
	
	@FXML
	private ImageView nextBtn;
	
	@FXML
	private VBox soundBox;
	
	@FXML
	private Group soundBoxContainer;
	
	
	@FXML
	private void onPlayBtnMouseClicked(MouseEvent event) {
		player.play();
	}
	
	@FXML
	private void onPlayBtnMouseEntered(MouseEvent event) {
		playBtn.setImage(playIconWhite);
	}
	
	@FXML
	private void onPlayBtnMouseExited(MouseEvent event) {
		playBtn.setImage(playIconGray);
	}
	
	@FXML
	private void onPauseBtnMouseClicked(MouseEvent event) {
		player.stop();
	}
	
	@FXML
	private void onPauseBtnMouseEntered(MouseEvent event) {
		pauseBtn.setImage(pauseIconWhite);		
	}
	
	@FXML
	private void onPauseBtnMouseExited(MouseEvent event) {
		pauseBtn.setImage(pauseIconGray);				
	}
	
	@FXML
	private void onNextBtnMouseClicked(MouseEvent event) {
		player.stop();
		loadNextSong();
		player.volumeProperty().bind(musicSlider.valueProperty());
		player.play();
	}
	
	@FXML
	private void onNextBtnMouseEntered(MouseEvent event) {
		nextBtn.setImage(nextIconWhite);						
	}
	
	@FXML
	private void onNextBtnMouseExited(MouseEvent event) {
		nextBtn.setImage(nextIconGray);		
	}
	
	@FXML
	private void onSoundBoxIconMouseEntered(MouseEvent event) {
		soundBox.setVisible(true);
		mouseOn = true;
	}
	
	@FXML
	private void onSoundBoxIconMouseExited(MouseEvent event) {
		mouseOn = false;		
	}
	
	@FXML
	private void onSoundBoxMouseEntered(MouseEvent event) {
		mouseOn = true;
	}
	
	@FXML
	private void onSoundBoxMouseExited(MouseEvent event) {
		mouseOn = false;		
	}
	
}
