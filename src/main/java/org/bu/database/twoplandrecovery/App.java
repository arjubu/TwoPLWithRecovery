package org.bu.database.twoplandrecovery;



import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class App
{
    private final int PRIME_HASH = 991;

    StableStorageReadWriteManager stableStorageReadWriteManager = new StableStorageReadWriteManager();

    int  cycle = 0;
    int transactionSize = 0;
    double startProbability = 0;
    double writeProbability = 0;
    double rollbackProbability = 0;
    int timeout = 0;

    int transactionCount = 0;
    List<Transaction> transactionList =  new ArrayList();
    LinkedList[] dataHashTable = new LinkedList[32];
    LinkedList[] transactionHashTable = new LinkedList[PRIME_HASH];

    int originalDataValue = 0;
    int totalReadWrite = 0;

    LinkedList linkedList = new LinkedList();


    public static void main( String[] args ) throws  Exception {
        App app = new App();
        app.startSimulation(args);
    }

    public void startSimulation(String[] args) throws IOException {

        File createLog = new File("log.csv");
        createLog.createNewFile();

        File createDatabaseFile = new File("database.txt");
        createDatabaseFile.createNewFile();

        cycle = Integer.valueOf(args[0]);
        transactionSize = Integer.valueOf(args[1]);
        startProbability = Double.valueOf(args[2]);
        writeProbability = Double.valueOf(args[3]);
        rollbackProbability = Double.valueOf(args[4]);
        timeout = Integer.valueOf(args[5]);

        RecoveryManager  recoveryManager = new RecoveryManager();
        recoveryManager.undo(recoveryManager.redo());

        stableStorageReadWriteManager.writeInDatabaseFile(originalDataValue);
        for (int i=0; i<cycle; i++){
            generateTransaction();
            processActiveTransaction();
        }
    }
    public void generateTransaction() throws IOException {
        double randProbability = generateRandomProbability();
        if(randProbability <= startProbability){
            transactionList.add(new Transaction(transactionCount));
            stableStorageReadWriteManager.writeTransactionStartInLogFile(transactionCount);
            transactionCount = transactionCount + 1;
        }
    }
    public void processActiveTransaction() throws IOException {
        for(int i = 0; i< transactionList.size(); i++){
            if(transactionList.get(i).status == OperationType.ACTIVE){
                double randProbability = generateRandomProbability();

                if(randProbability <= writeProbability){
                    int dataId = generateRandomDataId();
                    processWriteOperation(transactionList.get(i).transactionId, dataId, false);
                    totalReadWrite += 1;
                } else if (randProbability <= writeProbability + rollbackProbability) {
                    processCommitOrRollbackOperation(transactionList.get(i).transactionId, OperationType.ROLLBACK);
                }else{
                    int dataId = generateRandomDataId();
                    processReadOperation(transactionList.get(i).transactionId, dataId, false);
                    totalReadWrite += 1;
                }

                if(transactionList.get(i).operationCount == transactionSize){
                    processCommitOrRollbackOperation(transactionList.get(i).transactionId, OperationType.COMMIT);
                }
                if( totalReadWrite == 25){
                    stableStorageReadWriteManager.writeInDatabaseFile(originalDataValue);
                }
            }else {
                transactionList.get(i).waitTime += 1;
                if(transactionList.get(i).status == OperationType.WAITING && transactionList.get(i).waitTime > timeout){
                    processCommitOrRollbackOperation(transactionList.get(i).transactionId, OperationType.ROLLBACK);
                }
            }
        }
    }

    public void doTransaction(int transactionId, int dataId, OperationType lockType, OperationType status, boolean isRollbackOrCommit) throws IOException {
        HashTable hashTableData = new HashTable(transactionId, dataId, lockType, status);

        if(isRollbackOrCommit == false){
            insertNodeInTransactionTableAndDataTable(hashTableData, true);
        }
        insertNodeInTransactionTableAndDataTable(hashTableData,false);

        if(transactionList.get(transactionId).status != status){
            transactionList.get(transactionId).waitTime = 0;
        }
        transactionList.get(transactionId).status = status;

        if(isRollbackOrCommit == false){
            transactionList.get(transactionId).operationCount += 1;
        }

        if(isRollbackOrCommit == false && lockType == OperationType.WRITE){
            int oldVal = (originalDataValue >> dataId) & 1;
            originalDataValue = originalDataValue ^ (1 << dataId);
            int newVal = (originalDataValue >> dataId) & 1;
            stableStorageReadWriteManager.writeTransactionWriteInLogFile(transactionId, dataId, newVal, oldVal);
        }
    }

    public void processWriteOperation(int transactionId, int dataId, boolean isRollbackOrCommit) throws IOException {
        LinkedList head = dataHashTable[dataId];
        while (head != null){
            if(head.data.transactionId != transactionId ){
                doTransaction(transactionId, dataId, OperationType.WRITE, OperationType.WAITING, isRollbackOrCommit);
                return;
            }
            head = head.next;
        }
        doTransaction(transactionId, dataId, OperationType.WRITE, OperationType.GRANTED, isRollbackOrCommit);
    }

    public void processReadOperation(int transactionId, int dataId, boolean isRollbackOrCommit) throws IOException {
        LinkedList head = dataHashTable[dataId];
        while (head  != null){
            if(head.data.status == OperationType.WAITING){
                doTransaction(transactionId, dataId, OperationType.READ, OperationType.WAITING, isRollbackOrCommit);
                return;
            }
            head = head.next;
        }
        head = dataHashTable[dataId];
        while (head != null){
            if(head.data.transactionId != transactionId && head.data.lockType == OperationType.WRITE){
                doTransaction(transactionId, dataId, OperationType.READ, OperationType.WAITING, isRollbackOrCommit);
                return;
            }
            head = head.next;
        }
        doTransaction(transactionId, dataId, OperationType.READ, OperationType.GRANTED, isRollbackOrCommit);
    }

    public void processTransactionForRollbackAndCommit() throws IOException {
        for (int i=0; i<32; i++){
            LinkedList head = dataHashTable[i];
            dataHashTable[i] = null;
            while (head != null){
                if(head.data.lockType == OperationType.READ){
                    processReadOperation(head.data.transactionId, head.data.dataId, true);
                }
                if(head.data.lockType == OperationType.WRITE){
                    processWriteOperation(head.data.transactionId, head.data.dataId, true);
                }
                head = head.next;
            }
        }
    }

    public void processCommitOrRollbackOperation(int transactionId, OperationType type) throws IOException {
        deleteNodeFromTransactionTableAndDataTable(transactionId);
        transactionList.get(transactionId).status = type;
        if(type == OperationType.ROLLBACK){
            stableStorageReadWriteManager.writeTransactionRollbackInLogFile(transactionId);
        }
        if(type == OperationType.COMMIT){
            stableStorageReadWriteManager.writeTransactionCommitInLogFile(transactionId);
        }
        processTransactionForRollbackAndCommit();
    }

    public void insertNodeInTransactionTableAndDataTable(HashTable hashTableData, boolean transactionTable){
        if(transactionTable){
            int idx = hashTableData.transactionId % PRIME_HASH;
            transactionHashTable[idx] = linkedList.insertNodeInLinkedList(transactionHashTable[idx],hashTableData);
        }else{
            int idx = hashTableData.dataId;
            dataHashTable[idx] = linkedList.insertNodeInLinkedList(dataHashTable[idx], hashTableData);
        }
    }

    public void deleteNodeFromTransactionTableAndDataTable(int transactionId){
        int idx = transactionId % PRIME_HASH;
        transactionHashTable[idx] = linkedList.deleteNodeFromLinkedList(transactionHashTable[idx],transactionId);
        for(int i = 0; i < 32; i++){
            dataHashTable[i] = linkedList.deleteNodeFromLinkedList(dataHashTable[i], transactionId);
        }
    }

    public double generateRandomProbability(){
        Random r = new Random();
        int randomInt = r.nextInt(101);
        return randomInt / 100.0;
    }

    public int generateRandomDataId(){
        Random r = new Random();
        int randomInt = r.nextInt(32);
        return randomInt;
    }

}
