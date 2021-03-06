package com.example.wenda.tarucnfc.Domains;


import android.text.TextUtils;

import com.example.wenda.tarucnfc.InvalidInputException;

public class OfflineLogin {

    private int loginId;
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
    private String authorization;
    private String profilePicturePath;
    private int loginResponse;

    public String getGroupNo() {
        return groupNo;
    }

    public void setGroupNo(String groupNo) {
        this.groupNo = groupNo;
    }

    public void setLoginId(int loginId) {
        this.loginId = loginId;
    }

    public void setAccountID(String accountID) {
        this.accountID = accountID;
    }

    public void setProgramme(String programme) {
        this.programme = programme;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public void setCampus(String campus) {
        this.campus = campus;
    }

    public void setSchoolEmail(String schoolEmail) {
        this.schoolEmail = schoolEmail;
    }

    public void setSessionJoined(String sessionJoined) {
        this.sessionJoined = sessionJoined;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNRICNo(String NRICNo) {
        this.NRICNo = NRICNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setHomeAddress(String homeAddress) {
        this.homeAddress = homeAddress;
    }

    public void setCampusAddress(String campusAddress) {
        this.campusAddress = campusAddress;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public void setAccountBalance(String accountBalance) {
        this.accountBalance = accountBalance;
    }

    public void setPINcode(String PINcode) {
        this.PINcode = PINcode;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setProfilePicturePath(String profilePicturePath) {
        this.profilePicturePath = profilePicturePath;
    }

    public void set_Authorization(String authorization) {
        this.authorization = authorization;
    }

    public void setLoginResponse(int loginResponse) {
        this.loginResponse = loginResponse;
    }

    public int getLoginId() {
        return loginId;
    }

    public String getAccountID() {
        return accountID;
    }

    public String getProgramme() {
        return programme;
    }

    public String getFaculty() {
        return faculty;
    }

    public String getCampus() {
        return campus;
    }

    public String getSchoolEmail() {
        return schoolEmail;
    }

    public String getSessionJoined() {
        return sessionJoined;
    }

    public String getName() {
        return name;
    }

    public String getNRICNo() {
        return NRICNo;
    }

    public String getContactNo() {
        return contactNo;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getGender() {
        return gender;
    }

    public String getHomeAddress() {
        return homeAddress;
    }

    public String getCampusAddress() {
        return campusAddress;
    }

    public String getAccountType() {
        return accountType;
    }

    public String getAccountBalance() {
        return accountBalance;
    }

    public String getPINcode() {
        return PINcode;
    }

    public String getStatus() {
        return status;
    }

    public String getProfilePicturePath() {
        return profilePicturePath;
    }

    public String get_Authorization() {
        return authorization;
    }

    public int getLoginResponse() {
        return loginResponse;
    }

    public void verifyProgramme(String programme) throws InvalidInputException {
        if(programme.equals(""))
            throw new InvalidInputException("Please enter Programme.");
        else
            this.programme = programme;
    }

    public void verifyGroupNo(String groupNo) throws InvalidInputException {
        if(groupNo.equals(""))
            throw new InvalidInputException("Please enter Group No.");
        else
            this.groupNo = groupNo;
    }

    public void verifySchoolEmail(String schoolEmail) throws InvalidInputException {
        if(schoolEmail.equals(""))
            throw new InvalidInputException("Please enter School Email.");
        else if (isValidEmail(schoolEmail) == false)
            throw new InvalidInputException("Invalid School Email.");
        else
            this.schoolEmail = schoolEmail;
    }

    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
}
