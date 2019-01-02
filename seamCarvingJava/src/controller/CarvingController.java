package controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.CustomModel;
import model.EmptySeamException;
import model.ImageCarver;
import model.SeamDirection;
import model.SummaryModel;

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
	@FXML private Button continueButton;
	
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
		
		continueButton.setText("Continue");
		
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
						_im.getImageEnergy();
						//carvingImageView.setImage(_im.getImageEnergy());
					}
				};
				
				Runnable calculateSeam = new Runnable() {

					@Override
					public void run() {
						_im.calculateSeam();
					}	
				};
				
				Runnable showSeam = new Runnable() {
					@Override
					public void run() {
						try {
							carvingImageView.setImage(_im.getColouredSeam());
						} catch (EmptySeamException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				};
				
				Runnable removeSeamVertical = new Runnable() {
					@Override
					public void run() {
						try {
							_currentImage = _im.getImageWithSeamRemoved();
						} catch (EmptySeamException e) {
							// do nothing
						}
						carvingImageView.setImage(_currentImage);
						verticalCurrentSeamLabel.setText(Integer.toString(_vSeamsDone));
					}
				};
				
				Runnable removeSeamHorizontal = new Runnable() {
					@Override
					public void run() {
						try {
							_currentImage = _im.getImageWithSeamRemoved();
						} catch (EmptySeamException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						carvingImageView.setImage(_currentImage);
						horizontalCurrentSeamLabel.setText(Integer.toString(_hSeamsDone));
					}
				};
				
				while (_vertSeamsToDo > 0 || _horiSeamsToDo > 0) {
					try {
					
						long periodBetweenStages = 10;
						
						Thread.sleep(periodBetweenStages);
						if (_vertSeamsToDo > 0) {
							
							_im = new ImageCarver(_currentImage, SeamDirection.VERTICAL);
							showEnergy.run();
							calculateSeam.run();
							Thread.sleep(periodBetweenStages);
							Platform.runLater(showSeam);
							Thread.sleep(periodBetweenStages);
							_vertSeamsToDo--;
							_vSeamsDone++;
							Platform.runLater(removeSeamVertical);
							
						}
						Thread.sleep(periodBetweenStages);
						if (_horiSeamsToDo > 0) {
							
							_im = new ImageCarver(_currentImage, SeamDirection.HORIZONTAL);
							showEnergy.run();
							calculateSeam.run();
							Thread.sleep(periodBetweenStages);
							Platform.runLater(showSeam);
							Thread.sleep(periodBetweenStages);
							_horiSeamsToDo--;
							_hSeamsDone++;
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
	
	@FXML protected void handleContinue() {
		if (_horiSeamsToDo == 0 && _vertSeamsToDo == 0) {
			CustomModel summaryModel = new SummaryModel(_originalImage, _currentImage);
			summaryModel.start(_primaryStage);			
		}
	}

}
