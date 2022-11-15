package org.bu.database.twoplandrecovery;

import org.apache.commons.lang3.StringUtils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class StableStorageReadWriteManager {

    public void writeInDatabaseFile(int originalDataValue) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter("database.txt"));
        writer.write(StringUtils.leftPad(Integer.toBinaryString(originalDataValue), 32, '0'));
        writer.close();
    }

    public void writeTransactionStartInLogFile(int transactionId) throws IOException {
        FileWriter pw = new FileWriter("log.csv",true);
        pw.write(transactionId+","+"S\n");
        pw.close();
    }

    public void writeTransactionWriteInLogFile(int transactionId, int dataId, int newValue, int oldvalue) throws IOException {
        FileWriter pw = new FileWriter("log.csv",true);
        pw.write(transactionId+","+dataId+","+"F"+","+newValue+","+oldvalue+"\n");
        pw.close();
    }

    public void writeTransactionRollbackInLogFile(int transactionId) throws IOException {
        FileWriter pw = new FileWriter("log.csv",true);
        pw.write(transactionId+","+"R\n");
        pw.close();
    }

    public void writeTransactionCommitInLogFile(int transactionId) throws IOException {
        FileWriter pw = new FileWriter("log.csv",true);
        pw.write(transactionId+","+"C\n");
        pw.close();
    }
}
