package com.college.models;

import java.util.List;

public class CourseSemesterModel {
    private String courseId;
    private Integer semester;
    private List<String> subjectIdList;
    private Integer semesterYear;

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
