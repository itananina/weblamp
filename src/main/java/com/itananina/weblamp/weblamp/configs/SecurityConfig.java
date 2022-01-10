package com.itananina.weblamp.weblamp.configs;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final JwtRequestFilter jwtRequestFilter;

    @Value("${jwt.switchOn}")
    private boolean jwtSwitchOn;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        if(jwtSwitchOn) {
            http.csrf().disable()
                    .authorizeRequests()
                    .antMatchers("/api/v1/orders/{id:\\d+}").permitAll()
                    .antMatchers("/api/v1/orders/**").authenticated()
                    .anyRequest().permitAll()
                    .and()
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                    .headers().frameOptions().disable()
                    .and()
                    .exceptionHandling()
                    .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));

            http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class); //кто, перед кем
        } else {
            http.authorizeRequests()
                    .antMatchers("/api/v1/cart/**").authenticated()
                    .anyRequest().permitAll()
                    .and()
                    .formLogin()
                        .defaultSuccessUrl("/api/v1/cart/");
        }
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
