package co.istad.wallet.identity.features.auth;

import co.istad.wallet.identity.features.auth.dto.RegisterRequest;
import co.istad.wallet.identity.features.user.UserService;
import co.istad.wallet.identity.features.user.dto.UserRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("userForm", new RegisterRequest());
        return "register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("userForm") RegisterRequest userForm,
                           BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "register";
        }

        // Check if passwords match
        if (!userForm.getPassword().equals(userForm.getConfirmPassword())) {
            result.rejectValue("confirmPassword", "error.userForm", "Passwords do not match");
            return "register";
        }

        // Convert UserForm to UserRequest
        UserRequest userRequest = new UserRequest(
                userForm.getUsername(),
                userForm.getEmail(),
                userForm.getPassword(),
                userForm.getGivenName(),
                userForm.getFamilyName(),
                List.of("USER")
        );

        userService.createUser(userRequest);
        return "redirect:/login?registered";
    }
}
