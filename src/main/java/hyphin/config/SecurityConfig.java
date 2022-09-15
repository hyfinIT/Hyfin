package hyphin.config;

import hyphin.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Order(1)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final AuthProvider authProvider;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers("/assets/**")
                .antMatchers("/dev/**")
                .antMatchers("/end-challenges/**");
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/login").permitAll()
                .antMatchers("/loginprocessing").permitAll()
                .antMatchers("/start.html").permitAll()
                .antMatchers("/index.html").permitAll()
                .antMatchers("/").permitAll()
                .antMatchers("/log").permitAll()
                .antMatchers("/login/google").permitAll()
                .antMatchers("/auth").permitAll()
                .antMatchers("/RegistrationSuccess").permitAll()
                .antMatchers("/Register").permitAll()
                .antMatchers("/faqs.html").permitAll()
                .antMatchers("/glossary.html").permitAll()
                .antMatchers("/**").hasAnyAuthority("ROLE_USER", "USER", "ROLE_ADMIN", "ADMIN")
                .and()
                .formLogin()
                .loginPage("/start.html")
                .loginProcessingUrl("/loginprocessing")
                .usernameParameter("username").passwordParameter("password")
                .failureUrl("/access_denied")
                .defaultSuccessUrl("/1")
//                .successForwardUrl("/1")
                .usernameParameter("username")
                .passwordParameter("password")
                .permitAll()
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/start.html")
                .permitAll()
                .and()
                .rememberMe()
                .and()
                .exceptionHandling().accessDeniedPage("/access_denied");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .authenticationProvider(authProvider)
//                .inMemoryAuthentication()
//                .withUser("cat@gmail.com").password(passwordEncoder.encode("cat")).roles("USER")
//                .and()
//                .withUser("dog@gmail.com").password("dog").roles("USER")

        ;
    }
}
