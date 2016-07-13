package com.example.wenda.tarucnfc.Domains;


public class Registration {

    private String registrationID;
    private String name;
    private String nric;
    private String contact;
    private String email;

    public String getRegistrationID() {
        return registrationID;
    }

    public String getName() {
        return name;
    }

    public String getNric() {
        return nric;
    }

    public String getContact() {
        return contact;
    }

    public String getEmail() {
        return email;
    }

    public void setRegistrationID(String registrationID) {
        this.registrationID = registrationID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNric(String nric) {
        this.nric = nric;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
