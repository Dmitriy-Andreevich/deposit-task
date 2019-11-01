package space.meduzza.deposittask.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import space.meduzza.deposittask.service.client.ClientService;

@Configuration
@EnableWebSecurity
class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	private final ClientService clientService;

	private final PasswordEncoder passwordEncoder;

	public WebSecurityConfig(ClientService clientService, PasswordEncoder passwordEncoder) {
		this.clientService = clientService;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(clientService).passwordEncoder(passwordEncoder);
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http
				.csrf()
				.disable()
				.authorizeRequests()
				//                .antMatchers(HttpMethod.POST, "/login*", "/signup*", "/error").permitAll()
				.antMatchers("/login*", "/signup*", "/error")
				.permitAll()
				.anyRequest()
				.authenticated()
				.and()
				.formLogin()
				.permitAll();
	}
}