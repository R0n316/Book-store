package ru.alex.BookStoreApp.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import ru.alex.BookStoreApp.services.PersonDetailsService;


@EnableWebSecurity
@EnableMethodSecurity
@Configuration
public class SecurityConfig {

    private final PersonDetailsService personDetailsService;

    @Autowired
    public SecurityConfig(PersonDetailsService personDetailsService) {
        this.personDetailsService = personDetailsService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.authorizeHttpRequests(req -> req.requestMatchers(HttpMethod.POST,"/books/save").permitAll());
        http.authorizeHttpRequests(req -> req.requestMatchers(HttpMethod.PATCH, "/books/api/**").permitAll());
        http.authorizeHttpRequests(req -> req.requestMatchers("/books/favorites","/books/inCart").authenticated());
        http.authorizeHttpRequests(req -> req.anyRequest().permitAll());
        http.csrf(a -> a.ignoringRequestMatchers("/books/api/**"));
        http.formLogin(form -> form.loginPage("/auth/login").permitAll().loginProcessingUrl("/process_login")
                .defaultSuccessUrl("/books")
                .failureUrl("/auth/login?error"));
        http.logout(session -> session.logoutUrl("/auth/logout").logoutSuccessUrl("/books"));
        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider authProvider(){
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(personDetailsService);
        authProvider.setPasswordEncoder(getPasswordEncoder());
        return authProvider;
    }

    @Bean
    public PasswordEncoder getPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
