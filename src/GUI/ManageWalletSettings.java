package GUI;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class ManageWalletSettings extends GridPane {
    private Button home, deleteButton, viewButton;

    private Label text;

    private ChoiceBox<String> wallets;

    private PasswordField passwordField;

    private HBox hBox, hBox1;


    public ManageWalletSettings() {
        text = new Label("Manage your wallets: ");
        text.setTextFill(Color.WHITE);
        text.setFont(Font.font("Tahoma", FontWeight.EXTRA_BOLD, 25));


        home = new Button("Home");
        deleteButton = new Button("Delete");
        viewButton = new Button("View");
        passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setMinWidth(100);

        wallets = new ChoiceBox<>();
        wallets.setMinWidth(100);
        wallets.getSelectionModel().selectFirst();

        hBox = new HBox();
        hBox1 = new HBox();

        hBox.getChildren().addAll(wallets, deleteButton);
        hBox.setSpacing(5);
        hBox1.getChildren().addAll(passwordField, viewButton);
        hBox1.setSpacing(5);

        this.add(text, 1, 0);
       /* this.add(wallets, 1, 1);
        this.add(deleteButton, 2, 1);
        this.add(passwordField, 1, 2);
        this.add(viewButton, 2, 2);*/
        this.add(hBox, 1, 1);
        this.add(hBox1, 1, 2);
        this.add(home, 1, 3);


        for (String a : new Utility().getListOfWallets()) {
            System.out.println("Wallets: " + a);
        }
        this.setVgap(0);
        this.setHgap(0);
        this.setVisible(false);


    }

    public void setHomeButton(EventHandler<ActionEvent> actionEventEventHandler) {
        home.setOnAction(actionEventEventHandler);
    }

    public void setDeleteButton(EventHandler<ActionEvent> actionEventEventHandler) {
        deleteButton.setOnAction(actionEventEventHandler);
    }

    public void setViewButton(EventHandler<ActionEvent> actionEventEventHandler) {
        viewButton.setOnAction(actionEventEventHandler);
    }

    public String getPassword() throws Exception {
        if (passwordField.getText().isEmpty()) {
            throw new Exception("Password is empty");

        } else {
            return passwordField.getText();
        }
    }

    public void setWallets(String[] tempWallets) {
        wallets.getItems().setAll(tempWallets);
    }

    public String getSelectedWallet() {
        return wallets.getValue();
    }


}
