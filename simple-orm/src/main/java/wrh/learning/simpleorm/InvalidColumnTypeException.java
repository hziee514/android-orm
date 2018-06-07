package wrh.learning.simpleorm;

/**
 * @author bruce.wu
 * @date 2018/6/7
 */
public class InvalidColumnTypeException extends RuntimeException {

    public InvalidColumnTypeException(Class<?> type) {
        super(type.getName());
    }
}
