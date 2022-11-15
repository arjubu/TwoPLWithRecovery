package org.bu.database.twoplandrecovery;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Scanner;

public class RecoveryManager {

    List<String> logRecords = new ArrayList<>();
    char [] localStorageData;

    public List redo() throws IOException {

        int tid = 0;
        int did = 0;
        String operationType;

        List <String> activeTransactionList = new ArrayList<>();

        Scanner dataFileReader = new Scanner(new File("database.txt"));
        if(dataFileReader.hasNextLine())
            localStorageData = dataFileReader.nextLine().toCharArray();

        dataFileReader.close();

        Scanner csvReader = new Scanner(new File("log.csv"));
        //csvReader.useDelimiter(",");
        while (csvReader.hasNext()){
            logRecords.add(csvReader.next());
        }
        csvReader.close();
        if(logRecords.size()>0){
            for(String logRecord : logRecords){
                String [] logRecordValue = logRecord.split(",");
                if( logRecord.length() > 3){
                    tid = Integer.valueOf(logRecordValue[0]);
                    did = Integer.valueOf(logRecordValue[1]);
                    operationType = logRecordValue[2];
                }else{
                    tid = Integer.valueOf(logRecordValue[0]);
                    operationType = logRecordValue[1];
                }

                if(operationType.equals("S")){
                    activeTransactionList.add(String.valueOf(tid));
                }else if (operationType.equals("C") || operationType.equals("R")) {
                    activeTransactionList.remove(String.valueOf(tid));
                }else {
                    localStorageData [tid] = logRecordValue[3].charAt(0);
                }
            }
            FileWriter localStorageWrite  = new FileWriter("database.txt");
            localStorageWrite.write(new String(localStorageData));
            localStorageWrite.close();
        }
        return activeTransactionList;
    }

    public void undo( List activeTransactionList) throws IOException {
        int tid = 0;
        int did = 0;
        String operationType;

        if(logRecords.size()>0){
            for (int i = logRecords.size(); i-- > 0; ) {
                String [] logRecordValue = logRecords.get(i).split(",");
                if( logRecords.get(i).length() > 3){
                    tid = Integer.valueOf(logRecordValue[0]);
                    did = Integer.valueOf(logRecordValue[1]);
                    operationType = logRecordValue[2];
                }else{
                    tid = Integer.valueOf(logRecordValue[0]);
                    operationType = logRecordValue[1];
                }

                if(activeTransactionList.contains(tid) && operationType.equals("F")){
                    localStorageData [tid] = logRecordValue[4].charAt(0);
                }
            }
            FileWriter localStorageWrite  = new FileWriter("database.txt");
            localStorageWrite.write(new String(localStorageData));
            localStorageWrite.close();

            FileWriter writer = new FileWriter("log.csv");
            writer.close();
        }

    }
}
