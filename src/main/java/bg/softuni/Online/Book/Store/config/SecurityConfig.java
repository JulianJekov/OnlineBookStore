package bg.softuni.Online.Book.Store.config;

import bg.softuni.Online.Book.Store.repository.UserRepository;
import bg.softuni.Online.Book.Store.service.impl.BookStoreDetailsService;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain
    securityFilterChain(HttpSecurity http,
                        CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler) throws Exception {
        return
                http
                        .csrf(
                                AbstractHttpConfigurer::disable
                        )
                        .authorizeHttpRequests(
                                authorizeRequest -> {
                                    authorizeRequest
                                            .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                                            .requestMatchers("/", "/users/register", "/users/login", "/about").permitAll()
                                            .anyRequest().authenticated();
                                }
                        )
                        .formLogin(
                                formLogin -> {
                                    formLogin.
                                            loginPage("/users/login")
                                            .usernameParameter("email")
                                            .passwordParameter("password")
                                            .defaultSuccessUrl("/home", true)
//                                            .successHandler(customAuthenticationSuccessHandler)
                                            .failureUrl("/users/login-error");
                                }
                        )
                        .logout(
                                logout -> {
                                    logout
                                            .logoutUrl("/users/logout")
                                            .logoutSuccessUrl("/users/login")
                                            .invalidateHttpSession(true);
                                }
                        )
                        .build();
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return new BookStoreDetailsService(userRepository);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return Pbkdf2PasswordEncoder.defaultsForSpringSecurity_v5_8();
    }

}
