package co.istad.wallet.identity.features.oauth2;

import java.util.Optional;

import co.istad.wallet.identity.domain.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, String> {
    Optional<Client> findByClientId(String clientId);
}
