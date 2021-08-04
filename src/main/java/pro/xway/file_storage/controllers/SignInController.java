package pro.xway.file_storage.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pro.xway.file_storage.dto.RegistrationRequestDto;
import pro.xway.file_storage.dao.entities.User;
import pro.xway.file_storage.exception.*;
import pro.xway.file_storage.services.CategoryService;
import pro.xway.file_storage.services.UserService;

import java.util.Optional;

import static pro.xway.file_storage.controllers.Urls.*;

@Controller
@RequestMapping("/")
public class SignInController  {
    public static final String REGISTRATION_TEMPLATE = "registration";
    public static final String LOGIN_TEMPLATE = "login";
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final UserService userService;
    private final CategoryService categoryService;

    public SignInController(UserService userService,
                            CategoryService categoryService) {
        this.userService = userService;
        this.categoryService = categoryService;
    }

    @GetMapping(LOGIN)
    public String login() {
        return LOGIN_TEMPLATE;
    }

    @GetMapping(REGISTRATION)
    public String registration() {
        return REGISTRATION_TEMPLATE;
    }


    @GetMapping(REGISTRATION + EXCEPTION + "{exceptionName}")
    public String registrationWithException(Model model,
                                            @PathVariable String exceptionName) {
        Optional<ExceptionEnum> exceptionEnum = ExceptionEnum.search(exceptionName);
        if (exceptionEnum.isPresent()) {
            ExceptionEnum exception = exceptionEnum.get();
            model.addAttribute("error", exception.getMessage());
        }
        return REGISTRATION_TEMPLATE;
    }

    @PostMapping(REGISTRATION)
    public String registrationData(@ModelAttribute RegistrationRequestDto dto) {
        logger.info("Get request registration {}",dto);
        try {
            User user = userService.createUser(dto);
            categoryService.createDefaultCategory(user);
            return REDIRECT + LOGIN;
        } catch (ApplicationException e) {
            logger.error("Was application exception with message {}", e.getException().getMessage());
            return REDIRECT + REGISTRATION + EXCEPTION + e.getException().name();
        }
    }
}
