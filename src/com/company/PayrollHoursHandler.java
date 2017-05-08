package com.company;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

/**
 * Created by jeremyfransen on 5/5/17.
 */
public class PayrollHoursHandler {

    private ArrayList<String> fileString = new ArrayList<>();
    private ArrayList<String> empNames = new ArrayList<>();
    private ArrayList<Double> regHours = new ArrayList<>();
    private ArrayList<Double> overtimeHours = new ArrayList<>();

    public PayrollHoursHandler(String file1){
        fileString = openFile(file1);

        //Strip extra information
        for (int i = 0; i < 4; i ++){
            fileString.remove(0);
        }

        empNames = parseNames(fileString);
        regHours = parseRegHours(fileString);
        overtimeHours = parseOTHours(fileString);
    }

    public ArrayList<String> getEmpNames(){
        return empNames;
    }

    public ArrayList<Double> getRegHours(){
        return regHours;
    }

    public ArrayList<Double> getOvertimeHours(){
        return overtimeHours;
    }

    private ArrayList<String> openFile(String filePath){
        ArrayList<String> file = new ArrayList<>();
        BufferedReader br = null;
        FileReader fileReader = null;
        try{
            String line = "";
            fileReader = new FileReader(filePath);
            br = new BufferedReader(fileReader);
            while ((line = br.readLine()) != null){
                file.add(line);
            }

            br.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }

        return file;
    }

    private ArrayList<String> parseNames(ArrayList<String> file){

        ArrayList<String> names = new ArrayList<>();
        for (int i = 0; i < file.size(); i++){
            String name = "";
            int j = 1;
            while (file.get(i).charAt(j) != '"'){
                name += file.get(i).charAt(j);
                j++;
            }
            names.add(name.toUpperCase());
        }

        return names;
    }

    private ArrayList<Double> parseRegHours(ArrayList<String> file){
        ArrayList<Double> regHours = new ArrayList();
        for (int i = 0; i < file.size(); i++){
            double hours = 0;
            String[] empInfo = file.get(i).split(",");
            hours = round(Double.parseDouble(empInfo[3]), 2);
            regHours.add(hours);
        }
        return regHours;
    }

    private ArrayList<Double> parseOTHours(ArrayList<String> file){
        ArrayList<Double> otHours = new ArrayList<>();
        for (int i = 0; i < file.size(); i++){
            double hours = 0;
            String[] empInfo = file.get(i).split(",");
            hours = round(Double.parseDouble(empInfo[4]), 2);
            otHours.add(hours);
        }
        return otHours;
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
