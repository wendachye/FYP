package com.example.wenda.tarucnfc.Domains;

import android.graphics.Bitmap;
import android.text.TextUtils;

import com.example.wenda.tarucnfc.InvalidInputException;

public class Account {

    private String accountID;
    private String programme;
    private String groupNo;
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
    private String authorization;
    private int response;

    public int getResponse() {
        return response;
    }

    public void setResponse(int response) {
        this.response = response;
    }

    public String getGroupNo() {
        return groupNo;
    }

    public void setGroupNo(String groupNo) {
        this.groupNo = groupNo;
    }

    public String getAccountID() {
        return accountID;
    }

    public void setAccountID(String accountID) {
        this.accountID = accountID;
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

    public void verifyRecipientAccountID(String accountID) throws InvalidInputException {
        if(accountID.equals(""))
            throw new InvalidInputException("Please enter Recipient Account ID.");
        else if (accountID.length() > 10)
            throw new InvalidInputException("Recipient Account ID must be 10 character.");
        else if (accountID.length() < 10)
            throw new InvalidInputException("Recipient Account ID must be 10 character.");
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
            throw new InvalidInputException("Please enter Name.");
        else
            this.name = name;
    }

    public String getNRICNo() {
        return NRICNo;
    }

    public void setNRICNo(String NRICNo) {
        this.NRICNo = NRICNo;
    }

    public void verifyNRICNo(String NRICNo) throws InvalidInputException {
        if(NRICNo.equals(""))
            throw new InvalidInputException("Please enter NRIC.NO.");
        else if (NRICNo.length() > 12)
            throw new InvalidInputException("NRIC.NO must be 12 number.");
        else if (NRICNo.length() < 12)
            throw new InvalidInputException("NRIC.NO must be 12 number.");
        else
            this.NRICNo = NRICNo;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public void verifyContactNo(String contactNo) throws InvalidInputException {
        if(contactNo.equals(""))
            throw new InvalidInputException("Please enter Contact Number.");
        else if (contactNo.length() > 11)
            throw new InvalidInputException("Contact Number must be 11 number.");
        else if (contactNo.length() < 1)
            throw new InvalidInputException("Contact Number must be 11 number.");
        else
            this.contactNo = contactNo;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public void verifyEmail(String emailAddress) throws InvalidInputException {
        if(emailAddress.equals(""))
            throw new InvalidInputException("Please enter Email Address.");
        else if (isValidEmail(emailAddress) == false)
            throw new InvalidInputException("Invalid Email Address.");
        else
            this.emailAddress = emailAddress;
    }

    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
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

    public void verifyHomeAddress(String homeAddress) throws InvalidInputException {
        if(homeAddress.equals(""))
            throw new InvalidInputException("Please enter Home Address.");
        else
            this.homeAddress = homeAddress;
    }

    public String getCampusAddress() {
        return campusAddress;
    }

    public void setCampusAddress(String campusAddress) {
        this.campusAddress = campusAddress;
    }

    public void verifyCampusAddress(String campusAddress) throws InvalidInputException {
        if(campusAddress.equals(""))
            throw new InvalidInputException("Please enter Campus Address.");
        else
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

    public String getAuthorization() {
        return authorization;
    }

    public void setAuthorization(String authorization) {
        this.authorization = authorization;
    }
}
