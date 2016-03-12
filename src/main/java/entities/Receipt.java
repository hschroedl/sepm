package entities;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.util.Date;
import java.util.List;

public class Receipt {

    private int id;

    private Date date;
    private String receiver;
    private String receiverAddress;

    public Receipt(int id, Date date, String receiver, String receiverAddress, List<ReceiptEntry> receiptEntries) {
        this.id = id;
        this.date = date;
        this.receiver = receiver;
        this.receiverAddress = receiverAddress;
        this.receiptEntries = receiptEntries;
    }

    public List<ReceiptEntry> getReceiptEntries() {
        return receiptEntries;
    }

    public void setReceiptEntries(List<ReceiptEntry> receiptEntries) {
        this.receiptEntries = receiptEntries;
    }

    private List<ReceiptEntry> receiptEntries;

    public String getReceiverAddress() {
        return receiverAddress;
    }

    public void setReceiverAddress(String receiverAddress) {
        this.receiverAddress = receiverAddress;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
