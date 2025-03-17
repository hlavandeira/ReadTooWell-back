package es.readtoowell.api_biblioteca.config;

import es.readtoowell.api_biblioteca.config.security.CustomUserDetails;
import es.readtoowell.api_biblioteca.model.User;
import es.readtoowell.api_biblioteca.repository.UserRepository;
import es.readtoowell.api_biblioteca.config.security.JwtFilter;
import es.readtoowell.api_biblioteca.config.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Collections;
import java.util.List;

@EnableMethodSecurity
@Configuration
public class SecurityConfig {
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @Autowired
    public SecurityConfig(JwtUtil jwtUtil, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll()
                        // ADMIN
                        .requestMatchers(HttpMethod.POST, "/usuarios").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/usuarios/*").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/libros").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/libros/*").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/libros/*").hasRole("ADMIN")
                        // USER y AUTHOR
                        .requestMatchers(HttpMethod.PUT, "/usuarios/*").hasAnyRole("USER", "AUTHOR")
                        .requestMatchers(HttpMethod.POST, "/usuarios/*/seguir/*")
                                .hasAnyRole("USER", "AUTHOR")
                        .requestMatchers(HttpMethod.POST, "/usuarios/*/dejar-seguir/*")
                                .hasAnyRole("USER", "AUTHOR")
                        .requestMatchers("/objetivos/**").hasAnyRole("USER", "AUTHOR")
                        .requestMatchers("/listas/**").hasAnyRole("USER", "AUTHOR")
                        // TODOS
                        .requestMatchers(HttpMethod.GET, "/usuarios/**").authenticated()
                        .requestMatchers(HttpMethod.GET, "/libros/**").authenticated()


                        .anyRequest().authenticated()
                )
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return correo -> {
            User user = userRepository.findByCorreo(correo)
                    .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con correo: " + correo));

            return new CustomUserDetails(
                    user.getId(), // ✅ Pasamos el ID del usuario
                    user.getCorreo(),
                    user.getContraseña(),
                    List.of(new SimpleGrantedAuthority("ROLE_" + user.getRoleEnum().name()))
            );
        };
    }

    @Bean
    public JwtFilter jwtFilter() { // Creamos el Bean de JwtFilter
        return new JwtFilter(jwtUtil, userDetailsService());
    }
}
