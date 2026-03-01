package co.istad.wallet.identity.features.user;

import co.istad.wallet.identity.features.user.dto.UserRequest;
import co.istad.wallet.identity.features.user.dto.UserResponse;

public interface UserService {

    UserResponse createUser(UserRequest userRequest);
}