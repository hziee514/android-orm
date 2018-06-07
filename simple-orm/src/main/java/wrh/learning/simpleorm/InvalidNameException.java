package wrh.learning.simpleorm;

/**
 * @author bruce.wu
 * @date 2018/6/7
 */
public class InvalidNameException extends RuntimeException {
    public InvalidNameException(String message) {
        super(message);
    }
}
