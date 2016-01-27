package com.example.wenda.tarucnfc.Domains;

import android.graphics.Bitmap;

import com.example.wenda.tarucnfc.InvalidInputException;

public class Account {

    private String accountID;
    private String programme;
    private String faculty;
    private String campus;
    private String schoolEmail;
    private String sessionJoined;
    private String name;
    private String NRICNo;
    private String contactNo;
    private String emailAddress;
    private String gender;
    private String homeAddress;
    private String campusAddress;
    private String accountType;
    private String accountBalance;
    private String PINcode;
    private String status;
    private String profilePicturePath;
    private Bitmap profilePictureBitmap;

    public String getAccountID() {
        return accountID;
    }

    public void setAccountID(String accountID) {
        this.accountID = accountID;
    }

    public void verifyAccountID(String accountID) throws InvalidInputException {
        if(accountID.equals(""))
            throw new InvalidInputException("Please enter username.");
        //else if (isValidEmail(accountID) == false)
            //throw new InvalidInputException("Invalid username.");
        else
            this.accountID = accountID;
    }

    public String getFaculty() {
        return faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public String getSchoolEmail() {
        return schoolEmail;
    }

    public void setSchoolEmail(String schoolEmail) {
        this.schoolEmail = schoolEmail;
    }

    public String getProgramme() {
        return programme;
    }

    public void setProgramme(String programme) {
        this.programme = programme;
    }

    public String getCampus() {
        return campus;
    }

    public void setCampus(String campus) {
        this.campus = campus;
    }

    public String getSessionJoined() {
        return sessionJoined;
    }

    public void setSessionJoined(String sessionJoined) {
        this.sessionJoined = sessionJoined;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void verifyName(String name) throws InvalidInputException {
        if(name.equals(""))
            throw new InvalidInputException("Please enter name.");
        else
            this.name = name;
    }

    public String getNRICNo() {
        return NRICNo;
    }

    public void setNRICNo(String NRICNo) {
        this.NRICNo = NRICNo;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(String homeAddress) {
        this.homeAddress = homeAddress;
    }

    public String getCampusAddress() {
        return campusAddress;
    }

    public void setCampusAddress(String campusAddress) {
        this.campusAddress = campusAddress;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(String accountBalance) {
        this.accountBalance = accountBalance;
    }

    public String getPINcode() {
        return PINcode;
    }

    public void setPINcode(String PINcode) {
        this.PINcode = PINcode;
    }

    public void verifyPincode(String PINcode) throws InvalidInputException {
        if (PINcode.equals(""))
            throw new InvalidInputException("PIN Code can't be blank.");
        else if (PINcode.length() < 4)
            throw new InvalidInputException("PIN Code must contain 4 number only.");
        else if (PINcode.length() > 4)
            throw new InvalidInputException("PIN Code must contain 4 number only.");
            //else if (isAlphaNumeric(password) == false)
            //throw new InvalidInputException("Password must including upper/lowercase alphabets and numbers.");
        else
            this.PINcode = PINcode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Bitmap getProfilePictureBitmap() {
        return profilePictureBitmap;
    }

    public void setProfilePictureBitmap(Bitmap profilePictureBitmap) {
        this.profilePictureBitmap = profilePictureBitmap;
    }

    public String getProfilePicturePath() {
        return profilePicturePath;
    }

    public void setProfilePicturePath(String profilePicturePath) {
        this.profilePicturePath = profilePicturePath;
    }
}
