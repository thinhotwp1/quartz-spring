package telsoft.scheduler.quartz.core.log.rest;

import lombok.Getter;

@Getter
public enum TypeLog {

    INFO("info"),
    ERROR("error"),
    REQUEST("request"),
    RESPONSE("response");

    private final String type;

    TypeLog(String type) {
        this.type = type;
    }

}
