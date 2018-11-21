//import com.sun.tools.javac.Main;
package GUI;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.StageStyle;
import javafx.util.Pair;

import java.util.*;

public class MiddleView extends StackPane {
    private Button button;
    private Button button1;
    private Button button2;
    private Button button3 ;

    private Button loginUsingWallet;
    private Button loginUsingPrivKey;
    private Button createWallet;
    private Button manageSetting;
    private Button generateKeys;


    private String publicKey ="null";
    private String privateKey ="null2";

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





    private GridPane mainPanel;
    private GridPane generateWallet;
    private GridPane login;
    private GridPane wallet;

    private LoginUsingSavedWallet loginUsingSavedWalled;
    private LoginUsingPrivKey loginUsingPrivateKey;
    private CreateANewWallet createANewWallet;
    private ManageWalletSettings manageWalletSettings;
    private WalletScreen walletScreen;
    private GenerateKeyPair generateKeyPair;

    private List<GridPane> listOfScreen;
    private List<Button> buttonList;
    private Map<String,GridPane> mapOfScreen;

    Clipboard clipboard;
    ClipboardContent content;

    public MiddleView()
    {
        content = new ClipboardContent();
        clipboard = Clipboard.getSystemClipboard();

        listOfScreen = new ArrayList<>();
        buttonList = new ArrayList<>();
        mapOfScreen = new HashMap<>();


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

        generateKeyPair = new GenerateKeyPair();
        generateKeyPair.setHomeButton(new homeButtonAction());






         button = new Button("MainPanel");
         button1 = new Button("generaTeWallet");
         button2 = new Button("login");
         button3 = new Button("wallet");

         loginUsingWallet = new Button("Login using a saved wallet");
         loginUsingPrivKey = new Button("Login using private key");
         createWallet = new Button("Create a new wallet");
         manageSetting = new Button("Manage wallet settings");
         generateKeys = new Button("Generate keys");

/*
         buttonList.add(button);
        buttonList.add(button1);
        buttonList.add(button2);
        buttonList.add(button3);*/
        buttonList.add(loginUsingPrivKey);
        buttonList.add(loginUsingWallet);
        buttonList.add(createWallet);
        buttonList.add(manageSetting);
        buttonList.add(generateKeys);




        for(Button but: buttonList)
        {
            but.setPrefSize(250,250);
            but.setStyle("-fx-background-color: #4286f4; ");

            but.setOnMouseEntered(e->
            {
                but.setStyle("-fx-background-color: #f44289; ");
            });
            but.setOnMouseExited(e->
            {
                but.setStyle("-fx-background-color: #4286f4; ");
            });
            but.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 24));
            but.setTextAlignment(TextAlignment.CENTER);
            but.setWrapText(true);

        }





        mainPanel = new GridPane();
        //mainPanel.add(button,1,0);
        mainPanel.add(loginUsingWallet,1,0);
        mainPanel.add(loginUsingPrivKey,2,0);
        mainPanel.add(createWallet,1,1);
        mainPanel.add(manageSetting,2,1);
        mainPanel.add(generateKeys,3,0);






        button.setOnAction(e->{
            this.setScreenVisible(login);
        });

        loginUsingWallet.setOnAction(e->
        {
            this.setScreenVisible("loginUsingSavedWalled");
        });

        loginUsingPrivKey.setOnAction(e->
        {
            this.setScreenVisible("loginUsingPrivateKey");
        });
        createWallet.setOnAction(e->
        {
            this.setScreenVisible("createANewWallet");
        });
        manageSetting.setOnAction(e->
        {
            this.setScreenVisible("createANewWallet");
        });



         generateWallet = new GridPane();
        generateWallet.add(button1,1,0);
        button1.setOnAction(e->{
            this.setScreenVisible(mainPanel);
        });
        generateWallet.setVisible(false);


        PasswordField passwordField = new PasswordField();
        passwordField.setText("Type password here: ");
        passwordField.setPrefColumnCount(20);

        button2.setPrefSize(200,50);

        login = new GridPane();
        login.add(button2,1,1);
        login.add(passwordField,1,0);
        button2.setOnAction(e->{
            this.setScreenVisible(mainPanel);
        });
        login.setVisible(false);


        wallet = new GridPane();
        wallet.add(button3,1,0);
        wallet.setVisible(false);





        this.setPrefSize(MainView.WIDTH,(MainView.HEIGHT - TopView.HEIGHT - BotView.HEIGHT));

        this.getChildren().addAll(walletScreen,manageWalletSettings, createANewWallet,loginUsingPrivateKey,loginUsingSavedWalled,mainPanel,generateWallet,login,wallet);
        this.setAlignment(Pos.CENTER);



        listOfScreen.add(mainPanel);
        listOfScreen.add(loginUsingSavedWalled);
        listOfScreen.add(loginUsingPrivateKey);
        listOfScreen.add(createANewWallet);
        listOfScreen.add(manageWalletSettings);
        listOfScreen.add(walletScreen);

        mapOfScreen.put("mainPanel",mainPanel);
        mapOfScreen.put("loginUsingSavedWalled",loginUsingSavedWalled);
        mapOfScreen.put("loginUsingPrivateKey",loginUsingPrivateKey);
        mapOfScreen.put("createANewWallet",createANewWallet);
        mapOfScreen.put("manageWalletSettings",manageWalletSettings);
        mapOfScreen.put("walletScreen",walletScreen);
        mapOfScreen.put("generateKeys",generateKeyPair);



        listOfScreen.add(generateWallet);
        listOfScreen.add(login);
        listOfScreen.add(wallet);

        for(GridPane gridPane: listOfScreen)
        {
            gridPane.setVgap(20);
            gridPane.setHgap(20);
        }





        //this.setScreenVisible("Bartek");



    }

    public void setScreenVisible(GridPane name)
    {
       for(GridPane a: listOfScreen)
       {

               a.setVisible(false);

       }
       name.setVisible(true);

    }
    public void setScreenVisible(String name)
    {
        if(mapOfScreen.containsKey(name))
        {
            for(String n: mapOfScreen.keySet())
            {
                mapOfScreen.get(n).setVisible(false);
            }
            mapOfScreen.get(name).setVisible(true);
        }
        else
        {
            System.out.println("There is no screen with name: " + name);
        }


    }

    public class homeButtonAction implements EventHandler<ActionEvent>
    {

        @Override
        public void handle(ActionEvent actionEvent) {
            setScreenVisible("mainPanel");

            try {

                //System.out.println(loginUsingSavedWalled.getWallet());
                //System.out.println(loginUsingSavedWalled.getPassword());
                //loginUsingSavedWalled.setWallets("NEW WALLET");
                //System.out.println(createANewWallet.getPassword());
            }
            catch (Exception e)
            {
                System.out.println("Eroor");
               // e.printStackTrace();
            }

        }
    }
    public class loginButtonAction implements EventHandler<ActionEvent>
    {

        @Override
        public void handle(ActionEvent actionEvent) {
            setScreenVisible("walletScreen");

            try {

                //System.out.println(loginUsingSavedWalled.getWallet());
                //System.out.println(loginUsingSavedWalled.getPassword());
                //loginUsingSavedWalled.setWallets("NEW WALLET");
                System.out.println("Switched to Wallet");
            }
            catch (Exception e)
            {
                System.out.println("Eroor");
                // e.printStackTrace();
            }

        }
    }

    public void setGenerateWalletButton(EventHandler<ActionEvent> actionEventEventHandler) throws Exception
    {
        createANewWallet.setGenerateWalletButton(actionEventEventHandler);
    }

    public String getCreateNewWalletWalletName()
    {
        return createANewWallet.getWalletName();
    }


    public String getCreateNewWalletPassword() throws Exception {
        return createANewWallet.getPassword();
    }
    public String getCreateNewWalletPassword2() throws Exception {
        return createANewWallet.getPassword2();
    }

    public  void setLoginUsingPrivKey(EventHandler<ActionEvent> actionEventEventHandler)
    {
        loginUsingPrivateKey.setLoginButton(actionEventEventHandler);
    }
    public String getPasswordLoginUsingPrivKey()
    {
        try {
             return loginUsingPrivateKey.getPassword();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public String getLoginUsingPrivKeyPublicKey()
    {
        return loginUsingPrivateKey.getPublicKey();
    }

    public void setAmountWalletScreen(String amount)
    {
        walletScreen.setAmount(amount);
    }



    public void setWalletsLoginUsingSavedWallet(String[] tempWallets)
    {
        loginUsingSavedWalled.setWallets(tempWallets);
    }
    public String getSelectedWalletLoginUsingSavedWallet()
    {
        return loginUsingSavedWalled.getSelectedWallet();
    }
    public void setLoginButtonLoginUsingSavedWallet(EventHandler<ActionEvent> actionEventEventHandler)
    {
        loginUsingSavedWalled.setLoginButton(actionEventEventHandler);
    }
    public void setSendButtonWalletScreen(EventHandler<ActionEvent> actionEventEventHandler)
    {
        walletScreen.setSendButton(actionEventEventHandler);
    }
    public void setReciveButtonWalletScreen(EventHandler<ActionEvent> actionEventEventHandler)
    {
        walletScreen.setReciveButton(actionEventEventHandler);
    }

    public String getSendToWalletScreen() {
        return walletScreen.getSendTo();
    }

    public void setGenerateKeys(EventHandler<ActionEvent> ac) {
        generateKeys.setOnAction(ac);
    }
}


