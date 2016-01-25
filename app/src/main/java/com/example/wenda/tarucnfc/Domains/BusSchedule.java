package com.example.wenda.tarucnfc.Domains;

public class BusSchedule {

    private int busScheduleId;
    private int backEndId;
    private String departure;
    private String destination;
    private String routeTime;
    private String routeDay;
    private String status;

    public int getBusScheduleId() {
        return busScheduleId;
    }

    public int getBackEndId() {
        return backEndId;
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

    public void setBusScheduleId(int busScheduleId) {
        this.busScheduleId = busScheduleId;
    }

    public void setBackEndId(int backEndId) {
        this.backEndId = backEndId;
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

