package myy803.traineeship_app.services.supervisorassignment;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import myy803.traineeship_app.domain.Professor;
import myy803.traineeship_app.domain.TraineeshipPosition;
import myy803.traineeship_app.mappers.ProfessorMapper;
import myy803.traineeship_app.mappers.TraineeshipPositionsMapper;

@Component("supervisorAssignLoad")
public class AssignmentBasedOnLoad extends AbstractSupervisorAssignmentTemplate {

	public AssignmentBasedOnLoad(TraineeshipPositionsMapper positionsMapper, ProfessorMapper professorMapper) {
		super(positionsMapper, professorMapper);
	}

	@Override
	protected Professor chooseSupervisor(TraineeshipPosition position, List<Professor> professors) {
		Professor candidate = professors.get(0);
		for (Professor p : professors) {
			if (p.compareLoad(candidate) >= 0) {
				candidate = p;
			}
		}
		return candidate;
	}
}

