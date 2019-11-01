package space.meduzza.deposittask.service.transactional.payload;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Data
@AllArgsConstructor
public class AccountStatementPayload implements Serializable {
	@Min(1)
	@Max(Long.MAX_VALUE)
	private final long accountId;

	@NotNull
	private final List<Item> items;

	@Data
	@AllArgsConstructor
	public static class Item implements Serializable {
		private final BigDecimal amount;

		private final BigDecimal balance;

		private final Timestamp dateTime;
	}
}
