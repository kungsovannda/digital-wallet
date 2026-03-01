package co.istad.wallet.query.query;

import co.istad.wallet.common.vo.UserId;

public record GetUserWalletsQuery(
        UserId ownerId
) {
}
