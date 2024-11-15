package telsoft.scheduler.quartz.core.dto;

import lombok.Data;

@Data
public class LoginResponse {
    private String accessToken;
    private String tokenType = "Bearer";
}
