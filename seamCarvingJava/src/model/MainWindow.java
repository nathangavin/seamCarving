package model;

import controller.MainWindowController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainWindow extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/MainWindowView.fxml"));
		
		Parent root = (Parent) fxmlLoader.load();
		
		Scene scene = new Scene(root);
		
		primaryStage.setTitle("Seam Carving");
		primaryStage.setScene(scene);
		
		MainWindowController controller = fxmlLoader.<MainWindowController>getController();
		controller.init(primaryStage);
	}

}
