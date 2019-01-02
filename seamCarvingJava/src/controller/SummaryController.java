package controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

public class SummaryController extends CustomController {
	
	private Image _originalImage;
	private Image _carvedImage;
	
	@FXML private ImageView originalImageView;
	@FXML private ImageView carvedImageView;
	@FXML private Label originalImageLabel;
	@FXML private Label carvedImageLabel;
	@FXML private Button saveImageButton;
	
	public void setImages(Image originalImage, Image carvedImage) {
		_originalImage = originalImage;
		_carvedImage = carvedImage;
	}

	@Override
	protected void start() {
		originalImageView.setImage(_originalImage);
		carvedImageView.setImage(_carvedImage);
	}
	
	@FXML protected void handleSaveImage() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Save Image");
        fileChooser.setInitialDirectory(
                new File(System.getProperty("user.home"))
        );
        List<String> extensions = new ArrayList<String>();
        extensions.add("*.jpg");
        extensions.add("*.png");
        extensions.add("*.bmp");
        fileChooser.getExtensionFilters().addAll(	                
                new FileChooser.ExtensionFilter("Image Files", extensions)
        );
        fileChooser.setInitialFileName("CarvedImage");
        
        File savedFile = fileChooser.showSaveDialog(_primaryStage);
        
        if (savedFile != null) {
        	saveFileRoutine(savedFile);
        }
	}

	private void saveFileRoutine(File savedFile) {
		
		BufferedImage image = SwingFXUtils.fromFXImage(_carvedImage, null);
		try {
			ImageIO.write(image, "png", savedFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
