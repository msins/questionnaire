package edu.fer.project.questionnaire.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  private static final String LOGIN_PROCESSING_URL = "/login";
  private static final String LOGIN_FAILURE_URL = "/login?error";
  private static final String LOGIN_URL = "/login";
  private static final String LOGOUT_SUCCESS_URL = "/login";

  private final PasswordEncoder passwordEncoder;

  @Value("${spring.security.user.name}")
  private String username;

  @Value("${spring.security.user.password}")
  private String password;

  @Autowired
  public SecurityConfig(PasswordEncoder encoder) {
    this.passwordEncoder = encoder;
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    // Vaadin has csrf protection
    http.csrf().disable()
        .requestCache().requestCache(new CustomRequestCache())
        .and()
        .authorizeRequests()
        .requestMatchers(SecurityUtils::isFrameworkInternalRequest).permitAll()
        // TODO permissions on API
        .antMatchers("/api/**").permitAll()
        .anyRequest().authenticated()
        .and()
        .formLogin()
        .loginPage(LOGIN_URL).permitAll()
        .loginProcessingUrl(LOGIN_PROCESSING_URL)
        .failureUrl(LOGIN_FAILURE_URL)
        .and()
        .logout()
        .logoutSuccessUrl(LOGOUT_SUCCESS_URL)
        .deleteCookies("JSESSIONID");
  }


  @Override
  public void configure(WebSecurity web) throws Exception {
    web.ignoring().antMatchers(
        "/VAADIN/**",
        "/favicon.ico",
        "/robots.txt",
        "/manifest.webmanifest",
        "/sw.js",
        "/offline.html",
        "/icons/**",
        "/images/**",
        "/styles/**",
        "/frontend/**",
        "/frontent-es5/**",
        "/frontend-es6/**"
    );
  }

  @Override
  @Bean
  protected UserDetailsService userDetailsService() {
    var admin = User.builder()
        .username(username)
        .password(passwordEncoder.encode(password))
        .roles("ADMIN")
        .build();

    return new InMemoryUserDetailsManager(admin);
  }

}
