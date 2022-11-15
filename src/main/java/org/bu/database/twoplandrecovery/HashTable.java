package org.bu.database.twoplandrecovery;

public class HashTable {
    int transactionId;
    int dataId;
    OperationType lockType;
    OperationType status;

    public HashTable(int transactionId, int dataId, OperationType lockType, OperationType status) {
        this.transactionId = transactionId;
        this.dataId = dataId;
        this.lockType = lockType;
        this.status = status;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public int getDataId() {
        return dataId;
    }

    public void setDataId(int dataId) {
        this.dataId = dataId;
    }

    public OperationType getLockType() {
        return lockType;
    }

    public void setLockType(OperationType lockType) {
        this.lockType = lockType;
    }

    public OperationType getStatus() {
        return status;
    }

    public void setStatus(OperationType status) {
        this.status = status;
    }
}
