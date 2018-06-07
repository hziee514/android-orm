package wrh.learning.simpleorm;

/**
 * @author bruce.wu
 * @date 2018/6/7
 */
public class InvalidTypeException extends RuntimeException {
    public InvalidTypeException(Class<?> type) {
        super("Invalid object type: " + type.getName());
    }

    public InvalidTypeException(Class<?> type, Throwable cause) {
        super("Invalid object type: " + type.getName(), cause);
    }
}
