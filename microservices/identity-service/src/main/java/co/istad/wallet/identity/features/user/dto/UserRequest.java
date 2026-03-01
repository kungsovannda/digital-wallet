package co.istad.wallet.identity.features.user.dto;

import java.util.List;

public record UserRequest(
        String username,
        String email,
        String password,
        String givenName,
        String familyName,
        List<String> roles
) {
}