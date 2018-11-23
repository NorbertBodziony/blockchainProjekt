package GUI;


import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.StageStyle;
import javafx.util.Pair;

public class reciveDialog extends Dialog<Pair<String, String>> {
    double xOffset;
    double yOffset;
    private String publicKey;
    private Clipboard clipboard;
    private ClipboardContent content;

    public reciveDialog(String publicKey) {
        publicKey = new String(publicKey);
        clipboard = Clipboard.getSystemClipboard();
        content = new ClipboardContent();
        this.setTitle("Send");
        this.setHeaderText(null);
        this.setGraphic(null);
        this.initStyle(StageStyle.UNDECORATED);
        //this.setHeight(1000);
        //this.setWidth(1000);
        //this.getDialogPane().setStyle("-fx-background-color: #20a013; ");


        this.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL);


        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField publicKeyField = new TextField();
        publicKeyField.setText(publicKey);
        publicKeyField.setEditable(false);

        Button copy = new Button("Copy");
        Label text = new Label("Public key:");
        text.setTextFill(Color.WHITE);

        grid.add(text, 0, 0);
        grid.add(publicKeyField, 1, 0);
        grid.add(copy, 3, 0);

        copy.setOnAction(e ->
        {
            content.putString(publicKeyField.getText());
            clipboard.setContent(content);
            content.clear();
        });


        this.getDialogPane().setContent(grid);

        Platform.runLater(() -> publicKeyField.requestFocus());


        javafx.scene.image.Image image = new javafx.scene.image.Image(getClass().getResource("back3.png").toExternalForm());

        BackgroundImage myBI = new BackgroundImage(image,
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);
        this.getDialogPane().setBackground(new Background(myBI));


        this.getDialogPane().setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    xOffset = event.getSceneX();
                    yOffset = event.getSceneY();
                } catch (Exception e) {
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
}