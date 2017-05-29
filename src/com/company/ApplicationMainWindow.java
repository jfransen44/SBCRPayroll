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
 * Created by jeremyfransen on 5/23/17.
 */
public class ApplicationMainWindow extends VBox {


    private static VBox container;
    private static HBox topHBox, middleHBox, bottomHBox;
    private static Button selectReportsButton, processButton, removeFilesButton, saveButton;
    private static Label toOpenLabel, statusLabel;
    private static ListView<File> fileListView;
    private static DatePicker startDatePicker, endDatePicker;
    private static LocalDate startDate, endDate;
    private static PayrollCalculator payrollCalculator;


    public static void display(){

        Stage window = new Stage();
        container = new VBox();
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
        container.getChildren().addAll(topHBox, toOpenLabel, fileListView, statusLabel, middleHBox, bottomHBox);
        topHBox.getChildren().addAll(startDatePicker, endDatePicker, selectReportsButton);
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
        topHBox.setAlignment(Pos.TOP_CENTER);
        topHBox.setPadding(new Insets(5));
        topHBox.setMargin(startDatePicker, new Insets(5));
        topHBox.setMargin(endDatePicker, new Insets(5));
        topHBox.setMargin(selectReportsButton, new Insets(5));
        topHBox.setSpacing(25);

        /*startDatePicker.setPromptText("Start Date");
        endDatePicker.setPromptText("End Date");*/
        final LocalDate START_DATE_REFERENCE = LocalDate.of(2017, 5, 8);
        startDate = LocalDate.now();

        while (! startDate.getDayOfWeek().name().equals("MONDAY")){
            startDate = startDate.minusDays(1);
        }

        while ( DAYS.between(START_DATE_REFERENCE, startDate) % 14 != 0){
            startDate = startDate.minusDays(7);
        }
        
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
        });

        removeFilesButton.setOnAction(e -> {
            ObservableList<File> f = fileListView.getItems();
            f.removeAll((fileListView.getSelectionModel().getSelectedItems()));
            fileListView.setItems(f);
            fileListView.getSelectionModel().clearSelection();
        });

        saveButton.setOnAction(e -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            if (payrollCalculator != null) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Save Report");
                fileChooser.setInitialFileName(startDate.format(formatter) + " - " + endDate.format(formatter) + ".xls");
                File file = fileChooser.showSaveDialog(container.getScene().getWindow());
                if (file != null) {
                    payrollCalculator.saveFile(file.toString());
                }
            }
            else{
                AlertBox.display("Error", "You have not processed your reports");
            }
        });

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
                //System.out.println(week1Dates);
            }

            if (endDate != null){
                week2Dates = endDate.minusDays(6).toString().replaceAll("-" , "_") + "-" + endDate.toString().replaceAll("-" , "_");
                //System.out.println(week2Dates);
            }

            if (fileList != null && week1Dates != null && week2Dates != null){
                if (PayrollCalculator.checkReports(files, week1Dates, week2Dates)){
                    payrollCalculator = new PayrollCalculator(week1Dates, week2Dates);
                    payrollCalculator.processPayroll(files);
                    statusLabel.setText("Payroll processed");
                }
                else{
                    AlertBox.display("Error", "Selected dates and report dates do not match");
                }
            }

        });
    }
}
