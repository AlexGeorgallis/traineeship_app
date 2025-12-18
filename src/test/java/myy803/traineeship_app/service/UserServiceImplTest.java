package myy803.traineeship_app.service;

import myy803.traineeship_app.domain.*;
import myy803.traineeship_app.mappers.CompanyMapper;
import myy803.traineeship_app.mappers.ProfessorMapper;
import myy803.traineeship_app.mappers.StudentMapper;
import myy803.traineeship_app.mappers.UserMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Mock
    private UserMapper userMapper;

    @Mock
    private StudentMapper studentMapper;

    @Mock
    private ProfessorMapper professorMapper;

    @Mock
    private CompanyMapper companyMapper;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void saveUserStudentRole() {
        User user = new User();
        user.setUsername("student1");
        user.setPassword("password");
        user.setRole(Role.STUDENT);

        when(bCryptPasswordEncoder.encode("password")).thenReturn("encoded");

        userService.saveUser(user);

        // capture saved user with encoded password
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userMapper).save(userCaptor.capture());
        User savedUser = userCaptor.getValue();

        assertEquals("student1", savedUser.getUsername());
        assertEquals("encoded", savedUser.getPassword());
        assertEquals(Role.STUDENT, savedUser.getRole());

        // ensure student created
        ArgumentCaptor<Student> studentCaptor = ArgumentCaptor.forClass(Student.class);
        verify(studentMapper).save(studentCaptor.capture());
        Student savedStudent = studentCaptor.getValue();

        assertEquals("student1", savedStudent.getUsername());
        assertTrue(savedStudent.isLookingForTraineeship());
    }

    @Test
    void saveUserCompanyRole() {
        User user = new User();
        user.setUsername("company1");
        user.setPassword("password");
        user.setRole(Role.COMPANY);

        when(bCryptPasswordEncoder.encode("password")).thenReturn("encoded");
        userService.saveUser(user);

        // user saved
        verify(userMapper).save(any(User.class));

        // company created
        ArgumentCaptor<Company> companyCaptor = ArgumentCaptor.forClass(Company.class);
        verify(companyMapper).save(companyCaptor.capture());
        Company savedCompany = companyCaptor.getValue();

        assertEquals("company1", savedCompany.getUsername());
    }

    @Test
    void saveUserProfessorRole() {
        User user = new User();
        user.setUsername("professor1");
        user.setPassword("password");
        user.setRole(Role.PROFESSOR);
        when(bCryptPasswordEncoder.encode("password")).thenReturn("encoded");

        userService.saveUser(user);

        // user saved
        verify(userMapper).save(any(User.class));

        // professor created
        ArgumentCaptor<Professor> profCaptor = ArgumentCaptor.forClass(Professor.class);
        verify(professorMapper).save(profCaptor.capture());
        Professor savedProfessor = profCaptor.getValue();

        assertEquals("professor1", savedProfessor.getUsername());
    }

    @Test
    void isUserPresentUserFound() {
        User user = new User();
        user.setUsername("alex");
        when(userMapper.findByUsername("alex")).thenReturn(new User());

        assertTrue(userService.isUserPresent(user));
    }

    @Test
    void isUserPresentUserNotFound() {
        User user = new User();
        user.setUsername("alex");
        when(userMapper.findByUsername("alex")).thenReturn(null);

        assertFalse(userService.isUserPresent(user));
    }

    @Test
    void loadUserByUsername() {
        User user = new User();
        when(userMapper.findByUsername("alex")).thenReturn(user);

        assertSame(user, userService.loadUserByUsername("alex"));
    }

    @Test
    void findById() {
        User user = new User();
        when(userMapper.findByUsername("alex")).thenReturn(user);

        assertSame(user, userService.findById("alex"));
    }

}
