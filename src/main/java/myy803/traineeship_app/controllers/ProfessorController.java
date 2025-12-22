package myy803.traineeship_app.controllers;

import myy803.traineeship_app.domain.Evaluation;
import myy803.traineeship_app.domain.Professor;
import myy803.traineeship_app.domain.TraineeshipPosition;
import myy803.traineeship_app.mappers.ProfessorMapper;
import myy803.traineeship_app.service.ProfessorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class ProfessorController {

    @Autowired
    private ProfessorService professorService;
    @Autowired
    private ProfessorMapper professorMapper;

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

    // US14
    @RequestMapping("/professor/list_traineeships")
    public String listSupervisedTraineeships(Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        List<TraineeshipPosition> positions = professorService.getSupervisedPositions(username);

        model.addAttribute("positions", positions);
        return "professor/traineeships";
    }

    // US15
    @RequestMapping("/professor/evaluate_position")
    public String showEvaluationForm(@RequestParam("positionId") Integer positionId,
                                     Model model) {

        Evaluation evaluation = new Evaluation();

        model.addAttribute("positionId", positionId);
        model.addAttribute("evaluation", evaluation);

        return "professor/evaluate_position";
    }

    // US15 – submit evaluation
    @RequestMapping("/professor/save_evaluation")
    public String saveEvaluation(@RequestParam("positionId") Integer positionId,
                                 @ModelAttribute("evaluation") Evaluation evaluation,
                                 RedirectAttributes redirectAttributes) {

        String username =
                SecurityContextHolder.getContext().getAuthentication().getName();

        try {
            professorService.saveProfessorEvaluation(username, positionId, evaluation);
            redirectAttributes.addFlashAttribute("message", "Evaluation saved successfully.");
        } catch (IllegalStateException | IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/professor/list_traineeships";
    }
}

