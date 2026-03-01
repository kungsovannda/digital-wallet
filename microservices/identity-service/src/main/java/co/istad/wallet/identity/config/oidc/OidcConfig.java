package co.istad.wallet.identity.config.oidc;

import co.istad.wallet.identity.security.CustomUserDetails;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.server.authorization.oidc.authentication.OidcUserInfoAuthenticationContext;
import org.springframework.security.oauth2.server.authorization.oidc.authentication.OidcUserInfoAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Configuration
public class OidcConfig {

    @Bean
    public Function<OidcUserInfoAuthenticationContext, OidcUserInfo> userInfoMapper() {
        return context -> {
            OidcUserInfoAuthenticationToken authentication = context.getAuthentication();
            JwtAuthenticationToken principal = (JwtAuthenticationToken) authentication.getPrincipal();

            CustomUserDetails userDetails = null;
            if (context.getAuthorization() != null
                    && context.getAuthorization().getPrincipalName() != null) {

                Object principalObj = context.getAuthorization()
                        .getAttribute(java.security.Principal.class.getName());

                if (principalObj instanceof org.springframework.security.authentication.UsernamePasswordAuthenticationToken token) {
                    if (token.getPrincipal() instanceof CustomUserDetails) {
                        userDetails = (CustomUserDetails) token.getPrincipal();
                    }
                }
            }

            Map<String, Object> claims = new HashMap<>();
            claims.put("sub", principal.getName());

            if (userDetails != null) {
                claims.put("email", userDetails.getEmail());
                claims.put("family_name", userDetails.getFamilyName());
                claims.put("given_name", userDetails.getGivenName());
                claims.put("phone_number", userDetails.getPhoneNumber());
                claims.put("gender", userDetails.getGender());
                claims.put("birthdate", userDetails.getDob());
                claims.put("picture", userDetails.getProfileImage());
                claims.put("cover_image", userDetails.getCoverImage());
                claims.put("roles", userDetails.getRoles());
                claims.put("permissions", userDetails.getPermissions());
            }

            return new OidcUserInfo(claims);
        };
    }
}