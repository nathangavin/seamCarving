package seamCarving.gui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class MainWindow extends Application {

    private Stage _primaryStage;
    private Scene _primaryScene;
    private GridPane _grid;
    private Button _fileSelectButton;

    private Image _chosenImage;
    private SelectedImageScene _selectedImageScene;

    @Override
    public void start(Stage primaryStage) throws Exception {

        _primaryStage = primaryStage;
        _primaryStage.setTitle("Seam Carving");
        _primaryStage.setMinWidth(300);

        _grid = new GridPane();
        _grid.setAlignment(Pos.TOP_CENTER);
        _grid.setVgap(10);
        _grid.setHgap(10);
        _grid.setPadding(new Insets(5, 5, 5, 5));

        initialiseElements();
        addElementsToGrid();

        _primaryScene = new Scene(_grid);
        _primaryStage.setScene(_primaryScene);

        _primaryStage.show();
    }

    private void initialiseElements() {
        _fileSelectButton = new Button("Select Image");

        final FileChooser fileChooser = new FileChooser();

        _fileSelectButton.setOnAction(
                event -> {
                    configureFileChooser(fileChooser);
                    File file = fileChooser.showOpenDialog(_primaryStage);
                    if (file != null) {
                        openFile(file);
                        //SelectedImageScene selectedImageScene = new SelectedImageScene(_primaryStage, _chosenImage);
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

        System.out.println(file.getAbsolutePath());
        FileInputStream is = null;
        try {
            is = new FileInputStream(file.getAbsoluteFile());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        _chosenImage = new Image(is);
    }

}
