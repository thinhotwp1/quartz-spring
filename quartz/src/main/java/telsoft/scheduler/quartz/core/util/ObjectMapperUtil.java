package telsoft.scheduler.quartz.core.util;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ObjectMapperUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    // Generic method to map one object to another
    public static <S, T> T mapObject(S source, Class<T> targetClass) {
        return objectMapper.convertValue(source, targetClass);
    }
}
