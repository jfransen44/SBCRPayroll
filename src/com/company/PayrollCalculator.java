package com.company;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by jeremyfransen on 5/3/17.
 */
public class PayrollCalculator {
    private Workbook payrollCalculator = null;
    private Sheet week1 = null;
    private Sheet week2 = null;
    private Sheet total = null;
    private PayrollHoursHandler reportsWeek1DLV = null;
    private PayrollHoursHandler reportsWeek2DLV = null;
    private PayrollHoursHandler reportsWeek1Fairview = null;
    private PayrollHoursHandler reportsWeek2Fairview = null;
    private PayrollHoursHandler reportsWeek1Ventura = null;
    private PayrollHoursHandler reportsWeek2Ventura = null;

    public PayrollCalculator(String week1DLVReportPath, String week2DLVReportPath, String week1GoletaReportPath,
                             String week2GoletaReportPath, String week1VenturaReportPath,
                             String week2VenturaReportPath, String excelSheetPath){
        reportsWeek1DLV = new PayrollHoursHandler(week1DLVReportPath);
        reportsWeek2DLV = new PayrollHoursHandler(week2DLVReportPath);
        reportsWeek1Fairview = new PayrollHoursHandler(week1GoletaReportPath);
        reportsWeek2Fairview = new PayrollHoursHandler(week2GoletaReportPath);
        reportsWeek1Ventura = new PayrollHoursHandler(week1VenturaReportPath);
        reportsWeek2Ventura = new PayrollHoursHandler(week2VenturaReportPath);
        openFile(excelSheetPath);
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
            //total.shiftRows(8, 15, 10);
            addNames();
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
        /*createRow();
        Row row = week1.getRow(week1.getLastRowNum());
        Cell cell = row.getCell(0);
        cell.setCellValue("fu");

        row = week2.getRow(week2.getLastRowNum());
        cell = row.getCell(0);
        cell.setCellValue("fu");

        createRow();
        Row row2 = week1.getRow(week1.getLastRowNum());
        Cell cell2 = row2.getCell(0);
        cell2.setCellValue("fu");

        row2 = week2.getRow(week2.getLastRowNum());
        cell2 = row2.getCell(0);
        cell2.setCellValue("fu");*/
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
        //get last existing row to copy cells, then add new empty row
        Row prevRow1 = week1.getRow(week1.getLastRowNum());
        Row newRow1 = week1.createRow(week1.getLastRowNum() + 1);

        Row prevRow2 = week2.getRow(week2.getLastRowNum());
        Row newRow2 = week2.createRow(week2.getLastRowNum() + 1);

        Row prevRow3 = total.getRow(total.getLastRowNum() - 8);  //8 offset to account for rows after employees
        Row newRow3 = total.createRow(total.getLastRowNum() - 7); // 7
        total.shiftRows(total.getLastRowNum() - 7, total.getLastRowNum(), 1);


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
        //repairCellFormulas();
    }

    //adjust formulas in sheet "total" in totals section
    private void repairCellFormulas(){

        //fix formulas for total hours (column c)
        //Total De La Vina
        Row row = total.getRow(total.getLastRowNum() - 3);
        Cell cell = row.getCell(3);
        String formula = cell.getCellFormula();
        int num = Integer.parseInt(formula.substring(formula.length() - 2, formula.length() - 1));
        formula = formula.replace(Integer.toString(num), Integer.toString(total.getLastRowNum() - 7));
        cell.setCellFormula(formula);

        //Total Fairview
        row = total.getRow(total.getLastRowNum() - 2);
        cell = row.getCell(3);
        formula = cell.getCellFormula();
        formula = formula.replace(Integer.toString(num), Integer.toString(total.getLastRowNum() - 7));
        cell.setCellFormula(formula);

        //Total Ventura
        row = total.getRow(total.getLastRowNum() - 1);
        cell = row.getCell(3);
        formula = cell.getCellFormula();
        formula = formula.replace(Integer.toString(num), Integer.toString(total.getLastRowNum() - 7));
        cell.setCellFormula(formula);

        //Total OT De La Vina
        row = total.getRow(total.getLastRowNum() - 5);
        cell = row.getCell(7);
        formula = cell.getCellFormula();
        formula = formula.replace(Integer.toString(num), Integer.toString(total.getLastRowNum() - 7));
        cell.setCellFormula(formula);

        //Total OT Fairview
        cell = row.getCell(9);
        formula = cell.getCellFormula();
        formula = formula.replace(Integer.toString(num), Integer.toString(total.getLastRowNum() - 7));
        cell.setCellFormula(formula);

        //Total OT Ventura
        cell = row.getCell(9);
        formula = cell.getCellFormula();
        formula = formula.replace(Integer.toString(num), Integer.toString(total.getLastRowNum() - 7));
        cell.setCellFormula(formula);
    }

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

     // TODO: 5/8/17 delete unused employees
     // TODO: 5/8/17 sort alphabetically???
     // TODO: 5/8/17 save updated empty calculator??
     // TODO: 5/8/17 make single list of all names across both weeks - use for checking against calculator
}
