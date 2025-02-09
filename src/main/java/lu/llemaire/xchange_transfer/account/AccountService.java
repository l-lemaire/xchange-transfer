package lu.llemaire.xchange_transfer.account;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lu.llemaire.xchange_transfer.common.FXRatesAPIService;
import lu.llemaire.xchange_transfer.exceptions.AlreadyExistsException;
import lu.llemaire.xchange_transfer.exceptions.CurrencyServiceUnavailable;
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

    final FXRatesAPIService fxRatesAPIService;

    final AccountRepository accountRepository;

    public Account create(final Long id, final String currency, final BigDecimal balance) throws AlreadyExistsException, CurrencyServiceUnavailable {
        log.debug("Creating account with id {}", id);
        if (accountRepository.existsById(id)) {
            throw new AlreadyExistsException("Account with id " + id + " already exists");
        }

        if (!fxRatesAPIService.checkCurrency(currency)) {
            throw new AlreadyExistsException("Currency " + currency + " is not supported");
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

    @Transactional(readOnly = true)
    public Account findById(final Long id) throws NotFoundException {
        log.debug("Retrieving account with id {}", id);
        return accountRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Account with id " + id + " not found"));
    }

    public Account update(final Long id, final String currency, final BigDecimal balance) throws CurrencyServiceUnavailable, AlreadyExistsException {
        log.debug("Updating account with id {}", id);
        if (!fxRatesAPIService.checkCurrency(currency)) {
            throw new AlreadyExistsException("Currency " + currency + " is not supported");
        }

        return accountRepository.save(new Account(id, currency, balance));
    }

    /**
     * Move funds from one account to another in the same transaction
     *
     * <p>It will check if the account with id fromId has enough funds to move fromAmount</p>
     *
     * @param fromId     the id of the account to move funds from
     * @param toId       the id of the account to move funds to
     * @param fromAmount the amount to move from the account with id fromId
     * @param toAmount   the amount to move to the account with id toId
     * @return true if the funds were moved successfully, false otherwise
     */
    public boolean moveFunds(final Long fromId, final Long toId, final BigDecimal fromAmount, final BigDecimal toAmount) {
        log.debug("Moving funds from account with id {} to account with id {}", fromId, toId);
        Account from = accountRepository.findById(fromId).orElseThrow();
        if (from.getBalance().compareTo(fromAmount) < 0) {
            return false;
        }
        accountRepository.subtractBalance(fromId, fromAmount);
        accountRepository.addBalance(toId, toAmount);
        return true;
    }

    public void delete(final Long id) {
        log.debug("Deleting account with id {}", id);
        accountRepository.deleteById(id);
    }
}
