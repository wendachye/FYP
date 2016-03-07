package com.example.wenda.tarucnfc.Domains;

public class FoodOrder {

    private String foodOrderID;
    private String foodMenuID;
    private String foodTransactionID;
    private String itemQuantity;
    private String foodName;
    private String foodPrice;
    private String subTotal;
    private String totalPrice;
    private String GSTPrice;
    private String GrandTotal;
    private String paymentDateTime;

    public String getPaymentDateTime() {
        return paymentDateTime;
    }

    public void setPaymentDateTime(String paymentDateTime) {
        this.paymentDateTime = paymentDateTime;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getGrandTotal() {
        return GrandTotal;
    }

    public void setGrandTotal(String grandTotal) {
        GrandTotal = grandTotal;
    }

    public String getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(String subTotal) {
        this.subTotal = subTotal;
    }

    public String getGSTPrice() {
        return GSTPrice;
    }

    public void setGSTPrice(String GSTPrice) {
        this.GSTPrice = GSTPrice;
    }

    public String getFoodPrice() {
        return foodPrice;
    }

    public void setFoodPrice(String foodPrice) {
        this.foodPrice = foodPrice;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getFoodOrderID() {
        return foodOrderID;
    }

    public void setFoodOrderID(String foodOrderID) {
        this.foodOrderID = foodOrderID;
    }

    public String getFoodMenuID() {
        return foodMenuID;
    }

    public void setFoodMenuID(String foodMenuID) {
        this.foodMenuID = foodMenuID;
    }

    public String getFoodTransactionID() {
        return foodTransactionID;
    }

    public void setFoodTransactionID(String foodTransactionID) {
        this.foodTransactionID = foodTransactionID;
    }

    public String getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(String itemQuantity) {
        this.itemQuantity = itemQuantity;
    }
}
