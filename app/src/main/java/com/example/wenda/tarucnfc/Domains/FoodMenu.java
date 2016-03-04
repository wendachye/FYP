package com.example.wenda.tarucnfc.Domains;


public class FoodMenu {

    private String foodMenuID;
    private String foodStallID;
    private String foodCategory;
    private String foodName;
    private String foodPrice;
    private String foodGSTPrice;
    private String foodMenuImagePath;
    private int response;

    public String getFoodMenuID() {
        return foodMenuID;
    }

    public void setFoodMenuID(String foodMenuID) {
        this.foodMenuID = foodMenuID;
    }

    public String getFoodStallID() {
        return foodStallID;
    }

    public void setFoodStallID(String foodStallID) {
        this.foodStallID = foodStallID;
    }

    public String getFoodCategory() {
        return foodCategory;
    }

    public void setFoodCategory(String foodCategory) {
        this.foodCategory = foodCategory;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getFoodPrice() {
        return foodPrice;
    }

    public void setFoodPrice(String foodPrice) {
        this.foodPrice = foodPrice;
    }

    public String getFoodGSTPrice() {
        return foodGSTPrice;
    }

    public void setFoodGSTPrice(String foodGSTPrice) {
        this.foodGSTPrice = foodGSTPrice;
    }

    public String getFoodMenuImagePath() {
        return foodMenuImagePath;
    }

    public void setFoodMenuImagePath(String foodMenuImagePath) {
        this.foodMenuImagePath = foodMenuImagePath;
    }

    public int getResponse() {
        return response;
    }

    public void setResponse(int response) {
        this.response = response;
    }
}
