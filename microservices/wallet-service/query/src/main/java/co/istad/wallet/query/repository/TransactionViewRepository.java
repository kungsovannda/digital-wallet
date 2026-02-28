package co.istad.wallet.query.repository;

import co.istad.wallet.query.view.TransactionView;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TransactionViewRepository extends MongoRepository<TransactionView, String> {
    List<TransactionView> findAllByWalletId(String walletId);
}
