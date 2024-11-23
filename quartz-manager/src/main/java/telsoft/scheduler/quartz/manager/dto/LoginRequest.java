package telsoft.scheduler.quartz.manager.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String user;
    private String password;
}
