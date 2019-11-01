package space.meduzza.deposittask.service.client;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import space.meduzza.deposittask.service.client.input.CreateClientInput;
import space.meduzza.deposittask.service.client.repository.ClientRepository;
import space.meduzza.deposittask.service.client.repository.entity.ClientEntity;

import java.util.Collections;
import java.util.Optional;
import java.util.function.Function;

@Service
public class ClientServiceImpl implements ClientService {
	private final ClientRepository clientRepository;

	private final PasswordEncoder passwordEncoder;

	public ClientServiceImpl(ClientRepository clientRepository, PasswordEncoder passwordEncoder) {
		this.clientRepository = clientRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public ClientEntity createClient(CreateClientInput input) {
		return clientRepository.save(new ClientEntity(input.getEmail(),
		                                              passwordEncoder.encode(input.getPassword()),
		                                              "ROLE_USER",
		                                              Collections.emptyList()));
	}

	@Override
	public Optional<ClientEntity> findClientById(long id) {
		return clientRepository.findById(id);
	}

	@Override
	public Optional<ClientEntity> findClientByEmail(String email) {
		return clientRepository.findByEmail(email);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return findClientByEmail(username).map(clientToSpringUser()).orElseThrow(() -> new UsernameNotFoundException(
				"User not found"));
	}

	private Function<ClientEntity, User> clientToSpringUser() {
		return (client) -> new User(client.getEmail(), client.getPassword(), ClientService.getAuthorities(client));
	}
}
