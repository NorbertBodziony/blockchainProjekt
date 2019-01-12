package GUI;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;


public class BotView extends BorderPane {
    public final static int HEIGHT = 50;
    Label dontation;

    public BotView() {
        dontation = new Label("Wykonali: Norbert Bodziony, Bartłomiej Smalec, Wojciech Cichocki");
        //dontation.setTextFill(Color.WHITE);

        this.setPadding(new Insets(10));
        this.setPrefSize(MainView.WIDTH, HEIGHT);
        this.setCenter(dontation);
    }
}
