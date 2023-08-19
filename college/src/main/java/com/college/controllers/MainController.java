package com.college.controllers;

import com.college.constants.UserTypes;
import com.college.entities.Course;
import com.college.entities.Student;
import com.college.entities.Subject;
import com.college.entities.Teacher;
import com.college.models.*;
import com.college.repositories.CourseRepository;
import com.college.repositories.StudentRepository;
import com.college.repositories.SubjectRepository;
import com.college.repositories.TeacherRepository;
import com.college.services.MainService;
import com.college.services.SecurityUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class MainController {

    @Autowired
    MainService mainService;

    @Autowired
    SubjectRepository subjectRepository;

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    TeacherRepository teacherRepository;

    @Autowired
    StudentRepository studentRepository;

    @GetMapping("/greeting")
    public String greeting(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) {
        model.addAttribute("name", name);
        return "greeting";
    }

    @GetMapping("/courses")
    public String courses(Model model) {
        return mainService.navToCourses(model);

    }

    @GetMapping("/teachers")
    public String teachers(Model model) {
        return mainService.navToTeachers(model);
    }

    @GetMapping("/students")
    public String students(Model model) {
        return mainService.navToStudents(model);
    }

    @GetMapping("/addCourse")
    public ModelAndView addCourse(Model model) {
        return mainService.navAddCourse();
    }

    @GetMapping("/addTeacher")
    public ModelAndView addTeacher(Model model) {
        ModelAndView mav = new ModelAndView("addTeacher");
        mav.addObject("teacherModel", new TeacherModel());
        List<Subject> subjectList = subjectRepository.findAll();
        mav.addObject("subjectList", subjectList);
        return mav;
    }

    @GetMapping("/addStudent")
    public ModelAndView addStudent(Model model) {
        ModelAndView mav = new ModelAndView("addStudent");
        mav.addObject("studentModel", new StudentModel());
        List<Course> all = courseRepository.findAll();
        mav.addObject("courseList", all);
        return mav;
    }

    @PostMapping("/addCourseSubmit")
    public String addCourseSubmit(@ModelAttribute CourseModel courseModel, BindingResult bindingResult, Model model) {
        return mainService.addCourse(courseModel, bindingResult, model);
    }

    @PostMapping("/addCourseSemesterSubmit")
    public String addCourseSemesterSubmit(@ModelAttribute CourseSemesterModel courseSemesterModel, BindingResult bindingResult, Model model) {
        return mainService.addCourseSemesterSubmit(courseSemesterModel, bindingResult, model);
    }

    @PostMapping("/addSubjectSubmit")
    public String addSubjectSubmit(@ModelAttribute SubjectModel subjectModel, BindingResult bindingResult, Model model) {
        return mainService.addSubjectSubmit(subjectModel, bindingResult, model);
    }

    @PostMapping("/addTeacherSubmit")
    public String addTeacherSubmit(@ModelAttribute TeacherModel teacherModel, BindingResult bindingResult, Model model) {
        return mainService.addTeacherSubmit(teacherModel, bindingResult, model);
    }

    @PostMapping("/addStudentSubmit")
    public String addStudentSubmit(@ModelAttribute StudentModel studentModel, BindingResult bindingResult, Model model) {
        return mainService.addStudentSubmit(studentModel, bindingResult, model);
    }

    @GetMapping("/")
    public String home(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(null != authentication) {
            SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
            String userId = securityUser.getUserId();
            String userType = securityUser.getUserType();
            String name = securityUser.getUsername();
            model.addAttribute("name", name);
            if(UserTypes.ADMIN.toString().equals(userType)) {
                return "admin";
            } else if(UserTypes.TEACHER.toString().equals(userType)) {
                Optional<Teacher> bySecurityUserId = teacherRepository.findBySecurityUserId(userId);
                if(bySecurityUserId.isPresent()) {
                    Teacher teacher = bySecurityUserId.get();
                    List<String> subjectIdList = teacher.getSubjectIdList();
                    String subjects = "";
                    if(null != subjectIdList && !subjectIdList.isEmpty()) {
                        List<Subject> allById = subjectRepository.findAllById(subjectIdList);
                        if(null != allById && !allById.isEmpty()) {
                            subjects = String.join(",", allById.stream().map(Subject::getSubjectName).collect(Collectors.toList()));
                        }
                    }
                    TeacherResponse teacherResponse = new TeacherResponse();
                    teacherResponse.setTeacherName(teacher.getTeacherName());
                    teacherResponse.setSubjects(subjects);
                    model.addAttribute("teacherDetails", teacherResponse);
                }
                return "teacher";
            } else if(UserTypes.STUDENT.toString().equals(userType)) {
                Optional<Student> bySecurityUserId = studentRepository.findBySecurityUserId(userId);
                if(bySecurityUserId.isPresent()) {
                    Student student = bySecurityUserId.get();
                    String courseId = student.getCourseId();
                    if (null != courseId) {
                        Optional<Course> byId = courseRepository.findById(courseId);
                        if(byId.isPresent()) {
                            Course course = byId.get();
                            StudentResponse studentResponse = new StudentResponse();
                            studentResponse.setCurrentYear(student.getCurrentYear());
                            studentResponse.setStudentName(student.getStudentName());
                            studentResponse.setCourseName(course.getCourseName());
                            model.addAttribute("studentDetails", studentResponse);
                        }
                    }
                }
                return "student";
            } else {
                return "error";
            }
        }
        return "error";
    }

}
