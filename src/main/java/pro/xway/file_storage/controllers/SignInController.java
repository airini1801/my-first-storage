package pro.xway.file_storage.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pro.xway.file_storage.dto.RegistrationRequestDto;
import pro.xway.file_storage.dao.entities.User;
import pro.xway.file_storage.exception.*;
import pro.xway.file_storage.services.CategoryService;
import pro.xway.file_storage.services.UserService;

import java.util.Optional;

@Controller
@RequestMapping("/")
public class SignInController implements UrlConstants {

    private final UserService userService;
    private final CategoryService categoryService;

    public SignInController(UserService userService,
                            CategoryService categoryService) {
        this.userService = userService;
        this.categoryService = categoryService;
    }

    @GetMapping(LOGIN)
    public String login() {
        return "login";
    }

    @GetMapping(REGISTRATION)
    public String registration() {
        return "registration";
    }


    @GetMapping(REGISTRATION + EXCEPTION + "{exceptionName}")
    public String registrationWithException(Model model,
                                            @PathVariable String exceptionName) {
        Optional<ExceptionEnum> exceptionEnum = ExceptionEnum.search(exceptionName);
        if (exceptionEnum.isPresent()) {
            ExceptionEnum exception = exceptionEnum.get();
            model.addAttribute("error", exception.getMessage());
        }
        return "registration";
    }

    @PostMapping(REGISTRATION)
    public String registrationData(@ModelAttribute RegistrationRequestDto dto) {
        try {
            User user = userService.createUser(dto);
            categoryService.createDefaultCategory(user);
            return REDIRECT + LOGIN;
        } catch (ApplicationException e) {
            return REDIRECT + REGISTRATION + EXCEPTION + e.getException().name();
        }
    }
}
