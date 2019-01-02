package model;

import java.io.IOException;

import controller.CarvingController;
import controller.SummaryController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class SummaryModel extends CustomModel {

	private Image _originalImage, _carvedImage;
	
	
	public SummaryModel(Image originalImage, Image carvedImage) {
		super();
		_originalImage = originalImage;
		_carvedImage = carvedImage;
	}
	
	@Override
	public void start(Stage primaryStage) {
		
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/SummaryView.fxml"));
		
		Parent root = null;
		try {
			root = (Parent) fxmlLoader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Scene scene = new Scene(root);
		
		primaryStage.setScene(scene);
		
		SummaryController controller = fxmlLoader.<SummaryController>getController();
		controller.setImages(_originalImage, _carvedImage);
		controller.init(primaryStage);
	}

}
