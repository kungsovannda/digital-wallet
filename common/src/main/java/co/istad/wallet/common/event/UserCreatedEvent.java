package co.istad.wallet.common.event;

import co.istad.wallet.common.vo.UserId;

public record UserCreatedEvent(
        UserId userId,
        String username,
        String familyName,
        String givenName,
        String gender,
        String email
) {
}
