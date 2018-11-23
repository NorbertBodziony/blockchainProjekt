package GUI;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class ManageWalletSettings extends GridPane {
    Button home;

    Label text;


    public ManageWalletSettings() {
        text = new Label("Manage your wallets: ");
        text.setTextFill(Color.WHITE);
        text.setFont(Font.font("Tahoma", FontWeight.EXTRA_BOLD, 25));
        home = new Button("Home");
        home.setOnAction(e ->
        {
            System.out.println("Hello");
        });

        this.add(text, 1, 0);

        this.add(home, 1, 1);
        for (String a : new Utility().getListOfWallets()) {
            System.out.println(a);
        }
        this.setVgap(5);
        this.setHgap(5);
        this.setVisible(false);


    }


    public void setHomeButton(EventHandler<ActionEvent> actionEventEventHandler) {
        home.setOnAction(actionEventEventHandler);
    }


}
