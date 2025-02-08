package lu.llemaire.xchange_transfer.services;

import lombok.RequiredArgsConstructor;
import lu.llemaire.xchange_transfer.account.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountTransactionalServiceTest {
    final AccountRepository accountRepository;

    public int addBalance(Long accountId, Double amount) {
        return accountRepository.addBalance(accountId, amount);
    }

    public int subtractBalance(Long accountId, Double amount) {
        return accountRepository.subtractBalance(accountId, amount);
    }
}
