package space.meduzza.deposittask.service.transactional;

import space.meduzza.deposittask.service.account.repository.entity.AccountEntity;
import space.meduzza.deposittask.service.transactional.repository.entity.TransactionalEntity;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class TransactionalTestUtil {
	public static final BigDecimal INITIAL_BALANCE = BigDecimal.valueOf(1000.0);
	public static final BigDecimal INITIAL_AMOUNT = BigDecimal.valueOf(100.0);

	public static TransactionalEntity createTransactionalEntity(long transactionalId,
	                                                            AccountEntity accountEntity,
	                                                            BigDecimal amount,
	                                                            BigDecimal balance,
	                                                            Timestamp createTime,
	                                                            Timestamp updateTime) {
		final TransactionalEntity transactionalEntity = new TransactionalEntity(accountEntity, amount, balance);
		transactionalEntity.setId(transactionalId);
		transactionalEntity.setCreateTime(createTime);
		transactionalEntity.setUpdateTime(updateTime);

		return transactionalEntity;
	}

	public static TransactionalEntity createTransactionalEntity(long transactionId,
	                                                            AccountEntity accountEntity,
	                                                            BigDecimal amount,
	                                                            BigDecimal balance) {
		final TransactionalEntity transactionalEntity = new TransactionalEntity(accountEntity, amount, balance);
		transactionalEntity.setId(transactionId);
		transactionalEntity.setCreateTime(Timestamp.valueOf(LocalDateTime.now()));
		transactionalEntity.setUpdateTime(Timestamp.valueOf(LocalDateTime.now()));

		return transactionalEntity;
	}
}
