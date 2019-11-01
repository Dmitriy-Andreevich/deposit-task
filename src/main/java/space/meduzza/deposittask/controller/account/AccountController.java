package space.meduzza.deposittask.controller.account;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import space.meduzza.deposittask.controller.account.dao.AccountDao;
import space.meduzza.deposittask.controller.account.dao.AccountStatementDao;
import space.meduzza.deposittask.service.account.AccountService;
import space.meduzza.deposittask.service.account.input.CreateAccountInput;
import space.meduzza.deposittask.service.account.repository.entity.AccountEntity;
import space.meduzza.deposittask.service.transactional.TransactionalService;
import space.meduzza.deposittask.service.transactional.input.DepositMoneyInput;
import space.meduzza.deposittask.service.transactional.input.GetAccountBalanceInput;
import space.meduzza.deposittask.service.transactional.input.GetAccountStatementInput;
import space.meduzza.deposittask.service.transactional.input.WithdrawMoneyInput;
import space.meduzza.deposittask.service.transactional.payload.AccountBalancePayload;
import space.meduzza.deposittask.service.transactional.payload.AccountStatementPayload;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/account")
public class AccountController {
	private final AccountService accountService;

	private final TransactionalService transactionalService;

	public AccountController(AccountService accountService, TransactionalService transactionalService) {
		this.accountService = accountService;
		this.transactionalService = transactionalService;
	}

	@PostMapping("/create")
	ResponseEntity<AccountDao> create(@Valid @ModelAttribute CreateAccountInput input) {
		final AccountEntity createdAccount = accountService.createAccount(input);
		final AccountDao resultDao = new AccountDao(createdAccount.getId(), BigDecimal.ZERO);

		return ResponseEntity.ok().body(resultDao);
	}

	@GetMapping("/get")
	ResponseEntity<AccountDao> getBalance(@Valid @ModelAttribute GetAccountBalanceInput input) {
		final AccountBalancePayload accountBalance = transactionalService.getAccountBalance(input);
		final AccountDao resultDao = new AccountDao(accountBalance.getAccountId(), accountBalance.getBalance());

		return ResponseEntity.ok().body(resultDao);
	}

	@GetMapping("/statement")
	ResponseEntity<AccountStatementDao> getStatement(@Valid @ModelAttribute GetAccountStatementInput input) {
		final AccountStatementPayload accountStatement = transactionalService.getAccountStatement(input);
		final AccountStatementDao resultDao = new AccountStatementDao(accountStatement.getAccountId(),
		                                                              convertEntityStatementItemsToDaoStatementItems(
				                                                              accountStatement));

		return ResponseEntity.ok().body(resultDao);
	}

	private List<AccountStatementDao.TransactionItem> convertEntityStatementItemsToDaoStatementItems(
			AccountStatementPayload accountStatement) {
		return accountStatement.getItems().stream().map(entityStatementItemToDaoStatementItemFunction()).collect(
				Collectors.toList());
	}

	private Function<AccountStatementPayload.Item, AccountStatementDao.TransactionItem> entityStatementItemToDaoStatementItemFunction() {
		return (item) -> new AccountStatementDao.TransactionItem(item.getBalance().subtract(item.getAmount()),
		                                                         item.getAmount(),
		                                                         item.getBalance(),
		                                                         item.getDateTime());
	}

	@GetMapping("/deposit")
	ResponseEntity<AccountDao> deposit(@Valid @ModelAttribute DepositMoneyInput input) {
		final AccountBalancePayload accountBalance = transactionalService.depositMoney(input);
		final AccountDao resultDao = new AccountDao(accountBalance.getAccountId(), accountBalance.getBalance());

		return ResponseEntity.ok().body(resultDao);
	}

	@GetMapping("/withdraw")
	ResponseEntity<AccountDao> withdraw(@Valid @ModelAttribute WithdrawMoneyInput input) {
		final AccountBalancePayload accountBalance = transactionalService.withdrawMoney(input);
		final AccountDao resultDao = new AccountDao(accountBalance.getAccountId(), accountBalance.getBalance());

		return ResponseEntity.ok().body(resultDao);
	}

}
