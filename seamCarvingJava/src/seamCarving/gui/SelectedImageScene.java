package seamCarving.gui;

import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class SelectedImageScene {

    Stage _stage;
    Scene _scene;
    GridPane _grid;
    ImageView _imageView;

    Image _image;

    public SelectedImageScene(Stage stage, Image image) {

        _stage = stage;
        _image = image;

        _grid = new GridPane();
        initialiseElements();
        populateGrid();
        _scene = new Scene(_grid);
        _stage.setScene(_scene);
    }

    private void initialiseElements() {

        _imageView = new ImageView(_image);
    }

    private void populateGrid() {
    }

}
