package myy803.traineeship_app.service;

import myy803.traineeship_app.domain.Company;
import myy803.traineeship_app.domain.TraineeshipPosition;
import myy803.traineeship_app.mappers.CompanyMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CompanyServiceImpl implements CompanyService {

    private final CompanyMapper companyMapper;

    public CompanyServiceImpl(CompanyMapper companyMapper) {
        this.companyMapper = companyMapper;
    }

    @Override
    public Company getOrCreateProfile(String username) {
        Company company = companyMapper.findByUsername(username);
        if (company == null) {
            company = new Company(username);
        }
        return company;
    }

    @Override
    @Transactional
    public void updateProfile(Company formCompany, String username) {
        Company db = companyMapper.findByUsername(username);

        if (db == null) {
            db = new Company(username);
        }

        db.setCompanyName(formCompany.getCompanyName());
        db.setCompanyLocation(formCompany.getCompanyLocation());

        companyMapper.save(db);
    }

    @Override
    public List<TraineeshipPosition> getAvailablePositions(String username) {
        Company company = companyMapper.findByUsername(username);
        return (company != null) ? company.getAvailablePositions() : List.of();
    }

    @Override
    @Transactional
    public void savePositionForCompany(String username, TraineeshipPosition position) {
        Company company = companyMapper.findByUsername(username);

        if (company == null) {
            company = new Company(username);
        }

        position.setCompany(company);
        company.addPosition(position);

        companyMapper.save(company);
    }
}
