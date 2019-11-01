package space.meduzza.deposittask.service.transactional.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import space.meduzza.deposittask.service.transactional.repository.entity.TransactionalEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionalRepository extends JpaRepository<TransactionalEntity, Long> {
	Optional<TransactionalEntity> findTopByAccountIdOrderByIdDesc(long accountId);

	List<TransactionalEntity> findAllByAccountIdOrderByIdDesc(long accountId, Pageable page);
}
