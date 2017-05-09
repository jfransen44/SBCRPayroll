package com.company;

public class Main {

    public static void main(String[] args) {
        // write your code here
        String calcPath = "/Users/jeremyfransen/Desktop/PRCALCTEST.xls";
        String week1D = "/Users/jeremyfransen/Desktop/Week1D.csv";
        String week2D = "/Users/jeremyfransen/Desktop/Week2D.csv";

        String week1F = "/Users/jeremyfransen/Desktop/Week1F.csv";
        String week2F = "/Users/jeremyfransen/Desktop/Week2F.csv";

        String week1V = "/Users/jeremyfransen/Desktop/Week1V.csv";
        String week2V = "/Users/jeremyfransen/Desktop/Week2V.csv";

        /*PayrollCalculator payrollCalculator = new PayrollCalculator();
        payrollCalculator.openFile(filePath);
        payrollCalculator.addName("FUCKFAXCE");
        payrollCalculator.saveFile(filePath);*/
        //String filePath = "/Users/jeremyfransen/Desktop/pr1.csv";
        //PayrollHoursHandler payrollHoursHandler = new PayrollHoursHandler(filePath);
        //payrollHoursHandler.openFile(filePath, filePath);
        PayrollCalculator payrollCalculator = new PayrollCalculator(week1D, week2D, week1F, week2F,
                week1V, week2V, calcPath);
        payrollCalculator.saveFile("/Users/jeremyfransen/Desktop/output.xls");
    }
}
