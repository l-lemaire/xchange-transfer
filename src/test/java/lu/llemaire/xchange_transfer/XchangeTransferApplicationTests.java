package lu.llemaire.xchange_transfer;

import lu.llemaire.xchange_transfer.account.Account;
import lu.llemaire.xchange_transfer.account.AccountRepository;
import lu.llemaire.xchange_transfer.services.AccountTransactionalServiceTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.math.RoundingMode;

@SpringBootTest
class XchangeTransferApplicationTests extends AbstractTestContainer {

	@Autowired
	private AccountTransactionalServiceTest accountTransactionalServiceTest;

	@Autowired
	private AccountRepository accountRepository;

	@Test
	void createNewInstance() {
		Account account = new Account();
		account.setId(1L);
		account.setCurrency("EUR");
		account.setBalance(BigDecimal.valueOf(0.0));

		accountRepository.save(account);
	}

	@Test
	void retrieveInstance() {
		//Check if the account is saved, retrieve it and check if the values are the same
		Account account = accountRepository.findById(1L).orElseThrow();
		Assertions.assertEquals(1L, account.getId());
		Assertions.assertEquals("EUR", account.getCurrency());
		Assertions.assertEquals(BigDecimal.valueOf(0.0).setScale(4, RoundingMode.HALF_DOWN), account.getBalance());
	}

	@Test
	void createInstanceWithNullIdShouldThrowException() {
		Account account = new Account();
		account.setId(null);
		account.setCurrency("USD");
		account.setBalance(BigDecimal.valueOf(100.0));

		//Check if saving the account with a null id throws an exception
		Assertions.assertThrows(Exception.class, () -> accountRepository.save(account));
	}

	@Test
	void createInstanceWithNegativeBalanceShouldThrowException() {
		Account account = new Account();
		account.setId(2L);
		account.setCurrency("USD");
		account.setBalance(BigDecimal.valueOf(-100.0));

		//Check if saving the account with a negative balance throws an exception
		Assertions.assertThrows(Exception.class, () -> accountRepository.save(account));
	}

	@Test
	void createInstanceWithNullBalanceShouldThrowException() {
		Account account = new Account();
		account.setId(3L);
		account.setCurrency("USD");
		account.setBalance(null);

		//Check if saving the account with a null balance throws an exception
		Assertions.assertThrows(Exception.class, () -> accountRepository.save(account));
	}

	@Test
	void createInstanceWithNullCurrencyShouldThrowException() {
		Account account = new Account();
		account.setId(4L);
		account.setCurrency(null);
		account.setBalance(BigDecimal.valueOf(100.0));

		//Check if saving the account with a null currency throws an exception
		Assertions.assertThrows(Exception.class, () -> accountRepository.save(account));
	}

	@Test
	void createInstanceWithEmptyCurrencyShouldThrowException() {
		Account account = new Account();
		account.setId(5L);
		account.setCurrency("");
		account.setBalance(BigDecimal.valueOf(100.0));

		//Check if saving the account with an empty currency throws an exception
		Assertions.assertThrows(Exception.class, () -> accountRepository.save(account));
	}

	@Test
	void createInstanceWithWrongCurrencyShouldThrowException() {
		Account account = new Account();
		account.setId(6L);
		account.setCurrency("US");
		account.setBalance(BigDecimal.valueOf(100.0));

		//Check if saving the account with a wrong currency throws an exception
		Assertions.assertThrows(Exception.class, () -> accountRepository.save(account));
	}

	@Test
	void addBalance() {
		//Create an account with a balance of 0
		Account account = new Account();
		account.setId(6L);
		account.setCurrency("USD");
		account.setBalance(BigDecimal.valueOf(0.0));
		accountRepository.save(account);

		//Add 100 to the balance
		accountTransactionalServiceTest.addBalance(6L, 100.0);
		Account updatedAccount = accountRepository.findById(6L).orElseThrow();
		Assertions.assertEquals(BigDecimal.valueOf(100.0).setScale(4, RoundingMode.HALF_DOWN), updatedAccount.getBalance());
	}

	@Test
	void subtractBalance() {
		//Create an account with a balance of 100
		Account account = new Account();
		account.setId(7L);
		account.setCurrency("USD");
		account.setBalance(BigDecimal.valueOf(100.0));
		accountRepository.save(account);

		//Subtract 50 from the balance
		accountTransactionalServiceTest.subtractBalance(7L, 50.0);
		Account updatedAccount = accountRepository.findById(7L).orElseThrow();
		Assertions.assertEquals(BigDecimal.valueOf(50.0).setScale(4, RoundingMode.HALF_DOWN), updatedAccount.getBalance());
	}

	@Test
	void subtractBalanceWithInsufficientFundsShouldThrowException() {
		//Create an account with a balance of 100
		Account account = new Account();
		account.setId(8L);
		account.setCurrency("USD");
		account.setBalance(BigDecimal.valueOf(100.0));
		accountRepository.save(account);

		//Subtract 150 from the balance
		Assertions.assertThrows(Exception.class, () -> accountTransactionalServiceTest.subtractBalance(8L, 150.0));
	}

	@Test
	void addBalanceWithWrongAccountIdShouldReturnZero() {
		//Add 100 to the balance of an account that doesn't exist
		Assertions.assertEquals(0, accountTransactionalServiceTest.addBalance(9L, 100.0));
	}

	@Test
	void subtractBalanceWithWrongAccountIdShouldReturnZero() {
		//Subtract 100 from the balance of an account that doesn't exist
		Assertions.assertEquals(0, accountTransactionalServiceTest.subtractBalance(10L, 100.0));
	}
}
