package space.meduzza.deposittask.service.transactional;

import org.springframework.validation.annotation.Validated;
import space.meduzza.deposittask.service.transactional.input.*;
import space.meduzza.deposittask.service.transactional.payload.AccountBalancePayload;
import space.meduzza.deposittask.service.transactional.payload.AccountStatementPayload;
import space.meduzza.deposittask.service.transactional.repository.entity.TransactionalEntity;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.util.Optional;

@Validated
public interface TransactionalService {
	AccountBalancePayload getAccountBalance(@Valid GetAccountBalanceInput input);

	AccountStatementPayload getAccountStatement(@Valid GetAccountStatementInput input);

	AccountBalancePayload depositMoney(@Valid DepositMoneyInput input) throws ConstraintViolationException;

	AccountBalancePayload withdrawMoney(@Valid WithdrawMoneyInput input);

	Optional<TransactionalEntity> getLastAccountTransactional(@Valid GetLastAccountTransactionalInput input);
}
