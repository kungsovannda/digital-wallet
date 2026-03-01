package co.istad.wallet.query.interfaces.rest;

import co.istad.wallet.common.vo.WalletStatus;
import co.istad.wallet.common.vo.UserId;
import co.istad.wallet.common.vo.WalletId;
import co.istad.wallet.query.interfaces.dto.WalletResponseDto;
import co.istad.wallet.query.query.GetWalletsByStatusQuery;
import co.istad.wallet.query.query.GetUserWalletsQuery;
import co.istad.wallet.query.query.GetWalletQuery;
import lombok.RequiredArgsConstructor;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/wallets")
@RequiredArgsConstructor
public class WalletController {

    private final QueryGateway queryGateway;

    @GetMapping("/{walletId}")
    public WalletResponseDto getWallet(@PathVariable UUID walletId) {
        GetWalletQuery getWalletQuery = new GetWalletQuery(new WalletId(walletId));
        return queryGateway.query(getWalletQuery, ResponseTypes.instanceOf(WalletResponseDto.class)).join();

    }

    @GetMapping("/my-wallets")
    public List<WalletResponseDto> getUserWallets(@AuthenticationPrincipal Jwt jwt) {
        GetUserWalletsQuery getUserWalletsQuery = new GetUserWalletsQuery(
                extractUserId(jwt));
        return queryGateway.query(getUserWalletsQuery, ResponseTypes.multipleInstancesOf(WalletResponseDto.class))
                .join();

    }

    @GetMapping("/status")
    public List<WalletResponseDto> getWalletsByStatus(@RequestParam WalletStatus status) {
        GetWalletsByStatusQuery getWalletsByStatusQuery = new GetWalletsByStatusQuery(status);
        return queryGateway.query(getWalletsByStatusQuery, ResponseTypes.multipleInstancesOf(WalletResponseDto.class))
                .join();
    }

    private UserId extractUserId(Jwt jwt) {
        return new UserId(
                UUID.fromString(jwt.getClaims().get("uuid").toString()));
    }

}
