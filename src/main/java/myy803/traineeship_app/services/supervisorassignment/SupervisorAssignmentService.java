package myy803.traineeship_app.services.supervisorassignment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class SupervisorAssignmentService {
	private final SupervisorAssignmentStrategy load;
	private final SupervisorAssignmentStrategy interests;

	public SupervisorAssignmentService(
			@Qualifier("supervisorAssignLoad") SupervisorAssignmentStrategy load,
			@Qualifier("supervisorAssignInterests") SupervisorAssignmentStrategy interests
	) {
		this.load = load;
		this.interests = interests;
	}

	public void assign(String strategy, Integer positionId) {
		SupervisorAssignmentStrategy s = "interests".equals(strategy) ? interests : load;
		s.assign(positionId);
	}
}



