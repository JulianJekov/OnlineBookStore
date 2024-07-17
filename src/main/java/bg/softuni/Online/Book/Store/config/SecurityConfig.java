package bg.softuni.Online.Book.Store.config;

import bg.softuni.Online.Book.Store.model.enums.UserRole;
import bg.softuni.Online.Book.Store.oauth.OAuthSuccessHandler;
import bg.softuni.Online.Book.Store.repository.UserRepository;
import bg.softuni.Online.Book.Store.service.impl.BookStoreDetailsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final String rememberMeKey;

    public SecurityConfig(@Value("${bookstore.remember.me.key}") String rememberMeKey) {
        this.rememberMeKey = rememberMeKey;
    }

    @Bean
    public SecurityFilterChain
    securityFilterChain(HttpSecurity http,
                        CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler,
                        CustomAuthenticationFailureHandler customAuthenticationFailureHandler,
                        OAuthSuccessHandler oAuthSuccessHandler
    ) throws Exception {
        return
                http
                        .authorizeHttpRequests(
                                authorizeRequest -> {
                                    authorizeRequest
                                            .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                                            .requestMatchers("/", "/users/register", "/users/login",
                                                    "/users/login-error", "/about").permitAll()
                                            .requestMatchers("/error").permitAll()
                                            .requestMatchers("/activate").permitAll()
                                            .requestMatchers("requestActivationByEmail").permitAll()
                                            .requestMatchers("/books/add").hasRole(UserRole.ADMIN.name())
                                            .requestMatchers("/books/edit/**").hasRole(UserRole.ADMIN.name())
                                            .requestMatchers("/books/delete/**").hasRole(UserRole.ADMIN.name())
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
                                            .successHandler(customAuthenticationSuccessHandler)
                                            .failureHandler(customAuthenticationFailureHandler);
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
                        .rememberMe(
                                rememberMe -> {
                                    rememberMe
                                            .key(rememberMeKey)
                                            .rememberMeParameter("remember-me")
                                            .rememberMeCookieName("remember-me");
                                }
                        )
                        .oauth2Login(
                                oauth2Login -> {
                                    oauth2Login
                                            .successHandler(oAuthSuccessHandler);
                                }
                        )
                        .build();
    }

    @Bean
    public BookStoreDetailsService userDetailsService(UserRepository userRepository) {
        return new BookStoreDetailsService(userRepository);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return Pbkdf2PasswordEncoder.defaultsForSpringSecurity_v5_8();
    }

}
