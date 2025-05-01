package pl.ttpsc.taskmanager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import pl.ttpsc.taskmanager.service.UserService;

@Configuration
@EnableWebSecurity
public class SecurityConfig
{

	private final UserService _userService;

	public SecurityConfig(UserService userService)
	{
		_userService = userService;
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception
	{
		http
				.authorizeHttpRequests((requests) -> requests
						.requestMatchers("/login", "/register").permitAll()
						.requestMatchers("/index", "/", "/h2-console/**").authenticated()
						.anyRequest().authenticated()
				).formLogin((form) -> form
						.loginPage("/login")
						.defaultSuccessUrl("/index", true)
						.failureUrl("/loginError")
						.permitAll()
				).logout((logout) ->
						logout.permitAll()
				).csrf((csrf) -> csrf.ignoringRequestMatchers("/h2-console/**")
				).headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable)
				).userDetailsService(_userService);

		return http.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder()
	{
		return new BCryptPasswordEncoder();
	}

}
