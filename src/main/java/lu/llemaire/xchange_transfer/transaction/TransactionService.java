package lu.llemaire.xchange_transfer.transaction;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lu.llemaire.xchange_transfer.account.Account;
import lu.llemaire.xchange_transfer.account.AccountService;
import lu.llemaire.xchange_transfer.common.FXRatesAPIService;
import lu.llemaire.xchange_transfer.exceptions.CurrencyServiceUnavailable;
import lu.llemaire.xchange_transfer.exceptions.InsufficientBalanceException;
import lu.llemaire.xchange_transfer.exceptions.NotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Log4j2
public class TransactionService {

    private final AccountService accountService;

    private final FXRatesAPIService fxRatesAPIService;

    /**
     * Method that will create a transaction between two accounts
     *
     * <p>No transaction will be really created, it just update two account balance</p>
     *
     * <p>The two account currency will be loaded to perform the amount conversion with FXRatesAPI</p>
     * <p>Check the README for more information about FXRatesAPI</p>
     *
     * @param fromAccountId The account id from which the amount will be taken
     * @param toAccountId The account id to which the amount will be added
     * @param amount The amount to transfer
     */
    public void createTransaction(final Long fromAccountId, final Long toAccountId, final BigDecimal amount) throws NotFoundException, InsufficientBalanceException, CurrencyServiceUnavailable {
        log.debug("Creating transaction");

        Account fromAccount = accountService.findById(fromAccountId);
        Account toAccount = accountService.findById(toAccountId);

        //Check if the two accounts have the same currency
        if (fromAccount.getCurrency().equals(toAccount.getCurrency())) {
            //If the two accounts have the same currency, the amount is directly transferred from one account to the other
            if (!accountService.moveFunds(fromAccountId, toAccountId, amount, amount)) {
                throw new InsufficientBalanceException("Insufficient balance");
            }
        } else {
            //If the two accounts have different currencies, the amount is converted
            BigDecimal convertedAmount = fxRatesAPIService.getConvertedAmount(fromAccount.getCurrency(), toAccount.getCurrency(), amount);
            if (!accountService.moveFunds(fromAccountId, toAccountId, amount, convertedAmount)) {
                throw new InsufficientBalanceException("Insufficient balance");
            }
        }
    }
}
