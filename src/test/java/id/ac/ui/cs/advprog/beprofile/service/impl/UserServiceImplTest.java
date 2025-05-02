package id.ac.ui.cs.advprog.beprofile.service.impl;

import id.ac.ui.cs.advprog.beprofile.model.User;
import id.ac.ui.cs.advprog.beprofile.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User mockUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockUser = new User();
        mockUser.setId("user123");
        mockUser.setFullname("John Doe");
        mockUser.setEmail("john.doe@example.com");
    }

    @Test
    void testGetUserProfile_Success() {
        when(userRepository.findById("user123")).thenReturn(Optional.of(mockUser));

        User result = userService.getUserProfile("user123");

        assertNotNull(result);
        assertEquals("John Doe", result.getFullname());
        assertEquals("john.doe@example.com", result.getEmail());
        verify(userRepository, times(1)).findById("user123");
    }

    @Test
    void testGetUserProfile_UserNotFound() {
        when(userRepository.findById("user123")).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.getUserProfile("user123");
        });

        assertEquals("User not found with id: user123", exception.getMessage());
        verify(userRepository, times(1)).findById("user123");
    }

    @Test
    void testUpdateUserProfile_Success() {
        when(userRepository.findById("user123")).thenReturn(Optional.of(mockUser));
        when(userRepository.save(any(User.class))).thenReturn(mockUser);

        User updatedUser = new User();
        updatedUser.setFullname("Jane Doe");
        updatedUser.setEmail("jane.doe@example.com");

        User result = userService.updateUserProfile("user123", updatedUser);

        assertNotNull(result);
        assertEquals("Jane Doe", result.getFullname());
        assertEquals("jane.doe@example.com", result.getEmail());
        verify(userRepository, times(1)).findById("user123");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testDeleteUserAccount_Success() {
        doNothing().when(userRepository).deleteById("user123");

        userService.deleteUserAccount("user123");

        verify(userRepository, times(1)).deleteById("user123");
    }
}