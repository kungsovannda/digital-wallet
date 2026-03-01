package co.istad.wallet.identity.features.user.dto;

import java.util.List;

public record UserResponse(
        String uuid,
        String username,
        String email,
        String familyName,
        String givenName,
        Boolean isEnabled,
        List<String> roles
) {

}