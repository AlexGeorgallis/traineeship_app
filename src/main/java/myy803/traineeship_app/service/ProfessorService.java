package myy803.traineeship_app.service;

import myy803.traineeship_app.domain.Evaluation;
import myy803.traineeship_app.domain.Professor;
import myy803.traineeship_app.domain.TraineeshipPosition;

import java.util.List;

public interface ProfessorService {

    Professor getOrCreateProfile(String username);

    void updateProfile(String username, Professor formProfessor);

    // US14
    List<TraineeshipPosition> getSupervisedPositions(String username);

    // US15
    void saveProfessorEvaluation(String professorUsername, Integer positionId, Evaluation evaluation);
}

