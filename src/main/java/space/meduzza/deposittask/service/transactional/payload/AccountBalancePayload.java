package space.meduzza.deposittask.service.transactional.payload;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class AccountBalancePayload implements Serializable {
	@Min(1)
	@Max(Long.MAX_VALUE)
	private final long accountId;

	@NotNull
	@DecimalMin(value = "0")
	private final BigDecimal balance;
}
