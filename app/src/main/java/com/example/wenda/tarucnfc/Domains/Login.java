package com.example.wenda.tarucnfc.Domains;

import com.example.wenda.tarucnfc.InvalidInputException;


public class Login {
    private String loginID;
    private String accountID;
    private String password;
    private String previousPassword;
    private String confirmPassword;

    public String getLoginId() {
        return loginID;
    }

    public void setLoginId(String loginID) {
        this.loginID = loginID;
    }

    public void verifyLoginID(String loginID) throws InvalidInputException {
        if (loginID.equals(""))
            throw new InvalidInputException("Login ID can't be blank.");
        else if (loginID.length() < 7)
            throw new InvalidInputException("Login ID must be 7 numeric.");
        else if (loginID.length() > 7)
            throw new InvalidInputException("Login ID must be 7 numeric.");
        else
            this.loginID = loginID;
    }

    public String getAccountID() {
        return accountID;
    }

    public void setAccountID(String accountID) {
        this.accountID = accountID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void verifyPassword(String password) throws InvalidInputException {
        if (password.equals(""))
            throw new InvalidInputException("Password can't be blank.");
        else if (password.length() < 8)
            throw new InvalidInputException("Password must contain at least 8 characters.");
        //else if (isAlphaNumeric(password) == false)
            //throw new InvalidInputException("Password must including upper/lowercase alphabets and numbers.");
        else
            this.password = password;
    }

    public String getPreviousPassword() {
        return previousPassword;
    }

    public void setPreviousPassword(String previousPassword) {
        this.previousPassword = previousPassword;
    }

    public void verifyConfirmPassword(String confirmPassword) throws InvalidInputException {
        if (confirmPassword.equals(""))
            throw new InvalidInputException("Please enter new password again.");
        else if (confirmPassword.length() < 8)
            throw new InvalidInputException("New password must contain at least 8 characters.");
        //else if (isAlphaNumeric(confirmPassword) == false)
            //throw new InvalidInputException("Confirm Password must including upper/lowercase alphabets and numbers.");
        else if (!confirmPassword.equals(password))
            throw new InvalidInputException("Password and Confirm Password is not match.");
        else
            this.confirmPassword = confirmPassword;
    }
}

