package GUI;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.StageStyle;
import javafx.util.Pair;

import java.util.Optional;

import static java.lang.Thread.sleep;

public class WalletScreen extends GridPane {

    Button send, recive, home;

    String sendTo;
    String sendToAmount;


    Label labelAmout, historyOftransaction;
    String amount;
    String text;

    //Separator verticalSeparator;
    //Separator horizontalSeparator;

    Optional<Pair<String, String>> recipientAndAmount;


    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getAmount() {
        return amount;
    }


    public String getSendTo() {
        return sendTo;
    }

    public void setSendTo(String sendTo) {
        this.sendTo = sendTo;
    }

    public WalletScreen() {

        labelAmout = new Label();
        text = new String("Your amout: " + amount);
        labelAmout.setText(amount);
        labelAmout.setTextFill(Color.WHITE);
        historyOftransaction = new Label("History of transaction: ");


        labelAmout.setFont(Font.font("Tahoma", FontWeight.EXTRA_BOLD, 25));
        labelAmout.setTextFill(Color.WHITE);
        historyOftransaction.setFont(Font.font("Tahoma", FontWeight.EXTRA_BOLD, 25));
        historyOftransaction.setTextAlignment(TextAlignment.CENTER);
        historyOftransaction.setTextFill(Color.WHITE);



     /*   verticalSeparator = new Separator();
        verticalSeparator.setOrientation(Orientation.VERTICAL);
        verticalSeparator.setStyle("-fx-background-color: #000000; ");

        horizontalSeparator = new Separator();
        horizontalSeparator.setOrientation(Orientation.HORIZONTAL);*/


        send = new Button("Send");
        recive = new Button("Recive");

        send.setPrefSize(220, 30);
        send.setStyle("-fx-background-color: #53f442; ");

        send.setOnMouseEntered(e ->
        {
            send.setStyle("-fx-background-color: #20a013; ");
        });
        send.setOnMouseExited(e ->
        {
            send.setStyle("-fx-background-color: #53f442; ");
        });

       /* send.setOnAction(e->
        {
            recipientAndAmount = new sendDialog().showAndWait();
            recipientAndAmount.ifPresent(usernamePassword -> {
                System.out.println("Amount=" + usernamePassword.getKey() + ", Recipent address=" + usernamePassword.getValue());
                sendToAmount = usernamePassword.getKey();
                Controller.sendTo = usernamePassword.getValue();
                Controller.amount = usernamePassword.getKey();
                try {
                    sleep(5);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            });

        });*/


        recive.setPrefSize(220, 30);
        recive.setStyle("-fx-background-color: #53f442; ");

        recive.setOnMouseEntered(e ->
        {
            recive.setStyle("-fx-background-color: #20a013; ");
        });
        recive.setOnMouseExited(e ->
        {
            recive.setStyle("-fx-background-color: #53f442; ");
        });


        /*recive.setOnAction(e->
        {
            new reciveDialog().showAndWait();
        });*/

        home = new Button("Home");

        home.setPrefSize(220, 30);
        home.setStyle("-fx-background-color: #53f442; ");

        home.setOnMouseEntered(e ->
        {
            home.setStyle("-fx-background-color: #20a013; ");
        });
        home.setOnMouseExited(e ->
        {
            home.setStyle("-fx-background-color: #53f442; ");
        });

        this.add(send, 1, 0);
        this.add(recive, 2, 0);
        this.add(labelAmout, 1, 1);
        this.add(home, 2, 1);
        //this.add(verticalSeparator,3,0,1,20);
        this.add(historyOftransaction, 3, 0);
        //this.add(horizontalSeparator, 4,1,9,1);

        this.setVgap(5);
        this.setHgap(5);
        this.setVisible(false);


    }

    public void setHomeButton(EventHandler<ActionEvent> actionEventEventHandler) {
        home.setOnAction(actionEventEventHandler);
    }

    public void setSendButton(EventHandler<ActionEvent> actionEventEventHandler) {
        send.setOnAction(actionEventEventHandler);
    }
    public void setReciveButton(EventHandler<ActionEvent> actionEventEventHandler) {
        recive.setOnAction(actionEventEventHandler);
    }
}
