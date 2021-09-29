package com.rogerthat.rlvltd.com.dto;

/**
 * Created by macbookair on 10/03/17.
 */

public class PendingOrderSingleObj {

    private String reprtdate;
    private String orderDate;
    private String orderNumber;
    private String itemName;
    private String orderedQantity;
    private String balanceQantity;
    private String rate;
    private String value;
    private String dueOn;

    public String getReprtdate() {
        return reprtdate;
    }

    public void setReprtdate(String reprtdate) {
        this.reprtdate = reprtdate;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getOrderedQantity() {
        return orderedQantity;
    }

    public void setOrderedQantity(String orderedQantity) {
        this.orderedQantity = orderedQantity;
    }

    public String getBalanceQantity() {
        return balanceQantity;
    }

    public void setBalanceQantity(String balanceQantity) {
        this.balanceQantity = balanceQantity;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDueOn() {
        return dueOn;
    }

    public void setDueOn(String dueOn) {
        this.dueOn = dueOn;
    }
}
