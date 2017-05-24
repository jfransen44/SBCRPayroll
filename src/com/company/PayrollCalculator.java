package com.company;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;

import java.io.*;
import java.time.LocalDate;
import java.util.*;

/**
 * Created by jeremyfransen on 5/3/17.
 */
public class PayrollCalculator {
    private Workbook payrollCalculator = null;
    private Sheet week1 = null;
    private Sheet week2 = null;
    private Sheet total = null;
    private PayrollReport reportsWeek1DLV = null;
    private PayrollReport reportsWeek2DLV = null;
    private PayrollReport reportsWeek1Fairview = null;
    private PayrollReport reportsWeek2Fairview = null;
    private PayrollReport reportsWeek1Ventura = null;
    private PayrollReport reportsWeek2Ventura = null;
    private String week1Dates;
    private String week2Dates;

    public PayrollCalculator(/*String week1DLVReportPath, String week2DLVReportPath, String week1GoletaReportPath,
                             String week2GoletaReportPath, String week1VenturaReportPath,
                             String week2VenturaReportPath String[] files,*/ String week1Dates, String week2Dates){

        /*reportsWeek1DLV = new PayrollReport(week1DLVReportPath);
        reportsWeek2DLV = new PayrollReport(week2DLVReportPath);
        reportsWeek1Fairview = new PayrollReport(week1GoletaReportPath);
        reportsWeek2Fairview = new PayrollReport(week2GoletaReportPath);
        reportsWeek1Ventura = new PayrollReport(week1VenturaReportPath);
        reportsWeek2Ventura = new PayrollReport(week2VenturaReportPath);*/

        /*reportsWeek1DLV = new PayrollReport(files[0]);
        reportsWeek2DLV = new PayrollReport(files[1]);
        reportsWeek1Fairview = new PayrollReport(files[2]);
        reportsWeek2Fairview = new PayrollReport(files[3]);
        reportsWeek1Ventura = new PayrollReport(files[4]);
        reportsWeek2Ventura = new PayrollReport(files[5]);*/
        this.week1Dates = week1Dates;
        this.week2Dates = week2Dates;
        //System.out.println(verifyFiles(files));
        openFile("EmptyCalculator.xls");
    }


    //check dates in file names against user input dates, check that there are 2 files for each location - 1 for each week
    public static boolean checkReports(String[] files, String week1Dates, String week2Dates){
        HashMap<String, Integer> checkFiles = new HashMap<>();
        checkFiles.put("Santa Barbara", 0);
        checkFiles.put("Goleta", 0);
        checkFiles.put("Ventura", 0);

        for (String file : files){

            String fileLocation = PayrollReport.getLocation(file);
            if (file.contains(week1Dates)){
                switch (fileLocation){
                    case "Santa Barbara":
                        if (checkFiles.containsKey(fileLocation)){
                            int i = checkFiles.get(fileLocation);
                            checkFiles.replace(fileLocation, i, i + 1);
                        }
                        else {
                            checkFiles.put(fileLocation, 1);
                        }
                        break;
                    case "Goleta":
                        if (checkFiles.containsKey(fileLocation)){
                            int i = checkFiles.get(fileLocation);
                            checkFiles.replace(fileLocation, i, i + 1);
                        }
                        else {
                            checkFiles.put(fileLocation, 1);
                        }
                        break;
                    case "Ventura":
                        if (checkFiles.containsKey(fileLocation)){
                            int i = checkFiles.get(fileLocation);
                            checkFiles.replace(fileLocation, i, i + 1);
                        }
                        else {
                            checkFiles.put(fileLocation, 1);
                        }
                        break;
                }
            }
            else if (file.contains(week2Dates)){
                switch (fileLocation){
                    case "Santa Barbara":
                        if (checkFiles.containsKey(fileLocation)){
                            int i = checkFiles.get(fileLocation);
                            checkFiles.replace(fileLocation, i, i + 1);
                        }
                        else {
                            checkFiles.put(fileLocation, 1);
                        }
                        break;
                    case "Goleta":
                        if (checkFiles.containsKey(fileLocation)){
                            int i = checkFiles.get(fileLocation);
                            checkFiles.replace(fileLocation, i, i + 1);
                        }
                        else {
                            checkFiles.put(fileLocation, 1);
                        }
                        break;
                    case "Ventura":
                        if (checkFiles.containsKey(fileLocation)){
                            int i = checkFiles.get(fileLocation);
                            checkFiles.replace(fileLocation, i, i + 1);
                        }
                        else {
                            checkFiles.put(fileLocation, 1);
                        }
                        break;
                }
            }
        }

        //each location should have 1 file for each week
        for (Integer i : checkFiles.values()){
            if (i != 2)
                return false;
        }
        return true;
    }

    //create PayrollReport object for each report file
    private void processReports(String[] files){
        for (String file : files){
            String fileLocation = PayrollReport.getLocation(file);
            if (file.contains(week1Dates)){
                switch (fileLocation){
                    case "Santa Barbara":
                        reportsWeek1DLV = new PayrollReport(file);
                        break;
                    case "Goleta":
                        reportsWeek1Fairview = new PayrollReport(file);
                        break;
                    case "Ventura":
                        reportsWeek1Ventura = new PayrollReport(file);
                        break;
                }
            }
            else if (file.contains(week2Dates)){
                switch (fileLocation){
                    case "Santa Barbara":
                        reportsWeek2DLV = new PayrollReport(file);
                        break;
                    case "Goleta":
                        reportsWeek2Fairview = new PayrollReport(file);
                        break;
                    case "Ventura":
                        reportsWeek2Ventura = new PayrollReport(file);
                        break;
                }
            }
        }
    }


    //enter all data from each report into payrollCalculator workbook
    public void processPayroll(String[] files){
        processReports(files);

        addNames();

        enterRegHours(reportsWeek1DLV.getRegHours(), week1, "De La Vina");
        enterOTHours(reportsWeek1DLV.getOvertimeHours(), week1, "De La Vina");

        enterRegHours(reportsWeek2DLV.getRegHours(), week2, "De La Vina");
        enterOTHours(reportsWeek2DLV.getOvertimeHours(), week2, "De La Vina");

        enterRegHours(reportsWeek1Fairview.getRegHours(), week1, "Fairview");
        enterOTHours(reportsWeek1Fairview.getOvertimeHours(), week1, "Fairview");

        enterRegHours(reportsWeek2Fairview.getRegHours(), week2, "Fairview");
        enterOTHours(reportsWeek2Fairview.getOvertimeHours(), week2, "Fairview");

        enterRegHours(reportsWeek1Ventura.getRegHours(), week1, "Ventura");
        enterOTHours(reportsWeek1Ventura.getOvertimeHours(), week1, "Ventura");

        enterRegHours(reportsWeek2Ventura.getRegHours(), week2, "Ventura");
        enterOTHours(reportsWeek2Ventura.getOvertimeHours(), week2, "Ventura");
    }

    private void openFile(String filePath){
        try{
            InputStream inputStream = new FileInputStream(filePath);
            payrollCalculator =  WorkbookFactory.create(inputStream);

        }
        catch (FileNotFoundException e){
            e.printStackTrace();
        }
        catch (InvalidFormatException e){
            e.printStackTrace();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        finally {
            week1 = payrollCalculator.getSheet("WEEK 1");
            week2 = payrollCalculator.getSheet("WEEK 2");
            total = payrollCalculator.getSheet("TOTAL");
        }
    }

    public void saveFile(String filePath){
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(filePath);
            payrollCalculator.write(fileOutputStream);
            fileOutputStream.close();
            System.out.println("FILE SAVED SUCCESSFULLY");
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }


    public void addNames() {
        ArrayList<String> names = getNames();
        for (int i = 0; i < names.size(); i++){
            String name = names.get(i);
            createRow();
            Row row = week1.getRow(week1.getLastRowNum());
            Cell cell = row.getCell(0);
            cell.setCellValue(name.toUpperCase());

            row = week2.getRow(week2.getLastRowNum());
            cell = row.getCell(0);
            cell.setCellValue(name.toUpperCase());
        }
        repairCellFormulas();
    }

    private void createRow(){
        //get last existing row to copy cells, then add new empty row in each sheet
        Row prevRow1 = week1.getRow(week1.getLastRowNum());
        Row newRow1 = week1.createRow(week1.getLastRowNum() + 1);

        Row prevRow2 = week2.getRow(week2.getLastRowNum());
        Row newRow2 = week2.createRow(week2.getLastRowNum() + 1);

        Row prevRow3 = total.getRow(total.getLastRowNum() - 8);  // offset to account for rows after employees
        Row newRow3 = total.createRow(total.getLastRowNum() - 7);
        total.shiftRows(total.getLastRowNum() - 7, total.getLastRowNum(), 1);


        String prevLineNum = Integer.toString(newRow1.getRowNum());
        String newLineNum = Integer.toString(newRow1.getRowNum() + 1);
        String formula = "";

        //create cells in sheets "Week 1 and Week 2"
        for (int i = 0; i < prevRow1.getLastCellNum(); i++){
            Cell prevCell1 = prevRow1.getCell(i);
            CellStyle cs = prevCell1.getCellStyle();
            newRow1.createCell(i);
            Cell newCell1 = newRow1.getCell(i);
            newCell1.setCellStyle(cs);

            Cell prevCell2 = prevRow2.getCell(i);
            cs = prevCell2.getCellStyle();
            newRow2.createCell(i);
            Cell newCell2 = newRow2.getCell(i);
            newCell2.setCellStyle(cs);

            if (i == 7 || i == 8){
                formula = prevCell1.getCellFormula();
                formula = formula.replace(prevLineNum, newLineNum);
                newCell1.setCellFormula(formula);

                formula = prevCell2.getCellFormula();
                formula = formula.replace(prevLineNum, newLineNum);
                newCell2.setCellFormula(formula);
            }
        }

        //create cells in sheet "total"
        for (int i = 0; i < prevRow3.getLastCellNum(); i++){
            Cell prevCell = prevRow3.getCell(i);
            CellStyle cs = prevCell.getCellStyle();
            newRow3.createCell(i);
            Cell newCell = newRow3.getCell(i);
            newCell.setCellStyle(cs);

            if (i >= 2 && i < 12){
                formula = prevCell.getCellFormula();
                formula = formula.replace(prevLineNum, newLineNum);
                newCell.setCellFormula(formula);
            }
        }
    }

    //adjust formulas in sheet "total" in totals section
    private void repairCellFormulas(){
        Row row = null;
        String formula = "";
        Cell cell = null;
        int num = 0;

        //fix formulas for total hours (column D)
        for (int i = 1; i < 4; i++){
            row = total.getRow(total.getLastRowNum() - i);
            cell = row.getCell(3);
            formula = cell.getCellFormula();
            num = Integer.parseInt(formula.substring(formula.length() - 2, formula.length() - 1));
            formula = formula.replace(Integer.toString(num), Integer.toString(total.getLastRowNum() - 7));
            cell.setCellFormula(formula);
        }

        //fix formulas for Total OT
        row = total.getRow(total.getLastRowNum() - 5);
        for (int i = 7; i < 12; i += 2){
            cell = row.getCell(i);
            formula = cell.getCellFormula();
            formula = formula.replace(Integer.toString(num), Integer.toString(total.getLastRowNum() - 7));
            cell.setCellFormula(formula);
        }

        //reset total sum
        row = total.getRow(total.getLastRowNum());
        cell = row.getCell(3);
        formula = cell.getCellFormula();
        cell.setCellFormula(formula);
    }


    //collect, merge, and alphabetize all employee names
    private ArrayList<String> getNames(){
        ArrayList<String> names = reportsWeek1DLV.getEmpNames();
        names.removeAll(reportsWeek2DLV.getEmpNames());
        names.addAll(reportsWeek2DLV.getEmpNames());
        names.removeAll(reportsWeek1Fairview.getEmpNames());
        names.addAll(reportsWeek1Fairview.getEmpNames());
        names.removeAll(reportsWeek2Fairview.getEmpNames());
        names.addAll(reportsWeek2Fairview.getEmpNames());
        names.removeAll(reportsWeek1Ventura.getEmpNames());
        names.addAll(reportsWeek1Ventura.getEmpNames());
        names.removeAll(reportsWeek2Ventura.getEmpNames());
        names.addAll(reportsWeek2Ventura.getEmpNames());
        Collections.sort(names);
        return names;
    }

    private void enterRegHours(HashMap<String, Double> hours, Sheet sheet, String location){
        int cellNum = 0;

        switch (location){
            case "De La Vina":
                cellNum = 1;
                break;
            case "Fairview":
                cellNum = 3;
                break;
            case "Ventura":
                cellNum = 5;
                break;
        }

        for (int i = 8; i <= sheet.getLastRowNum(); i++){
            Double empHours = 0.00;
            String name;
            Row row = sheet.getRow(i);
            Cell cell = row.getCell(cellNum);
            name = row.getCell(0).getStringCellValue();
            if (hours.containsKey(name)) {
                empHours = hours.get(name);
            }
            cell.setCellValue(empHours);
        }
    }

    private void enterOTHours(HashMap<String, Double> otHours, Sheet sheet, String location){

        int cellNum = 0;

        switch (location){
            case "De La Vina":
                cellNum = 2;
                break;
            case "Fairview":
                cellNum = 4;
                break;
            case "Ventura":
                cellNum = 6;
                break;
        }

        for (int i = 8; i <= sheet.getLastRowNum(); i++){
            Double empHours = 0.00;
            String name;
            Row row = sheet.getRow(i);
            Cell cell = row.getCell(cellNum);
            name = row.getCell(0).getStringCellValue();
            if (otHours.containsKey(name)) {
                empHours = otHours.get(name);
            }
            cell.setCellValue(empHours);
        }
    }

    public String getPayrollDates(){
        final LocalDate START = LocalDate.of(2017, 5, 8);
        LocalDate test = LocalDate.of(2017, 5, 22);
        System.out.println(START.compareTo(test));
        return "";
    }
}
