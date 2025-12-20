package myy803.traineeship_app.controllers;

import myy803.traineeship_app.domain.Student;
import myy803.traineeship_app.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class StudentController {

    @Autowired
    private StudentService studentService;

    @RequestMapping("/student/dashboard")
    public String getStudentDashboard() {
        return "student/dashboard";
    }

    @RequestMapping("/student/profile")
    public String retrieveStudentProfile(Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Student student = studentService.getOrCreateByUsername(username);
        model.addAttribute("student", student);
        return "student/profile";
    }

    @RequestMapping("/student/save_profile")
    public String saveProfile(@ModelAttribute("student") Student formStudent) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        studentService.updateStudentProfile(username, formStudent);
        return "redirect:/student/dashboard";
    }
}



