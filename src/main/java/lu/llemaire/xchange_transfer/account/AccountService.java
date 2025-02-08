package lu.llemaire.xchange_transfer.account;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lu.llemaire.xchange_transfer.exceptions.AlreadyExistsException;
import lu.llemaire.xchange_transfer.exceptions.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Log4j2
public class AccountService {

    final AccountRepository accountRepository;

    public Account create(Long id, String currency, BigDecimal balance) throws AlreadyExistsException {
        log.debug("Creating account with id {}", id);
        if (accountRepository.existsById(id)) {
            throw new AlreadyExistsException("Account with id " + id + " already exists");
        }

        return accountRepository.save(new Account(id, currency, balance));
    }

    @Transactional(readOnly = true)
    public List<Account> findAll() {
        log.debug("Retrieving all accounts");
        return accountRepository.findAll()
                .stream()
                .map(a -> a.setBalance(new BigDecimal("0.0").setScale(4, RoundingMode.HALF_DOWN)))
                .toList();
    }

    public Account findById(Long id) throws NotFoundException {
        log.debug("Retrieving account with id {}", id);
        return accountRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Account with id " + id + " not found"));
    }

    public Account update(Long id, String currency, BigDecimal balance) {
        log.debug("Updating account with id {}", id);
        return accountRepository.save(new Account(id, currency, balance));
    }

    public void delete(Long id) {
        log.debug("Deleting account with id {}", id);
        accountRepository.deleteById(id);
    }
}
