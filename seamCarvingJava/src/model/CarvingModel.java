package model;

import java.io.IOException;

import controller.CarvingController;
import controller.CustomController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class CarvingModel extends CustomModel {

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
		
		CustomController controller = fxmlLoader.<CarvingController>getController();
		controller.init(primaryStage);

	}

}
