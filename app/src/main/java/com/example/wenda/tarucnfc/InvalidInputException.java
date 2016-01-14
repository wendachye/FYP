package com.example.wenda.tarucnfc;

/**
 * Created by Wenda on 1/13/2016.
 */
public class InvalidInputException extends Exception {
    private String info;

    public InvalidInputException(String info)
    {
        this.info=info;
    }

    public String getInfo()
    {
        return info;
    }
}