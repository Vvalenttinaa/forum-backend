package com.example.forumapp.config;

import com.example.forumapp.models.enums.Role;
import com.example.forumapp.services.UserServiceDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    UserServiceDetails userService;

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                //.csrf(csrf -> csrf.csrfTokenRequestHandler(new XorCsrfTokenRequestAttributeHandler()))
                .authorizeHttpRequests((authorize) -> authorize
                        //.requestMatchers("/csrf").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/**")
                        .permitAll()
                        .requestMatchers("/user/**").hasRole("admin")
                      //  .requestMatchers(HttpMethod.PUT,"/forum/comment/approve/**").hasAnyRole("admin", "moderator")
                      //  .requestMatchers(HttpMethod.PUT,"/forum/comment/block/**").hasAnyRole(Role.ADMIN.toString().toLowerCase(), Role.MODERATOR.toString().toLowerCase())
                        .anyRequest().authenticated()
                )
                .sessionManagement(manager -> manager.sessionCreationPolicy(STATELESS))
                .authenticationProvider(daoAuthenticationProvider())
                .addFilterBefore(
                        jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        // .httpBasic(Customizer.withDefaults());
        return http.build();
    }

    @Bean
    DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }

//    @Bean
//    UserDetailsService userDetailsService() {
//        UserDetails user1 = User.builder()
//                .username("marko")
//                .password(passwordEncoder().encode("marko"))
//                .roles(Role.ADMIN.toString())
//                .build();
//
//        UserDetails user2 = User.builder()
//                .username("bojana")
//                .password(passwordEncoder().encode("bojana"))
//                .roles(Role.USER.toString())
//                .build();
//
//        System.out.println(passwordEncoder().encode("admin"));
//        System.out.println(passwordEncoder().encode("user"));
//
//        return new InMemoryUserDetailsManager(user1, user2);
//    }
}
