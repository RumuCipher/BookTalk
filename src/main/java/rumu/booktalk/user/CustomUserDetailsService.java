package rumu.booktalk.user;

import org.jspecify.annotations.NonNull;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @NonNull
    public UserDetails loadUserByUsername(@NonNull String email) throws UsernameNotFoundException {

        // Normalize the email using the same policy as registration.
        String normalizedEmail = EmailNormalizer.normalize(email);

        User user = userRepository.findByEmail(normalizedEmail)
                .orElseThrow(() ->
                        new UsernameNotFoundException("Invalid email or password")
                );

        // 2. Map user role to GrantedAuthority using the mandatory "ROLE_" prefix
        String roleName = "ROLE_" + user.getRole().name();
        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(roleName));

        // 3. Return Spring Security User details object
        // NOTE: We pass user.getUserId().toString() as the username principal so JWT sub gets the ID!
        return new org.springframework.security.core.userdetails.User(
                user.getUserId().toString(),
                user.getPassword(),
                authorities
        );
    }
}
