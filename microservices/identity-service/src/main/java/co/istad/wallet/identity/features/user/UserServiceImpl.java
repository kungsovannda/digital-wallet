package co.istad.wallet.identity.features.user;

import co.istad.wallet.common.event.UserCreatedEvent;
import co.istad.wallet.common.vo.UserId;
import co.istad.wallet.identity.domain.Role;
import co.istad.wallet.identity.domain.User;
import co.istad.wallet.identity.features.role.RoleRepository;
import co.istad.wallet.identity.features.user.dto.UserRequest;
import co.istad.wallet.identity.features.user.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.axonframework.eventhandling.GenericDomainEventMessage;
import org.axonframework.eventhandling.gateway.EventGateway;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.Month;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final EventGateway eventGateway;

    @Override
    public UserResponse createUser(UserRequest userRequest) {
        if (userRepository.existsByUsername(userRequest.username()))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exists");
        if (userRepository.existsByEmail(userRequest.email()))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");

        Set<Role> roles = new HashSet<>();

        userRequest.roles().forEach(role -> {
            roles.add(
                    roleRepository.findByName(role).orElseThrow(
                            () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found")
                    )
            );
        });

        User user = User.builder()
                .uuid(UUID.randomUUID().toString())
                .password(passwordEncoder.encode(userRequest.password()))
                .email(userRequest.email())
                .username(userRequest.username())
                .familyName(userRequest.familyName())
                .givenName(userRequest.givenName())
                .isEnabled(true)
                .accountNonLocked(true)
                .accountNonExpired(true)
                .credentialsNonExpired(true)
                .dob(LocalDate.of(2000, Month.APRIL,10))
                .roles(roles)
                .build();

        user = userRepository.save(user);
        eventGateway.publish(
                new GenericDomainEventMessage<>(
                        "User",
                        user.getUuid(),
                        0,
                        new UserCreatedEvent(
                                new UserId(UUID.fromString(user.getUuid())),
                                user.getUsername(),
                                user.getFamilyName(),
                                user.getGivenName(),
                                user.getGender(),
                                user.getEmail()
                        )
                )
        );
        return new UserResponse(
                user.getUuid(),
                user.getUsername(),
                user.getEmail(),
                user.getFamilyName(),
                user.getGivenName(),
                user.getIsEnabled(),
                user.getRoles().stream().map(Role::getName).toList()
        );
    }
}