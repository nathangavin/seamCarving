package controller;

import javafx.stage.Stage;

public abstract class CustomController {

	protected Stage _primaryStage;
	
	public void init(Stage primaryStage) {
		_primaryStage = primaryStage;
		_primaryStage.show();
		start();
	}
	
	protected abstract void start();
	
}
