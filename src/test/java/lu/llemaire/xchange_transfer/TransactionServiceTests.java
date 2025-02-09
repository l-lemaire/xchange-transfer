package lu.llemaire.xchange_transfer;

import lu.llemaire.xchange_transfer.account.Account;
import lu.llemaire.xchange_transfer.account.AccountService;
import lu.llemaire.xchange_transfer.exceptions.AlreadyExistsException;
import lu.llemaire.xchange_transfer.exceptions.CurrencyServiceUnavailable;
import lu.llemaire.xchange_transfer.exceptions.InsufficientBalanceException;
import lu.llemaire.xchange_transfer.exceptions.NotFoundException;
import lu.llemaire.xchange_transfer.transaction.TransactionService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.math.RoundingMode;

@SpringBootTest
public class TransactionServiceTests extends AbstractTestContainer {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private AccountService accountService;

    @Test
    void checkCurrency() throws CurrencyServiceUnavailable {
        // Check if the currency is available
        Assertions.assertEquals(Boolean.TRUE, transactionService.checkCurrency("EUR"));
    }

    @Test
    void checkCurrencyWithInvalidCurrencyShouldThrowException() {
        // Check if the currency is available
        Assertions.assertThrows(Exception.class, () -> transactionService.checkCurrency("KKK"));
    }

    @Test
    void getConvertedAmount() throws CurrencyServiceUnavailable {
        Assertions.assertNotNull(transactionService.getConvertedAmount("USD", "EUR", new BigDecimal("100.0")));
    }

    @Test
    void getConvertedAmountWithInvalidCurrencyShouldThrowException() {
        Assertions.assertThrows(Exception.class, () -> transactionService.getConvertedAmount("KKK", "EUR", new BigDecimal("100.0")));
        Assertions.assertThrows(Exception.class, () -> transactionService.getConvertedAmount("USD", "KKK", new BigDecimal("100.0")));
    }

    @Test
    void createTransactionWithSameCurrencyShouldChangeAccounts() throws AlreadyExistsException, InsufficientBalanceException, NotFoundException, CurrencyServiceUnavailable {
        accountService.create(10L, "EUR", new BigDecimal("100.0"));
        accountService.create(11L, "EUR", new BigDecimal("100.0"));

        transactionService.createTransaction(10L, 11L, new BigDecimal("50.0"));

        Account fromAccount = accountService.findById(10L);
        Assertions.assertEquals(BigDecimal.valueOf(50.0).setScale(4, RoundingMode.HALF_DOWN), fromAccount.getBalance());

        Account toAccount = accountService.findById(11L);
        Assertions.assertEquals(BigDecimal.valueOf(150.0).setScale(4, RoundingMode.HALF_DOWN), toAccount.getBalance());
    }

    @Test
    void createTransactionWithDifferentCurrencyShouldChangeAccounts() throws AlreadyExistsException, InsufficientBalanceException, NotFoundException, CurrencyServiceUnavailable {
        accountService.create(12L, "EUR", new BigDecimal("100.0"));
        accountService.create(13L, "LBP", new BigDecimal("0.0"));

        transactionService.createTransaction(12L, 13L, new BigDecimal("50.0"));

        Account fromAccount = accountService.findById(12L);
        Assertions.assertEquals(BigDecimal.valueOf(50.0).setScale(4, RoundingMode.HALF_DOWN), fromAccount.getBalance());

        Account toAccount = accountService.findById(13L);
        Assertions.assertTrue(BigDecimal.valueOf(50000.0).setScale(4, RoundingMode.HALF_DOWN).compareTo(toAccount.getBalance()) < 0);
    }

    @Test
    void createTransactionWithInvalidFromAccountShouldThrowException() throws AlreadyExistsException {
        accountService.create(14L, "EUR", new BigDecimal("100.0"));
        accountService.create(15L, "EUR", new BigDecimal("100.0"));

        Assertions.assertThrows(Exception.class, () -> transactionService.createTransaction(16L, 15L, new BigDecimal("50.0")));
    }

    @Test
    void createTransactionWithInvalidToAccountShouldThrowException() throws AlreadyExistsException {
        accountService.create(17L, "EUR", new BigDecimal("100.0"));
        accountService.create(18L, "EUR", new BigDecimal("100.0"));

        Assertions.assertThrows(Exception.class, () -> transactionService.createTransaction(17L, 19L, new BigDecimal("50.0")));
    }

    @Test
    void createTransactionWithInsufficientBalanceShouldThrowException() throws AlreadyExistsException {
        accountService.create(20L, "EUR", new BigDecimal("100.0"));
        accountService.create(21L, "EUR", new BigDecimal("100.0"));

        Assertions.assertThrows(Exception.class, () -> transactionService.createTransaction(20L, 21L, new BigDecimal("150.0")));
    }
}
