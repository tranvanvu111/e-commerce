package ecommerce.com.auth_service.dto.response;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponse {
    private String accessToken;
    private Long expiresIn;

    @Builder.Default
    private String tokenType = "Bearer";
}
