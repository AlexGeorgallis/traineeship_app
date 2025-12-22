package myy803.traineeship_app.service;

import myy803.traineeship_app.domain.Company;
import myy803.traineeship_app.domain.TraineeshipPosition;
import myy803.traineeship_app.mappers.CompanyMapper;
import myy803.traineeship_app.mappers.TraineeshipPositionsMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompanyServiceImplTest {

    @Mock
    private CompanyMapper companyMapper;

    @Mock
    private TraineeshipPositionsMapper positionsMapper;

    @InjectMocks
    private CompanyServiceImpl companyService;

    @Test
    void savePositionForCompany_createsAndLinksPosition() {
        String username = "company1";
        Company company = new Company(username);
        company.setPositions(new ArrayList<>());

        TraineeshipPosition position = new TraineeshipPosition();
        position.setTitle("Backend Trainee");

        when(companyMapper.findByUsername(username)).thenReturn(company);

        companyService.savePositionForCompany(username, position);

        assertSame(company, position.getCompany(), "Position should reference company");
        assertEquals(1, company.getPositions().size(), "Company should have one position");
        assertSame(position, company.getPositions().get(0));

        verify(companyMapper).save(company);
    }

    @Test
    void deletePosition_succeedsForOwnUnassignedPosition() {
        String username = "company1";
        Company company = new Company(username);
        List<TraineeshipPosition> list = new ArrayList<>();

        TraineeshipPosition position = new TraineeshipPosition();
        position.setId(100);
        position.setAssigned(false);
        position.setCompany(company);
        list.add(position);
        company.setPositions(list);

        when(positionsMapper.findById(100)).thenReturn(Optional.of(position));

        companyService.deletePosition(username, 100);

        assertTrue(company.getPositions().isEmpty(), "Company positions should no longer contain the deleted one");
        verify(positionsMapper).delete(position);
    }

    @Test
    void deletePosition_throwsWhenAssigned() {
        String username = "company1";
        Company company = new Company(username);
        company.setPositions(new ArrayList<>());

        TraineeshipPosition position = new TraineeshipPosition();
        position.setId(200);
        position.setAssigned(true);
        position.setCompany(company);
        company.getPositions().add(position);

        when(positionsMapper.findById(200)).thenReturn(Optional.of(position));

        assertThrows(IllegalStateException.class,
                () -> companyService.deletePosition(username, 200));

        verify(positionsMapper, never()).delete(any());
    }
}
