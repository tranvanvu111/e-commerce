package ecommerce.com.auth_service.service;

import ecommerce.com.auth_service.dto.request.CreateUserRequest;
import ecommerce.com.auth_service.dto.response.UserResponse;
import ecommerce.com.auth_service.entity.User;
import ecommerce.com.auth_service.mapper.UserMapper;
import ecommerce.com.auth_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {



    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserMapper userMapper;


    public UserResponse createUser(CreateUserRequest userRequest) {
        User user = userMapper.toUser(userRequest);
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        User savedUser = userRepository.save(user);
        return userMapper.toUserResponse(savedUser);
    }
}
