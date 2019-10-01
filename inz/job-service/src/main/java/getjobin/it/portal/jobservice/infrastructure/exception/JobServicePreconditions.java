package getjobin.it.portal.jobservice.infrastructure.exception;

public class JobServicePreconditions {

    public static void checkArgument(boolean condition, String message) {
        if(!condition) {
            throw new JobServiceException(message);
        }
    }
}
