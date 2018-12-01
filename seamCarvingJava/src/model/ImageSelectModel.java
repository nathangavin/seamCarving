package model;

import java.io.IOException;

import controller.ImageSelectController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ImageSelectModel extends CustomModel {

	@Override
	public void start(Stage primaryStage) {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/ImageSelectView.fxml"));
		
		Parent root = null;
		try {
			root = (Parent) fxmlLoader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Scene scene = new Scene(root);
		
		primaryStage.setTitle("Seam Carving");
		primaryStage.setScene(scene);
		
		ImageSelectController controller = fxmlLoader.<ImageSelectController>getController();
		controller.init(primaryStage);
	}

}
