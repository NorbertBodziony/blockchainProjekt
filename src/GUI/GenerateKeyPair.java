package GUI;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.StageStyle;
import javafx.util.Pair;

import java.util.Optional;

public class GenerateKeyPair extends GridPane {

    Button  home;

    String publicKey ="a";
    String privateKey ="b";

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public GenerateKeyPair()
    {

        home = new Button("Home");

        home.setOnAction(e->
        {
            System.out.println("Hello");
        });

        this.add(home,1,0);

        //Optional<Pair<String,String>>recipientAndAmount =  new keysDialog().showAndWait();





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
    public class keysDialog extends Dialog<Pair<String, String>>
    {
        double xOffset;
        double yOffset;
        public keysDialog()
        {
            this.setTitle("Send");
            this.setHeaderText(null);
            this.setGraphic(null);
            this.initStyle(StageStyle.UNDECORATED);
            this.setResizable(true);
            //this.getDialogPane().setPrefSize(700,1000);



            ButtonType loginButtonType = new ButtonType("OK", ButtonBar.ButtonData.FINISH);


            this.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);




            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20, 150, 10, 10));

            TextField PublicKey = new TextField();
            PublicKey.appendText(publicKey);
            PublicKey.setEditable(false);
            TextField recipent = new TextField();
            recipent.appendText(privateKey);
            recipent.setEditable(false);

            grid.add(new Label("Public Key:"), 0, 0);
            grid.add(PublicKey, 1, 0);
            grid.add(new Label("Private Key:"), 0, 1);
            grid.add(recipent, 1, 1);

            Node loginButton = this.getDialogPane().lookupButton(loginButtonType);
            loginButton.setDisable(true);

            PublicKey.textProperty().addListener((observable, oldValue, newValue) -> {
                loginButton.setDisable(newValue.trim().isEmpty());
            });

            this.getDialogPane().setContent(grid);

            Platform.runLater(() -> PublicKey.requestFocus());


            this.setResultConverter(dialogButton -> {
                if (dialogButton == loginButtonType) {
                    return new Pair<>(PublicKey.getText(), recipent.getText());
                }
                return null;
            });




            BackgroundImage myBI= new BackgroundImage(new Image("back3.png",this.getWidth(),this.getWidth(),false,true),
                    BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                    BackgroundSize.DEFAULT);
            this.getDialogPane().setBackground(new Background(myBI));




            this.getDialogPane().setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    try {
                        xOffset = event.getSceneX();
                        yOffset = event.getSceneY();
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                }
            });



            //move around here
            this.getDialogPane().setOnMouseDragged(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    setX(event.getScreenX() - xOffset);
                    setY(event.getScreenY() - yOffset);



                }
            });


        }


}}
