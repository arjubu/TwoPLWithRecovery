package org.bu.database.twoplandrecovery;

public class OperationManager {
    int transactionId;
    OperationType operationType;
    int dataId;

    public OperationManager(int transactionId, OperationType operationType, int dataId){
        this.transactionId = transactionId;
        this. operationType = operationType;
        this.dataId =  dataId;
    }

    public int getTransactionId(){
        return transactionId;
    }
    public  OperationType getOperationType(){
        return  operationType;
    }
}
