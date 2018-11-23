package GUI;

import constants.Constants;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import wallet.Wallet;

public class MainView extends Application {

    public final static int HEIGHT = 700;
    public final static int WIDTH = 1000;
    Controller controller;
    Wallet wallet;
    private double xOffset = 0;
    private double yOffset = 0;


    private BotView botView;
    private MiddleView middleView;
    private TopView topView;
    private VBox vBox;


    @Override
    public void start(Stage stage) throws Exception {

        topView = new TopView();
        middleView = new MiddleView();
        middleView.setAlignment(Pos.CENTER);
        botView = new BotView();

        wallet = new Wallet(Constants.NODE_IP, Constants.NODE_PORT);


        vBox = new VBox(5);
        vBox.setPadding(new Insets(1));
        vBox.setAlignment(Pos.CENTER);


        vBox.getChildren().add(topView);
        vBox.getChildren().add(middleView);
        vBox.getChildren().add(botView);

        vBox.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ESCAPE) {
                    System.exit(0);
                }
            }
        });


        vBox.setOnMousePressed(new ListenForMouse());

        //move around here
        vBox.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                stage.setX(event.getScreenX() - xOffset);
                stage.setY(event.getScreenY() - yOffset);


            }
        });

        javafx.scene.image.Image image = new javafx.scene.image.Image(getClass().getResource("back3.png").toExternalForm());

        BackgroundImage myBI = new BackgroundImage(image,
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);
        vBox.setBackground(new Background(myBI));

        //vBox.setStyle("-fx-background-image: url(\"back3.png\");");


        Scene scene = new Scene(vBox, WIDTH, HEIGHT);


        scene.setFill(null);


        stage.initStyle(StageStyle.UNDECORATED);


        controller = new Controller(this, wallet);


        stage.setScene(scene);
        stage.setTitle("BlockChain 0.1");
        stage.show();


    }


    public void setOnMousePressed(EventHandler<MouseEvent> mouseEventEventHandler) {
        vBox.setOnMousePressed(mouseEventEventHandler);
    }

    public double getxOffset() {
        return xOffset;
    }

    public void setxOffset(double xOffset) {
        this.xOffset = xOffset;
    }

    public double getyOffset() {
        return yOffset;
    }

    public void setyOffset(double yOffset) {
        this.yOffset = yOffset;
    }

    public void setScreenVisible(String name) {
        middleView.setScreenVisible(name);
    }

    public String getCreateNewWalletWalletName() {
        return middleView.getCreateNewWalletWalletName();
    }

    public String getCreateNewWalletPassword() throws Exception {
        return middleView.getCreateNewWalletPassword();
    }

    public String getCreateNewWalletPassword2() throws Exception {
        return middleView.getCreateNewWalletPassword2();
    }

    public void setGenerateWalletButton(EventHandler<ActionEvent> actionEventEventHandler) throws Exception {
        middleView.setGenerateWalletButton(actionEventEventHandler);

    }

    public void setLoginUsingPrivKey(EventHandler<ActionEvent> actionEventEventHandler) {
        middleView.setLoginUsingPrivKey(actionEventEventHandler);
    }

    public String getPasswordLoginUsingPrivKey() {
        return middleView.getPasswordLoginUsingPrivKey();
    }

    public String getLoginUsingPrivKeyPublicKey() {
        return middleView.getLoginUsingPrivKeyPublicKey();
    }

    public void setAmountWalletScreen(String amount) {
        middleView.setAmountWalletScreen(amount);
    }

    public void setWalletsLoginUsingSavedWallet(String[] tempWallets) {
        middleView.setWalletsLoginUsingSavedWallet(tempWallets);
    }

    public String getPasswordLoginUsingSavedWallet() throws Exception {
        return middleView.getPasswordLoginUsingSavedWallet();
    }

    public String getSelectedWalletLoginUsingSavedWallet() {
        return middleView.getSelectedWalletLoginUsingSavedWallet();
    }

    public void setLoginButtonLoginUsingSavedWallet(EventHandler<ActionEvent> actionEventEventHandler) {
        middleView.setLoginButtonLoginUsingSavedWallet(actionEventEventHandler);
    }

    public void setSendButtonWalletScreen(EventHandler<ActionEvent> actionEventEventHandler) {
        middleView.setSendButtonWalletScreen(actionEventEventHandler);
    }

    public void setReciveButtonWalletScreen(EventHandler<ActionEvent> actionEventEventHandler) {
        middleView.setReciveButtonWalletScreen(actionEventEventHandler);
    }

    public String getSendToWalletScreen() {
        return middleView.getSendToWalletScreen();
    }

    public void setGenerateKeys(EventHandler<ActionEvent> ac) {
        middleView.setGenerateKeys(ac);
    }

    private class ListenForMouse implements EventHandler<MouseEvent> {


        @Override
        public void handle(MouseEvent event) {
            try {
                setxOffset(event.getSceneX());
                setyOffset(event.getSceneY());
            } catch (Exception e) {
                e.printStackTrace();
            }
            //System.out.println(a.getText());


        }
    }


}
