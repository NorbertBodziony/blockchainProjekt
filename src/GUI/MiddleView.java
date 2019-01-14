//import com.sun.tools.javac.Main;
package GUI;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MiddleView extends StackPane {
    private Clipboard clipboard;
    private ClipboardContent content;

    private Button loginUsingWallet;
    private Button loginUsingPrivKey;
    private Button createWallet;
    private Button manageSetting;
    private Button generateKeys;
    private Button addCompany;

    private String publicKey = "null";
    private String privateKey = "null2";
    private GridPane mainPanel;
    private GridPane generateWallet;


    private LoginUsingSavedWallet loginUsingSavedWalled;
    private LoginUsingPrivKey loginUsingPrivateKey;
    private CreateANewWallet createANewWallet;
    private ManageWalletSettings manageWalletSettings;
    private WalletScreen walletScreen;

    private List<GridPane> listOfScreen;
    private List<Button> buttonList;

    private Map<String, GridPane> mapOfScreen;

    public MiddleView() {
        content = new ClipboardContent();
        clipboard = Clipboard.getSystemClipboard();

        listOfScreen = new ArrayList<>();
        buttonList = new ArrayList<>();
        mapOfScreen = new HashMap<>();

        mainPanel = new GridPane();


        loginUsingSavedWalled = new LoginUsingSavedWallet();
        loginUsingSavedWalled.setHomeButton(new homeButtonAction());

        loginUsingPrivateKey = new LoginUsingPrivKey();
        loginUsingPrivateKey.setHomeButton(new homeButtonAction());
        loginUsingPrivateKey.setLoginButton(new loginButtonAction());


        createANewWallet = new CreateANewWallet();
        createANewWallet.setHomeButton(new homeButtonAction());

        manageWalletSettings = new ManageWalletSettings();
        manageWalletSettings.setHomeButton(new homeButtonAction());

        walletScreen = new WalletScreen();
        walletScreen.setHomeButton(new homeButtonAction());


        loginUsingWallet = new Button("Login using a saved wallet");
        loginUsingPrivKey = new Button("Login using private key");
        createWallet = new Button("Create a new wallet");
        manageSetting = new Button("Manage wallet settings");
        generateKeys = new Button("Generate keys");
        addCompany = new Button("Add company");

        buttonList.add(loginUsingPrivKey);
        buttonList.add(loginUsingWallet);
        buttonList.add(createWallet);
        buttonList.add(manageSetting);
        buttonList.add(generateKeys);
        buttonList.add(addCompany);


        for (Button but : buttonList) {
            but.setPrefSize(250, 250);
            but.setStyle("-fx-background-color: #4286f4; ");

            but.setOnMouseEntered(e ->
            {
                but.setStyle("-fx-background-color: #f44289; ");
            });
            but.setOnMouseExited(e ->
            {
                but.setStyle("-fx-background-color: #4286f4; ");
            });
            but.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 24));
            but.setTextAlignment(TextAlignment.CENTER);
            but.setWrapText(true);

        }


        mainPanel.add(loginUsingWallet, 1, 0);
        mainPanel.add(loginUsingPrivKey, 2, 0);
        mainPanel.add(createWallet, 1, 1);
        mainPanel.add(manageSetting, 2, 1);
        mainPanel.add(generateKeys, 3, 0);
        mainPanel.add(addCompany, 3, 1);


        loginUsingWallet.setOnAction(e ->
        {
            this.setScreenVisible("loginUsingSavedWalled");
        });

        loginUsingPrivKey.setOnAction(e ->
        {
            this.setScreenVisible("loginUsingPrivateKey");
        });
        createWallet.setOnAction(e ->
        {
            this.setScreenVisible("createANewWallet");
        });
        manageSetting.setOnAction(e ->
        {
            this.setScreenVisible("manageWalletSettings");
        });

        this.setPrefSize(MainView.WIDTH, (MainView.HEIGHT - TopView.HEIGHT - BotView.HEIGHT));

        this.getChildren().addAll(walletScreen, manageWalletSettings, createANewWallet, loginUsingPrivateKey, loginUsingSavedWalled, mainPanel);
        this.setAlignment(Pos.CENTER);

        mapOfScreen.put("mainPanel", mainPanel);
        mapOfScreen.put("loginUsingSavedWalled", loginUsingSavedWalled);
        mapOfScreen.put("loginUsingPrivateKey", loginUsingPrivateKey);
        mapOfScreen.put("createANewWallet", createANewWallet);
        mapOfScreen.put("manageWalletSettings", manageWalletSettings);
        mapOfScreen.put("walletScreen", walletScreen);


        for (GridPane gridPane : mapOfScreen.values()) {
            gridPane.setVgap(20);
            gridPane.setHgap(20);
        }


    }

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

    public void setScreenVisible(GridPane name) {
        for (GridPane a : listOfScreen) {

            a.setVisible(false);

        }
        name.setVisible(true);

    }

    public void setScreenVisible(String name) {
        if (mapOfScreen.containsKey(name)) {
            for (String n : mapOfScreen.keySet()) {
                mapOfScreen.get(n).setVisible(false);
            }
            mapOfScreen.get(name).setVisible(true);
        } else {
            System.out.println("There is no screen with name: " + name);
        }


    }

    public void setGenerateWalletButton(EventHandler<ActionEvent> actionEventEventHandler) {
        createANewWallet.setGenerateWalletButton(actionEventEventHandler);
    }

    public String getCreateNewWalletWalletName() {
        return createANewWallet.getWalletName();
    }

    public String getCreateNewWalletPassword() throws Exception {
        return createANewWallet.getPassword();
    }

    public String getCreateNewWalletPassword2() throws Exception {
        return createANewWallet.getPassword2();
    }

    public void setLoginUsingPrivKey(EventHandler<ActionEvent> actionEventEventHandler) {
        loginUsingPrivateKey.setLoginButton(actionEventEventHandler);
    }

    public String getPasswordLoginUsingPrivKey() {
        try {
            return loginUsingPrivateKey.getPassword();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public String getLoginUsingPrivKeyPublicKey() {
        return loginUsingPrivateKey.getPublicKey();
    }

    public void setAmountWalletScreen(String amount) {
        walletScreen.setAmount(amount);
    }

    public void setRefreshWalletScree(EventHandler<ActionEvent> actionEventEventHandler) {
        walletScreen.setRefresh(actionEventEventHandler);
    }
    public void setLabelAmount(String am)
    {
        walletScreen.setLabelAmout(am);
    }

    public void setWalletsLoginUsingSavedWallet(String[] tempWallets) {
        loginUsingSavedWalled.setWallets(tempWallets);
    }

    public String getSelectedWalletLoginUsingSavedWallet() {
        return loginUsingSavedWalled.getSelectedWallet();
    }

    public void setLoginButtonLoginUsingSavedWallet(EventHandler<ActionEvent> actionEventEventHandler) {
        loginUsingSavedWalled.setLoginButton(actionEventEventHandler);
    }

    public void setAddCompany(EventHandler<ActionEvent> actionEventEventHandler) {
        addCompany.setOnAction(actionEventEventHandler);
    }

    public String getPasswordLoginUsingSavedWallet() throws Exception {
        return loginUsingSavedWalled.getPassword();
    }

    public void setSendButtonWalletScreen(EventHandler<ActionEvent> actionEventEventHandler) {
        walletScreen.setSendButton(actionEventEventHandler);
    }

    public void setReciveButtonWalletScreen(EventHandler<ActionEvent> actionEventEventHandler) {
        walletScreen.setReciveButton(actionEventEventHandler);
    }

    public String getSendToWalletScreen() {
        return walletScreen.getSendTo();
    }

    public void setGenerateKeys(EventHandler<ActionEvent> ac) {
        generateKeys.setOnAction(ac);
    }

    public void setWalletsManageSetting(String[] tempWallets) {
        manageWalletSettings.setWallets(tempWallets);
    }

    public String getSelectedWalletManageSetting() {
        return manageWalletSettings.getSelectedWallet();
    }

    public void setDeleteButtonManageSetting(EventHandler<ActionEvent> actionEventEventHandler) {
        manageWalletSettings.setDeleteButton(actionEventEventHandler);
    }

    public void setViewButtonManageSetting(EventHandler<ActionEvent> actionEventEventHandler) {
        manageWalletSettings.setViewButton(actionEventEventHandler);
    }

    public String getPasswordManageSetting() throws Exception {
        return manageWalletSettings.getPassword();
    }



    public void setRefreshHistoryWaletScreen(EventHandler<ActionEvent> actionEventEventHandler) {
        walletScreen.setRefreshHistory(actionEventEventHandler);
    }

    public void setInTextAreaWalletScreen(String in)
    {
        walletScreen.setInTextArea(in);
    }
    public void setOutTextAreaWalletScreen(String in)
    {
        walletScreen.setOutTextArea(in);
    }

    public void setInTextAreaClearWalletScreen()
    {
        walletScreen.setInTextAreaClear();
    }
    public void setOutTextAreaClearWalletScreen()
    {
        walletScreen.setOutTextAreaClear();
    }
    public String getStartTimeWalletString()
    {
       return walletScreen.getStartTime();
    }
    public String getStopTimeWalletScreen()
    {
        return walletScreen.getStopTime();
    }

    public String getSelectedRadioButtonWalletScreen()
    {
        return walletScreen.getSelectedRadioButton();
    }
    public String getSpecAccontWalletScreen()
    {
        return walletScreen.getSpecAccont();
    }

    public String getSurnameTextFieldWalletScreen() {
        return walletScreen.getSurnameTextField();
    }

    public String getNameTextFieldWalletScreen() {
        return walletScreen.getName();
    }


    public void clearSurnameTextFieldWalletScreen() {
        walletScreen.clearSurnameTextField();
    }

    public void setAddPersonalDataWalletScreen(EventHandler<ActionEvent> actionEventEventHandler) {
        walletScreen.setAddPersonalData(actionEventEventHandler);
    }

    public void setSearchWalletScreen(EventHandler<ActionEvent> actionEventEventHandler) {
        walletScreen.setSearch(actionEventEventHandler);
    }


    public class homeButtonAction implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent actionEvent) {
            setScreenVisible("mainPanel");
            System.out.println("Switched to home");


        }
    }

    public class loginButtonAction implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent actionEvent) {
            setScreenVisible("walletScreen");
            System.out.println("Switched to Wallet");
        }
    }
}


