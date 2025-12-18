package myy803.traineeship_app.controllers;

import myy803.traineeship_app.domain.User;
import myy803.traineeship_app.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.instanceOf;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Test
        // returns login view
    void testLogin() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/login"));
    }

    @Test
    void testRegister() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/register"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attribute("user", instanceOf(User.class)));
    }

    @Test
    void testRegisterWithExistingUser() throws Exception {
        // if user exists, it shows "User already registered!"
        when(userService.isUserPresent(any(User.class))).thenReturn(true);

        mockMvc.perform(post("/save")
                        .param("username", "test")
                        .param("password", "password"))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/login"))
                .andExpect(model().attribute("successMessage", "User already registered!"));

        // confirm that saveUser is NOT being called
        verify(userService, never()).saveUser(any(User.class));
    }


    @Test
    void testRegisterNewUser() throws Exception {
        when(userService.isUserPresent(any(User.class))).thenReturn(false);

        mockMvc.perform(post("/save")
                .param("username", "test")
                .param("password", "password"))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/login"))
                .andExpect(model().attribute("successMessage", "User registered successfully!"));

        // capture the savedUser
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userService).saveUser(captor.capture());

        User savedUser = captor.getValue();
        Assertions.assertEquals("test", savedUser.getUsername());
    }
}
