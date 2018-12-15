package controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.CarvingModel;
import model.CustomModel;

public class ImageSelectController extends CustomController {

	@FXML private Button selectImageButton;
	@FXML private Button beginCarvingButton;
	@FXML private ImageView originalImageView;
	@FXML private Label originalSizeLabel;
	@FXML private Label newSizeLabel;
	@FXML private Label originalHeightLabel;
	@FXML private Label originalWidthLabel;
	@FXML private Label newHeightLabel;
	@FXML private Label newWidthLabel;
	@FXML private TextField originalHeightValue;
	@FXML private TextField originalWidthValue;
	@FXML private TextField newHeightInput;
	@FXML private TextField newWidthInput;
	@FXML private Label newHeightPx;
	@FXML private Label newWidthPx;
	
	private FileChooser _fileChooser;
	private Image _chosenImage;
	
	protected void start() {
		_fileChooser = new FileChooser();
		configureFileChooser(_fileChooser);
	}

    private void configureFileChooser(FileChooser fileChooser) {
        fileChooser.setTitle("Select Image");
        fileChooser.setInitialDirectory(
                new File(System.getProperty("user.home"))
        );
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("JPEG", "*.jpeg"),
                new FileChooser.ExtensionFilter("PNG", "*.png"),
                new FileChooser.ExtensionFilter("bitmap", "*.bmp")
        );

    }
	
	@FXML protected void handleSelectImage(ActionEvent event) {
		File file = _fileChooser.showOpenDialog(_primaryStage);
        if (file != null) {
            openFile(file);
        }
	}
	
    private void openFile(File file) {
        FileInputStream is = null;
        try {
            is = new FileInputStream(file.getAbsoluteFile());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        _chosenImage = new Image(is);
        originalImageView.setImage(_chosenImage);
        populateOriginalDimensions(_chosenImage);
    }
	
    private void populateOriginalDimensions(Image image) {
    	originalHeightValue.setText("" + (int)image.getHeight() + "px");
    	originalWidthValue.setText("" + (int)image.getWidth() + "px");
    	
    	newHeightInput.setText("" + (int)image.getHeight());
    	newWidthInput.setText("" + (int)image.getWidth());
    }
    
	@FXML protected void handleBeginCarving(ActionEvent event) {
		
		String height = newHeightInput.getText();
		String width = newWidthInput.getText();
		
		if (_chosenImage == null) {
			
			showAlert("Please select an image.");
			
		} else if (!isValidInput(height) || !isValidInput(width)) {
			
			showAlert("Please enter valid new dimensions.");
			
		} else {
			
			int heightValue = Integer.parseInt(height);
			int widthValue = Integer.parseInt(width);
			
			if (heightValue > (int) _chosenImage.getHeight() ||
					widthValue > (int) _chosenImage.getWidth()) {
				
				showAlert("Please enter values lower than original dimensions.");
				
			} else if (heightValue < (int) _chosenImage.getHeight() ||
					widthValue < (int) _chosenImage.getWidth()) {
				
				beginCarving(_primaryStage, _chosenImage, widthValue, heightValue);
				
			} else {
				
				showAlert("Please enter values lower than original dimensions.");
				
			}
		}
	}

	private void beginCarving(Stage primaryStage, Image chosenImage, int newWidth, int newHeight) {
		CustomModel carvingModel = new CarvingModel(chosenImage, newWidth, newHeight);
		carvingModel.start(primaryStage);
		
	}

	private boolean isValidInput(String input) {
		
		if (input == null ||
				"".equals(input) ||
				input.length() == 0 ||
				!input.matches("[0-9]+")) {
			return false;
		}
		
		int value = Integer.parseInt(input);
		
		if (value == 0) {
			return false;
		}
		
		return true;
	}

	private void showAlert(String bodyText) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Information");
		alert.setHeaderText(null);
		alert.setContentText(bodyText);
		alert.showAndWait();
	}
	
}
