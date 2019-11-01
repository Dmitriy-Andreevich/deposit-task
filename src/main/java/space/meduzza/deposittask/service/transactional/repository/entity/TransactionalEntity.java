package space.meduzza.deposittask.service.transactional.repository.entity;

import lombok.*;
import lombok.experimental.Accessors;
import space.meduzza.deposittask.service.account.repository.entity.AccountEntity;
import space.meduzza.deposittask.util.BaseEntity;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Entity
@Table(name = "transactions")
public class TransactionalEntity extends BaseEntity {
	@NotNull
	@ManyToOne()
	@JoinColumn(name = "account_id", nullable = false)
	AccountEntity account;

	@NotNull
	@Column(nullable = false, scale = 5)
	BigDecimal amount;

	@NotNull @DecimalMin(value = "0")
	@Column(nullable = false, scale = 5)
	BigDecimal balance;
}
