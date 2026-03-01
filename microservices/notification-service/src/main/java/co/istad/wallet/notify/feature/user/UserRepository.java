package co.istad.wallet.notify.feature.user;

import co.istad.wallet.notify.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
}
