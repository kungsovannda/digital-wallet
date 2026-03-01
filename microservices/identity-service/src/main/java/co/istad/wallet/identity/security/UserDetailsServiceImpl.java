package co.istad.wallet.identity.security;

import co.istad.wallet.identity.domain.Permission;
import co.istad.wallet.identity.domain.Role;
import co.istad.wallet.identity.domain.User;
import co.istad.wallet.identity.features.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("User not found with username: " + username)
        );

        List<GrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));
            role.getPermissions().forEach(permission -> {
                authorities.add(new SimpleGrantedAuthority(permission.getName()));
            });
        });

        log.info("USER AUTHORITIES: {}", authorities);

        return new CustomUserDetails(
                user.getUuid(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                user.getFamilyName(),
                user.getGivenName(),
                user.getPhoneNumber(),
                user.getGender(),
                user.getDob(),
                user.getProfileImage(),
                user.getCoverImage(),
                user.getAccountNonExpired(),
                user.getAccountNonLocked(),
                user.getCredentialsNonExpired(),
                user.getIsEnabled(),
                user.getRoles().stream().map(Role::getName).collect(Collectors.toSet()),
                user.getPermissions().stream().map(Permission::getName).collect(Collectors.toSet())
        );
    }
}
