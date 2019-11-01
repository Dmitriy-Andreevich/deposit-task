package space.meduzza.deposittask.controller.registration;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import space.meduzza.deposittask.service.client.ClientService;
import space.meduzza.deposittask.service.client.input.CreateClientInput;
import space.meduzza.deposittask.util.AuthenticationFacade;

import javax.validation.Valid;

@Controller
public class RegistrationController {
	private final AuthenticationFacade authenticationFacade;

	private final ClientService clientService;

	public RegistrationController(AuthenticationFacade authenticationFacade, ClientService clientService) {
		this.authenticationFacade = authenticationFacade;
		this.clientService = clientService;
	}

	@PostMapping("/signup")
	ResponseEntity registration(@Valid @ModelAttribute CreateClientInput input) {
		clientService.createClient(input);
		authenticationFacade.setAuthentication(input.getEmail());
		return ResponseEntity.ok().build();
	}
}
