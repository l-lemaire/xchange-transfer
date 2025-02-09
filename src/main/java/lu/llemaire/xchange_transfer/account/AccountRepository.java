package lu.llemaire.xchange_transfer.account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;

public interface AccountRepository extends JpaRepository<Account, Long> {

    @Modifying
    @Query("UPDATE Account a SET a.balance = a.balance + ?2 WHERE a.id = ?1")
    int addBalance(final Long accountId, final BigDecimal amount);

    @Modifying
    @Query("UPDATE Account a SET a.balance = a.balance - ?2 WHERE a.id = ?1")
    int subtractBalance(final Long accountId, final BigDecimal amount);
}
