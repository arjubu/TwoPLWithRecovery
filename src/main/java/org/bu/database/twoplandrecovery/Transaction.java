package org.bu.database.twoplandrecovery;

import java.util.ArrayList;
import java.util.List;

public class Transaction {
    int transactionId;
    int waitingTime;
    int numberOfOperation;
    boolean isBlocked;
    boolean isActive;
    int maximumNumberOfOperation;
    List<OperationManager> operationManagers = new ArrayList<>();

    public Transaction(int transactionId, int maximumNumberOfOperation ){
        this.transactionId = transactionId;
        this.maximumNumberOfOperation = maximumNumberOfOperation;
        this.waitingTime = 0;
        this.numberOfOperation = 0;
        this.isActive = true;
        this.isBlocked = false;
    }

    public void addOperation(OperationManager operationManager){
        operationManagers.add(operationManager);
        numberOfOperation++;
    }

    public  void blockOperation(){
        this.isBlocked = true;
    }

    public void unblockOperation(){
        this.isBlocked = false;
    }
    public void activateOperation(){
        this.isActive = true;
    }
    public void inActivateOperation(){
        this.isActive = false;
    }

}
