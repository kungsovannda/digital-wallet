package co.istad.wallet.query.repository;

import co.istad.wallet.query.view.WalletView;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface WalletViewRepository extends MongoRepository<WalletView, String> {
}
