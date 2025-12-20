package myy803.traineeship_app.services.positionsearch;

import myy803.traineeship_app.domain.Student;
import myy803.traineeship_app.domain.TraineeshipPosition;
import myy803.traineeship_app.mappers.StudentMapper;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

public abstract class AbstractPositionsSearchTemplate implements PositionsSearchStrategy {

    protected final StudentMapper studentMapper;

    protected AbstractPositionsSearchTemplate(StudentMapper studentMapper) {
        this.studentMapper = studentMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<TraineeshipPosition> search(String applicantUsername) {
        Student applicant = studentMapper.findByUsername(applicantUsername);
        if (applicant == null) return List.of();

        Set<TraineeshipPosition> result = new LinkedHashSet<>();

        collectMatchingPositions(applicant, result);

        return new ArrayList<>(result);
    }

    protected abstract void collectMatchingPositions(Student applicant, Set<TraineeshipPosition> out);
}

