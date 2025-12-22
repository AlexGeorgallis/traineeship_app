package myy803.traineeship_app.service;

import myy803.traineeship_app.domain.Evaluation;
import myy803.traineeship_app.domain.EvaluationType;
import myy803.traineeship_app.domain.Professor;
import myy803.traineeship_app.domain.TraineeshipPosition;
import myy803.traineeship_app.mappers.ProfessorMapper;
import myy803.traineeship_app.mappers.TraineeshipPositionsMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProfessorServiceImplTest {

    @Mock
    private ProfessorMapper professorMapper;

    @Mock
    private TraineeshipPositionsMapper positionsMapper;

    @InjectMocks
    private ProfessorServiceImpl professorService;

    @Test
    void saveProfessorEvaluation_addsEvaluationForSupervisor() {
        String profUsername = "prof1";
        Integer positionId = 50;

        Professor supervisor = new Professor(profUsername);
        TraineeshipPosition position = new TraineeshipPosition();
        position.setId(positionId);
        position.setSupervisor(supervisor);

        Evaluation formEvaluation = new Evaluation();
        formEvaluation.setMotivation(4);
        formEvaluation.setEfficiency(5);
        formEvaluation.setEffectiveness(3);

        when(positionsMapper.findById(positionId)).thenReturn(Optional.of(position));

        professorService.saveProfessorEvaluation(profUsername, positionId, formEvaluation);

        assertNotNull(position.getEvaluations(), "Evaluations list should be initialized");
        assertEquals(1, position.getEvaluations().size(), "One evaluation should be stored");

        Evaluation stored = position.getEvaluations().get(0);
        assertEquals(EvaluationType.PROFESSOR_EVALUATION, stored.getEvaluationType());
        assertEquals(4, stored.getMotivation());
        assertEquals(5, stored.getEfficiency());
        assertEquals(3, stored.getEffectiveness());

        verify(positionsMapper).save(position);
    }

    @Test
    void saveProfessorEvaluation_throwsWhenNotSupervisor() {
        String profUsername = "prof1";
        Integer positionId = 51;

        Professor supervisor = new Professor("otherProf");
        TraineeshipPosition position = new TraineeshipPosition();
        position.setId(positionId);
        position.setSupervisor(supervisor);

        Evaluation formEvaluation = new Evaluation();
        formEvaluation.setMotivation(4);
        formEvaluation.setEfficiency(4);
        formEvaluation.setEffectiveness(4);

        when(positionsMapper.findById(positionId)).thenReturn(Optional.of(position));

        assertThrows(IllegalStateException.class,
                () -> professorService.saveProfessorEvaluation(profUsername, positionId, formEvaluation));

        verify(positionsMapper, never()).save(any());
    }
}
