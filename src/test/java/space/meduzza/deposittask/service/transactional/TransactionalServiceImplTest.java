package space.meduzza.deposittask.service.transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.AdditionalAnswers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import space.meduzza.deposittask.service.account.AccountService;
import space.meduzza.deposittask.service.account.AccountTestUtil;
import space.meduzza.deposittask.service.account.input.FindAccountInput;
import space.meduzza.deposittask.service.account.repository.entity.AccountEntity;
import space.meduzza.deposittask.service.client.ClientTestUtil;
import space.meduzza.deposittask.service.client.repository.entity.ClientEntity;
import space.meduzza.deposittask.service.transactional.input.DepositMoneyInput;
import space.meduzza.deposittask.service.transactional.input.GetAccountBalanceInput;
import space.meduzza.deposittask.service.transactional.input.GetAccountStatementInput;
import space.meduzza.deposittask.service.transactional.input.WithdrawMoneyInput;
import space.meduzza.deposittask.service.transactional.payload.AccountBalancePayload;
import space.meduzza.deposittask.service.transactional.payload.AccountStatementPayload;
import space.meduzza.deposittask.service.transactional.repository.TransactionalRepository;
import space.meduzza.deposittask.service.transactional.repository.entity.TransactionalEntity;
import space.meduzza.deposittask.util.AuthenticationFacade;
import space.meduzza.deposittask.util.AuthenticationFacadeImpl;

import javax.validation.ConstraintViolationException;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

// FOR Validation
@SpringBootTest
class TransactionalServiceImplTest {

	@MockBean
	private AccountService accountService;

	@MockBean(classes = AuthenticationFacadeImpl.class)
	private AuthenticationFacade authenticationFacade;

	@MockBean
	private TransactionalRepository transactionalRepository;

	@Autowired
	private TransactionalService transactionalService;

	private ClientEntity client1;

	private ClientEntity client2;

	private AccountEntity account1;

	private TransactionalEntity transactional1;

	@BeforeEach
	void setUp() {
		client1 = ClientTestUtil.createClientEntity(1);
		client2 = ClientTestUtil.createClientEntity(2);
		account1 = AccountTestUtil.createAccountEntity(1, client1);
		transactional1 = TransactionalTestUtil.createTransactionalEntity(1,
		                                                                 account1,
		                                                                 TransactionalTestUtil.INITIAL_AMOUNT,
		                                                                 TransactionalTestUtil.INITIAL_BALANCE);
	}

	@Test
	void check_ClientEntityIsResourceOwner_Assert() {
		mockResourceOwner(client1.getId(), client1.getId());
		assertThat(authenticationFacade.isResourceOwnerOrException(account1.getClient().getId())).isTrue();
	}

	@Test
	void check_ClientEntityIsResourceOwner_ExceptionThrown() {
		mockResourceOwner(client1.getId(), client2.getId());
		assertThrows(AccessDeniedException.class,
		             () -> authenticationFacade.isResourceOwnerOrException(client2.getId()));
	}

	@Test
	void get_MyAccountBalance_ReturnEqualBalance() {
		mockResourceOwner(client1.getId(), client1.getId());
		final GetAccountBalanceInput getAccountBalanceInput = new GetAccountBalanceInput(account1.getId());
		when(transactionalRepository.findTopByAccountIdOrderByIdDesc(account1.getId())).thenReturn(Optional.of(
				transactional1));
		final AccountBalancePayload accountBalancePayload = transactionalService.getAccountBalance(
				getAccountBalanceInput);
		assertThat(accountBalancePayload.getBalance()).isEqualTo(transactional1.getBalance());
	}

	@Test
	void get_MyAccountStatement_ReturnEqualStatement() {
		mockResourceOwner(client1.getId(), client1.getId());
		when(transactionalRepository.findAllByAccountIdOrderByIdDesc(any(Long.class), any(Pageable.class))).thenReturn(
				Collections.singletonList(transactional1));
		final AccountStatementPayload accountStatement
				= transactionalService.getAccountStatement(new GetAccountStatementInput(account1.getId(), 0));
		assertThat(accountStatement.getItems()).hasSize(1);
	}

	@ParameterizedTest
	@ValueSource(doubles = {1, 1.5, 2, 5, 10.55555, 100.4022})
	void try_depositMoneyFromOwnAccount_ReturnValidationOkey(double amount) {
		mockResourceOwner(client1.getId(), client1.getId());
		mockResults();
		final AccountBalancePayload withdrawMoneyResult = transactionalService.depositMoney(new DepositMoneyInput(
				account1.getId(),
				BigDecimal.valueOf(amount)));
		assertThat(withdrawMoneyResult.getBalance()).isEqualTo(transactional1
				                                                       .getBalance()
				                                                       .add(BigDecimal.valueOf(amount)));
		assertThat(withdrawMoneyResult.getBalance()).isGreaterThanOrEqualTo(BigDecimal.ZERO);
	}

	@ParameterizedTest
	@ValueSource(doubles = {-100, -1, 0, 0.5})
	void try_depositMoneyFromOwnAccount_WithNoValidValue_ValidateExceptionThrown(double amount) {
		mockResourceOwner(client1.getId(), client1.getId());
		mockResults();
		assertThrows(ConstraintViolationException.class,
		             () -> transactionalService.depositMoney(new DepositMoneyInput(account1.getId(),
		                                                                           BigDecimal.valueOf(amount))));
	}

	@ParameterizedTest
	@ValueSource(doubles = {1, 1.5, 2, 5, 10.55555, 100.4022})
	void try_withdrawMoneyFromOwnAccount_ReturnValidationOkey(double amount) {
		mockResourceOwner(client1.getId(), client1.getId());
		mockResults();
		final AccountBalancePayload withdrawMoneyResult = transactionalService.withdrawMoney(new WithdrawMoneyInput(
				account1.getId(),
				BigDecimal.valueOf(amount)));
		assertThat(withdrawMoneyResult.getBalance()).isEqualTo(transactional1
				                                                       .getBalance()
				                                                       .subtract(BigDecimal.valueOf(amount)));
		assertThat(withdrawMoneyResult.getBalance()).isGreaterThanOrEqualTo(BigDecimal.ZERO);
	}

	@ParameterizedTest
	@ValueSource(doubles = {-100, -1, 0, 0.5})
	void try_withdrawMoneyFromOwnAccount_WithNoValidValue_ValidateExceptionThrown(double amount) {
		mockResourceOwner(client1.getId(), client1.getId());
		mockResults();
		assertThrows(ConstraintViolationException.class,
		             () -> transactionalService.withdrawMoney(new WithdrawMoneyInput(account1.getId(),
		                                                                             BigDecimal.valueOf(amount))));
	}

	@ParameterizedTest
	@ValueSource(doubles = {1001, 10000})
	void withdrawMoney_MoreThanCurrentBalance_Error(double amount) {
		mockResourceOwner(client1.getId(), client1.getId());
		mockResults();
		assertThrows(IllegalStateException.class,
		             () -> transactionalService.withdrawMoney(new WithdrawMoneyInput(account1.getId(),
		                                                                             BigDecimal.valueOf(amount))));
	}

	private void mockResults() {
		when(transactionalRepository.findTopByAccountIdOrderByIdDesc(account1.getId())).thenReturn(Optional.of(
				transactional1));
		when(transactionalRepository.save(any(TransactionalEntity.class))).then(AdditionalAnswers.returnsFirstArg());
	}

	private void mockResourceOwner(long resourceOwnerClientId, long requestAuthorClientId) {
		final FindAccountInput findAccountInput = new FindAccountInput(resourceOwnerClientId);
		when(accountService.findAccount(findAccountInput)).thenReturn(Optional.of(account1));
		when(authenticationFacade.isResourceOwner(anyLong())).thenReturn(
				resourceOwnerClientId == requestAuthorClientId);
		when(authenticationFacade.isResourceOwnerOrException(anyLong())).thenCallRealMethod();
	}

	@TestConfiguration
	static class Config {
		@Bean
		public TransactionalService transactionalService(TransactionalRepository transactionalRepository,
		                                                 AccountService accountService,
		                                                 AuthenticationFacade authenticationFacade) {
			return new TransactionalServiceImpl(transactionalRepository, accountService, authenticationFacade);
		}
	}
}