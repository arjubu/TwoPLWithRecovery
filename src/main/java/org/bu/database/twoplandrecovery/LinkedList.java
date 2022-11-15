package org.bu.database.twoplandrecovery;

public class LinkedList {
    HashTable data;
    LinkedList next;

    public  LinkedList(){

    }
    public LinkedList insertNodeInLinkedList(LinkedList head, HashTable hashTableData){
        if(head == null){
            head = new LinkedList(hashTableData);
            return head;
        }
        LinkedList curr = head;
        while (curr.next != null){
            curr = curr.next;
        }
        curr.next = new LinkedList(hashTableData);
        return head;
    }

    public LinkedList deleteNodeFromLinkedList(LinkedList head, int trid){
        if(head == null){
            return head;
        }
        while (head != null && head.data.transactionId == trid){
            head = head.next;
        }
        if(head == null){
            return head;
        }
        LinkedList prev = head;
        LinkedList curr = head.next;

        while (curr != null){
            if(curr.data.transactionId == trid){
                prev.next = curr.next;
            }else{
                prev = curr;
            }
            curr = curr.next;
        }
        prev.next = null;
        return head;
    }

    public LinkedList(HashTable data) {
        this.data = data;
        this.next = null;
    }

    public HashTable getData() {
        return data;
    }

    public void setData(HashTable data) {
        this.data = data;
    }

    public LinkedList getNext() {
        return next;
    }

    public void setNext(LinkedList next) {
        this.next = next;
    }
}
