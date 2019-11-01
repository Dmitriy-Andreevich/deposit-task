package space.meduzza.deposittask.service.account.repository.entity;

import lombok.*;
import lombok.experimental.Accessors;
import space.meduzza.deposittask.service.client.repository.entity.ClientEntity;
import space.meduzza.deposittask.util.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Entity
@Table(name = "accounts")
public class AccountEntity extends BaseEntity {
	@NotNull
	@ManyToOne()
	@JoinColumn(nullable = false)
	ClientEntity client;
}
