package space.meduzza.deposittask.service.client;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import space.meduzza.deposittask.service.client.input.CreateClientInput;
import space.meduzza.deposittask.service.client.repository.ClientRepository;
import space.meduzza.deposittask.service.client.repository.entity.ClientEntity;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class ClientServiceImplTest {
	@MockBean
	ClientRepository clientRepository;

	@MockBean
	PasswordEncoder passwordEncoder;

	@Autowired
	private ClientService clientService;

	private ClientEntity client1;

	@BeforeEach
	void setUp() {
		client1 = ClientTestUtil.createClientEntity(1);
	}

	@Test
	void save_ClientEntity_ReturnEqualClient() {
		when(clientRepository.save(any(ClientEntity.class))).thenReturn(client1);
		final ClientEntity createdClient = clientService.createClient(new CreateClientInput(client1.getEmail(),
		                                                                                    client1.getPassword()));
		assertThat(createdClient).isEqualTo(client1);
	}

	@Test
	void find_ClientEntityById_ReturnEqualClient() {
		when(clientRepository.findById(anyLong())).thenReturn(Optional.of(client1));
		final Optional<ClientEntity> findClient = clientService.findClientById(client1.getId());
		assertThat(findClient.isPresent()).isTrue();
		assertThat(findClient.get()).isEqualTo(client1);
	}

	@Test
	void find_ClientByEmail_ReturnEqualClient() {
		when(clientRepository.findByEmail(anyString())).thenReturn(Optional.of(client1));
		final Optional<ClientEntity> findClient = clientService.findClientByEmail(client1.getEmail());
		assertThat(findClient.isPresent()).isTrue();
		assertThat(findClient.get()).isEqualTo(client1);
	}

	@Test
	void find_SpringUserByUsernameFromDB_ReturnSpringUserEqualDatabaseClientEntity() {
		when(clientService.findClientByEmail(anyString())).thenReturn(Optional.of(client1));
		final UserDetails userDetails = clientService.loadUserByUsername(client1.getEmail());
		assertThat(userDetails.getPassword()).isEqualTo(client1.getPassword());
		assertThat(userDetails.getUsername()).isEqualTo(client1.getEmail());
		assertThat(userDetails.getAuthorities()).hasSize(ClientService.getAuthorities(client1).size());
	}

	@TestConfiguration
	static class Config {
		@Bean
		public ClientService clientService(ClientRepository clientRepository, PasswordEncoder passwordEncoder) {
			return new ClientServiceImpl(clientRepository, passwordEncoder);
		}
	}
}