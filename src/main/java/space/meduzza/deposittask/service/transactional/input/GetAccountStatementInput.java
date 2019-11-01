package space.meduzza.deposittask.service.transactional.input;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
@AllArgsConstructor
public class GetAccountStatementInput {
	@Min(1)
	@Max(Long.MAX_VALUE)
	public long accountId;

	@Min(0) @Max(Integer.MAX_VALUE) int page;
}
