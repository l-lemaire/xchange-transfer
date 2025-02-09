package lu.llemaire.xchange_transfer.exceptions;

public class InsufficientBalanceException extends Exception {
    public InsufficientBalanceException(final String message) {
        super(message);
    }
}
