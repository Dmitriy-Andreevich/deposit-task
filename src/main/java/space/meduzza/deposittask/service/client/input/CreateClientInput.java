package space.meduzza.deposittask.service.client.input;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
public class CreateClientInput {
	@Email @Size(max = 100) String email;

	@NotBlank @Size(min = 6, max = 100) String password;
}
