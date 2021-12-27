package agh.ics.oop.evolutiongenerator.gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import agh.ics.oop.evolutiongenerator.*;

import java.io.FileNotFoundException;

public class App extends Application implements IPositionChangeObserver {
    private WorldMap map;
    private GridPane mapGridPane;
    private SimulationEngine engine;
    private int numOfAnimals;
    private int height;
    private int width;
    private Scene primaryScene;
    private final int moveDelay = 500;

    @Override
    public void start(Stage primaryStage) {
        TextField numOfAnimalsInput = new TextField("2");
        Label numOfAnimalsLabel = new Label("Number of animals: ");
        HBox numOfAnimalsInputHBox = new HBox(numOfAnimalsLabel, numOfAnimalsInput);
        numOfAnimalsInputHBox.setAlignment(Pos.CENTER);

        TextField widthInput = new TextField("6");
        Label widthInputLabel = new Label("Width: ");
        HBox widthInputHBox = new HBox(widthInputLabel, widthInput);
        widthInputHBox.setAlignment(Pos.CENTER);

        TextField heightInput = new TextField("6");
        Label heightInputLabel = new Label("Height: ");
        HBox heightInputHBox = new HBox(heightInputLabel, heightInput);
        heightInputHBox.setAlignment(Pos.CENTER);

        TextField energyInput = new TextField("7");
        Label energyInputLabel = new Label("Animal's energy: ");
        HBox energyInputHBox = new HBox(energyInputLabel, energyInput);
        energyInputHBox.setAlignment(Pos.CENTER);

        Button startSimulation = new Button("Start");
        VBox inputVBox = new VBox(numOfAnimalsInputHBox, widthInputHBox, heightInputHBox, energyInputHBox, startSimulation);
        VBox.setMargin(inputVBox, new Insets(15, 0, 0, 0));
        inputVBox.setSpacing(10);
        inputVBox.setAlignment(Pos.CENTER);

        primaryStage.setTitle("World map");

        Scene initialScene = new Scene(inputVBox, 800, 500);
        primaryStage.setScene(initialScene);
        primaryStage.show();

        startSimulation.setOnAction(event -> {
            this.numOfAnimals = Integer.parseInt(numOfAnimalsInput.getText());
            this.width = Integer.parseInt(widthInput.getText());
            this.height = Integer.parseInt(heightInput.getText());
            int energy = Integer.parseInt(energyInput.getText());

            this.map = new WorldMap(this.width, this.height);
            this.engine = new SimulationEngine(this.map, this.numOfAnimals, this.width, this.height, energy);
            this.engine.addObserver(this);
            this.engine.changeMoveDelay(this.moveDelay);
            this.mapGridPane = new GridPane();

            VBox appWindow = new VBox(this.mapGridPane);
            appWindow.setAlignment(Pos.CENTER);
            this.mapGridPane.setAlignment(Pos.CENTER);
            this.primaryScene = new Scene(appWindow, 1000, 800);
            primaryStage.setScene(this.primaryScene);

            for (int i = 0; i < this.width; i++) {
                ColumnConstraints columnConstraints = new ColumnConstraints(65);
                this.mapGridPane.getColumnConstraints().add(columnConstraints);
            }

            for (int i = 0; i < this.height; i++) {
                RowConstraints rowConstraints = new RowConstraints(65);
                this.mapGridPane.getRowConstraints().add(rowConstraints);
            }

            try {
                this.drawCurrMap();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            Thread engineThread = new Thread(this.engine);
            engineThread.start();
        });
        primaryStage.setOnCloseRequest(e -> {
            Platform.exit();
            System.exit(0);
        });
    }

    public void drawCurrMap() throws FileNotFoundException {
        this.mapGridPane.setGridLinesVisible(false);
        this.mapGridPane.setGridLinesVisible(true);

        for (int i = 0; i < this.height; i++) {
            for (int j = 0; j < this.width; j++) {
                IMapElement elem = (IMapElement) this.map.objectAt(new Vector2d(j, i));
                if (elem != null) {
                    try {
                        VBox vBox;
                        vBox = new GuiElementBox(elem).createImage();
                        GridPane.setConstraints(vBox, j, this.height - i - 1);
                        GridPane.setHalignment(vBox, HPos.CENTER);
                        this.mapGridPane.add(vBox, j, this.height - i - 1);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                } else {
                    Label label = new Label();
                    GridPane.setConstraints(label, j, this.height - i - 1);
                    GridPane.setHalignment(label, HPos.CENTER);
                    this.mapGridPane.add(label, j, this.height - i - 1);
                }
            }
        }
    }

    @Override
    public void positionChanged(Animal animal, Vector2d oldPosition, Vector2d newPosition) {
        Platform.runLater(() -> {
            this.mapGridPane.getChildren().clear();
            try {
                this.drawCurrMap();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        });
    }

    public static void main(String[] args) {
        Application.launch(App.class, args);
    }
}