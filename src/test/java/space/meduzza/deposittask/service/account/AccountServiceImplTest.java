package space.meduzza.deposittask.service.account;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import space.meduzza.deposittask.service.account.input.CreateAccountInput;
import space.meduzza.deposittask.service.account.input.FindAccountInput;
import space.meduzza.deposittask.service.account.repository.AccountRepository;
import space.meduzza.deposittask.service.account.repository.entity.AccountEntity;
import space.meduzza.deposittask.service.client.ClientTestUtil;
import space.meduzza.deposittask.service.client.repository.entity.ClientEntity;
import space.meduzza.deposittask.util.AuthenticationFacade;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class AccountServiceImplTest {
	@MockBean
	private AccountRepository accountRepository;

	@MockBean
	private AuthenticationFacade authenticationFacade;

	@Autowired
	private AccountService accountService;

	private ClientEntity client1;

	private AccountEntity account1;

	@BeforeEach
	void setUp() {
		client1 = ClientTestUtil.createClientEntity(1);
		account1 = AccountTestUtil.createAccountEntity(1, client1);
	}

	@Test
	void save_AccountEntity_ReturnEqualAccount() {
		when(authenticationFacade.findCurrentClient()).thenReturn(client1);
		when(accountRepository.save(any(AccountEntity.class))).thenReturn(new AccountEntity(client1));
		final AccountEntity account = accountService.createAccount(new CreateAccountInput());
		assertThat(account.getClient().getId()).isEqualTo(client1.getId());
	}

	@Test
	void find_AccountEntity_ReturnEqualAccount() {
		when(accountRepository.findById(account1.getId())).thenReturn(Optional.of(account1));
		final AccountEntity findAccount = accountService
				.findAccount(new FindAccountInput(account1.getId()))
				.orElseThrow(NoSuchElementException::new);
		assertThat(findAccount).isEqualTo(account1);
	}

	@TestConfiguration
	static class Config {
		@Bean
		public AccountService accountService(AccountRepository accountRepository,
		                                     AuthenticationFacade authenticationFacade) {
			return new AccountServiceImpl(accountRepository, authenticationFacade);
		}
	}
}