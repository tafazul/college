package com.college.models;

public class CoursesResponse {
    private String courseName;
    private Integer courseDuration;
    private Integer courseSemesters;
    private Integer courseSubjects;

    public Integer getCourseSubjects() {
        return courseSubjects;
    }

    public void setCourseSubjects(Integer courseSubjects) {
        this.courseSubjects = courseSubjects;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public Integer getCourseDuration() {
        return courseDuration;
    }

    public void setCourseDuration(Integer courseDuration) {
        this.courseDuration = courseDuration;
    }

    public Integer getCourseSemesters() {
        return courseSemesters;
    }

    public void setCourseSemesters(Integer courseSemesters) {
        this.courseSemesters = courseSemesters;
    }
}
