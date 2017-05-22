package com.company;

import java.util.Scanner;

public class Main {

    static String[] files = {"/Users/jeremyfransen/Downloads/PayrollExport_2017_05_08-2017_05_14.csv",
                             "/Users/jeremyfransen/Downloads/PayrollExport_2017_05_08-2017_05_14-2.csv",
                             "/Users/jeremyfransen/Downloads/PayrollExport_2017_05_08-2017_05_14-3.csv",
                             "/Users/jeremyfransen/Downloads/PayrollExport_2017_05_15-2017_05_21.csv",
                             "/Users/jeremyfransen/Downloads/PayrollExport_2017_05_15-2017_05_21-2.csv",
                             "/Users/jeremyfransen/Downloads/PayrollExport_2017_05_15-2017_05_21-3.csv"};


    static String[] files2 = {"/Users/jeremyfransen/desktop/test files/PayrollExport_2017_04_24-2017_04_30-3.csv",
                              "/Users/jeremyfransen/desktop/test files/PayrollExport_2017_04_24-2017_04_30.csv",
                              "/Users/jeremyfransen/desktop/test files/PayrollExport_2017_04_24-2017_04_30-2.csv",
                              "/Users/jeremyfransen/desktop/test files/PayrollExport_2017_05_01-2017_05_07-2.csv",
                              "/Users/jeremyfransen/desktop/test files/PayrollExport_2017_05_01-2017_05_07.csv",
                              "/Users/jeremyfransen/desktop/test files/PayrollExport_2017_05_01-2017_05_07-3.csv"};
    static String file3 = "/Users/jeremyfransen/desktop/test files/PayrollExport_2017_05_01-2017_05_07-3.csv";

    static String date1 = "2017_05_08-2017_05_14";
    static String date2 = "2017_05_15-2017_05_21";
    public static void main(String[] args) {
        /*Scanner scanner = new Scanner(System.in);
        System.out.println("Enter file path for report: Week 1 DLV");
        files[0] = scanner.next();
        System.out.println("Enter file path for report: Week 2 DLV");
        files[1] = scanner.next();
        System.out.println("Enter file path for report: Week 1 Fairview");
        files[2] = scanner.next();
        System.out.println("Enter file path for report: Week 2 Fairview");
        files[3] = scanner.next();
        System.out.println("Enter file path for report: Week 1 Ventura");
        files[4] = scanner.next();
        System.out.println("Enter file path for report: Week 2 Ventura");
        files[5] = scanner.next();*/
        if (PayrollCalculator.checkReports(files, date1, date2)) {
            PayrollCalculator payrollCalculator = new PayrollCalculator(date1, date2);
            payrollCalculator.processReports(files);
            payrollCalculator.processPayroll();
            payrollCalculator.saveFile("/Users/jeremyfransen/documents/SBCR/PAYROLL/PAYROLL HISTORY/2017/5-8-17 - 5-21-17");
        } else
            System.out.println("FILE ERROR");
    }
}
