package myy803.traineeship_app.services.supervisorassignment;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import myy803.traineeship_app.domain.Professor;
import myy803.traineeship_app.domain.TraineeshipPosition;
import myy803.traineeship_app.mappers.ProfessorMapper;
import myy803.traineeship_app.mappers.TraineeshipPositionsMapper;

@Component("supervisorAssignInterests")
public class AssignmentBasedOnInterests extends AbstractSupervisorAssignmentTemplate {

	public AssignmentBasedOnInterests(TraineeshipPositionsMapper positionsMapper, ProfessorMapper professorMapper) {
		super(positionsMapper, professorMapper);
	}

	@Override
	protected Professor chooseSupervisor(TraineeshipPosition position, List<Professor> professors) {
		String topicsStr = position.getTopics();
		if (topicsStr == null || topicsStr.isBlank()) return professors.get(0);

		String[] topics = topicsStr.split("[,\\s+\\.]");
		Professor candidate = null;

		for (Professor p : professors) {
			if (p.match(topics)) candidate = p; // your original behavior
		}
		return (candidate != null) ? candidate : professors.get(0);
	}
}

