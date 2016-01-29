package com.example.wenda.tarucnfc.Domains;

/**
 * Created by Wenda on 1/22/2016.
 */
public class ClassSchedule {

    private int classScheduleID;
    private int backendID;
    private String faculty;
    private String programme;
    private String groupNo;
    private String subject;
    private String tutorlecturer;
    private String location;
    private String day;
    private String startTime;
    private String endTime;

    public int getClassScheduleID() {
        return classScheduleID;
    }

    public int getBackendID() {
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

    public void setClassScheduleID(int classScheduleID) {
        this.classScheduleID = classScheduleID;
    }

    public void setBackendID(int backendID) {
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
