package GUI;


import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.StageStyle;
import javafx.util.Pair;

public class keysDialog extends Dialog<Pair<String, String>> {
    private static String publicKey = "null";
    private static String privateKey = "null2";
    double xOffset;
    double yOffset;
    Clipboard clipboard;
    ClipboardContent content;

    public keysDialog(String publicKey, String privateKey) {
        this.setPublicKey(publicKey);
        this.setPrivateKey(privateKey);

        this.setTitle("Send");
        this.setHeaderText(null);
        this.setGraphic(null);
        this.initStyle(StageStyle.UNDECORATED);
        this.setResizable(true);

        content = new ClipboardContent();
        clipboard = Clipboard.getSystemClipboard();
        Button copy = new Button("Copy");
        Button copy1 = new Button("Copy");


        this.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL);


        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField PublicKey = new TextField();
        PublicKey.appendText(getPublicKey());
        PublicKey.setEditable(false);
        TextField recipent = new TextField();
        recipent.appendText(getPrivateKey());
        recipent.setEditable(false);

        grid.add(new Label("Public Key:"), 0, 0);
        grid.add(PublicKey, 1, 0);
        grid.add(copy, 2, 0);
        grid.add(new Label("Private Key:"), 0, 1);
        grid.add(recipent, 1, 1);
        grid.add(copy1, 2, 1);

        copy.setOnAction(e ->
        {
            content.putString(publicKey);
            clipboard.setContent(content);
            content.clear();
        });

        copy1.setOnAction(e ->
        {
            content.putString(privateKey);
            clipboard.setContent(content);
            content.clear();
        });

        this.getDialogPane().setContent(grid);

        Platform.runLater(() -> PublicKey.requestFocus());
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


}