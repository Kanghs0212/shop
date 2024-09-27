package com.metato.shop;

import com.metato.shop.Member.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    PasswordEncoder passwordEncoder(){
       return new BCryptPasswordEncoder();
    }

    @Bean
    public CsrfTokenRepository csrfTokenRepository() {
        HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
        repository.setHeaderName("X-XSRF-TOKEN");
        return repository;
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
//        http.csrf((csrf)->csrf.disable()); // 외부에서 내 사이트에 form으로 포스트공격하는걸 막는것. 잠시끈다.

        http.sessionManagement((session) -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        http.addFilterBefore(new JwtFilter(), ExceptionTranslationFilter.class);

        http.csrf(csrf -> csrf.csrfTokenRepository(csrfTokenRepository())
                .ignoringRequestMatchers("/login")
                .ignoringRequestMatchers("/login/jwt")
        );
        http.authorizeHttpRequests((authorize)->
                    authorize.requestMatchers("/**").permitAll()
                );
//        http.formLogin((formLogin) -> formLogin.loginPage("/login")
//                .defaultSuccessUrl("/")
//        );
//        http.logout(logout -> logout.logoutUrl("/logout").logoutSuccessUrl("/"));
        return http.build();
    }
}
