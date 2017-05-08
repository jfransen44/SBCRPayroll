package com.company;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;

/**
 * Created by jeremyfransen on 5/3/17.
 */
public class PayrollCalculator {
    private Workbook payrollCalculator = null;
    private Sheet week1 = null;
    private Sheet week2 = null;
    private Sheet total = null;
    private PayrollHoursHandler reportsWeek1 = null;
    private PayrollHoursHandler reportsWeek2 = null;

    public PayrollCalculator(String week1ReportPath, String week2ReportPath, String excelSheetPath){
        reportsWeek1 = new PayrollHoursHandler(week1ReportPath);
        reportsWeek2 = new PayrollHoursHandler(week2ReportPath);
        openFile(excelSheetPath);
        getNames();
    }

    public void openFile(String filePath){
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


    public void addName(String name) {
        createRow();
        Row row = week1.getRow(week1.getLastRowNum());
        Cell cell = row.getCell(0);
        cell.setCellValue(name.toUpperCase());

        row = week2.getRow(week2.getLastRowNum());
        cell = row.getCell(0);
        cell.setCellValue(name.toUpperCase());
    }

    private void createRow(){
        //get last existing row to copy cells, then add new empty row
        Row prevRow1 = week1.getRow(week1.getLastRowNum());
        Row newRow1 = week1.createRow(week1.getLastRowNum() + 1);

        Row prevRow2 = week2.getRow(week2.getLastRowNum());
        Row newRow2 = week2.createRow(week2.getLastRowNum() + 1);

        Row prevRow3 = total.getRow(total.getLastRowNum() - 8);  //offset to account for rows after employees
        Row newRow3 = total.createRow(total.getLastRowNum() - 7);

        String prevLineNum = Integer.toString(newRow1.getRowNum());
        String newLineNum = Integer.toString(newRow1.getRowNum() + 1);
        String formula = "";

        //create row in sheets "Week 1 and Week 2"
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
        repairCellFormulas();
    }

    //adjust formulas in sheet "total" in totals section
    private void repairCellFormulas(){
        Row row = total.getRow(total.getLastRowNum() - 2);
        Cell cell = row.getCell(3);
        String formula = cell.getCellFormula();
        int num = Integer.parseInt(formula.substring(formula.length() - 3, formula.length() - 1));
        formula = formula.replace(Integer.toString(num), Integer.toString(num + 1));
        cell.setCellFormula(formula);

        row = total.getRow(total.getLastRowNum() - 3);
        cell = row.getCell(3);
        formula = cell.getCellFormula();
        formula = formula.replace(Integer.toString(num), Integer.toString(num + 1));
        cell.setCellFormula(formula);
    }

    /*private void checkNames(){
        ArrayList<String> week1Names = reports.getEmpNamesWeek1();
        ArrayList<String> week2Names = reports.getEmpNamesWeek2();

        for (int i = 0; i < week1Names.size(); i++){
            String name = week1Names.get(i);
            for (int j = 0; j < week1.getLastRowNum(); j++){

            }
        }
    }*/

    private void getNames(){
        ArrayList<String> week1Names = reportsWeek1.getEmpNames();
        ArrayList<String> week2Names = reportsWeek2.getEmpNames();
        week1Names.removeAll(week2Names);
        week1Names.addAll(week2Names);
        Collections.sort(week1Names);
        for (int i = 0; i < week1Names.size(); i++)
            System.out.println(week1Names.get(i));
    }

     // TODO: 5/8/17 delete unused employees
     // TODO: 5/8/17 sort alphabetically???
     // TODO: 5/8/17 save updated empty calculator??
     // TODO: 5/8/17 make single list of all names across both weeks - use for checking against calculator
}
