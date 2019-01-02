package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class SummaryController extends CustomController {
	
	private Image _originalImage;
	private Image _carvedImage;
	
	@FXML private ImageView originalImageView;
	@FXML private ImageView carvedImageView;
	@FXML private Label originalImageLabel;
	@FXML private Label carvedImageLabel;
	
	public void setImages(Image originalImage, Image carvedImage) {
		_originalImage = originalImage;
		_carvedImage = carvedImage;
	}

	@Override
	protected void start() {
		originalImageView.setImage(_originalImage);
		carvedImageView.setImage(_carvedImage);
	}

}
