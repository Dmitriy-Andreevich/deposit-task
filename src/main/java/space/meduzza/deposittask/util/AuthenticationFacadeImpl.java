package space.meduzza.deposittask.util;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import space.meduzza.deposittask.service.client.ClientService;
import space.meduzza.deposittask.service.client.repository.entity.ClientEntity;

import javax.validation.constraints.NotNull;
import java.util.NoSuchElementException;

@Component
public class AuthenticationFacadeImpl implements AuthenticationFacade {
	private final ClientService clientService;

	public AuthenticationFacadeImpl(ClientService clientService) {
		this.clientService = clientService;
	}

	@Override
	public void setAuthentication(String clientName) {
		SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(clientName, null));
	}

	@Override
	public Authentication getAuthentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}

	@Override
	public String getCurrentClientName() {
		return SecurityContextHolder.getContext().getAuthentication().getName();
	}

	@Override
	public ClientEntity findCurrentClient() throws NoSuchElementException {
		return clientService.findClientByEmail(getCurrentClientName()).orElseThrow(NoSuchElementException::new);
	}

	@Override
	public boolean isResourceOwner(long resourceOwnerId) {
		return findCurrentClient().getId() == resourceOwnerId;
	}

	@Override
	public boolean isResourceOwnerOrException(long resourceOwnerId) throws AccessDeniedException {
		if (isResourceOwner(resourceOwnerId)) {
			return true;
		}
		throw new AccessDeniedException("Access Denied!");
	}

	@Override
	public boolean isResourceOwnerOrException(long resourceOwnerId, @NotNull String errorMessage) throws
			AccessDeniedException {
		if (isResourceOwner(resourceOwnerId)) {
			return true;
		}
		throw new AccessDeniedException(errorMessage);
	}
}
