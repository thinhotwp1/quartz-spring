package telsoft.scheduler.quartz.core.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String user;
    private String password;
}
