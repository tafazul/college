package com.college.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Document(collection = "student")
public class Student {
    @Id
    private String id;
    private String securityUserId;
    private String studentName;
    private String courseId;
    private Integer currentYear;
    private Map<String, Double> marksScoredBySubject;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSecurityUserId() {
        return securityUserId;
    }

    public void setSecurityUserId(String securityUserId) {
        this.securityUserId = securityUserId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public Integer getCurrentYear() {
        return currentYear;
    }

    public void setCurrentYear(Integer currentYear) {
        this.currentYear = currentYear;
    }

    public Map<String, Double> getMarksScoredBySubject() {
        return marksScoredBySubject;
    }

    public void setMarksScoredBySubject(Map<String, Double> marksScoredBySubject) {
        this.marksScoredBySubject = marksScoredBySubject;
    }
}
