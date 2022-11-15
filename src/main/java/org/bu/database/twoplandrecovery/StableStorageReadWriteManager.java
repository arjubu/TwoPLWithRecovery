package org.bu.database.twoplandrecovery;

import org.apache.commons.lang3.StringUtils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class StableStorageReadWriteManager {

    public void writeInDatabaseFile(int original_data_value) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter("database.txt"));
        writer.write(StringUtils.leftPad(Integer.toBinaryString(original_data_value), 32, '0'));
        writer.close();
    }

    public void writeTransactionStartInLogFile(int transactionId) throws IOException {
        FileWriter pw = new FileWriter("log.csv",true);
        pw.write(transactionId+","+"S\n");
        pw.close();
    }

    public void writeTransactionWriteInLogFile(int transactionId, int data_id, int new_val, int old_val) throws IOException {
        FileWriter pw = new FileWriter("log.csv",true);
        pw.write(transactionId+","+String.valueOf(data_id)+","+"F"+","+new_val+","+old_val+"\n");
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
