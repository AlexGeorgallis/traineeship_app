package myy803.traineeship_app.services.positionsearch;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import myy803.traineeship_app.domain.Student;
import myy803.traineeship_app.domain.TraineeshipPosition;
import myy803.traineeship_app.mappers.StudentMapper;
import myy803.traineeship_app.mappers.TraineeshipPositionsMapper;

@Component("positionSearchInterests")
public class SearchBasedOnInterests extends AbstractPositionsSearchTemplate {

	private final TraineeshipPositionsMapper positionsMapper;

	public SearchBasedOnInterests(StudentMapper studentMapper, TraineeshipPositionsMapper positionsMapper) {
		super(studentMapper);
		this.positionsMapper = positionsMapper;
	}

	@Override
	protected void collectMatchingPositions(Student applicant, Set<TraineeshipPosition> out) {
		String interests = applicant.getInterests();
		if (interests == null || interests.isBlank()) return;

		String[] tokens = interests.split("[,\\s+\\.]");
		for (String t : tokens) {
			if (t.isBlank()) continue;
			out.addAll(positionsMapper.findByTopicsContainingAndIsAssignedFalse(t));
		}
	}
}

