package myy803.traineeship_app.controllers;

import java.util.List;

import myy803.traineeship_app.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import myy803.traineeship_app.domain.Company;
import myy803.traineeship_app.domain.TraineeshipPosition;
import myy803.traineeship_app.mappers.CompanyMapper;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CompanyController {

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @RequestMapping("/company/dashboard")
    public String getCompanyDashboard() {
        return "company/dashboard";
    }

    @RequestMapping("/company/profile")
    public String retrieveCompanyProfile(Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Company company = companyService.getOrCreateProfile(username);
        model.addAttribute("company", company);
        return "company/profile";
    }

    @RequestMapping("/company/save_profile")
    public String saveProfile(@ModelAttribute("profile") Company company) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        companyService.updateProfile(company, username);
        return "company/dashboard";
    }

    @RequestMapping("/company/list_available_positions")
    public String listAvailablePositions(Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        model.addAttribute("positions", companyService.getAvailablePositions(username));
        return "company/available_positions";
    }

    @RequestMapping("/company/show_position_form")
    public String showPositionForm(Model model) {
        model.addAttribute("position", new TraineeshipPosition());
        return "company/position";
    }

    @RequestMapping("/company/save_position")
    public String savePosition(@ModelAttribute("position") TraineeshipPosition position) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        companyService.savePositionForCompany(username, position);
        return "redirect:/company/dashboard";
    }

    // US9
    @RequestMapping("/company/list_assigned_positions")
    public String listAssignedPositions(Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        model.addAttribute("positions", companyService.getAssignedPositions(username));
        return "company/assigned_positions";
    }

    // US11
    @RequestMapping("/company/delete_position")
    public String deletePosition(@RequestParam("positionId") Integer positionId,
                                 RedirectAttributes redirectAttributes) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        try {
            companyService.deletePosition(username, positionId);
        } catch (IllegalStateException | IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/company/list_available_positions";
    }

}

