package lu.llemaire.xchange_transfer.transaction;

import lombok.RequiredArgsConstructor;
import lu.llemaire.xchange_transfer.exceptions.CurrencyServiceUnavailable;
import lu.llemaire.xchange_transfer.exceptions.InsufficientBalanceException;
import lu.llemaire.xchange_transfer.exceptions.NotFoundException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionResource implements TransactionResourceApi {

    private final TransactionService transactionService;

    @Override
    public void create(Long sourceAccountId, Long targetAccountId, BigDecimal amount) throws InsufficientBalanceException, NotFoundException, CurrencyServiceUnavailable {
        transactionService.createTransaction(sourceAccountId, targetAccountId, amount);
    }
}
