package myy803.traineeship_app.service;

import myy803.traineeship_app.domain.Student;
import myy803.traineeship_app.domain.TraineeshipPosition;
import myy803.traineeship_app.mappers.StudentMapper;
import myy803.traineeship_app.mappers.TraineeshipPositionsMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommitteeServiceTest {

    @Mock
    private StudentMapper studentMapper;

    @Mock
    private TraineeshipPositionsMapper positionsMapper;

    @InjectMocks
    private CommitteeService committeeService;

    @Test
    void assignPositionToStudent_updatesStudentAndPosition() {
        String username = "student1";
        Integer positionId = 10;

        Student student = new Student(username);
        student.setLookingForTraineeship(true);

        TraineeshipPosition position = new TraineeshipPosition();
        position.setId(positionId);
        position.setAssigned(false);

        when(studentMapper.findByUsername(username)).thenReturn(student);
        when(positionsMapper.findById(positionId)).thenReturn(Optional.of(position));

        committeeService.assignPositionToStudent(positionId, username);

        assertTrue(position.isAssigned(), "Position should be marked as assigned");
        assertSame(student, position.getStudent(), "Position should reference the student");
        assertSame(position, student.getAssignedTraineeship(), "Student should reference the position");
        assertFalse(student.isLookingForTraineeship(), "Student should no longer be looking for a traineeship");

        verify(positionsMapper).save(position);
    }

    @Test
    void assignPositionToStudent_throwsWhenAlreadyAssigned() {
        String username = "student1";
        Integer positionId = 10;

        Student student = new Student(username);
        TraineeshipPosition position = new TraineeshipPosition();
        position.setId(positionId);
        position.setAssigned(true);

        when(studentMapper.findByUsername(username)).thenReturn(student);
        when(positionsMapper.findById(positionId)).thenReturn(Optional.of(position));

        assertThrows(IllegalStateException.class,
                () -> committeeService.assignPositionToStudent(positionId, username));

        verify(positionsMapper, never()).save(any());
    }

    @Test
    void completeTraineeship_setsPassFailFlag() {
        // given
        Integer positionId = 42;
        TraineeshipPosition position = new TraineeshipPosition();
        position.setId(positionId);
        position.setPassFailGrade(false);

        when(positionsMapper.findById(positionId)).thenReturn(Optional.of(position));

        committeeService.completeTraineeship(positionId, true);

        assertTrue(position.isPassFailGrade(), "Position should be marked as PASS");
        verify(positionsMapper).save(position);
    }
}
