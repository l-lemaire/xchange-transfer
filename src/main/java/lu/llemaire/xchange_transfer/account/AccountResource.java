package lu.llemaire.xchange_transfer.account;

import lombok.RequiredArgsConstructor;
import lu.llemaire.xchange_transfer.exceptions.AlreadyExistsException;
import lu.llemaire.xchange_transfer.exceptions.CurrencyServiceUnavailable;
import lu.llemaire.xchange_transfer.exceptions.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountResource implements AccountResourceApi {

    private final AccountService accountService;

    @Override
    public ResponseEntity<List<Account>> findAll() {
        return ResponseEntity.ok(accountService.findAll());
    }

    @Override
    public ResponseEntity<Account> findById(final Long id) throws NotFoundException {
        return ResponseEntity.ok(accountService.findById(id));
    }

    @Override
    public ResponseEntity<Account> create(final Long id, final String currency, final BigDecimal balance) throws AlreadyExistsException, CurrencyServiceUnavailable {
        return ResponseEntity.ok(accountService.create(id, currency, balance));
    }

    @Override
    public ResponseEntity<Account> update(final Long id, final String currency, final BigDecimal balance) throws AlreadyExistsException, CurrencyServiceUnavailable {
        return ResponseEntity.ok(accountService.update(id, currency, balance));
    }

    @Override
    public ResponseEntity<Void> delete(final Long id) {
        accountService.delete(id);
        return ResponseEntity.ok().build();
    }
}
