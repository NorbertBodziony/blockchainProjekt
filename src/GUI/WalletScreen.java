package GUI;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.util.Pair;

import java.util.Optional;

public class WalletScreen extends GridPane {

    Button send, recive, home, refresh;

    String sendTo;
    String sendToAmount;


    Label labelAmout, historyOftransaction;
    String amount;
    String text;



    Optional<Pair<String, String>> recipientAndAmount;


    public WalletScreen() {

        labelAmout = new Label();
        text = "Your amout: ";
        labelAmout.setText("Your amount: ");
        labelAmout.setTextFill(Color.WHITE);
        historyOftransaction = new Label("History of transaction: ");


        labelAmout.setFont(Font.font("Tahoma", FontWeight.EXTRA_BOLD, 25));
        labelAmout.setTextFill(Color.WHITE);
        historyOftransaction.setFont(Font.font("Tahoma", FontWeight.EXTRA_BOLD, 25));
        historyOftransaction.setTextAlignment(TextAlignment.CENTER);
        historyOftransaction.setTextFill(Color.WHITE);

        send = new Button("Send");
        recive = new Button("Recive");
        refresh = new Button("Refresh");

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

        refresh.setPrefSize(220, 30);
        refresh.setStyle("-fx-background-color: #53f442; ");

        refresh.setOnMouseEntered(e ->
        {
            refresh.setStyle("-fx-background-color: #20a013; ");
        });
        refresh.setOnMouseExited(e ->
        {
            refresh.setStyle("-fx-background-color: #53f442; ");
        });

        this.add(send, 1, 0);
        this.add(recive, 2, 0);
        this.add(labelAmout, 1, 1);
        this.add(refresh, 2, 1);
        this.add(home, 1, 2);

        //this.add(verticalSeparator,3,0,1,20);
        this.add(historyOftransaction, 3, 0);
        //this.add(horizontalSeparator, 4,1,9,1);

        this.setVgap(5);
        this.setHgap(5);
        this.setVisible(false);


    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getSendTo() {
        return sendTo;
    }

    public void setSendTo(String sendTo) {
        this.sendTo = sendTo;
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
    public void setRefresh(EventHandler<ActionEvent> actionEventEventHandler) {
        refresh.setOnAction(actionEventEventHandler);
    }

    public void setLabelAmout(String am)
    {
        labelAmout.setText("Your amount: " + am);
    }
}
