package GUI;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class TopView extends BorderPane {
    public final static int HEIGHT = 50;

    private Text name;
    private Text version;
    private Button logout;
    private HBox hBox;

    public  int returnOne()
    {
        return  1;
    }


    public TopView()
    {

        name = new Text("Oxygen Network");
        name.setFont(Font.font("Tahoma", FontWeight.EXTRA_BOLD, 25));
        name.setFill(Color.DARKOLIVEGREEN);

        version = new Text("Version: 0.2");
        version.setFill(Color.WHITE);
        logout = new Button("Logout");
        logout.setStyle("-fx-background-color: #4286f4; ");

        logout.setOnMouseEntered(e->
        {
            logout.setStyle("-fx-background-color: #f44289; ");
        });
        logout.setOnMouseExited(e->
        {
            logout.setStyle("-fx-background-color: #4286f4; ");
        });


       logout.setOnAction(new LogoutAction());


        hBox = new HBox();
        hBox.setSpacing(30);
        hBox.setAlignment(Pos.CENTER);





        hBox.getChildren().addAll(version,logout);


        this.setPrefSize(MainView.WIDTH,HEIGHT);
        this.setPadding(new Insets(20));
        this.setLeft(name);
        this.setRight(hBox);


    }

    public void setLogoutOnAction(LogoutAction logoutAction) {
        logout.setOnAction(logoutAction);
    }

    public class LogoutAction implements EventHandler
    {

        @Override
        public void handle(Event event) {
            System.exit(0);
        }
    }


}
