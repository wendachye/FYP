package com.example.wenda.tarucnfc.Domains;


import android.graphics.Bitmap;

import com.example.wenda.tarucnfc.InvalidInputException;

public class FoodMenu {

    private String foodMenuID;
    private String foodStallID;
    private String foodCategory;
    private String foodDescription;
    private String foodName;
    private String foodPrice;
    private String foodGSTPrice;
    private String foodMenuImagePath;
    private Bitmap foodPictureBitmap;
    private int response;

    public Bitmap getFoodPictureBitmap() {
        return foodPictureBitmap;
    }

    public void setFoodPictureBitmap(Bitmap foodPictureBitmap) {
        this.foodPictureBitmap = foodPictureBitmap;
    }

    public String getFoodDescription() {
        return foodDescription;
    }

    public void setFoodDescription(String foodDescription) {
        this.foodDescription = foodDescription;
    }

    public void verifyFoodDescription(String foodDescription) throws InvalidInputException {
        if(foodDescription.equals(""))
            throw new InvalidInputException("Please enter Food Description.");
        else if(!foodDescription.matches("[a-zA-Z]+"))
            throw new InvalidInputException("Please enter Character Only in Food Description.");
        else
            this.foodDescription = foodDescription;
    }

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

    public void verifyFoodName(String foodName) throws InvalidInputException {
        if(foodName.equals(""))
            throw new InvalidInputException("Please enter Food Name.");
        else if(!foodName.matches("[a-zA-Z]+"))
            throw new InvalidInputException("Please enter Character Only in Food Name.");
        else
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
