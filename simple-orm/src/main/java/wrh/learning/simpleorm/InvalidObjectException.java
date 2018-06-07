package wrh.learning.simpleorm;

/**
 * @author bruce.wu
 * @date 2018/6/7
 */
public class InvalidObjectException extends RuntimeException {
    public InvalidObjectException(String message, Throwable cause) {
        super(message, cause);
    }
}
