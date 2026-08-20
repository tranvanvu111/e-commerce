package ecommerce.com.auth_service.mapper;

import ecommerce.com.auth_service.dto.request.CreateUserRequest;
import ecommerce.com.auth_service.dto.response.UserResponse;
import ecommerce.com.auth_service.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    UserResponse toUserResponse(User user);
    User toUser(CreateUserRequest userRequest);
}
