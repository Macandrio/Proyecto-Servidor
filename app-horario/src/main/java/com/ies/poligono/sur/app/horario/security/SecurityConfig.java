package com.ies.poligono.sur.app.horario.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import jakarta.servlet.http.HttpServletResponse;





@Configuration
@EnableWebSecurity
@EnableMethodSecurity  
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private JwtService jwtService;

    
//    cifrar contraseñas
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    autenticar usuarios con su email
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

//    toda la lógica de seguridad de tus rutas
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> {}) 
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/login").permitAll()
                .requestMatchers("/api/recuperacion-password").permitAll()
                
                // AUSENCIAS
                .requestMatchers(HttpMethod.PATCH, "/api/ausencias/justificar-dia").hasAnyRole("ADMINISTRADOR")
                .requestMatchers(HttpMethod.POST, "/api/ausencias/**").hasAnyRole("ADMINISTRADOR", "PROFESOR")
                .requestMatchers(HttpMethod.GET, "/api/ausencias/**").hasAnyRole("ADMINISTRADOR", "PROFESOR")
                .requestMatchers(HttpMethod.DELETE, "/api/ausencias/**").hasAnyRole("ADMINISTRADOR", "PROFESOR")

                // USUARIOS
                .requestMatchers(HttpMethod.PUT, "/api/usuarios/*/cambiar-contraseña").hasAnyRole("ADMINISTRADOR", "PROFESOR")
                .requestMatchers(HttpMethod.GET, "/api/usuarios/*/imagen").hasAnyRole("ADMINISTRADOR", "PROFESOR")
                .requestMatchers(HttpMethod.POST, "/api/usuarios/*/imagen").hasAnyRole("ADMINISTRADOR", "PROFESOR")
                // PERMISOS GENERALES 
                .requestMatchers(HttpMethod.POST, "/api/register").hasRole("ADMINISTRADOR")
                .requestMatchers(HttpMethod.GET, "/api/usuarios/**").hasRole("ADMINISTRADOR")
                .requestMatchers(HttpMethod.POST, "/api/usuarios/**").hasRole("ADMINISTRADOR")
                .requestMatchers(HttpMethod.PUT, "/api/usuarios/**").hasRole("ADMINISTRADOR")
                .requestMatchers(HttpMethod.DELETE, "/api/usuarios/**").hasRole("ADMINISTRADOR")
                
                
                
                .anyRequest().authenticated()
                
                
            )
            
            .exceptionHandling(eh -> eh
                    .accessDeniedHandler(customAccessDeniedHandler())
                )
            
            .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .addFilterBefore(jwtRequestFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


//    Leer el token JWT de cada petición
    @Bean
    public JwtRequestFilter jwtRequestFilter() {
        return new JwtRequestFilter(jwtService, customUserDetailsService);
    }
    
    
    @Bean
    public AccessDeniedHandler customAccessDeniedHandler() {
        return (request, response, accessDeniedException) -> {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"No tienes permisos para acceder a esta funcionalidad.\"}");
        };
    }

}
