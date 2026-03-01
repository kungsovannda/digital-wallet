package co.istad.wallet.identity.features.user;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping(path = "/api/v1/users")
public class UserController {

    @GetMapping
    @PreAuthorize("hasAuthority('user:read:own')")
    public Map<?, ?> getUsers() {
        return Map.of("message", "User retrieve successfully");
    }
}