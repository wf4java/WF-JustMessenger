package wf.spring.justmessenger.config;



import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.catalina.filters.CorsFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import wf.spring.justmessenger.properties.SecurityProperties;
import wf.spring.justmessenger.security.auth.JwtAuthenticationManager;

import java.net.Inet4Address;
import java.net.InetAddress;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationManager jwtAuthenticationManager;
    private final SecurityProperties securityProperties;


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(configurer -> configurer.configurationSource(corsConfigurationSource()))
                .httpBasic(AbstractHttpConfigurer::disable)
                .anonymous(AbstractHttpConfigurer::disable)
                .securityContext(configurer -> configurer.securityContextRepository(securityContextRepository()))
                .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(registry -> registry
                        .requestMatchers("/ws/**").permitAll()
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/**").authenticated()
                        .anyRequest().denyAll())


                .addFilterBefore(jwtAuthenticationFilter(), BearerTokenAuthenticationFilter.class);



        return http.build();
    }


    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(securityProperties.getCorsAllowedOrigins());
        configuration.addAllowedMethod("*");
        configuration.applyPermitDefaultValues();
        configuration.setAllowCredentials(true);


        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public BearerTokenAuthenticationFilter jwtAuthenticationFilter() {
        return new BearerTokenAuthenticationFilter(jwtAuthenticationManager);
    }


    @Bean
    public RequestAttributeSecurityContextRepository securityContextRepository() {
        return new RequestAttributeSecurityContextRepository();
    }


    @Bean
    public static PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
