package space.meduzza.deposittask.service.client.repository.entity;

import lombok.*;
import lombok.experimental.Accessors;
import space.meduzza.deposittask.service.account.repository.entity.AccountEntity;
import space.meduzza.deposittask.util.BaseEntity;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Entity
@Table(name = "clients")
public class ClientEntity extends BaseEntity {
	@Email @Size(max = 100)
	@Column(nullable = false, unique = true)
	String email;

	@NotBlank
	@Column(nullable = false)
	String password;

	@NotBlank
	@Column(nullable = false)
	private String authorities;

	@OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
	List<AccountEntity> accounts;
}
