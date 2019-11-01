package space.meduzza.deposittask.service.account;

import space.meduzza.deposittask.service.account.input.CreateAccountInput;
import space.meduzza.deposittask.service.account.input.FindAccountInput;
import space.meduzza.deposittask.service.account.repository.entity.AccountEntity;

import java.util.Optional;

public interface AccountService {
	AccountEntity createAccount(CreateAccountInput input);

	Optional<AccountEntity> findAccount(FindAccountInput input);
}
