package myy803.traineeship_app.service;

import myy803.traineeship_app.domain.Company;
import myy803.traineeship_app.domain.Evaluation;
import myy803.traineeship_app.domain.TraineeshipPosition;

import java.util.List;

public interface CompanyService {

    Company getOrCreateProfile(String username);

    void updateProfile(Company formCompany, String username);

    List<TraineeshipPosition> getAvailablePositions(String username);

    void savePositionForCompany(String username, TraineeshipPosition position);

    // US9
    List<TraineeshipPosition> getAssignedPositions(String username);

    // US11
    void deletePosition(String username, Integer positionId);

    // US12
    void createCompanyEvaluation(String username, Integer positionId, Evaluation evaluation);

}

