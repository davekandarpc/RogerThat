package com.rogerthat.rlvltd.com.dto;

/**
 * Created by macbookair on 10/03/17.
 */

public class OrderDetailsSingleObj {

    private String lnNumber;
    private String category;
    private String particulars;
    private String quantity;
    private String deliveryDate;
    private String statusStage;


    public String getLnNumber() {
        return lnNumber;
    }

    public void setLnNumber(String lnNumber) {
        this.lnNumber = lnNumber;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getParticulars() {
        return particulars;
    }

    public void setParticulars(String particulars) {
        this.particulars = particulars;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getStatusStage() {
        return statusStage;
    }

    public void setStatusStage(String statusStage) {
        this.statusStage = statusStage;
    }
}
