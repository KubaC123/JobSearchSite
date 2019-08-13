package getjobin.it.portal.jobservice.infrastructure.exceptions;

public class JobServicePreconditions {

    public static void checkArgument(boolean condition, String message) {
        if(!condition) {
            throw new JobServiceIllegalArgumentException(message);
        }
    }
}
