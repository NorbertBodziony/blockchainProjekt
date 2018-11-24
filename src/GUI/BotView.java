package GUI;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;


public class BotView extends BorderPane {
    public final static int HEIGHT = 50;
    Label dontation;

    public BotView() {
        dontation = new Label("Donation via: 1F1tAaz5x1HUXrCNLbtMDqcw6o5GNn4xqX");
        dontation.setTextFill(Color.WHITE);

        this.setPadding(new Insets(10));
        this.setPrefSize(MainView.WIDTH, HEIGHT);
        this.setCenter(dontation);
    }
}
