package space.meduzza.deposittask.service.client;

import space.meduzza.deposittask.service.account.repository.entity.AccountEntity;
import space.meduzza.deposittask.service.client.repository.entity.ClientEntity;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

public class ClientTestUtil {

	public static ClientEntity createClientEntity(long clientId,
	                                              String email,
	                                              String password,
	                                              String authorities,
	                                              List<AccountEntity> accounts,
	                                              Timestamp createTime,
	                                              Timestamp updateTime) {
		final ClientEntity clientEntity = new ClientEntity(email, password, authorities, accounts);
		clientEntity.setId(clientId);
		clientEntity.setCreateTime(createTime);
		clientEntity.setUpdateTime(updateTime);

		return clientEntity;
	}

	public static ClientEntity createClientEntity(long clientId) {
		final ClientEntity clientEntity = new ClientEntity("test" + clientId + "@mail.com",
		                                                   "password" + clientId,
		                                                   "ROLE_USER",
		                                                   Collections.emptyList());
		clientEntity.setId(clientId);
		clientEntity.setCreateTime(Timestamp.valueOf(LocalDateTime.now()));
		clientEntity.setUpdateTime(Timestamp.valueOf(LocalDateTime.now()));

		return clientEntity;
	}
}
