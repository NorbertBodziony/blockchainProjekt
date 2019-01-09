package GUI;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.util.Pair;

import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class WalletScreen extends GridPane {

    Button send, recive, home, refresh, refreshHistory;

    String sendTo;
    String sendToAmount;

    TextArea inTextArea, outTextArea;


    Label labelAmout, historyOftransaction, Income, Outcome, StartTime, StopTime;
    String amount;
    String text;

    CheckBox checkBox1;

    DatePicker start, stop;

    HBox hBox;



    Optional<Pair<String, String>> recipientAndAmount;


    public WalletScreen() {

        hBox = new HBox(4);

        inTextArea = new TextArea();
        inTextArea.setEditable(false);
        outTextArea = new TextArea();
        outTextArea.setEditable(false);

        start = new DatePicker();

        start.setEditable(false);
        stop = new DatePicker();
        stop.setEditable(false);

        labelAmout = new Label();
        text = "Your amout: ";
        labelAmout.setText("Your amount: ");
        labelAmout.setTextFill(Color.WHITE);
        historyOftransaction = new Label("History of transaction: ");

        Income = new Label("Income: ");
        Outcome = new Label("Outcome: ");
        StartTime = new Label("Start time: ");
        StopTime = new Label("Stop time: ");
        checkBox1 = new CheckBox("Search by date: ");

        Income.setFont(Font.font(null, FontWeight.EXTRA_BOLD, 15));
        Income.setTextFill(Color.WHITE);

        Outcome.setFont(Font.font(null, FontWeight.EXTRA_BOLD, 15));
        Outcome.setTextFill(Color.WHITE);

        StartTime.setFont(Font.font(null, FontWeight.EXTRA_BOLD, 15));
        StartTime.setTextFill(Color.WHITE);

        StopTime.setFont(Font.font(null, FontWeight.EXTRA_BOLD, 15));
        StopTime.setTextFill(Color.WHITE);

        start.setPrefWidth(130);
        stop.setPrefWidth(130);

        hBox.setPadding(new Insets(1));
        hBox.getChildren().addAll(StartTime,start,StopTime,stop);


        labelAmout.setFont(Font.font("Tahoma", FontWeight.EXTRA_BOLD, 25));
        labelAmout.setTextFill(Color.WHITE);
        historyOftransaction.setFont(Font.font("Tahoma", FontWeight.EXTRA_BOLD, 25));
        historyOftransaction.setTextAlignment(TextAlignment.CENTER);
        historyOftransaction.setTextFill(Color.WHITE);

        send = new Button("Send");
        recive = new Button("Recive");
        refresh = new Button("Refresh");
        refreshHistory = new Button("Refresh");

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


        this.add(historyOftransaction, 3, 0);
        this.add(hBox,3,1);
        this.add(refreshHistory,3,2);

        this.add(Income,3,3);
        this.add(inTextArea,3,4);
        this.add(Outcome,3,5);
        this.add(outTextArea,3,6);




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

    public void setRefreshHistory(EventHandler<ActionEvent> actionEventEventHandler) {
        refreshHistory.setOnAction(actionEventEventHandler);
    }

    public void setInTextArea(String in)
    {
        inTextArea.appendText(in+"\n");
    }
    public void setOutTextArea(String in)
    {
        outTextArea.appendText(in+"\n");
    }

    public void setInTextAreaClear()
    {
        inTextArea.clear();
    }
    public void setOutTextAreaClear()
    {
        outTextArea.clear();
    }
    public String getStartTime()
    {
        String temp = start.getValue().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) + " 00:00:00";
        return temp;
    }
    public String getStopTime()
    {
        String temp = stop.getValue().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) + " 00:00:00";
        return temp;
    }
}
