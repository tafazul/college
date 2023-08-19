package com.college.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "teacher")
public class Teacher {
    @Id
    private String id;
    private String securityUserId;
    private String teacherName;
    private List<String> subjectIdList;

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

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public List<String> getSubjectIdList() {
        return subjectIdList;
    }

    public void setSubjectIdList(List<String> subjectIdList) {
        this.subjectIdList = subjectIdList;
    }
}
