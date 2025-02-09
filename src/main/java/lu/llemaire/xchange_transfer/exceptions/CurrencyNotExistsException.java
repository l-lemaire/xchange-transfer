package lu.llemaire.xchange_transfer.exceptions;

public class CurrencyNotExistsException extends Exception {
    public CurrencyNotExistsException(String message) {
        super(message);
    }
}
