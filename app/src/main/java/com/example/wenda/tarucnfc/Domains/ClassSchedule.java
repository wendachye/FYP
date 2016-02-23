package com.example.wenda.tarucnfc.Domains;

public class ClassSchedule {

    private String classScheduleID;
    private String backendID;
    private String faculty;
    private String programme;
    private String groupNo;
    private String subject;
    private String tutorlecturer;
    private String location;
    private String day;
    private String startTime;
    private String endTime;
    private String status;
    private int response;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getResponse() {
        return response;
    }

    public void setResponse(int response) {
        this.response = response;
    }

    public String getClassScheduleID() {
        return classScheduleID;
    }

    public String getBackendID() {
        return backendID;
    }

    public String getFaculty() {
        return faculty;
    }

    public String getProgramme() {
        return programme;
    }

    public String getGroupNo() {
        return groupNo;
    }

    public String getSubject() {
        return subject;
    }

    public String getTutorlecturer() {
        return tutorlecturer;
    }

    public String getLocation() {
        return location;
    }

    public String getDay() {
        return day;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setClassScheduleID(String classScheduleID) {
        this.classScheduleID = classScheduleID;
    }

    public void setBackendID(String backendID) {
        this.backendID = backendID;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public void setProgramme(String programme) {
        this.programme = programme;
    }

    public void setGroupNo(String groupNo) {
        this.groupNo = groupNo;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setTutorlecturer(String tutorlecturer) {
        this.tutorlecturer = tutorlecturer;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
