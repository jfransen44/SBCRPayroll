package com.company;

import java.util.Scanner;

public class Main {

    static String[] files = {"/Users/jeremyfransen/desktop/week1D.csv", "/Users/jeremyfransen/desktop/week2D.csv",
                             "/Users/jeremyfransen/desktop/week1F.csv", "/Users/jeremyfransen/desktop/week2F.csv",
                             "/Users/jeremyfransen/desktop/week1V.csv", "/Users/jeremyfransen/desktop/week2V.csv"};

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

        PayrollCalculator payrollCalculator = new PayrollCalculator(files);
        payrollCalculator.processPayroll();
        payrollCalculator.saveFile("/Users/jeremyfransen/desktop/output.xls");
    }
}
