package su.balynsky.android.atms.exceptions;

/**
 * @author Sergey Balynsky
 *         on 26.01.2015.
 */
public class TechnicalException extends Exception {
    public TechnicalException(String detailMessage) {
        super(detailMessage);
    }

    public TechnicalException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public TechnicalException(Throwable throwable) {
        super(throwable);
    }

    public TechnicalException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
