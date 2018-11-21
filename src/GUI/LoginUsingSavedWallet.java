package GUI;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class LoginUsingSavedWallet extends GridPane {

    Button login, home;


    String name;
    Label text;
    ChoiceBox<String> wallets;
    PasswordField passwordField;
    TextField publicKey;



    public LoginUsingSavedWallet()
    {
        text = new Label("Login using a saved wallet: ");
        text.setTextFill(Color.WHITE);
        wallets = new ChoiceBox<>();
        wallets.setPrefWidth(100);




        passwordField = new PasswordField();
        publicKey = new TextField();


        wallets.getItems().setAll("Wallet 1", "Wallet 2", "Wallet 3");
        wallets.getSelectionModel().selectFirst();
        //wallets.setStyle("-fx-background-color: #53f442; ");


        text.setFont(Font.font("Tahoma", FontWeight.EXTRA_BOLD, 25));
       // passwordField



        login = new Button("Login");
        home = new Button("Home");

        home.setOnAction(e->
        {
            System.out.println("Hello");
        });

        this.add(text,1,0);
        this.add(wallets,1,1);
        this.add(publicKey,1,2,2,1);
        this.add(passwordField,1,3,2,1);

        HBox hBox = new HBox();
        hBox.getChildren().addAll(login,home);
        hBox.setSpacing(20);
        this.add(hBox,1,4,2,1);


       /* this.add(login,1,3);
        this.add(home,2,3);*/

        this.setVgap(5);
        this.setHgap(5);
        this.setVisible(false);


    }

    public Button getHome() {
        return home;
    }

    public void setHomeButton(EventHandler<ActionEvent> actionEventEventHandler)
    {
        home.setOnAction(actionEventEventHandler);
    }
    public void setLoginButton(EventHandler<ActionEvent> actionEventEventHandler)
    {
        login.setOnAction(actionEventEventHandler);
    }
    public String getPassword() throws Exception
    {
        if(passwordField.getText().isEmpty())
        {
            throw new Exception("Password is empty");

        }
        else {
            return passwordField.getText();
        }
    }

    public void setWallets(String[] tempWallets)
    {
        wallets.getItems().addAll(tempWallets);
    }

    public String getSelectedWallet()
    {
        return wallets.getValue();
    }
}
