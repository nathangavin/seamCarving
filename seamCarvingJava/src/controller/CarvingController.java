package controller;

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
		
		_currentImage = _originalImage;
		
		carvingImageView.setImage(_currentImage);
		beginCarving();
	}
	
	public void setImageAndHeightAndWidth(Image image, int height, int width) {
		_originalImage = image;
		_chosenHeight = height;
		_chosenWidth = width;
		
	}
	
	private void beginCarving() {
		
		int vSeamsTD = _vertSeamsToDo;
		int hSeamsTD = _horiSeamsToDo;	
		
		while (vSeamsTD > 0 || hSeamsTD > 0) {
			if (vSeamsTD > 0) {
				
				ImageCarver im = new ImageCarver(_currentImage, SeamDirection.VERTICAL);
				carvingImageView.setImage(im.getImageEnergy());
				carvingImageView.setImage(im.getColouredSeam());
				_currentImage = im.getImageWithSeamRemoved();
				carvingImageView.setImage(_currentImage);
				
				int c = Integer.parseInt(verticalCurrentSeamLabel.getText());
				c++;
				verticalCurrentSeamLabel.setText("" + c);
				
				vSeamsTD--;
			}
		
			if (hSeamsTD > 0) {
				ImageCarver im = new ImageCarver(_currentImage, SeamDirection.HORIZONTAL);
				carvingImageView.setImage(im.getImageEnergy());
				carvingImageView.setImage(im.getColouredSeam());
				_currentImage = im.getImageWithSeamRemoved();
				carvingImageView.setImage(_currentImage);
				
				int c = Integer.parseInt(horizontalCurrentSeamLabel.getText());
				c++;
				horizontalCurrentSeamLabel.setText("" + c);
				
				hSeamsTD--;
			}
		}
		
		
	}

}
