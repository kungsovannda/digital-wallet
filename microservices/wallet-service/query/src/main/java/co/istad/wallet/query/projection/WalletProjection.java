package co.istad.wallet.query.projection;

import co.istad.wallet.common.event.*;
import co.istad.wallet.common.vo.WalletStatus;
import co.istad.wallet.query.interfaces.dto.WalletResponseDto;
import co.istad.wallet.query.query.GetWalletQuery;
import co.istad.wallet.query.repository.WalletViewRepository;
import co.istad.wallet.query.view.WalletView;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@ProcessingGroup("wallet-projection")
@RequiredArgsConstructor
public class WalletProjection {

    private final WalletViewRepository walletViewRepository;

    @QueryHandler
    public WalletResponseDto handle(GetWalletQuery query){
        WalletView walletView = walletViewRepository.findById(query.walletId().toString()).orElseThrow();
        return new WalletResponseDto(
                walletView.getWalletId(),
                walletView.getOwnerId(),
                walletView.getBalance(),
                walletView.getType(),
                walletView.getStatus(),
                walletView.getWithdrawnToday(),
                walletView.getDailyWithdrawLimit(),
                walletView.getLastWithdrawalDate(),
                walletView.getCreatedAt(),
                walletView.getUpdatedAt()
        );
    }

    @EventHandler
    public void on(WalletCreatedEvent event){
        WalletView walletView = new WalletView(
                event.walletId().id().toString(),
                event.ownerId().id().toString(),
                event.balance().balance(),
                event.balance().currency().toString(),
                event.type(),
                event.status(),
                null,
                event.dailyWithdrawalLimit().balance(),
                null,
                event.createdAt(),
                null
        );

        walletViewRepository.save(walletView);
    }

    @EventHandler
    public void on(WalletFrozenEvent event){
        WalletView walletView = walletViewRepository.findById(event.walletId().toString()).orElse(null);
        if(walletView != null){
            walletView.setStatus(WalletStatus.FROZEN);
            walletViewRepository.save(walletView);
        }
    }

    @EventHandler
    public void on(MoneyDepositedEvent event){
        WalletView walletView = walletViewRepository.findById(event.walletId().toString()).orElse(null);
        if(walletView != null){
            walletView.setBalance(event.balance().balance());
            walletViewRepository.save(walletView);
        }
    }

    @EventHandler
    public void on(MoneyWithdrawnEvent event){
        WalletView walletView = walletViewRepository.findById(event.walletId().toString()).orElse(null);
        if(walletView != null){
            walletView.setBalance(event.balance().balance());
            walletView.setWithdrawnToday(event.withdrawnToday().balance());
            walletView.setLastWithdrawalDate(event.occurredDate());
            walletViewRepository.save(walletView);
        }
    }

    @EventHandler
    public void on(MoneyDebitedEvent event){
        WalletView walletView = walletViewRepository.findById(event.walletId().toString()).orElse(null);
        if(walletView != null){
            walletView.setBalance(event.balance().balance());
            walletView.setWithdrawnToday(event.withdrawnToday().balance());
            walletView.setLastWithdrawalDate(event.occurredDate());
            walletViewRepository.save(walletView);
        }
    }

    @EventHandler
    public void on(MoneyCreditedEvent event){
        WalletView walletView = walletViewRepository.findById(event.walletId().toString()).orElse(null);
        if(walletView != null){
            walletView.setBalance(event.balance());
            walletViewRepository.save(walletView);
        }
    }


}
