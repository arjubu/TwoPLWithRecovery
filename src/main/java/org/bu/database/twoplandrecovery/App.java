package org.bu.database.twoplandrecovery;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.stream.Stream;

public class App
{
    int cycles;
    int transactionSize;
    int startProbability;
    int writeProbability;
    int rollbackProbability;
    int timeout = 0;
    ArrayList<Integer> databaseAsStream;

    ArrayList<Transaction> activeTransactionList = new ArrayList();

    public static void main( String[] args ) throws FileNotFoundException {
        App app = new App();
        app.inputArgumentsAndDatabaseAsStream();
        app.startTransaction();

    }

    public void startTransaction(){
        for (int i=0; i<cycles; i++){
            float randTransactionStartProbability = Float.valueOf(String.format("%.2f", new Random().nextFloat()));
            if(randTransactionStartProbability <= startProbability){
                Transaction transaction = new Transaction(activeTransactionList.size(), transactionSize);
                activeTransactionList.add(transaction);
            }

            for(Transaction transaction : activeTransactionList){

            }
        }
    }

    public void inputArgumentsAndDatabaseAsStream() throws FileNotFoundException {
        Scanner scanner =  new Scanner(System.in);
        String inputParamsString = scanner.nextLine();
        int [] inputParams = Stream.of(inputParamsString.split(" ")).mapToInt(Integer::parseInt).toArray();
        cycles = inputParams[0];
        transactionSize = inputParams[1];
        startProbability = inputParams[2];
        writeProbability = inputParams[3];
        rollbackProbability = inputParams[4];
        timeout = inputParams[5];
        databaseAsStream = new DatabaseManager().readDatabase();
    }

    public boolean




}
