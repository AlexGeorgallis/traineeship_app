package myy803.traineeship_app.controllers;

import myy803.traineeship_app.domain.Student;
import myy803.traineeship_app.domain.TraineeshipPosition;
import myy803.traineeship_app.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    @RequestMapping("/student/logbook")
    public String showLogbookForm(Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Student student = studentService.getOrCreateByUsername(username);

        TraineeshipPosition position = student.getAssignedTraineeship();
        boolean hasAssigned = (position != null);

        String logText = "";
        if (hasAssigned && position.getStudentLogbook() != null) {
            logText = position.getStudentLogbook();
        }

        model.addAttribute("hasAssigned", hasAssigned);
        model.addAttribute("logText", logText);

        return "student/logbook";
    }

    @RequestMapping("/student/save_logbook")
    public String saveLogbook(@RequestParam("logText") String logText) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        studentService.updateLogbook(username, logText);
        return "redirect:/student/dashboard";
    }
}



