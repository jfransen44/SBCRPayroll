package com.company;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.time.LocalDate;
import java.util.List;

/**
 * Created by jeremyfransen on 5/23/17.
 */
public class ApplicationMainWindow extends VBox {

    private HBox topHBox, middleHBox, bottomHBox;
    private Button selectReportsButton, processButton, removeFilesButton, saveButton;
    private Label toOpenLabel, statusLabel;
    private ListView<File> fileListView;
    private DatePicker startDatePicker, endDatePicker;
    private LocalDate startDate, endDate;

    public ApplicationMainWindow(){
        this.setPrefSize(700, 500);
        topHBox = new HBox();
        middleHBox = new HBox();
        bottomHBox = new HBox();
        toOpenLabel = new Label("Files to open");
        statusLabel = new Label();
        fileListView = new ListView<>();
        fileListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        startDatePicker = new DatePicker();
        endDatePicker = new DatePicker();
        selectReportsButton = new Button("Select Reports");
        processButton = new Button("Process");
        removeFilesButton = new Button("Remove Selected");
        saveButton = new Button("Save");
        this.getChildren().addAll(topHBox, toOpenLabel, fileListView, statusLabel, middleHBox, bottomHBox);
        topHBox.getChildren().addAll(startDatePicker, endDatePicker, selectReportsButton);
        middleHBox.getChildren().add(removeFilesButton);
        bottomHBox.getChildren().addAll(processButton, saveButton);

        setLayout();
    }

    private void setLayout(){
        this.setPadding(new Insets(25));
        topHBox.setAlignment(Pos.TOP_CENTER);
        topHBox.setPadding(new Insets(5));
        topHBox.setMargin(startDatePicker, new Insets(5));
        topHBox.setMargin(endDatePicker, new Insets(5));
        topHBox.setMargin(selectReportsButton, new Insets(5));
        topHBox.setSpacing(25);

        startDatePicker.setPromptText("Start Date");
        endDatePicker.setPromptText("End Date");

        middleHBox.setAlignment(Pos.CENTER_RIGHT);
        middleHBox.setPadding(new Insets(5));
        middleHBox.setMargin(removeFilesButton, new Insets(5));
        middleHBox.setSpacing(15);

        bottomHBox.setPadding(new Insets(5));
        bottomHBox.setMargin(processButton, new Insets(5));
        bottomHBox.setMargin(saveButton, new Insets(5));
        bottomHBox.setAlignment(Pos.TOP_RIGHT);
    }

    private void setListeners(){
        startDatePicker.setOnAction(e -> {
            startDate = startDatePicker.getValue();
            System.out.println(startDate.toString().replaceAll("-", "_") + "-" + startDate.plusDays(6).toString().replaceAll("-", "_")  );
        });

        endDatePicker.setOnAction(e -> {
            endDate = endDatePicker.getValue();
        });

        selectReportsButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select Payroll Reports");


            List<File> selectedFiles = fileChooser.showOpenMultipleDialog(this.getScene().getWindow());
            ObservableList<File> obsList = FXCollections.observableList(fileListView.getItems());
            if (selectedFiles != null) {
                for (File file : selectedFiles){
                    if (! obsList.contains(file))
                        obsList.add(file);
                }
                fileListView.setItems(obsList);
            }
        });

        removeFilesButton.setOnAction(e -> {
            ObservableList<File> f = fileListView.getItems();
            f.removeAll((fileListView.getSelectionModel().getSelectedItems()));
            fileListView.setItems(f);
            fileListView.getSelectionModel().clearSelection();
        });

        saveButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Report");
            File file = fileChooser.showSaveDialog(this.getScene().getWindow());
            if (file != null){

            }
        });

        processButton.setOnAction(e -> {

        });
    }
}
