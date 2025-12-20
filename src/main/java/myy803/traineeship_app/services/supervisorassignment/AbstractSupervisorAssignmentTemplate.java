package myy803.traineeship_app.services.supervisorassignment;

import myy803.traineeship_app.domain.Professor;
import myy803.traineeship_app.domain.TraineeshipPosition;
import myy803.traineeship_app.mappers.ProfessorMapper;
import myy803.traineeship_app.mappers.TraineeshipPositionsMapper;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public abstract class AbstractSupervisorAssignmentTemplate implements SupervisorAssignmentStrategy {

    protected final TraineeshipPositionsMapper positionsMapper;
    protected final ProfessorMapper professorMapper;

    protected AbstractSupervisorAssignmentTemplate(TraineeshipPositionsMapper positionsMapper,
                                                   ProfessorMapper professorMapper) {
        this.positionsMapper = positionsMapper;
        this.professorMapper = professorMapper;
    }

    @Override
    @Transactional
    public void assign(Integer positionId) {
        TraineeshipPosition position = positionsMapper.findById(positionId).orElseThrow();
        List<Professor> professors = professorMapper.findAll();
        if (professors.isEmpty()) return;

        Professor supervisor = chooseSupervisor(position, professors);
        if (supervisor == null) return;

        position.setSupervisor(supervisor);
        supervisor.addPosition(position);

        positionsMapper.save(position);
    }

    protected abstract Professor chooseSupervisor(TraineeshipPosition position, List<Professor> professors);
}

