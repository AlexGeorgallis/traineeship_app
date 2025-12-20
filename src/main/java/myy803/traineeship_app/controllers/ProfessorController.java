package myy803.traineeship_app.controllers;

import myy803.traineeship_app.domain.Professor;
import myy803.traineeship_app.service.ProfessorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ProfessorController {

    @Autowired
    private ProfessorService professorService;

    @RequestMapping("/professor/dashboard")
    public String getProfessorDashboard() {
        return "professor/dashboard";
    }

    @RequestMapping("/professor/profile")
    public String retrieveProfessorProfile(Model model) {
        String username =
                SecurityContextHolder.getContext().getAuthentication().getName();

        Professor professor = professorService.getOrCreateProfile(username);
        model.addAttribute("professor", professor);
        return "professor/profile";
    }

    @RequestMapping("/professor/save_profile")
    public String saveProfile(@ModelAttribute("profile") Professor formProfessor) {
        String username =
                SecurityContextHolder.getContext().getAuthentication().getName();

        professorService.updateProfile(username, formProfessor);
        return "professor/dashboard";
    }
}
