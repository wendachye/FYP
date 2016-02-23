package com.example.wenda.tarucnfc.Domains;

public class BusSchedule {

    private String busScheduleID;
    private String backEndID;
    private String departure;
    private String destination;
    private String routeTime;
    private String routeDay;
    private String status;
    private int response;

    public int getResponse() {
        return response;
    }

    public void setResponse(int response) {
        this.response = response;
    }

    public String getBusScheduleID() {
        return busScheduleID;
    }

    public String getBackEndID() {
        return backEndID;
    }

    public String getDeparture() {
        return departure;
    }

    public String getDestination() {
        return destination;
    }

    public String getRouteTime() {
        return routeTime;
    }

    public String getRouteDay() {
        return routeDay;
    }

    public String getStatus() {
        return status;
    }

    public void setBusScheduleID(String busScheduleId) {
        this.busScheduleID = busScheduleId;
    }

    public void setBackEndID(String backEndID) {
        this.backEndID = backEndID;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setRouteTime(String routeTime) {
        this.routeTime = routeTime;
    }

    public void setRouteDay(String routeDay) {
        this.routeDay = routeDay;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

