package es.readtoowell.api_biblioteca.config;

import es.readtoowell.api_biblioteca.model.entity.User;
import es.readtoowell.api_biblioteca.repository.user.UserRepository;
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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * Configuración de seguridad para la aplicación.
 * Define reglas de acceso, autenticación y autorización.
 */
@EnableMethodSecurity
@Configuration
public class SecurityConfig {
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    /**
     * Constructor de SecurityConfig.
     *
     * @param jwtUtil        Utilidad para gestionar JWT.
     * @param userRepository Repositorio de usuarios.
     */
    @Autowired
    public SecurityConfig(JwtUtil jwtUtil, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    /**
     * Configura la cadena de filtros de seguridad y las reglas de autorización.
     *
     * @param http Objeto de configuración de seguridad HTTP.
     * @return La cadena de filtros de seguridad configurada.
     * @throws Exception Si ocurre un error en la configuración.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(auth -> auth
                        // Usuarios no autenticados incluidos
                        .requestMatchers("/auth/**").permitAll() // Inicio de sesión y registro
                        .requestMatchers("/libros").permitAll() // Catálogo de libros

                        // USER
                        .requestMatchers(HttpMethod.GET, "/solicitud-autor/comprobar-pendiente").hasRole("USER")
                        .requestMatchers(HttpMethod.POST, "/solicitud-autor").hasRole("USER")

                        // ADMIN
                        .requestMatchers(HttpMethod.POST, "/usuarios").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/usuarios/*").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/usuarios/*/autor").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.POST, "/libros").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/libros/*").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/libros/*").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.PUT, "/sugerencias/*").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/sugerencias/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.PUT, "/solicitud-autor/*").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/solicitud-autor/**").hasRole("ADMIN")

                        // USER y AUTHOR
                        .requestMatchers(HttpMethod.PUT, "/usuarios").hasAnyRole("USER", "AUTHOR")
                        .requestMatchers(HttpMethod.PUT, "/usuarios/**").hasAnyRole("USER", "AUTHOR")
                        .requestMatchers(HttpMethod.POST, "/usuarios/seguir/*").hasAnyRole("USER", "AUTHOR")
                        .requestMatchers(HttpMethod.POST, "/usuarios/dejar-seguir/*").hasAnyRole("USER", "AUTHOR")

                        .requestMatchers("/objetivos/**").hasAnyRole("USER", "AUTHOR")
                        .requestMatchers("/listas/**").hasAnyRole("USER", "AUTHOR")
                        .requestMatchers(HttpMethod.POST, "/sugerencias/*").hasAnyRole("USER", "AUTHOR")
                        .requestMatchers("/biblioteca/**").hasAnyRole("USER", "AUTHOR")

                        // Todos los usuarios autenticados
                        .requestMatchers("/usuarios/**").authenticated()
                        .requestMatchers("/libros/**").authenticated()
                        .requestMatchers("/libros/*/autor").authenticated()

                        // Cualquier otra petición
                        .anyRequest().authenticated()
                )
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    /**
     * Proveedor de codificación de contraseñas.
     *
     * @return Instancia de BCryptPasswordEncoder.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Devuelve el AuthenticationManager que gestiona la autenticación de usuarios.
     *
     * @param authConfig Configuración de autenticación de Spring Security.
     * @return Instancia de AuthenticationManager.
     * @throws Exception Si ocurre un error al obtener el AuthenticationManager.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    /**
     * Proveedor de autenticación basado en la base de datos.
     *
     * @return Proveedor de autenticación.
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * Servicio de detalles de usuario basado en la base de datos.
     *
     * @return Implementación de UserDetailsService.
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return correo -> {
            User user = userRepository.findByEmail(correo)
                    .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con correo: " + correo));

            return new CustomUserDetails(
                    user.getId(),
                    user.getEmail(),
                    user.getPassword(),
                    List.of(new SimpleGrantedAuthority("ROLE_" + user.getRoleEnum().name()))
            );
        };
    }

    /**
     * Bean del filtro JWT para ser utilizado en la configuración de seguridad.
     *
     * @return Instancia de JwtFilter.
     */
    @Bean
    public JwtFilter jwtFilter() { // Creamos el Bean de JwtFilter
        return new JwtFilter(jwtUtil, userDetailsService());
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:5173"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        configuration.setExposedHeaders(List.of("Authorization"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
