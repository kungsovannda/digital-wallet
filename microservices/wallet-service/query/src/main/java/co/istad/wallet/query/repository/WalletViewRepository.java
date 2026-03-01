package co.istad.wallet.query.repository;

import co.istad.wallet.common.vo.WalletStatus;
import co.istad.wallet.query.view.WalletView;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface WalletViewRepository extends MongoRepository<WalletView, String> {
    List<WalletView> findAllByOwnerId(String ownerId);

    List<WalletView> findAllByStatus(WalletStatus status);
}
