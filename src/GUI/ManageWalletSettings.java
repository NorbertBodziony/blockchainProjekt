package GUI;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class ManageWalletSettings extends GridPane {

    Button login, home;



    Label text;
    ChoiceBox<String> wallets;
    PasswordField passwordField;
    PasswordField repeatPassword;
    String name;
    public String getName() {
        return name;
    }

    public ManageWalletSettings()
    {
        name = new String("ManageWalletSettings");
        text = new Label("Choose a passphrase to encrypt your private key: ");
        //wallets = new ChoiceBox<>();
        //wallets.setPrefWidth(100);

        passwordField = new PasswordField();
        repeatPassword = new PasswordField();



       /* wallets.getItems().setAll("Wallet 1", "Wallet 2", "Wallet 3");
        wallets.getSelectionModel().selectFirst();
*/

        text.setFont(Font.font("Tahoma", FontWeight.EXTRA_BOLD, 25));
        // passwordField



        login = new Button("Generate wallet");
        home = new Button("Home");

        home.setOnAction(e->
        {
            System.out.println("Hello");
        });

        this.add(text,1,0);

        this.add(passwordField,1,1,2,1);
        this.add(repeatPassword,1,2,2,1);

        HBox hBox = new HBox();
        hBox.getChildren().addAll(login,home);
        hBox.setSpacing(20);
        this.add(hBox,1,3,2,1);


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

}
