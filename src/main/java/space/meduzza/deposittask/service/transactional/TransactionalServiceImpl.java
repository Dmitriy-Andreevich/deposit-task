package space.meduzza.deposittask.service.transactional;

import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import space.meduzza.deposittask.service.account.AccountService;
import space.meduzza.deposittask.service.account.input.FindAccountInput;
import space.meduzza.deposittask.service.account.repository.entity.AccountEntity;
import space.meduzza.deposittask.service.transactional.input.*;
import space.meduzza.deposittask.service.transactional.payload.AccountBalancePayload;
import space.meduzza.deposittask.service.transactional.payload.AccountStatementPayload;
import space.meduzza.deposittask.service.transactional.repository.TransactionalRepository;
import space.meduzza.deposittask.service.transactional.repository.entity.TransactionalEntity;
import space.meduzza.deposittask.util.AuthenticationFacade;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class TransactionalServiceImpl implements TransactionalService {
	private final TransactionalRepository transactionalRepository;

	private final AccountService accountService;

	private final AuthenticationFacade authenticationFacade;

	public TransactionalServiceImpl(TransactionalRepository transactionalRepository,
	                                AccountService accountService,
	                                AuthenticationFacade authenticationFacade) {
		this.transactionalRepository = transactionalRepository;
		this.accountService = accountService;
		this.authenticationFacade = authenticationFacade;
	}

	@Override
	public AccountBalancePayload getAccountBalance(GetAccountBalanceInput input) {
		final AccountEntity validAccount = getAccountEntityWithPermission(input.getAccountId());

		return getLastAccountTransactional(new GetLastAccountTransactionalInput(validAccount.getId())).map(
				transactionalEntityToAccountBalancePayload()).orElse(new AccountBalancePayload(input.getAccountId(),
		                                                                                       BigDecimal.ZERO));
	}

	private Function<TransactionalEntity, AccountBalancePayload> transactionalEntityToAccountBalancePayload() {
		return (transactionalEntity) -> new AccountBalancePayload(transactionalEntity.getAccount().getId(),
		                                                          transactionalEntity.getBalance());
	}

	@Override
	public AccountStatementPayload getAccountStatement(GetAccountStatementInput input) {
		final AccountEntity validAccount = getAccountEntityWithPermission(input.getAccountId());

		final List<AccountStatementPayload.Item> statementItems = transactionalRepository
				.findAllByAccountIdOrderByIdDesc(validAccount.getId(), PageRequest.of(input.getPage(), 10))
				.stream()
				.map(transactionalEntityToStatementElement())
				.collect(Collectors.toList());

		return new AccountStatementPayload(input.getAccountId(), statementItems);
	}

	private Function<TransactionalEntity, AccountStatementPayload.Item> transactionalEntityToStatementElement() {
		return (transactionalEntity) -> new AccountStatementPayload.Item(transactionalEntity.getAmount(),
		                                                                 transactionalEntity.getBalance(),
		                                                                 transactionalEntity.getCreateTime());
	}

	@Override
	@Transactional
	public AccountBalancePayload depositMoney(DepositMoneyInput input) throws ConstraintViolationException {
		final AccountEntity validAccount = getAccountEntityWithPermission(input.getAccountId());

		final TransactionalEntity lastTransactional = getLastAccountTransactional(new GetLastAccountTransactionalInput(
				input.getAccountId())).orElse(null);

		if (isFirstAccountTransactional(lastTransactional)) {
			transactionalRepository.save(new TransactionalEntity(validAccount, input.getAmount(), input.getAmount()));
			return new AccountBalancePayload(validAccount.getId(), input.getAmount());
		}

		final BigDecimal expectedBalance = calculateBalance(lastTransactional.getBalance(), input.getAmount());
		transactionalRepository.save(new TransactionalEntity(validAccount, input.getAmount(), expectedBalance));

		return new AccountBalancePayload(validAccount.getId(), expectedBalance);
	}

	private boolean isFirstAccountTransactional(TransactionalEntity lastTransactional) {
		return Objects.isNull(lastTransactional);
	}

	@Override
	@Transactional
	public AccountBalancePayload withdrawMoney(WithdrawMoneyInput input) {
		final AccountEntity validAccount = getAccountEntityWithPermission(input.getAccountId());

		final TransactionalEntity lastTransactional = getLastAccountTransactional(new GetLastAccountTransactionalInput(
				input.getAccountId())).orElse(null);

		if (isFirstAccountTransactional(lastTransactional) || isNotValidWithdrawAmount(lastTransactional.getBalance(),
		                                                                               input.getAmount())) {
			throw new IllegalStateException("Sorry, but you have a low balance.");
		}

		final BigDecimal expectedBalance = calculateBalance(lastTransactional.getBalance(), input.getAmount().negate());
		transactionalRepository.save(new TransactionalEntity(validAccount,
		                                                     input.getAmount().negate(),
		                                                     expectedBalance));

		return new AccountBalancePayload(validAccount.getId(), expectedBalance);
	}

	private boolean isNotValidWithdrawAmount(BigDecimal balance, BigDecimal amount) {
		return balance.compareTo(amount) < 0;
	}

	@Override
	public Optional<TransactionalEntity> getLastAccountTransactional(GetLastAccountTransactionalInput input) {
		return transactionalRepository.findTopByAccountIdOrderByIdDesc(input.getAccountId());
	}

	private AccountEntity getAccountEntityWithPermission(long accountId) {
		final AccountEntity accountEntity = accountService
				.findAccount(new FindAccountInput(accountId))
				.orElseThrow(() -> new AccessDeniedException("Access Denied!"));
		authenticationFacade.isResourceOwnerOrException(accountEntity.getClient().getId());

		return accountEntity;
	}

	private BigDecimal calculateBalance(BigDecimal realBalance, BigDecimal amount) {
		return realBalance.add(amount);
	}
}
