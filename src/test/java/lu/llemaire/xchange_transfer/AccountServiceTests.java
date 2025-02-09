package lu.llemaire.xchange_transfer;

import lu.llemaire.xchange_transfer.account.Account;
import lu.llemaire.xchange_transfer.account.AccountService;
import lu.llemaire.xchange_transfer.exceptions.AlreadyExistsException;
import lu.llemaire.xchange_transfer.exceptions.NotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.math.RoundingMode;

@SpringBootTest
public class AccountServiceTests extends AbstractTestContainer {

    @Autowired
    private AccountService accountService;

    @Test
    void createNewInstanceThenCallFindShouldNotChange() throws NotFoundException, AlreadyExistsException {
        // Create a new account
        accountService.create(9L, "EUR", BigDecimal.valueOf(0.0));

        // Check if the account is saved, retrieve it and check if the values are the same
        {
            Account account = accountService.findById(9L);
            Assertions.assertEquals(9L, account.getId());
            Assertions.assertEquals("EUR", account.getCurrency());
            Assertions.assertEquals(BigDecimal.valueOf(0.0).setScale(4, RoundingMode.HALF_DOWN), account.getBalance());
        }

        // Check if the findAll method returns the same account
        {
            Account account = accountService.findAll().stream().filter(a -> a.getId() == 9L).findFirst().orElseThrow();
            Assertions.assertEquals(9L, account.getId());
            Assertions.assertEquals("EUR", account.getCurrency());
            Assertions.assertEquals(BigDecimal.valueOf(0.0).setScale(4, RoundingMode.HALF_DOWN), account.getBalance());
        }
    }
}
