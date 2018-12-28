package controller;

import java.util.Timer;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.ImageCarver;
import model.SeamDirection;

public class CarvingController extends CustomController {

	@FXML private ImageView carvingImageView;
	@FXML private Label totalSeamsLabel;
	@FXML private Label currentSeamLabel;
	@FXML private Label horizontalSeamsLabel;
	@FXML private Label verticalSeamsLabel;
	@FXML private Label horizontalTotalSeamsLabel;
	@FXML private Label horizontalCurrentSeamLabel;
	@FXML private Label verticalCurrentSeamLabel;
	@FXML private Label verticalTotalSeamsLabel;
	@FXML private Button cancelCarvingButton;
	
	private Image _originalImage;
	private Image _currentImage;
	private int _chosenHeight;
	private int _chosenWidth;
	private int _vertSeamsToDo;
	private int _horiSeamsToDo;
	private int _vSeamsDone;
	private int _hSeamsDone;
	
	@Override
	protected void start() {
		carvingImageView.setImage(_originalImage);
		int origHeight =(int) _originalImage.getHeight();
		int origWidth = (int) _originalImage.getWidth();
		
		int totalVerticalSeams = origWidth - _chosenWidth;
		int totalHorizontalSeams = origHeight - _chosenHeight;
		
		horizontalTotalSeamsLabel.setText("" + totalHorizontalSeams);
		verticalTotalSeamsLabel.setText("" + totalVerticalSeams);
		
		_vertSeamsToDo = totalVerticalSeams;
		_horiSeamsToDo = totalHorizontalSeams;
		
		_vSeamsDone = 0;
		_hSeamsDone = 0;
		
		
		_currentImage = _originalImage;
		
		carvingImageView.setImage(_currentImage);
		
		Thread thread = new Thread(new Runnable() {
			
			private ImageCarver _im;
			
			@Override
			public void run() {
				
				Runnable showEnergy = new Runnable() {
					@Override
					public void run() {
						carvingImageView.setImage(_im.getImageEnergy());
					}
				};
				
				Runnable showSeam = new Runnable() {
					@Override
					public void run() {
						carvingImageView.setImage(_im.getColouredSeam());
					}
				};
				
				Runnable removeSeamVertical = new Runnable() {
					@Override
					public void run() {
						_currentImage = _im.getImageWithSeamRemoved();
						carvingImageView.setImage(_currentImage);
						_vertSeamsToDo--;
						_vSeamsDone++;
						verticalCurrentSeamLabel.setText(Integer.toString(_vSeamsDone));
					}
				};
				
				Runnable removeSeamHorizontal = new Runnable() {
					@Override
					public void run() {
						_currentImage = _im.getImageWithSeamRemoved();
						carvingImageView.setImage(_currentImage);
						_horiSeamsToDo--;
						_hSeamsDone++;
						horizontalCurrentSeamLabel.setText(Integer.toString(_hSeamsDone));
					}
				};
				
				while (_vertSeamsToDo > 0 || _horiSeamsToDo > 0) {
					try {
					
						long periodBetweenStages = 10;
						
						Thread.sleep(periodBetweenStages);
						if (_vertSeamsToDo > 0) {
							
							_im = new ImageCarver(_currentImage, SeamDirection.VERTICAL);
							Platform.runLater(showEnergy);
							Thread.sleep(periodBetweenStages);
							Platform.runLater(showSeam);
							Thread.sleep(periodBetweenStages);
							Platform.runLater(removeSeamVertical);
							
						}
						Thread.sleep(periodBetweenStages);
						if (_horiSeamsToDo > 0) {
							
							_im = new ImageCarver(_currentImage, SeamDirection.HORIZONTAL);
							Platform.runLater(showEnergy);
							Thread.sleep(periodBetweenStages);
							Platform.runLater(showSeam);
							Thread.sleep(periodBetweenStages);
							Platform.runLater(removeSeamHorizontal);
							
						}
					
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
			}
			
		});
		
		thread.setDaemon(true);
		thread.start();
		
	}
	
	
	public void setImageAndHeightAndWidth(Image image, int height, int width) {
		_originalImage = image;
		_chosenHeight = height;
		_chosenWidth = width;
		
	}

}
