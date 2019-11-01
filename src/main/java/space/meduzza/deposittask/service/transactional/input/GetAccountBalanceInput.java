package space.meduzza.deposittask.service.transactional.input;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
@AllArgsConstructor
public class GetAccountBalanceInput {
	@Min(1)
	@Max(Long.MAX_VALUE)
	private final long accountId;
}
