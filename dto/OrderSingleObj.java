package com.rogerthat.rlvltd.com.dto;

/**
 * Created by macbookair on 10/03/17.
 */

public class OrderSingleObj {

    private String soNumber;
    private String soDate;
    private String customerName;
    private String notes;
    private String status;

    public String getSoNumber() {
        return soNumber;
    }

    public void setSoNumber(String soNumber) {
        this.soNumber = soNumber;
    }

    public String getSoDate() {
        return soDate;
    }

    public void setSoDate(String soDate) {
        this.soDate = soDate;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
