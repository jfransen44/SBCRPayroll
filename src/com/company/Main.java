package com.company;

import java.util.Scanner;

public class Main {

    static String[] files = {"/Users/jeremyfransen/desktop/week1D.csv", "/Users/jeremyfransen/desktop/week2D.csv",
                             "/Users/jeremyfransen/desktop/week1F.csv", "/Users/jeremyfransen/desktop/week2F.csv",
                             "/Users/jeremyfransen/desktop/week1V.csv", "/Users/jeremyfransen/desktop/week2V.csv"};
    static String[] files2 = {"/Users/jeremyfransen/desktop/test files/PayrollExport_2017_04_24-2017_04_30-3.csv",
                              "/Users/jeremyfransen/desktop/test files/PayrollExport_2017_04_24-2017_04_30.csv",
                              "/Users/jeremyfransen/desktop/test files/PayrollExport_2017_04_24-2017_04_30-2.csv",
                              "/Users/jeremyfransen/desktop/test files/PayrollExport_2017_05_01-2017_05_07-2.csv",
                              "/Users/jeremyfransen/desktop/test files/PayrollExport_2017_05_01-2017_05_07.csv",
                              "/Users/jeremyfransen/desktop/test files/PayrollExport_2017_05_01-2017_05_07-3.csv"};

    static String date1 = "2017_04_24-2017_04_30";
    static String date2 = "2017_05_01-2017_05_07";

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
        if (PayrollCalculator.checkReports(files2, date1, date2)) {
            PayrollCalculator payrollCalculator = new PayrollCalculator(date1, date2);
            payrollCalculator.processReports(files2);
            payrollCalculator.processPayroll();
            payrollCalculator.saveFile("/Users/jeremyfransen/desktop/output2.xls");
        }
        else
            System.out.println("FILE ERROR");
    }
}
