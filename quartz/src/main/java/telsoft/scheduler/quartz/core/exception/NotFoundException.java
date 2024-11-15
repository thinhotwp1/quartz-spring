package telsoft.scheduler.quartz.core.exception;

public class NotFoundException extends RuntimeException{
    public NotFoundException(String message) {
        super(message);
    }
    @Override
    public String toString() {
        return super.toString();
    }
}
