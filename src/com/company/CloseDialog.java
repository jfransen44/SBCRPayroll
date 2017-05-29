package com.company;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Created by jeremyfransen on 5/28/17.
 */
public class CloseDialog{
    private static String response = "";

    public static String display(){
        Stage window = new Stage();
        Button noButton = new Button("No");
        Button yesButton = new Button("Yes");
        Button saveButton = new Button("Save");
        Label label = new Label("You have not saved your file.  Do you still want to quit?");
        VBox vBox = new VBox();
        HBox hBox = new HBox();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setMinWidth(300);
        window.setMinHeight(100);
        hBox.setSpacing(25);
        hBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(20);
        vBox.setAlignment(Pos.CENTER);
        hBox.getChildren().addAll(noButton, yesButton, saveButton);
        vBox.getChildren().addAll(label, hBox);

        noButton.setOnAction(e -> {
            setResponse(noButton.getText());
            window.close();
        });
        yesButton.setOnAction(e -> {
            setResponse(yesButton.getText());
            window.close();
        });
        saveButton.setOnAction(e -> {
            setResponse(saveButton.getText());
            window.close();
        });

        window.setOnCloseRequest(e -> {
            e.consume();
            setResponse("No");
            window.close();
        });

        saveButton.setDefaultButton(true);
        Scene scene = new Scene(vBox);
        window.setScene(scene);
        window.showAndWait();

        return response;
    }

    private static void setResponse(String text){
        response = text;
    }
}
