package space.meduzza.deposittask.controller.account.dao;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class AccountDao implements Serializable {
	private final long accountId;

	private final BigDecimal balance;
}
