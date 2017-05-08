package com.company;

public class Main {

    public static void main(String[] args) {
        // write your code here
        String filePath = "/Users/jeremyfransen/Documents/SBCR/Payroll/PAYROLL CALCULATORS/PRCALCTEST.xls";
        /*PayrollCalculator payrollCalculator = new PayrollCalculator();
        payrollCalculator.openFile(filePath);
        payrollCalculator.addName("FUCKFAXCE");
        payrollCalculator.saveFile(filePath);*/
        //String filePath = "/Users/jeremyfransen/Desktop/pr1.csv";
        //PayrollHoursHandler payrollHoursHandler = new PayrollHoursHandler(filePath);
        //payrollHoursHandler.openFile(filePath, filePath);
        PayrollCalculator payrollCalculator = new PayrollCalculator("/Users/jeremyfransen/Desktop/Week1.csv",
                "/Users/jeremyfransen/Desktop/Week2.csv", filePath);
    }
}
