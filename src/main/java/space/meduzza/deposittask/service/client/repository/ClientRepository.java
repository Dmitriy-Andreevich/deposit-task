package space.meduzza.deposittask.service.client.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import space.meduzza.deposittask.service.client.repository.entity.ClientEntity;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<ClientEntity, Long> {
	Optional<ClientEntity> findByEmail(String email);
}
