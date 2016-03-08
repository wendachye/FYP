package com.example.wenda.tarucnfc.Domains;

import com.example.wenda.tarucnfc.InvalidInputException;

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
    private String classType;

    public String getClassType() {
        return classType;
    }

    public void setClassType(String classType) {
        this.classType = classType;
    }

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

    public void verifySubject(String subject) throws InvalidInputException {
        if(subject.equals(""))
            throw new InvalidInputException("Please enter Subject.");
        else
            this.subject = subject;
    }

    public void verifyTutorlecturer(String tutorlecturer) throws InvalidInputException {
        if(tutorlecturer.equals(""))
            throw new InvalidInputException("Please enter Tutor or Lecturer Name.");
        else
            this.tutorlecturer = tutorlecturer;
    }

    public void verifyLocation(String location) throws InvalidInputException {
        if(location.equals(""))
            throw new InvalidInputException("Please enter Location.");
        else
            this.location = location;
    }
}
