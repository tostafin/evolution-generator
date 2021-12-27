package agh.ics.oop.evolutiongenerator.gui;

import agh.ics.oop.evolutiongenerator.IMapElement;
import agh.ics.oop.evolutiongenerator.Vector2d;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class GuiElementBox {
    public final String source;
    private final Vector2d pos;
    public GuiElementBox(IMapElement iMapElement) {
        this.source = iMapElement.getSource();
        this.pos = iMapElement.getPosition();
    }

    public VBox createImage() throws FileNotFoundException {
        Image image;
        try {
            image = new Image(new FileInputStream(this.source));
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("File hasn't been found!");
        }
        ImageView elemView = new ImageView(image);
        elemView.setFitHeight(20);
        elemView.setFitWidth(20);
        Label label;
        if (this.source.equals("src/main/resources/grass.png")) label = new Label("Trawa");
        else label = new Label("Z" + this.pos);

        VBox vBox = new VBox();
        vBox.getChildren().addAll(elemView, label);
        vBox.setAlignment(Pos.CENTER);
        return vBox;
    }
}