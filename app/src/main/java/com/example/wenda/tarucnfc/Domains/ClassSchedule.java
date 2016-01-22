package com.example.wenda.tarucnfc.Domains;

/**
 * Created by Wenda on 1/22/2016.
 */
public class ClassSchedule {

    private int classScheduleID;
    private int backendID;
    private String faculty;
    private String programme;
    private String group;
    private String subject;
    private String tutorlecturer;
    private String location;
    private String date;
    private String time;

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

    public String getGroup() {
        return group;
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

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
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

    public void setGroup(String group) {
        this.group = group;
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

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
