package GUI;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.StageStyle;
import javafx.util.Pair;

public class sendDialog extends Dialog<Pair<String, String>> {
    double xOffset;
    double yOffset;

    public sendDialog() {
        this.setTitle("Send");
        this.setHeaderText(null);
        this.setGraphic(null);
        this.initStyle(StageStyle.UNDECORATED);
        this.setResizable(true);


        ButtonType loginButtonType = new ButtonType("Send", ButtonBar.ButtonData.OK_DONE);


        this.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);


        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField amount = new TextField();
        amount.setPromptText("Amount");
        TextField recipent = new TextField();
        recipent.setPromptText("Adress");

        grid.add(new Label("Amount:"), 0, 0);
        grid.add(amount, 1, 0);
        grid.add(new Label("Address:"), 0, 1);
        grid.add(recipent, 1, 1);

        Node loginButton = this.getDialogPane().lookupButton(loginButtonType);
        loginButton.setDisable(true);

        amount.textProperty().addListener((observable, oldValue, newValue) -> {
            loginButton.setDisable(newValue.trim().isEmpty());
        });

        this.getDialogPane().setContent(grid);

        Platform.runLater(() -> amount.requestFocus());

        this.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                return new Pair<>(amount.getText(), recipent.getText());
            }
            return null;
        });

/*        javafx.scene.image.Image image = new javafx.scene.image.Image(getClass().getResource("back3.png").toExternalForm());

        BackgroundImage myBI = new BackgroundImage(image,
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);
        this.getDialogPane().setBackground(new Background(myBI));*/

        this.getDialogPane().setBackground(new Background(new BackgroundFill(Color.GRAY, CornerRadii.EMPTY, Insets.EMPTY)));


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