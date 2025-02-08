package lu.llemaire.xchange_transfer.account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface AccountRepository extends JpaRepository<Account, Long> {

    @Modifying
    @Query("UPDATE Account a SET a.balance = a.balance + ?2 WHERE a.id = ?1")
    int addBalance(Long accountId, Double amount);

    @Modifying
    @Query("UPDATE Account a SET a.balance = a.balance - ?2 WHERE a.id = ?1")
    int subtractBalance(Long accountId, Double amount);
}
