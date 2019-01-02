import javafx.application.Application;
import javafx.stage.Stage;
import model.ImageSelectModel;

public class MainWindow extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		primaryStage.setTitle("Seam Carving");
		primaryStage.setWidth(900);
		primaryStage.setHeight(650);
		
		ImageSelectModel imageSelect = new ImageSelectModel();
		imageSelect.start(primaryStage);
		
	}

}
