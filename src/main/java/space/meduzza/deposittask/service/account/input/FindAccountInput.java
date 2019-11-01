package space.meduzza.deposittask.service.account.input;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
@AllArgsConstructor
public class FindAccountInput {
	@Min(1) @Max(Long.MAX_VALUE) long id;
}
