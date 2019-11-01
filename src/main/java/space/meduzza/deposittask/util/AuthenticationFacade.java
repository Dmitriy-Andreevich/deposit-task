package space.meduzza.deposittask.util;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import space.meduzza.deposittask.service.client.repository.entity.ClientEntity;

import java.util.NoSuchElementException;

public interface AuthenticationFacade {
	void setAuthentication(String clientName);

	Authentication getAuthentication();

	String getCurrentClientName();

	ClientEntity findCurrentClient() throws NoSuchElementException;

	boolean isResourceOwner(long resourceOwnerId);

	boolean isResourceOwnerOrException(long resourceOwnerId) throws AccessDeniedException;

	boolean isResourceOwnerOrException(long resourceOwnerId, String errorMessage) throws AccessDeniedException;
}
