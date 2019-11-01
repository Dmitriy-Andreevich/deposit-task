package space.meduzza.deposittask.service.account;

import org.springframework.stereotype.Service;
import space.meduzza.deposittask.service.account.input.CreateAccountInput;
import space.meduzza.deposittask.service.account.input.FindAccountInput;
import space.meduzza.deposittask.service.account.repository.AccountRepository;
import space.meduzza.deposittask.service.account.repository.entity.AccountEntity;
import space.meduzza.deposittask.service.client.repository.entity.ClientEntity;
import space.meduzza.deposittask.util.AuthenticationFacade;

import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService {
	private final AccountRepository accountRepository;

	private final AuthenticationFacade authenticationFacade;

	public AccountServiceImpl(AccountRepository accountRepository, AuthenticationFacade authenticationFacade) {
		this.accountRepository = accountRepository;
		this.authenticationFacade = authenticationFacade;
	}

	@Override
	public AccountEntity createAccount(CreateAccountInput input) {
		final ClientEntity currentClient = authenticationFacade.findCurrentClient();

		return accountRepository.save(new AccountEntity(currentClient));
	}

	@Override
	public Optional<AccountEntity> findAccount(FindAccountInput input) {
		return accountRepository.findById(input.getId());
	}
}
