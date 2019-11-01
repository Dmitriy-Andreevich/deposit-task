package space.meduzza.deposittask.service.account;

import space.meduzza.deposittask.service.account.repository.entity.AccountEntity;
import space.meduzza.deposittask.service.client.repository.entity.ClientEntity;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class AccountTestUtil {

	public static AccountEntity createAccountEntity(long accountId,
	                                                ClientEntity clientEntity,
	                                                Timestamp createTime,
	                                                Timestamp updateTime) {
		final AccountEntity accountEntity = new AccountEntity(clientEntity);
		accountEntity.setId(accountId);
		accountEntity.setCreateTime(createTime);
		accountEntity.setUpdateTime(updateTime);

		return accountEntity;
	}

	public static AccountEntity createAccountEntity(long accountId, ClientEntity clientEntity) {
		final AccountEntity accountEntity = new AccountEntity(clientEntity);
		accountEntity.setId(accountId);
		accountEntity.setCreateTime(Timestamp.valueOf(LocalDateTime.now()));
		accountEntity.setUpdateTime(Timestamp.valueOf(LocalDateTime.now()));

		return accountEntity;
	}
}
