package com.company;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static java.time.temporal.ChronoUnit.DAYS;

/**
 * Created by jeremyfransen on 7/11/17.
 */

public class ApplicationMainWindow extends VBox {


    private static VBox container;
    private static HBox topHBox, middleHBox, bottomHBox;
    private static Button selectReportsButton, processButton, removeFilesButton, saveButton;
    private static Label toOpenLabel, statusLabel, startDateLabel, endDateLabel;
    private static ListView<File> fileListView;
    private static DatePicker startDatePicker, endDatePicker;
    private static LocalDate startDate, endDate;
    private static PayrollCalculator payrollCalculator;
    private static Stage window;
    private static boolean isFileSaved = true;


    public static void display(){

        window = new Stage();
        container = new VBox();
        topHBox = new HBox();
        middleHBox = new HBox();
        bottomHBox = new HBox();
        toOpenLabel = new Label("Files to open");
        statusLabel = new Label();
        startDateLabel = new Label("Start date:");
        endDateLabel = new Label("End date:");
        fileListView = new ListView<>();
        fileListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        startDatePicker = new DatePicker();
        endDatePicker = new DatePicker();
        selectReportsButton = new Button("Select Reports");
        processButton = new Button("Process");
        removeFilesButton = new Button("Remove Selected");
        removeFilesButton.setDisable(true);
        saveButton = new Button("Save");
        container.getChildren().addAll(topHBox, toOpenLabel, fileListView, statusLabel, middleHBox, bottomHBox);
        topHBox.getChildren().addAll(startDateLabel, startDatePicker,  endDateLabel, endDatePicker, selectReportsButton);
        middleHBox.getChildren().add(removeFilesButton);
        bottomHBox.getChildren().addAll(processButton, saveButton);
        setLayout();
        setListeners();
        Scene scene = new Scene(container);
        window.setScene(scene);
        window.show();
    }

    private static void setLayout(){
        container.setPrefSize(700, 500);
        container.setPadding(new Insets(25));
        topHBox.setAlignment(Pos.CENTER);
        topHBox.setPadding(new Insets(5));
        topHBox.setMargin(startDatePicker, new Insets(5));
        topHBox.setMargin(endDatePicker, new Insets(5));
        topHBox.setMargin(selectReportsButton, new Insets(5));
        topHBox.setSpacing(5);

        startDatePicker.setMaxWidth(120);
        endDatePicker.setMaxWidth(120);
        final LocalDate START_DATE_REFERENCE = LocalDate.of(2017, 5, 8);
        startDate = LocalDate.now();

        //set start date to nearest previous monday
        while (! startDate.getDayOfWeek().name().equals("MONDAY")){
            startDate = startDate.minusDays(1);
        }

        //set startdate to two week pay period
        while ( DAYS.between(START_DATE_REFERENCE, startDate) % 14 != 0){
            startDate = startDate.minusDays(7);
        }

        //set startDate to correct pay period
        startDate = startDate.minusDays(14);
        
        startDatePicker.setValue(startDate);
        endDatePicker.setValue(startDate.plusDays(13));
        endDate = startDate.plusDays(13);

        middleHBox.setAlignment(Pos.CENTER_RIGHT);
        middleHBox.setPadding(new Insets(5));
        middleHBox.setMargin(removeFilesButton, new Insets(5));
        middleHBox.setSpacing(15);

        bottomHBox.setPadding(new Insets(5));
        bottomHBox.setMargin(processButton, new Insets(5));
        bottomHBox.setMargin(saveButton, new Insets(5));
        bottomHBox.setAlignment(Pos.TOP_RIGHT);
    }

    private static void setListeners(){

        startDatePicker.setOnAction(e -> {
            startDate = startDatePicker.getValue();
            endDate = startDate.plusDays(13);
            endDatePicker.setValue(startDate.plusDays(13));
        });

        endDatePicker.setOnAction(e -> {
            endDate = endDatePicker.getValue();
        });

        selectReportsButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select Payroll Reports");

            List<File> selectedFiles = fileChooser.showOpenMultipleDialog(container.getScene().getWindow());
            ObservableList<File> obsList = FXCollections.observableList(fileListView.getItems());
            if (selectedFiles != null) {
                for (File file : selectedFiles){
                    if (! obsList.contains(file))
                        obsList.add(file);
                }
                fileListView.setItems(obsList);
            }
            removeFilesButton.setDisable(false);
        });

        removeFilesButton.setOnAction(e -> {
            ObservableList<File> f = fileListView.getItems();
            f.removeAll((fileListView.getSelectionModel().getSelectedItems()));
            fileListView.setItems(f);
            fileListView.getSelectionModel().clearSelection();
            if (fileListView.getItems().isEmpty()) {
                removeFilesButton.setDisable(true);
            }
        });

        saveButton.setOnAction(e -> saveFile());

        processButton.setOnAction(e -> {
            String [] files = new String [6];
            String week1Dates = null;
            String week2Dates = null;
            List <File> fileList = null;

            if (fileListView.getItems() != null && fileListView.getItems().size() == 6){
                fileList = fileListView.getItems();
                for (int i = 0; i < fileList.size(); i++){
                    files[i] = fileList.get(i).toString();
                }
            }
            else {
                AlertBox.display("Error", "You must select 6 reports");
            }

            if (startDate != null){
                week1Dates = startDate.toString().replaceAll("-" , "_") + "-" + startDate.plusDays(6).toString().replaceAll("-" , "_");
            }

            if (endDate != null){
                week2Dates = endDate.minusDays(6).toString().replaceAll("-" , "_") + "-" + endDate.toString().replaceAll("-" , "_");
            }

            if (fileList != null && week1Dates != null && week2Dates != null){
                if (PayrollCalculator.checkReports(files, week1Dates, week2Dates)){
                    payrollCalculator = new PayrollCalculator(week1Dates, week2Dates);
                    payrollCalculator.processPayroll(files);
                    statusLabel.setText("Payroll processed");
                    isFileSaved = false;
                }
                else{
                    AlertBox.display("Error", "Selected dates and report dates do not match");
                }
            }

        });

        window.setOnCloseRequest(e -> {
            e.consume();
            if (! isFileSaved()){
                String response = CloseDialog.display();

                if (response == "Yes"){
                    window.close();
                }
                else if (response == "Save"){
                    saveFile();
                }
            }
            else{
                window.close();
            }
        });
    }

    private static boolean isFileSaved(){
        return isFileSaved;
    }

    private static void saveFile(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M-d-yyyy");
        if (payrollCalculator != null) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Report");
            fileChooser.setInitialFileName(startDate.format(formatter) + " - " + endDate.format(formatter) + ".xls");
            File file = fileChooser.showSaveDialog(container.getScene().getWindow());
            if (file != null) {
                payrollCalculator.saveFile(file.toString());
                isFileSaved = true;
            }
        }
        else{
            AlertBox.display("Error", "You have not processed your reports");
        }
    }

}
