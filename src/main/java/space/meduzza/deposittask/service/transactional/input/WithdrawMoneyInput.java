package space.meduzza.deposittask.service.transactional.input;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class WithdrawMoneyInput {
	@Min(1)
	@Max(Long.MAX_VALUE)
	public long accountId;

	@NotNull
	@DecimalMin(value = "1")
	public BigDecimal amount;
}
