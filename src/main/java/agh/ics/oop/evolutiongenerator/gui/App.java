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
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import agh.ics.oop.evolutiongenerator.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.LinkedHashMap;
import java.util.Map;

public class App extends Application implements IPositionChangeObserver {
    private final int moveDelay = 1000;
    private final int cellSize = 40;
    private final Map<String, Image> images = new LinkedHashMap<>();
    private WorldMap boundedMap;
    private WorldMap unboundedMap;
    private GridPane unboundedMapGridPane;
    private GridPane boundedMapGridPane;
    private SimulationEngine boundedMapEngine;
    private SimulationEngine unboundedMapEngine;
    private int numOfAnimals;
    private int height;
    private int width;
    private int startEnergy;
    private int moveEnergy;
    private int plantEnergy;
    private double jungleRatio;
    private Vector2d jungleLowerLeft;
    private Vector2d jungleUpperRight;
    private Scene scene;

    @Override
    public void init() {
        try {
            images.put("src/main/resources/up.png",
                    new Image(new FileInputStream("src/main/resources/up.png"), 20, 20, false, false));
            images.put("src/main/resources/up_right.png",
                    new Image(new FileInputStream("src/main/resources/up_right.png"), 20, 20, false, false));
            images.put("src/main/resources/right.png",
                    new Image(new FileInputStream("src/main/resources/right.png"), 20, 20, false, false));
            images.put("src/main/resources/down_right.png",
                    new Image(new FileInputStream("src/main/resources/down_right.png"), 20, 20, false, false));
            images.put("src/main/resources/down.png",
                    new Image(new FileInputStream("src/main/resources/down.png"), 20, 20, false, false));
            images.put("src/main/resources/down_left.png",
                    new Image(new FileInputStream("src/main/resources/down_left.png"), 20, 20, false, false));
            images.put("src/main/resources/left.png",
                    new Image(new FileInputStream("src/main/resources/left.png"), 20, 20, false, false));
            images.put("src/main/resources/up_left.png",
                    new Image(new FileInputStream("src/main/resources/up_left.png"), 20, 20, false, false));
            images.put("src/main/resources/grass.png",
                    new Image(new FileInputStream("src/main/resources/grass.png"), 20, 20, false, false));

        } catch (FileNotFoundException e) {
            System.out.println("File hasn't been found! " + e);
        }
    }

    @Override
    public void start(Stage primaryStage) {
        TextField numOfAnimalsInput = new TextField("15");
        Label numOfAnimalsLabel = new Label("Number of animals: ");
        HBox numOfAnimalsInputHBox = new HBox(numOfAnimalsLabel, numOfAnimalsInput);
        numOfAnimalsInputHBox.setAlignment(Pos.CENTER);

        TextField widthInput = new TextField("15");
        Label widthInputLabel = new Label("Width: ");
        HBox widthInputHBox = new HBox(widthInputLabel, widthInput);
        widthInputHBox.setAlignment(Pos.CENTER);

        TextField heightInput = new TextField("15");
        Label heightInputLabel = new Label("Height: ");
        HBox heightInputHBox = new HBox(heightInputLabel, heightInput);
        heightInputHBox.setAlignment(Pos.CENTER);

        TextField startEnergyInput = new TextField("40");
        Label startEnergyInputLabel = new Label("Animal's start energy: ");
        HBox startEnergyInputHBox = new HBox(startEnergyInputLabel, startEnergyInput);
        startEnergyInputHBox.setAlignment(Pos.CENTER);

        TextField moveEnergyInput = new TextField("2");
        Label moveEnergyInputLabel = new Label("Animal's move energy: ");
        HBox moveEnergyInputHBox = new HBox(moveEnergyInputLabel, moveEnergyInput);
        moveEnergyInputHBox.setAlignment(Pos.CENTER);

        TextField plantEnergyInput = new TextField("30");
        Label plantEnergyInputLabel = new Label("Plant's energy: ");
        HBox plantEnergyInputHBox = new HBox(plantEnergyInputLabel, plantEnergyInput);
        plantEnergyInputHBox.setAlignment(Pos.CENTER);

        TextField jungleRatioInput = new TextField("0.2");
        Label jungleRatioLabel = new Label("Jungle ratio: ");
        HBox jungleRatioHBox = new HBox(jungleRatioLabel, jungleRatioInput);
        jungleRatioHBox.setAlignment(Pos.CENTER);

        Button startSimulation = new Button("Start");
        VBox inputVBox = new VBox(numOfAnimalsInputHBox, widthInputHBox, heightInputHBox, startEnergyInputHBox,
                moveEnergyInputHBox, plantEnergyInputHBox, jungleRatioHBox, startSimulation);
        VBox.setMargin(inputVBox, new Insets(15, 0, 0, 0));
        inputVBox.setSpacing(10);
        inputVBox.setAlignment(Pos.CENTER);

        primaryStage.setTitle("World map");

        this.scene = new Scene(inputVBox, 1800, 1000);
        primaryStage.setScene(this.scene);
        primaryStage.show();

        startSimulation.setOnAction(event -> {
            try {
                this.numOfAnimals = Integer.parseInt(numOfAnimalsInput.getText());
                this.width = Integer.parseInt(widthInput.getText());
                this.height = Integer.parseInt(heightInput.getText());
                this.startEnergy = Integer.parseInt(startEnergyInput.getText());
                this.moveEnergy = Integer.parseInt(moveEnergyInput.getText());
                this.plantEnergy = Integer.parseInt(plantEnergyInput.getText());
                this.jungleRatio = Double.parseDouble(jungleRatioInput.getText());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input: all input values must be numbers! " + e);
            }

            this.unboundedMapGridPane = new GridPane();
            this.boundedMapGridPane = new GridPane();
            HBox appWindow = new HBox(this.unboundedMapGridPane, this.boundedMapGridPane);
            appWindow.setSpacing(50);
            appWindow.setAlignment(Pos.CENTER);
            this.unboundedMapGridPane.setAlignment(Pos.CENTER);
            this.boundedMapGridPane.setAlignment(Pos.CENTER);
            this.scene = new Scene(appWindow, 1400, 1000);
            primaryStage.setScene(this.scene);

            int jungleWidth = (int) (this.width * this.jungleRatio);
            int jungleHeight = (int) (this.height * this.jungleRatio);
            this.jungleLowerLeft = new Vector2d((this.width - jungleWidth) / 2,
                    (this.height - jungleHeight) / 2);
            this.jungleUpperRight = new Vector2d(this.jungleLowerLeft.x + jungleWidth,
                    this.jungleLowerLeft.y + jungleHeight);

            for (int i = 0; i < this.width; i++) {
                ColumnConstraints columnConstraints = new ColumnConstraints(this.cellSize);
                this.unboundedMapGridPane.getColumnConstraints().add(columnConstraints);
                this.boundedMapGridPane.getColumnConstraints().add(columnConstraints);
            }

            for (int i = 0; i < this.height; i++) {
                RowConstraints rowConstraints = new RowConstraints(this.cellSize);
                this.unboundedMapGridPane.getRowConstraints().add(rowConstraints);
                this.boundedMapGridPane.getRowConstraints().add(rowConstraints);
            }
            this.boundedMap = new WorldMap(false, this.unboundedMapGridPane, this.width, this.height,
                    this.startEnergy, this.moveEnergy, this.plantEnergy, jungleWidth, jungleHeight,
                    this.jungleLowerLeft, this.jungleUpperRight);
            this.unboundedMap = new WorldMap(true, this.boundedMapGridPane, this.width, this.height,
                    this.startEnergy, this.moveEnergy, this.plantEnergy, jungleWidth, jungleHeight,
                    this.jungleLowerLeft, this.jungleUpperRight);

            this.boundedMapEngine = new SimulationEngine(this.boundedMap, this.numOfAnimals, this.width, this.height,
                    this.startEnergy);
            this.boundedMapEngine.addObserver(this);
            this.boundedMapEngine.setMoveDelay(this.moveDelay);

            this.unboundedMapEngine = new SimulationEngine(this.unboundedMap, this.numOfAnimals, this.width,
                    this.height, this.startEnergy);
            this.unboundedMapEngine.addObserver(this);
            this.unboundedMapEngine.setMoveDelay(this.moveDelay);

            Thread boundedMapEngine = new Thread(this.boundedMapEngine);
            boundedMapEngine.start();

            Thread unboundedMapEngine = new Thread(this.unboundedMapEngine);
            unboundedMapEngine.start();
        });
        primaryStage.setOnCloseRequest(e -> {
            Platform.exit();
            System.exit(0);
        });
    }

    public void drawCurrMap(WorldMap map, GridPane gridPane) throws FileNotFoundException {
        gridPane.setGridLinesVisible(false);
        gridPane.setGridLinesVisible(true);

        for (int i = 0; i < this.height; i++) {
            for (int j = 0; j < this.width; j++) {
                IMapElement elem = (IMapElement) map.objectAt(new Vector2d(j, i));
                if (elem != null) {
                    VBox vBox;
                    vBox = new GuiElementBox(elem).createImage(this.images.get(elem.getImageSource()));
                    GridPane.setConstraints(vBox, j, this.height - i - 1);
                    GridPane.setHalignment(vBox, HPos.CENTER);
                    gridPane.add(vBox, j, this.height - i - 1);
                } else {
                    Label label = new Label();
                    label.setMinWidth(this.cellSize);
                    label.setMinHeight(this.cellSize);
                    if (this.jungleLowerLeft.x <= j && j <= this.jungleUpperRight.x &&
                            this.jungleLowerLeft.y <= i && i <= this.jungleUpperRight.y) {
                        label.setStyle("-fx-background-color: #0f451d");
                    } else label.setStyle("-fx-background-color: #41c464");
                    GridPane.setConstraints(label, j, this.height - i - 1);
                    GridPane.setHalignment(label, HPos.CENTER);
                    gridPane.add(label, j, this.height - i - 1);
                }
            }
        }
    }

    @Override
    public void positionChanged(Animal animal, Vector2d oldPosition, Vector2d newPosition, WorldMap map,
                                GridPane gridPane) {
        Platform.runLater(() -> {
            gridPane.getChildren().clear();
            try {
                this.drawCurrMap(map, gridPane);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        });
    }

    public static void main(String[] args) {
        Application.launch(App.class, args);
    }
}