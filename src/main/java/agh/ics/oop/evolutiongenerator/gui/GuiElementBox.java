package agh.ics.oop.evolutiongenerator.gui;

import agh.ics.oop.evolutiongenerator.IMapElement;
import agh.ics.oop.evolutiongenerator.Vector2d;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class GuiElementBox {
    public final String source;
    private final Vector2d pos;
    private final int energy;
    public GuiElementBox(IMapElement iMapElement) {
        this.source = iMapElement.getSource();
        this.pos = iMapElement.getPosition();
        this.energy = iMapElement.getEnergy();
    }

    public VBox createImage(Image image) {
        ImageView elemView = new ImageView(image);
        Label nameAndPosLabel;
        VBox vBox = new VBox();
        double labelFontSize = 8.5;
        if (this.source.equals("src/main/resources/grass.png")) {
            nameAndPosLabel = new Label("Trawa");
            nameAndPosLabel.setFont(new Font(labelFontSize));
            vBox.getChildren().addAll(elemView, nameAndPosLabel);
        }
        else {
            nameAndPosLabel = new Label("Z" + this.pos);
            nameAndPosLabel.setFont(new Font(labelFontSize));
            Label energyLabel = new Label(String.valueOf(this.energy));
            energyLabel.setFont(new Font(labelFontSize));
            vBox.getChildren().addAll(elemView, nameAndPosLabel, energyLabel);
        }
        vBox.setAlignment(Pos.CENTER);
        return vBox;
    }
}