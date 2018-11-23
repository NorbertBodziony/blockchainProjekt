package GUI;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class LoginUsingPrivKey extends GridPane {

    Button login, home;
    Label text;
    ChoiceBox<String> wallets;
    TextField publicKey;
    PasswordField passwordField;


    public LoginUsingPrivKey() {
        text = new Label("Login using an private key: ");


        publicKey = new TextField();
        Tooltip tooltipPublicKey = new Tooltip("Type publick key here: ");
        publicKey.setTooltip(tooltipPublicKey);

        Tooltip tooltipPassword = new Tooltip("Type private key here: ");

        passwordField = new PasswordField();
        passwordField.setTooltip(tooltipPassword);
        //passwordField.setStyle("-fx-background-color: #53f442; ");


        text.setFont(Font.font("Tahoma", FontWeight.EXTRA_BOLD, 25));
        text.setTextFill(Color.WHITE);


        login = new Button("Login");
        home = new Button("Home");

        home.setOnAction(e ->
        {
            System.out.println("Hello");
        });

        this.add(text, 1, 0);

        this.add(publicKey, 1, 1, 2, 1);
        this.add(passwordField, 1, 2, 2, 1);


        HBox hBox = new HBox();
        hBox.getChildren().addAll(login, home);
        hBox.setSpacing(20);
        this.add(hBox, 1, 3, 2, 1);


        this.setVgap(5);
        this.setHgap(5);
        this.setVisible(false);


    }


    public void setHomeButton(EventHandler<ActionEvent> actionEventEventHandler) {
        home.setOnAction(actionEventEventHandler);
    }

    public void setLoginButton(EventHandler<ActionEvent> actionEventEventHandler) {
        login.setOnAction(actionEventEventHandler);
    }

    public String getPassword() throws Exception {
        if (passwordField.getText().isEmpty()) {
            throw new Exception("Password is empty");

        } else {
            return passwordField.getText();
        }
    }

    public String getPublicKey() {
        return publicKey.getText();
    }

}
