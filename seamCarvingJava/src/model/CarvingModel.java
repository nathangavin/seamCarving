package model;

import java.io.IOException;

import controller.CarvingController;
import controller.CustomController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class CarvingModel extends CustomModel {
	
	private Image _image;
	private int _newWidth;
	private int _newHeight;
	
	public CarvingModel(Image image, int newWidth, int newHeight) {
		super();
		_image = image;
		_newHeight = newHeight;
		_newWidth = newWidth;
	}

	@Override
	public void start(Stage primaryStage) {
		
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/CarvingView.fxml"));
		
		Parent root = null;
		try {
			root = (Parent) fxmlLoader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Scene scene = new Scene(root);
		
		primaryStage.setScene(scene);
		
		CarvingController controller = fxmlLoader.<CarvingController>getController();
		controller.setImageAndHeightAndWidth(_image, _newHeight, _newWidth);
		controller.init(primaryStage);

	}

}
