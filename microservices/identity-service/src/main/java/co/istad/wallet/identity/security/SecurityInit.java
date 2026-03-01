package co.istad.wallet.identity.security;

import co.istad.wallet.identity.domain.Role;
import co.istad.wallet.identity.domain.User;
import co.istad.wallet.identity.features.oauth2.JpaRegisteredClientRepository;
import co.istad.wallet.identity.features.role.RoleRepository;
import co.istad.wallet.identity.features.user.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Month;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class SecurityInit {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final JpaRegisteredClientRepository jpaRegisteredClientRepository;

    @PostConstruct
    public void init() {
        if(roleRepository.count() == 0){
            Role userRole = new Role();
            userRole.setName("USER");
            roleRepository.save(userRole);

            Role adminRole = new Role();
            adminRole.setName("ADMIN");
            roleRepository.save(adminRole);

            if(userRepository.count() == 0) {
                User user = new User();
                user.setUuid(UUID.randomUUID().toString());
                user.setUsername("kungsovannda");
                user.setPassword(passwordEncoder.encode("password"));
                user.setDob(LocalDate.of(2006, Month.NOVEMBER, 18));
                user.setEmail("kungsovannda@gmail.com");
                user.setCoverImage("https://i.pinimg.com/736x/00/a5/c1/00a5c1587438846dd7cdf7f75a06b925.jpg");
                user.setGender("Male");
                user.setFamilyName("Kung");
                user.setGivenName("Sovannda");
                user.setIsEnabled(true);
                user.setAccountNonExpired(true);
                user.setAccountNonLocked(true);
                user.setCredentialsNonExpired(true);
                user.setPhoneNumber("+85516797411");
                user.setProfileImage("https://i.pinimg.com/736x/07/38/5f/07385f7fb818f5edd361961d8242a6c3.jpg");
                Set<Role> roles = new HashSet<>();
                roles.add(userRole);
                roles.add(adminRole);
                user.setRoles(roles);

                userRepository.save(user);
            }
        }

    }

    @PostConstruct
    public void initClient() {
        if (jpaRegisteredClientRepository.findByClientId("dw-client") == null) {
            TokenSettings tokenSettings = TokenSettings.builder()
                    .accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED)
                    .accessTokenTimeToLive(Duration.ofDays(3))
                    .reuseRefreshTokens(false)
                    .refreshTokenTimeToLive(Duration.ofDays(5))
                    .build();

            ClientSettings clientSettings = ClientSettings.builder()
                    .requireProofKey(true)
                    .requireAuthorizationConsent(false)
                    .build();

            var client = RegisteredClient.withId(UUID.randomUUID().toString())
                    .clientId("dw-client")
                    .clientName("Digital Wallet Client")
                    .clientSecret(passwordEncoder.encode("secret"))
                    .clientSettings(clientSettings)
                    .tokenSettings(tokenSettings)
                    .authorizationGrantTypes(auth -> {
                        auth.add(AuthorizationGrantType.AUTHORIZATION_CODE);
                        auth.add(AuthorizationGrantType.REFRESH_TOKEN);
                        auth.add(AuthorizationGrantType.CLIENT_CREDENTIALS);
                    })
                    .clientAuthenticationMethods(auth -> {
                        auth.add(ClientAuthenticationMethod.CLIENT_SECRET_BASIC);
                        auth.add(ClientAuthenticationMethod.CLIENT_SECRET_POST);
                    })
                    .clientIdIssuedAt(Instant.now())
                    .postLogoutRedirectUri("http://localhost:8080")
                    .redirectUris(uri -> {
                                uri.add("http://localhost:8080/login/oauth2/code/dw-cleint");
                                uri.add("http://localhost:8080");
                    })
                    .scopes(scope -> {
                                scope.add(OidcScopes.OPENID);
                                scope.add(OidcScopes.EMAIL);
                                scope.add(OidcScopes.PROFILE);
                    })
                    .build();

            jpaRegisteredClientRepository.save(client);
        }
    }
}
