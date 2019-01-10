package GUI;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.StageStyle;

public class updatePersonalData extends Dialog<Nine<String>> {
    double xOffset;
    double yOffset;

    public updatePersonalData() {
//        this.getDialogPane().setPrefSize(700,1000);
        this.setHeight(500);
        // this.setWidth(1000);

        this.setTitle("Update Personal Data");
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

        TextField name = new TextField();
        name.setPromptText("Name");
        TextField surname = new TextField();
        surname.setPromptText("Surname");

        TextField company = new TextField();
        company.setPromptText("CompanyID");

        TextField email = new TextField();
        email.setPromptText("Email");

        TextField country = new TextField();
        country.setPromptText("Country");

        TextField postalCode = new TextField();
        postalCode.setPromptText("Postal Code");

        TextField city = new TextField();
        city.setPromptText("City");

        TextField street = new TextField();
        street.setPromptText("Street");

        TextField apartment_number = new TextField();
        apartment_number.setPromptText("Aaprtment number: ");

        grid.add(new Label("Name:"), 0, 0);
        grid.add(name, 1, 0);
        grid.add(new Label("Surname:"), 0, 1);
        grid.add(surname, 1, 1);

        grid.add(new Label("Company:"), 0, 2);
        grid.add(company, 1, 2);


        grid.add(new Label("Email:"), 0, 3);
        grid.add(email, 1, 3);

        grid.add(new Label("Country:"), 0, 4);
        grid.add(country, 1, 4);

        grid.add(new Label("Postal code:"), 0, 5);
        grid.add(postalCode, 1, 5);

        grid.add(new Label("City:"), 0, 6);
        grid.add(city, 1, 6);

        grid.add(new Label("Street:"), 0, 7);
        grid.add(street, 1, 7);

        grid.add(new Label("Apartment number:"), 0, 8);
        grid.add(apartment_number, 1, 8);

        Node loginButton = this.getDialogPane().lookupButton(loginButtonType);
        loginButton.setDisable(true);

        apartment_number.textProperty().addListener((observable, oldValue, newValue) -> {
            loginButton.setDisable(newValue.trim().isEmpty());
        });


        this.getDialogPane().setContent(grid);

        Platform.runLater(() -> name.requestFocus());

        this.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                return new Nine<>(name.getText(), surname.getText(), company.getText(), email.getText(), country.getText(), postalCode.getText(), city.getText(), street.getText(), apartment_number.getText());
            }
            return null;
        });

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

