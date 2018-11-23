package GUI;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class CreateANewWallet extends GridPane {

    Button generateWalletButton, home;


    Label text, walletLabel;
    //ChoiceBox<String> wallets;
    PasswordField passwordField;
    PasswordField repeatPassword;
    TextField walletName;


    public CreateANewWallet() {
        text = new Label("Choose a passphrase to encrypt your private key: ");
        text.setTextFill(Color.WHITE);
        walletLabel = new Label("Wallet Name: ");
        walletLabel.setTextFill(Color.WHITE);

        passwordField = new PasswordField();
        repeatPassword = new PasswordField();
        walletName = new TextField();

        text.setFont(Font.font("Tahoma", FontWeight.EXTRA_BOLD, 25));

        generateWalletButton = new Button("Generate wallet");
        home = new Button("Home");


        home.setOnAction(e ->
        {
            System.out.println("Hello");
        });

        this.add(text, 1, 0);

        this.add(passwordField, 1, 1, 2, 1);
        this.add(repeatPassword, 1, 2, 2, 1);
        this.add(walletLabel, 1, 3);
        this.add(walletName, 2, 3);

        HBox hBox = new HBox();
        hBox.getChildren().addAll(generateWalletButton, home);
        hBox.setSpacing(20);

        this.add(hBox, 1, 4, 2, 1);


        this.setVgap(5);
        this.setHgap(5);
        this.setVisible(false);


    }

    public Button getHome() {
        return home;
    }

    public void setHomeButton(EventHandler<ActionEvent> actionEventEventHandler) {
        home.setOnAction(actionEventEventHandler);
    }

    public void setGenerateWalletButton(EventHandler<ActionEvent> actionEventEventHandler) {
        generateWalletButton.setOnAction(actionEventEventHandler);
    }

    public String getWalletName() {
        return walletName.getText();
    }

    public String getPassword() throws Exception {
        if (passwordField.getText().isEmpty()) {
            throw new Exception("Password is empty");

        } else {
            return passwordField.getText();

        }
    }

    public String getPassword2() throws Exception {
        if (repeatPassword.getText().isEmpty()) {
            throw new Exception("Password is empty");

        } else {
            return repeatPassword.getText();
        }
    }

}
