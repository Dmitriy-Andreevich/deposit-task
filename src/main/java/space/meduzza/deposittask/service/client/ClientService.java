package space.meduzza.deposittask.service.client;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import space.meduzza.deposittask.service.client.input.CreateClientInput;
import space.meduzza.deposittask.service.client.repository.entity.ClientEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public interface ClientService extends UserDetailsService {
	static List<SimpleGrantedAuthority> getAuthorities(ClientEntity clientEntity) {
		return Arrays.stream(clientEntity.getAuthorities().split(",")).map(SimpleGrantedAuthority::new).collect(
				Collectors.toList());
	}

	ClientEntity createClient(CreateClientInput input);

	Optional<ClientEntity> findClientById(long id);

	Optional<ClientEntity> findClientByEmail(String email);
}
