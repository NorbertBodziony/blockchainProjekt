package GUI;

import constants.Constants;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import wallet.Wallet;

public class MainView extends Application {

    public final static int HEIGHT = 700;
    public final static int WIDTH = 1000;
    private Controller controller;
    private Wallet wallet;

    private BotView botView;
    private MiddleView middleView;
    private TopView topView;
    private VBox vBox;

    private double xOffset = 0;
    private double yOffset = 0;

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


        vBox.setOnMousePressed(e ->
        {
            this.setxOffset(e.getSceneX());
            this.setyOffset(e.getSceneY());

        });


        vBox.setOnMouseDragged(e ->
        {
            stage.setX(e.getScreenX() - xOffset);
            stage.setY(e.getScreenY() - yOffset);
        });

        javafx.scene.image.Image image = new javafx.scene.image.Image(getClass().getResource("back3.png").toExternalForm());

        BackgroundImage myBI = new BackgroundImage(image,
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);
        vBox.setBackground(new Background(myBI));


        Scene scene = new Scene(vBox, WIDTH, HEIGHT);


        scene.setFill(null);


        stage.initStyle(StageStyle.UNDECORATED);


        controller = new Controller(this, wallet);


        stage.setScene(scene);
        stage.setTitle("Oxygen Network");
        stage.show();


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

    public void setGenerateKeys(EventHandler<ActionEvent> ac) {
        middleView.setGenerateKeys(ac);
    }


    public void setWalletsManageSetting(String[] tempWallets) {
        middleView.setWalletsManageSetting(tempWallets);
    }

    public String getSelectedWalletManageSetting() {
        return middleView.getSelectedWalletManageSetting();
    }

    public void setDeleteButtonManageSetting(EventHandler<ActionEvent> actionEventEventHandler) {
        middleView.setDeleteButtonManageSetting(actionEventEventHandler);
    }

    public void setViewButtonManageSetting(EventHandler<ActionEvent> actionEventEventHandler) {
        middleView.setViewButtonManageSetting(actionEventEventHandler);
    }

    public String getPasswordManageSetting() throws Exception {
        return middleView.getPasswordManageSetting();
    }
    public void setRefreshWalletScree(EventHandler<ActionEvent> actionEventEventHandler) {
        middleView.setRefreshWalletScree(actionEventEventHandler);
    }
    public void setLabelAmountWalletScreen(String am)
    {
        middleView.setLabelAmount(am);
    }
    public void setRefreshHistoryWaletScreen(EventHandler<ActionEvent> actionEventEventHandler) {
        middleView.setRefreshHistoryWaletScreen(actionEventEventHandler);
    }

    public void setInTextAreaWalletScreen(String in)
    {
        middleView.setInTextAreaWalletScreen(in);
    }
    public void setOutTextAreaWalletScreen(String in)
    {
        middleView.setOutTextAreaWalletScreen(in);
    }
    public String getStartTimeWalletString()
    {
        return middleView.getStartTimeWalletString();
    }
    public String getStopTimeWalletScreen()
    {
        return middleView.getStopTimeWalletScreen();
    }
    public void setInTextAreaClearWalletScreen()
    {
        middleView.setInTextAreaClearWalletScreen();
    }
    public void setOutTextAreaClearWalletScreen()
    {
        middleView.setOutTextAreaClearWalletScreen();
    }
    public String getSelectedRadioButtonWalletScreen()
    {
        return middleView.getSelectedRadioButtonWalletScreen();
    }
    public String getSpecAccontWalletScreen()
    {
        return middleView.getSpecAccontWalletScreen();
    }



}
