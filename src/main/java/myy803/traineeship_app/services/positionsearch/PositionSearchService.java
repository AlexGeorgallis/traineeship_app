package myy803.traineeship_app.services.positionsearch;

import myy803.traineeship_app.domain.TraineeshipPosition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class PositionSearchService {
	private final PositionsSearchStrategy location;
	private final PositionsSearchStrategy interests;

	public PositionSearchService(
			@Qualifier("positionSearchLocation") PositionsSearchStrategy location,
			@Qualifier("positionSearchInterests") PositionsSearchStrategy interests
	) {
		this.location = location;
		this.interests = interests;
	}

	public List<TraineeshipPosition> search(String strategy, String studentUsername) {
		PositionsSearchStrategy s = "interests".equals(strategy) ? interests : location;
		return s.search(studentUsername);
	}
}




