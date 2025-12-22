package myy803.traineeship_app.service;

import myy803.traineeship_app.domain.Evaluation;
import myy803.traineeship_app.domain.EvaluationType;
import myy803.traineeship_app.domain.Professor;
import myy803.traineeship_app.domain.TraineeshipPosition;
import myy803.traineeship_app.mappers.ProfessorMapper;
import myy803.traineeship_app.mappers.TraineeshipPositionsMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class ProfessorServiceImpl implements ProfessorService {

    private final ProfessorMapper professorMapper;
    private final TraineeshipPositionsMapper traineeshipPositionsMapper;

    public ProfessorServiceImpl(ProfessorMapper professorMapper, TraineeshipPositionsMapper traineeshipPositionsMapper) {
        this.professorMapper = professorMapper;
        this.traineeshipPositionsMapper = traineeshipPositionsMapper;
    }

    @Override
    public Professor getOrCreateProfile(String username) {
        Professor professor = professorMapper.findByUsername(username);
        if (professor == null) {
            professor = new Professor(username);
        }
        return professor;
    }

    @Override
    @Transactional
    public void updateProfile(String username, Professor formProfessor) {

        Professor db = professorMapper.findByUsername(username);
        if (db == null) {
            db = new Professor(username);
        }

        db.setProfessorName(formProfessor.getProfessorName());
        db.setInterests(formProfessor.getInterests());

        professorMapper.save(db);
    }

    // US14
    @Override
    public List<TraineeshipPosition> getSupervisedPositions(String username) {
        Professor professor = professorMapper.findByUsername(username);
        if (professor == null || professor.getSupervisedPositions() == null) {
            return Collections.emptyList();
        }
        return professor.getSupervisedPositions();
    }

    // US15
    @Override
    @Transactional
    public void saveProfessorEvaluation(String professorUsername,
                                        Integer positionId,
                                        Evaluation formEvaluation) {

        TraineeshipPosition position = traineeshipPositionsMapper.findById(positionId)
                .orElseThrow(() -> new IllegalArgumentException("Position not found: " + positionId));

        if (position.getSupervisor() == null ||
                !professorUsername.equals(position.getSupervisor().getUsername())) {
            throw new IllegalStateException("You are not the supervisor of this traineeship.");
        }

        Evaluation evaluation = new Evaluation();
        evaluation.setEvaluationType(EvaluationType.PROFESSOR_EVALUATION);
        evaluation.setMotivation(formEvaluation.getMotivation());
        evaluation.setEfficiency(formEvaluation.getEfficiency());
        evaluation.setEffectiveness(formEvaluation.getEffectiveness());

        if (position.getEvaluations() == null) {
            position.setEvaluations(new ArrayList<>());
        }
        position.getEvaluations().add(evaluation);

        traineeshipPositionsMapper.save(position);
    }
}

