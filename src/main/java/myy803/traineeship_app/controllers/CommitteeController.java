package myy803.traineeship_app.controllers;

import myy803.traineeship_app.services.positionsearch.PositionSearchService;
import myy803.traineeship_app.services.supervisorassignment.SupervisorAssignmentService;
import myy803.traineeship_app.service.CommitteeService;
import myy803.traineeship_app.domain.TraineeshipPosition;
import myy803.traineeship_app.domain.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class CommitteeController {

    @Autowired private PositionSearchService positionSearchService;
    @Autowired private SupervisorAssignmentService supervisorAssignmentService;
    @Autowired private CommitteeService committeeService;

    @RequestMapping("/committee/dashboard")
    public String getCommitteeDashboard() {
        return "committee/dashboard";
    }

    @RequestMapping("/committee/list_traineeship_applications")
    public String listTraineeshipApplications(Model model) {
        List<Student> traineeshipApplications =
                committeeService.getPendingApplications();

        model.addAttribute("traineeship_applications", traineeshipApplications);
        return "committee/traineeship_applications";
    }

    @RequestMapping("/committee/list_assigned_traineeships")
    public String listAssignedTraineeships(Model model) {
        model.addAttribute("positions", committeeService.getInProgressPositions());
        return "committee/list_assigned_traineeships";
    }

    @RequestMapping("/committee/find_positions")
    public String findPositions(
            @RequestParam("selected_student_id") String studentUsername,
            @RequestParam("strategy") String strategy,
            Model model) {

        List<TraineeshipPosition> positions =
                positionSearchService.search(strategy, studentUsername);

        model.addAttribute("positions", positions);
        model.addAttribute("student_username", studentUsername);

        return "committee/available_positions";
    }

    @RequestMapping("/committee/assign_position")
    public String assignPosition(
            @RequestParam("selected_position_id") Integer positionId,
            @RequestParam("applicant_username") String studentUsername,
            Model model) {

        committeeService.assignPositionToStudent(positionId, studentUsername);

        model.addAttribute("position_id", positionId);
        return "committee/supervisor_assignment";
    }

    @RequestMapping("/committee/assign_supervisor")
    public String assignSupervisor(
            @RequestParam("selected_position_id") Integer positionId,
            @RequestParam("strategy") String strategy,
            RedirectAttributes redirectAttributes) {

        try {
            supervisorAssignmentService.assign(strategy, positionId);
            redirectAttributes.addFlashAttribute(
                    "message",
                    "Supervisor assigned successfully for traineeship " + positionId
            );
        } catch (IllegalArgumentException | IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/committee/list_assigned_traineeships";
    }

    // US21
    @RequestMapping("/committee/view_traineeship")
    public String viewTraineeship(
            @RequestParam("positionId") Integer positionId,
            Model model) {

        TraineeshipPosition position = committeeService.getPositionById(positionId);

        model.addAttribute("position", position);
        model.addAttribute("evaluations", position.getEvaluations());

        return "committee/traineeship_details";
    }

    // === US21: complete with pass/fail ===
    @RequestMapping("/committee/complete_traineeship")
    public String completeTraineeship(
            @RequestParam("positionId") Integer positionId,
            @RequestParam("decision") String decision,
            RedirectAttributes redirectAttributes) {

        boolean pass = "PASS".equalsIgnoreCase(decision);

        committeeService.completeTraineeship(positionId, pass);

        redirectAttributes.addFlashAttribute(
                "message",
                "Traineeship " + positionId + " completed with final grade: " + (pass ? "PASS" : "FAIL")
        );

        return "redirect:/committee/list_assigned_traineeships";
    }
}
