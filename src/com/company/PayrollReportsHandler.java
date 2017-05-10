package com.company;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by jeremyfransen on 5/5/17.
 */
public class PayrollReportsHandler {

    private ArrayList<String> fileString = new ArrayList<>();
    private ArrayList<String> empNames = new ArrayList<>();
    private HashMap<String, Double> regHours = new HashMap<>();
    private HashMap<String, Double> overtimeHours = new HashMap<>();

    public PayrollReportsHandler(String file1){
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

    public HashMap<String, Double> getRegHours(){
        return regHours;
    }

    public HashMap<String, Double> getOvertimeHours(){
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

    private HashMap<String, Double> parseRegHours(ArrayList<String> file){
        HashMap<String, Double> regHours = new HashMap<>();

        for (int i = 0; i < file.size(); i++){
            String name = "";
            double hours = 0;
            String[] empInfo = file.get(i).split(",");
            name = empInfo[0].replace("\"", "") + "," + empInfo[1].replace("\"", "");
            name = name.toUpperCase();
            hours = round(Double.parseDouble(empInfo[3]), 2);
            regHours.put(name, hours);
        }

        return regHours;
    }

    private HashMap<String, Double> parseOTHours(ArrayList<String> file){
        HashMap<String, Double> otHours = new HashMap<>();

        for (int i = 0; i < file.size(); i++){
            String name = "";
            double hours = 0;
            String[] empInfo = file.get(i).split(",");
            name = empInfo[0].replace("\"", "") + "," + empInfo[1].replace("\"", "");
            name = name.toUpperCase();
            hours = round(Double.parseDouble(empInfo[4]), 2);
            otHours.put(name, hours);
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
