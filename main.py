import csv

log_records_as_stream = []
active_transaction_list = []
data_stream_local_buffer = []


def redo():
    with open('log.csv', mode='r') as file:
        log_records = csv.reader(file, delimiter=',')
        for log_record in log_records:
            log_records_as_stream.append(log_record)
        file.close()

        data_stream = open('database.txt', 'r')
        global data_stream_local_buffer
        data_stream_local_buffer = list(data_stream.readline())
        data_stream.close()

        for log_record in log_records_as_stream:
            if len(log_record) > 2:
                tid = log_record[0]
                operation = log_record[2]
                did = log_record[1]
            else:
                tid = log_record[0]
                operation = log_record[1]

            if operation == "S":
                active_transaction_list.append(tid)
            elif operation == "C" or operation == "R":
                active_transaction_list.remove(tid)
            elif operation == "F":
                data_stream_local_buffer[int(did)] = log_record[3]
                # with open("log.csv", mode='a', newline='') as csv_file:
                # csv_writer = csv.writer(csv_file)
                # csv_writer.writerow([tid, did, operation, log_record[3], log_record[4]])
                # csv_file.close()

        new_database_file = open("database.txt", "w")
        new_database_file.write(''.join(data_stream_local_buffer))
        new_database_file.close()


def undo():
    for log_record in reversed(log_records_as_stream):
        if len(log_record) > 2:
            tid = log_record[0]
            operation = log_record[2]
            did = log_record[1]
        else:
            tid = log_record[0]
            operation = log_record[1]
        if tid in active_transaction_list and operation == 'F':
            data_stream_local_buffer[int(did)] = log_record[4]

    new_database_file = open("database.txt", "w")
    new_database_file.write(''.join(data_stream_local_buffer))
    new_database_file.close()

    filename = "log.csv"
    f = open(filename, "w+")
    f.close()


if __name__ == '__main__':
    redo()
    undo()
