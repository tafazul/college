package com.college.services;

import com.college.constants.UserTypes;
import com.college.entities.*;
import com.college.models.*;
import com.college.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MainService {
    @Autowired
    CourseRepository courseRepository;

    @Autowired
    TeacherRepository teacherRepository;

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    SubjectRepository subjectRepository;

    @Autowired
    CourseSemesterRepository courseSemesterRepository;

    @Autowired
    UserRepository userRepository;

    public String navToCourses(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SecurityUser securityUser = setUserNameInModelReturnUser(model);
        if(null == securityUser) {
            return "error";
        }
        List<CoursesResponse> courses = getCourses();
        model.addAttribute("courseList", courses);
        if(UserTypes.ADMIN.toString().equals(securityUser.getUserType())) {
            return "courses";
        } else {
            return "error";
        }
    }

    public String navToTeachers(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(null != authentication) {
            SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
            String userId = securityUser.getUserId();
            String userType = securityUser.getUserType();
            String name = securityUser.getUsername();
            model.addAttribute("name", name);
            List<Teacher> teacherList = teacherRepository.findAll();
            List<TeacherResponse> teacherResponseList = new ArrayList<>();
            Map<String, Subject> subjectMap = new HashMap<>();
            if(null != teacherList && !teacherList.isEmpty()) {
                List<String> subjectIdList = new ArrayList<>();
                for(Teacher t : teacherList) {
                    List<String> subjectIdList1 = t.getSubjectIdList();
                    if(null != subjectIdList1 && !subjectIdList1.isEmpty()) {
                        subjectIdList.addAll(subjectIdList1);
                    }
                }
                if(!subjectIdList.isEmpty()) {
                    List<Subject> allById = subjectRepository.findAllById(subjectIdList);
                    if(null != allById && !allById.isEmpty()) {
                        for(Subject subject : allById) {
                            subjectMap.put(subject.getId(), subject);
                        }
                    }
                }

                for (Teacher t : teacherList) {
                    TeacherResponse teacherResponse = new TeacherResponse();
                    teacherResponse.setTeacherName(t.getTeacherName());
                    List<String> subjectNames = new ArrayList<>();
                    if(null != t.getSubjectIdList() && !t.getSubjectIdList().isEmpty()) {
                        for(String subjectId : t.getSubjectIdList()) {
                            Subject subject = subjectMap.get(subjectId);
                            if(null != subject) {
                                subjectNames.add(subject.getSubjectName());
                            }
                        }
                    }
                    teacherResponse.setSubjects(subjectNames.isEmpty() ? "" : String.join(", ", subjectNames));
                    teacherResponseList.add(teacherResponse);
                }
            }
            model.addAttribute("teacherList", teacherResponseList);
            if(UserTypes.ADMIN.toString().equals(userType)) {
                return "teachers";
            } else {
                return "error";
            }
        }
        return "error";
    }

    public String navToStudents(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(null != authentication) {
            SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
            String userId = securityUser.getUserId();
            String userType = securityUser.getUserType();
            String name = securityUser.getUsername();
            model.addAttribute("name", name);
            List<StudentResponse> studentResponses = new ArrayList<>();
            List<Student> studentList = studentRepository.findAll();
            Map<String, Course> courseMap = new HashMap<>();
            if(null != studentList && !studentList.isEmpty()) {
                List<String> courseIdList = studentList.stream().map(student -> student.getCourseId()).collect(Collectors.toList());
                if(null != courseIdList && !courseIdList.isEmpty()) {
                    List<Course> coursesList = courseRepository.findAllById(courseIdList);
                    if(null != coursesList && !coursesList.isEmpty()) {
                        for(Course course : coursesList) {
                            courseMap.put(course.getId(), course);
                        }
                    }
                }
                for (Student student : studentList) {
                    StudentResponse studentResponse = new StudentResponse();
                    studentResponse.setStudentName(student.getStudentName());
                    studentResponse.setCurrentYear(student.getCurrentYear());
                    Course course = courseMap.get(student.getCourseId());
                    studentResponse.setCourseName(null != course ? course.getCourseName() : "");
                    studentResponses.add(studentResponse);
                }
            }
            model.addAttribute("studentList", studentResponses);
            if(UserTypes.ADMIN.toString().equals(userType)) {
                return "students";
            } else {
                return "error";
            }
        }
        return "error";
    }

    public String addCourse(CourseModel courseModel, BindingResult bindingResult, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(null != authentication) {
            SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
            String userId = securityUser.getUserId();
            String userType = securityUser.getUserType();
            String name = securityUser.getUsername();
            model.addAttribute("name", name);
            Course course = new Course();
            course.setCourseName(courseModel.getCourseName());
            course.setCourseDuration(courseModel.getCourseDuration());
            courseRepository.save(course);
            List<CoursesResponse> courses = getCourses();
            model.addAttribute("courseList", courses);
            if(UserTypes.ADMIN.toString().equals(userType)) {
                return "courses";
            } else {
                return "error";
            }
        }
        return "error";
    }

    public SecurityUser setUserNameInModelReturnUser(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(null != authentication) {
            SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
            String name = securityUser.getUsername();
            model.addAttribute("name", name);
            return securityUser;
        }
        return null;
    }

    public ModelAndView navAddCourse() {
        ModelAndView mav = new ModelAndView("addCourse");
        mav.addObject("courseModel", new CourseModel());
        mav.addObject("courseSemesterModel", new CourseSemesterModel());
        mav.addObject("subjectModel", new SubjectModel());
        List<Course> courseList = courseRepository.findAll();
        List<Subject> subjectList = subjectRepository.findAll();
        mav.addObject("courseList", courseList);
        mav.addObject("subjectList", subjectList);
        return mav;
    }

    public String addCourseSemesterSubmit(CourseSemesterModel courseSemesterModel, BindingResult bindingResult, Model model) {
        CourseSemester courseSemester = new CourseSemester();
        courseSemester.setCourseId(courseSemesterModel.getCourseId());
        courseSemester.setSemesterYear(courseSemesterModel.getSemesterYear());
        courseSemester.setSemester(courseSemesterModel.getSemester());
        courseSemester.setSubjectIdList(courseSemesterModel.getSubjectIdList());
        courseSemesterRepository.save(courseSemester);
        List<CoursesResponse> courses = getCourses();
        model.addAttribute("courseList", courses);
        return "courses";
    }

    public String addSubjectSubmit(SubjectModel subjectModel, BindingResult bindingResult, Model model) {
        Subject subject = new Subject();
        subject.setSubjectName(subjectModel.getSubjectName());
        subject.setMaxMarks(subjectModel.getMaxMarks());
        subjectRepository.save(subject);
        List<CoursesResponse> courses = getCourses();
        model.addAttribute("courseList", courses);
        return "courses";
    }

    public List<CoursesResponse> getCourses() {
        List<CoursesResponse> coursesResponseList = new ArrayList<>();
        List<Course> courseList = courseRepository.findAll();
        List<CourseSemester> allSemesters = courseSemesterRepository.findAll();
        Map<String, List<CourseSemester>> mapCourseSem = new HashMap<>();
        if(!allSemesters.isEmpty()) {
            for (CourseSemester courseSemester : allSemesters) {
                List<CourseSemester> courseSemesterList = new ArrayList<>();
                List<CourseSemester> orDefault = mapCourseSem.getOrDefault(courseSemester.getCourseId(), new ArrayList<>());
                courseSemesterList.addAll(orDefault);
                courseSemesterList.add(courseSemester);
                mapCourseSem.put(courseSemester.getCourseId(), courseSemesterList);
            }
        }
        if(!courseList.isEmpty()) {
            for (Course course : courseList) {
                CoursesResponse coursesResponse = new CoursesResponse();
                coursesResponse.setCourseName(course.getCourseName());
                coursesResponse.setCourseDuration(course.getCourseDuration());
                List<CourseSemester> courseSemesterList = mapCourseSem.getOrDefault(course.getId(), new ArrayList<>());
                Integer totalSubjects = 0;
                if(!courseSemesterList.isEmpty()) {
                    for (CourseSemester c : courseSemesterList) {
                        List<String> subjectIdList = c.getSubjectIdList();
                        if(null != subjectIdList && !subjectIdList.isEmpty()) {
                            totalSubjects = totalSubjects + subjectIdList.size();
                        }
                    }
                }
                coursesResponse.setCourseSemesters(courseSemesterList.size());
                coursesResponse.setCourseSubjects(totalSubjects);
                coursesResponseList.add(coursesResponse);
            }
        }
        return coursesResponseList;
    }

    public String addTeacherSubmit(TeacherModel teacherModel, BindingResult bindingResult, Model model) {
        User user = new User();
        user.setUsername(teacherModel.getUsername());
        user.setPassword(teacherModel.getPassword());
        user.setUserType(UserTypes.TEACHER.toString());
        User save = userRepository.save(user);
        Teacher teacher = new Teacher();
        teacher.setTeacherName(teacherModel.getTeacherName());
        teacher.setSecurityUserId(save.getId());
        teacher.setSubjectIdList(teacherModel.getSubjectIdList());
        teacherRepository.save(teacher);
        return navToTeachers(model);
    }

    public String addStudentSubmit(StudentModel studentModel, BindingResult bindingResult, Model model) {
        User user = new User();
        user.setPassword(studentModel.getPassword());
        user.setUsername(studentModel.getUsername());
        user.setUserType(UserTypes.STUDENT.toString());
        User save = userRepository.save(user);
        Student student = new Student();
        student.setSecurityUserId(save.getId());
        student.setStudentName(studentModel.getStudentName());
        student.setCourseId(studentModel.getCourseId());
        student.setCurrentYear(studentModel.getCurrentYear());
        studentRepository.save(student);
        return navToStudents(model);
    }
}
