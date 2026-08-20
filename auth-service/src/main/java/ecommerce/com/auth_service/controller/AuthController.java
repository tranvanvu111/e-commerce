package ecommerce.com.auth_service.controller;

import ecommerce.com.auth_service.dto.request.CreateUserRequest;
import ecommerce.com.auth_service.dto.request.LoginRequest;
import ecommerce.com.auth_service.dto.response.ApiResponse;
import ecommerce.com.auth_service.dto.response.LoginResponse;
import ecommerce.com.auth_service.dto.response.UserResponse;
import ecommerce.com.auth_service.service.AuthService;
import ecommerce.com.auth_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody LoginRequest loginRequest) {
        LoginResponse loginResponse = authService.login(loginRequest);
        return ResponseEntity.ok().body(
                ApiResponse.<LoginResponse>builder()
                        .code(200)
                        .message("Login successfully")
                        .result(loginResponse)
                        .build()
        );
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<UserResponse>> signup(@RequestBody CreateUserRequest createUserRequest){
        return ResponseEntity.ok().body(
                ApiResponse.<UserResponse>builder()
                        .code(200)
                        .result(userService.createUser(createUserRequest))
                        .build()
        );
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> getMyInfo() {
        return ResponseEntity.ok().body(
                ApiResponse.<UserResponse>builder()
                        .code(200)
                        .message("Get user info successfully")
                        .result(authService.getMyInfo())
                        .build()
        );
    }

}
