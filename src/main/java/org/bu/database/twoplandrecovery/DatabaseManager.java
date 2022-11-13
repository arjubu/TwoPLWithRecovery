package org.bu.database.twoplandrecovery;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public  class  DatabaseManager {

    public static ArrayList readDatabase() throws FileNotFoundException {
        String dir = System.getProperty("user.dir");
        File file = new File(dir+"\\database.txt");
        Scanner scanner = new Scanner(file);
        ArrayList<Integer> databaseAsStream =  new ArrayList();
        String databaseString = scanner.next();
        for(int i =0; i<databaseString.length(); i++){
            databaseAsStream.add(Integer.parseInt(String.valueOf(databaseString.charAt(i))));
        }
        return databaseAsStream;
    }
}
