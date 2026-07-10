package com.chen.sfs.config.security;

import com.chen.sfs.repository.UsersRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.PATCH;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder.BCryptVersion.$2B;

@Slf4j
@Configuration
public class SecurityConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) {
		return http.authorizeHttpRequests(auth ->
				   auth.requestMatchers(POST, "/api/files/upload").hasAuthority("sfs:files:upload")
			               .requestMatchers(GET, "/api/files/{id}/download").hasAuthority("sfs:files:download")
			               .requestMatchers(DELETE, "/api/files/{id}").hasAuthority("sfs:files:delete")
			               .requestMatchers(GET, "/api/files").hasAuthority("sfs:files:list")
			               .requestMatchers(PATCH, "/api/users/me/password").hasAuthority("sfs:users:password")
			               .requestMatchers(PUT, "/api/users/{username}").hasAuthority("sfs:users:update")
			               .requestMatchers(POST, "/api/users").hasAuthority("sfs:users:create")
			               .requestMatchers("/actuator/health").permitAll()
			               .anyRequest().authenticated()
			   )
		           .httpBasic(Customizer.withDefaults())
		           .formLogin(form ->
				   form.successHandler((_, resp, auth) -> {
					       resp.setStatus(OK.value());
					       resp.setContentType(APPLICATION_JSON_VALUE);
					       resp.getWriter().write("{\"success\":true,\"message\":\"ok\",\"data\":{\"username\":\"" + auth.getName() + "\"}}");
				       })
			               .failureHandler((_, resp, _) -> {
					       resp.setStatus(UNAUTHORIZED.value());
					       resp.setCharacterEncoding(UTF_8);
					       resp.setContentType(APPLICATION_JSON_VALUE);
					       resp.getWriter().write("{\"success\": false,\"message\":\"Invalid username or password\"}");
				       })
			   )
		           .logout(logout ->
				   logout.logoutUrl("/logout")
			   )
		           .csrf(AbstractHttpConfigurer::disable)
		           .exceptionHandling(err ->
				   err.accessDeniedHandler((_, resp, _) -> {
					      resp.setStatus(FORBIDDEN.value());
					      resp.setContentType(APPLICATION_JSON_VALUE);
					      resp.setCharacterEncoding(UTF_8);
					      resp.getWriter().write("{\"success\": false,\"message\":\"Insufficient permissions\"}");
				      })
			              .authenticationEntryPoint((_, resp, _) -> {
					      resp.setStatus(UNAUTHORIZED.value());
					      resp.setCharacterEncoding(UTF_8);
					      resp.setContentType(APPLICATION_JSON_VALUE);
					      resp.getWriter().write("{\"success\": false,\"message\":\"Invalid username or password\"}");
				      })
			   )
		           .build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder($2B, 12);
	}

	@Bean
	public UserDetailsService userDetailsService(UsersRepository repository) {
		return new DefaultUserDetailsService(repository);
	}

}
