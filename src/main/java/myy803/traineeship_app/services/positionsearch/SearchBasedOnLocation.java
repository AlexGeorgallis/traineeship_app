package myy803.traineeship_app.services.positionsearch;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import myy803.traineeship_app.domain.Company;
import myy803.traineeship_app.domain.Student;
import myy803.traineeship_app.domain.TraineeshipPosition;
import myy803.traineeship_app.mappers.CompanyMapper;
import myy803.traineeship_app.mappers.StudentMapper;

@Component("positionSearchLocation")
public class SearchBasedOnLocation extends AbstractPositionsSearchTemplate {

	private final CompanyMapper companyMapper;

	public SearchBasedOnLocation(StudentMapper studentMapper, CompanyMapper companyMapper) {
		super(studentMapper);
		this.companyMapper = companyMapper;
	}

	@Override
	protected void collectMatchingPositions(Student applicant, Set<TraineeshipPosition> out) {
		String loc = applicant.getPreferredLocation();
		if (loc == null || loc.isBlank()) return;

		List<Company> companies = companyMapper.findByCompanyLocation(loc);
		for (Company c : companies) {
			out.addAll(c.getAvailablePositions());
		}
	}
}

