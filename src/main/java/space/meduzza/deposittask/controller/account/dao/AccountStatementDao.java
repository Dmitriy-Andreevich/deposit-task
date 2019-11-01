package space.meduzza.deposittask.controller.account.dao;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Data
@AllArgsConstructor
public class AccountStatementDao implements Serializable {
	private final long accountId;

	private final List<TransactionItem> transactions;

	@Data
	@AllArgsConstructor
	public static class TransactionItem implements Serializable {
		private final BigDecimal initialBalance;

		private final BigDecimal amount;

		private final BigDecimal resultBalance;

		private final Timestamp dateTime;
	}
}
