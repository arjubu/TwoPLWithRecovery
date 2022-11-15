package org.bu.database.twoplandrecovery;

public class Transaction {
    int transactionId;
    int waitTime;
    int operationCount;
    OperationType status;

    public Transaction(int transactionId) {
        this.transactionId = transactionId;
        this.waitTime = 0;
        this.operationCount = 0;
        this.status = OperationType.ACTIVE;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public int getWaitTime() {
        return waitTime;
    }

    public void setWaitTime(int waitTime) {
        this.waitTime = waitTime;
    }

    public int getOperationCount() {
        return operationCount;
    }

    public void setOperationCount(int operationCount) {
        this.operationCount = operationCount;
    }

    public OperationType getStatus() {
        return status;
    }

    public void setStatus(OperationType status) {
        this.status = status;
    }
}
