package lu.llemaire.xchange_transfer.exceptions;

public class CurrencyServiceUnavailable extends Exception {

    public CurrencyServiceUnavailable() {
        super("An unexpected error occurred");
    }

    public CurrencyServiceUnavailable(final String message) {
        super(message);
    }
}
