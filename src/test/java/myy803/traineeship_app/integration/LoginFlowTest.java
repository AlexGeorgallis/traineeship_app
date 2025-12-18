package myy803.traineeship_app.integration;

import myy803.traineeship_app.domain.Role;
import myy803.traineeship_app.domain.User;
import myy803.traineeship_app.mappers.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class LoginFlowTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    UserMapper userMapper;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Test
    void loginWithValidCredentials() throws Exception {
        User u = new User();
        u.setUsername("testuser1");
        u.setPassword(bCryptPasswordEncoder.encode("testpassword"));
        u.setRole(Role.STUDENT);
        userMapper.save(u);

        mockMvc.perform(formLogin("/login")
                .user("username", "testuser1")
                .password("password", "testpassword"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/student/dashboard"));
    }

    @Test
    void loginWithInvalidCredentials() throws Exception {
        User u = new User();
        u.setUsername("testuser2");
        u.setPassword(bCryptPasswordEncoder.encode("testpassword"));
        u.setRole(Role.STUDENT);
        userMapper.save(u);

        mockMvc.perform(formLogin("/login")
                .user("username", "testuser2")
                .password("password", "wrongpassword!"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?error=true"));
    }
}
