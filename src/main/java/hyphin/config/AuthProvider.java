package hyphin.config;

import hyphin.model.user.MyUserPrincipalByLogin;
import hyphin.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AuthProvider implements AuthenticationProvider {

    private final UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(8);
    }


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String userName = authentication.getName();
        String password = (String) authentication.getCredentials();
        hyphin.model.user.User principal = userRepository.getByEmail(userName);
        UserDetails userPrincipal = new MyUserPrincipalByLogin(principal);

        if (Objects.nonNull(principal) && userPrincipal.getUsername().equals(userName)) {

            if (!passwordEncoder.matches(password, userPrincipal.getPassword())) {
                throw new BadCredentialsException("Wrong password");
            }
            Collection<? extends GrantedAuthority> authorities = userPrincipal.getAuthorities();
            return new UsernamePasswordAuthenticationToken(userPrincipal, password, authorities);
        } else {
            throw new BadCredentialsException("Username not found");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
}
