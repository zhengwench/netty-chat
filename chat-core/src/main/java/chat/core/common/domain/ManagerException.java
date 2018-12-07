package chat.core.common.domain;

/**
 * @auther a-de
 * @date 2018/11/6 18:41
 */
public class ManagerException extends RuntimeException {
    public ManagerException(String c) {
        super(c);
    }

    public ManagerException(String c, Throwable t) {
        super(c, t);
    }

    public ManagerException(Throwable t) {
        super(t);
    }
}
