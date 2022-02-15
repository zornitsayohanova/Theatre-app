package com.theatreapp.theatre.config;

import com.theatreapp.theatre.services.UserServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@AllArgsConstructor
@EnableWebSecurity
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserServiceImpl userServiceImpl;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
            auth
                    .userDetailsService(userServiceImpl)
                    .passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.ALWAYS);
        http.csrf().disable();
        http.headers().frameOptions().disable();
        http
                .authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/add-actor-to-favourites/**").hasAuthority("USER")
                .antMatchers("/add-play-to-favourites/**").hasAuthority("USER")
                .antMatchers("/add-theatre-to-favourites/**").hasAuthority("USER")
                .antMatchers("/rate**/**").hasAuthority("USER")
                .antMatchers("/profile").hasAuthority("USER")
                .antMatchers("/targeted**").hasAuthority("USER")
                .antMatchers("/favourite**").hasAuthority("USER")
                .antMatchers("/rated**").hasAuthority("USER")
                .antMatchers("/watched**").hasAuthority("USER")
                .antMatchers("/get-targeted-actor-progress**").hasAuthority("USER")
                .antMatchers("/get-targeted-theatre-progress**").hasAuthority("USER")
                .antMatchers("/reserved-seats/**").hasAuthority("USER")
                .antMatchers("/won-seats/**").hasAuthority("USER")
                .antMatchers("/won-seats/").hasAuthority("USER")
                .antMatchers("/reserve-selected-seats").hasAuthority("USER")
                .antMatchers("/add-comment/**").hasAuthority("USER")
                .antMatchers("/game-rules-and-user-statistics").hasAuthority("USER")
                .antMatchers("/view-game-quiz").hasAuthority("USER")
                .antMatchers("/send-game-quiz").hasAuthority("USER")
                .antMatchers("/choose-play-date/**").authenticated()
                .antMatchers("/view-play-date-seating-plan/**").authenticated()
                .antMatchers("/add**").hasAuthority("ADMIN1")
                .antMatchers("/edit**").hasAuthority("ADMIN1")
                .antMatchers("/delete**").hasAuthority("ADMIN1")
                .antMatchers("/select**").hasAuthority("ADMIN1")
                .antMatchers("/choose-game-condition/**").hasAuthority("ADMIN1")
                .antMatchers("/choose-won-seats/**").hasAuthority("ADMIN1")
                .antMatchers("/find-user").hasAuthority("ADMIN2")
                .antMatchers("/get-user-reserved-seats/**").hasAuthority("ADMIN2")
                .antMatchers("/delete-paid-seat/**").hasAuthority("ADMIN2")
                .antMatchers("/mark-seat-as-won/**").hasAuthority("ADMIN2")
                .anyRequest().permitAll()
                .and()
                .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/home", true)
                .failureUrl("/login?error")
                .permitAll()
                .and()
                .exceptionHandling().accessDeniedPage("/unauthorized")
                .and()
                .logout()
                .logoutSuccessUrl("/home");
    }
}