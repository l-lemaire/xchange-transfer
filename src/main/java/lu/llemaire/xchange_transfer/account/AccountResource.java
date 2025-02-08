package lu.llemaire.xchange_transfer.account;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lu.llemaire.xchange_transfer.exceptions.AlreadyExistsException;
import lu.llemaire.xchange_transfer.exceptions.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
@Log4j2
public class AccountResource implements AccountResourceApi {

    final AccountService accountService;

    @Override
    public ResponseEntity<List<Account>> findAll() {
        return ResponseEntity.ok(accountService.findAll());
    }

    @Override
    public ResponseEntity<Account> findById(Long id) throws NotFoundException {
        return ResponseEntity.ok(accountService.findById(id));
    }

    @Override
    public ResponseEntity<Account> create(Long id, String currency, BigDecimal balance) throws AlreadyExistsException {
        return ResponseEntity.ok(accountService.create(id, currency, balance));
    }

    @Override
    public ResponseEntity<Account> update(Long id, String currency, BigDecimal balance) {
        return ResponseEntity.ok(accountService.update(id, currency, balance));
    }

    @Override
    public ResponseEntity<Void> delete(Long id) {
        accountService.delete(id);
        return ResponseEntity.ok().build();
    }
}
