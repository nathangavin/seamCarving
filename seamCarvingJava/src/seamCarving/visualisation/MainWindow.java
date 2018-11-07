package seamCarving.visualisation;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class MainWindow extends Application {

    private Stage _primaryStage;
    private Scene _primaryScene;
    private GridPane _grid;
    private Button _fileSelectButton;

    private SelectedImageScene _selectedImageScene;

    private Image _chosenImage;

    @Override
    public void start(Stage primaryStage) throws Exception {

        _primaryStage = primaryStage;
        _primaryStage.setTitle("Seam Carving");
        _primaryStage.setMinWidth(300);

        _grid = new GridPane();
        _grid.setAlignment(Pos.TOP_CENTER);
        _grid.setVgap(10);
        _grid.setHgap(10);
        _grid.setPadding(new Insets(5,5,5,5));

        initialiseElements();
        addElementsToGrid();

        _primaryScene = new Scene(_grid);
        _primaryStage.setScene(_primaryScene);

        _primaryStage.show();
    }

    private void initialiseElements() {
        _fileSelectButton = new Button("Select Image");

        FileChooser fileChooser = new FileChooser();

        _fileSelectButton.setOnAction(
                event -> {
                    configureFileChooser(fileChooser);
                    File file = fileChooser.showOpenDialog(_primaryStage);
                    if (file != null) {
                        openFile(file);
                    }
                }
        );
    }

    private void addElementsToGrid() {
        _grid.getChildren().clear();
        _grid.add(_fileSelectButton, 0, 0, 1, 1);
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

    private void openFile(File file) {

        try {
            _chosenImage = ImageIO.read(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
