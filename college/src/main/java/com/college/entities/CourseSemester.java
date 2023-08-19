package com.college.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "courseSemester")
public class CourseSemester {
    @Id
    private String id;
    private String courseId;
    private Integer semester;
    private List<String> subjectIdList;
    private Integer semesterYear;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public Integer getSemester() {
        return semester;
    }

    public void setSemester(Integer semester) {
        this.semester = semester;
    }

    public List<String> getSubjectIdList() {
        return subjectIdList;
    }

    public void setSubjectIdList(List<String> subjectIdList) {
        this.subjectIdList = subjectIdList;
    }

    public Integer getSemesterYear() {
        return semesterYear;
    }

    public void setSemesterYear(Integer semesterYear) {
        this.semesterYear = semesterYear;
    }
}
