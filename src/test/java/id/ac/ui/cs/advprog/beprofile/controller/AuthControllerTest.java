package id.ac.ui.cs.advprog.beprofile.controller;

import id.ac.ui.cs.advprog.beprofile.dto.ApiResponseDto;
import id.ac.ui.cs.advprog.beprofile.dto.TokenVerificationResponseDto;
import id.ac.ui.cs.advprog.beprofile.enums.Role;
import id.ac.ui.cs.advprog.beprofile.service.TokenVerificationService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class AuthControllerTest {

    @Mock
    private TokenVerificationService tokenVerificationService;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void verifyToken_shouldReturnUnauthorized_whenHeaderMissing() {
        when(request.getHeader("Authorization")).thenReturn(null);

        ResponseEntity<ApiResponseDto<TokenVerificationResponseDto>> response =
                authController.verifyToken(request);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        ApiResponseDto<TokenVerificationResponseDto> body = response.getBody();
        assertNotNull(body);
        assertEquals(HttpStatus.UNAUTHORIZED.value(), body.getStatus());
        assertEquals("Invalid authentication token", body.getMessage());
        assertNull(body.getData());
    }

    @Test
    void verifyToken_shouldReturnUnauthorized_whenHeaderInvalidPrefix() {
        when(request.getHeader("Authorization")).thenReturn("Token abcdef");

        ResponseEntity<ApiResponseDto<TokenVerificationResponseDto>> response =
                authController.verifyToken(request);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        ApiResponseDto<TokenVerificationResponseDto> body = response.getBody();
        assertNotNull(body);
        assertEquals(HttpStatus.UNAUTHORIZED.value(), body.getStatus());
        assertEquals("Invalid authentication token", body.getMessage());
        assertNull(body.getData());
    }

    @Test
    void verifyToken_shouldReturnOk_whenTokenValid() {
        String token = "valid-token";
        TokenVerificationResponseDto dto = TokenVerificationResponseDto.builder()
                .valid(true)
                .userId("user123")
                .email(null)
                .role(null)
                .expiresIn(null)
                .build();
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(tokenVerificationService.verifyToken(token)).thenReturn(dto);

        ResponseEntity<ApiResponseDto<TokenVerificationResponseDto>> response =
                authController.verifyToken(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        ApiResponseDto<TokenVerificationResponseDto> body = response.getBody();
        assertNotNull(body);
        assertEquals(HttpStatus.OK.value(), body.getStatus());
        assertEquals("Token verified successfully", body.getMessage());
        assertNotNull(body.getData());
        assertTrue(body.getData().isValid());
        assertEquals("user123", body.getData().getUserId());
    }

    @Test
    void verifyToken_shouldReturnUnauthorized_whenTokenInvalid() {
        String token = "invalid-token";
        TokenVerificationResponseDto dto = TokenVerificationResponseDto.builder()
                .valid(false)
                .build();
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(tokenVerificationService.verifyToken(token)).thenReturn(dto);

        ResponseEntity<ApiResponseDto<TokenVerificationResponseDto>> response =
                authController.verifyToken(request);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        ApiResponseDto<TokenVerificationResponseDto> body = response.getBody();
        assertNotNull(body);
        assertEquals(HttpStatus.UNAUTHORIZED.value(), body.getStatus());
        assertEquals("Invalid or expired token", body.getMessage());
        assertNull(body.getData());
    }
}