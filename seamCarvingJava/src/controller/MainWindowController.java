package controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class MainWindowController {

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
	
	private Stage _primaryStage;
	private FileChooser _fileChooser;
	private Image _chosenImage;
	
	public void init(Stage primaryStage) {
		_primaryStage = primaryStage;
		_primaryStage.show();
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
    }
	
	@FXML protected void handleBeginCarving(ActionEvent event) {
		
	}

}
