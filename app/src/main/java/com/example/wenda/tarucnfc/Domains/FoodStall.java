package com.example.wenda.tarucnfc.Domains;


import com.example.wenda.tarucnfc.InvalidInputException;

public class FoodStall {

    private String foodStallID;
    private String accountID;
    private String stallName;
    private String location;
    private String joinedDate;
    private String status;
    private String foodStallImagePath;
    private int response;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getFoodStallID() {
        return foodStallID;
    }

    public void setFoodStallID(String foodStallID) {
        this.foodStallID = foodStallID;
    }

    public String getAccountID() {
        return accountID;
    }

    public void setAccountID(String accountID) {
        this.accountID = accountID;
    }

    public String getStallName() {
        return stallName;
    }

    public void setStallName(String stallName) {
        this.stallName = stallName;
    }

    public String getJoinedDate() {
        return joinedDate;
    }

    public void setJoinedDate(String joinedDate) {
        this.joinedDate = joinedDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFoodStallImagePath() {
        return foodStallImagePath;
    }

    public void setFoodStallImagePath(String foodStallImagePath) {
        this.foodStallImagePath = foodStallImagePath;
    }

    public int getResponse() {
        return response;
    }

    public void setResponse(int response) {
        this.response = response;
    }

    public void verifyAccountID(String accountID) throws InvalidInputException {
        if(accountID.equals(""))
            throw new InvalidInputException("Please enter Account ID.");
        else if (accountID.length() > 10)
            throw new InvalidInputException("Account ID must be 10 character.");
        else if (accountID.length() < 10)
            throw new InvalidInputException("Account ID must be 10 character.");
        else
            this.accountID = accountID;
    }

    public void verifyStallName(String stallName) throws InvalidInputException {
        if(stallName.equals(""))
            throw new InvalidInputException("Please enter Stall Name.");
        else
            this.stallName = stallName;
    }
}
