package id.ac.ui.cs.advprog.beprofile.controller;

import id.ac.ui.cs.advprog.beprofile.model.ConsultationHistory;
import id.ac.ui.cs.advprog.beprofile.model.User;
import id.ac.ui.cs.advprog.beprofile.service.ConsultationHistoryService;
import id.ac.ui.cs.advprog.beprofile.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @Mock
    private ConsultationHistoryService consultationHistoryService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();

        // Mock data pengguna
        User mockUser = new User();
        mockUser.setId("user123");
        mockUser.setFullname("John Doe");
        mockUser.setEmail("john.doe@example.com");

        // Set up UserService mocks
        when(userService.getUserProfile("user123")).thenReturn(mockUser);
        when(userService.updateUserProfile(eq("user123"), any(User.class))).thenAnswer(invocation -> {
            User updatedUser = invocation.getArgument(1);
            mockUser.setFullname(updatedUser.getFullname());
            mockUser.setEmail(updatedUser.getEmail());
            return mockUser;
        });
        doNothing().when(userService).deleteUserAccount("user123");

        // Set up ConsultationHistoryService mocks
        List<ConsultationHistory> mockHistory = new ArrayList<>();
        when(consultationHistoryService.getConsultationHistory("user123")).thenReturn(mockHistory);
    }

    @Test
    void testGetUserProfile_Success() throws Exception {
        mockMvc.perform(get("/users/user123")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullname").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"));
    }

    @Test
    void testUpdateUserProfile_Success() throws Exception {
        mockMvc.perform(put("/users/user123")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"fullname\": \"Jane Doe\", \"email\": \"jane.doe@example.com\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullname").value("Jane Doe"))
                .andExpect(jsonPath("$.email").value("jane.doe@example.com"));
    }

    @Test
    void testDeleteUserAccount_Success() throws Exception {
        mockMvc.perform(delete("/users/user123")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void testGetConsultationHistory_Success() throws Exception {
        mockMvc.perform(get("/users/user123/consultations")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }
}