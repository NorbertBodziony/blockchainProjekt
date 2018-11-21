package GUI;

import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;


import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;


public class BotView extends BorderPane {
    public final static int HEIGHT = 50;

    String urlPath;
    URI uriPath;

    public BotView() {
        urlPath = "https://twitter.com/NorbertBodziony";
        try {
            uriPath = new URI(urlPath);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        Label a = new Label("Donation via: 1F1tAaz5x1HUXrCNLbtMDqcw6o5GNn4xqX");
        a.setTextFill(Color.WHITE);


         Hyperlink hlink = new Hyperlink(urlPath);
         hlink.setCursor(Cursor.HAND);
        hlink.setOnAction(e -> {
            System.out.println("A");
          if(Desktop.isDesktopSupported())
          {
              try {
                  Desktop.getDesktop().browse(new URL("https://google.com").toURI());
              } catch (IOException b) {
                  b.printStackTrace();
              } catch (URISyntaxException c) {
                  c.printStackTrace();
              }
          }
          else
          {
              System.out.println("Not supported");
          }

        });

        this.setPadding(new Insets(10));
        this.setPrefSize(MainView.WIDTH, HEIGHT);
        this.setCenter(a);
    }
}
