package pro.xway.file_storage.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pro.xway.file_storage.dto.RegistrationRequestDto;
import pro.xway.file_storage.configs.SecurityConfig;
import pro.xway.file_storage.dao.entities.Role;
import pro.xway.file_storage.dao.entities.User;
import pro.xway.file_storage.dao.repositories.UserRepository;
import pro.xway.file_storage.exception.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private BCryptPasswordEncoder bCrypt;
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {

        this.userRepository = userRepository;
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findByUsername(userName);
        if (userOptional.isEmpty()) {
         throw new UsernameNotFoundException("User not found");
        }
        return userOptional.get();
    }

    public User createUser(RegistrationRequestDto dto) throws ApplicationException {
        if (!dto.getPassword().equals(dto.getPasswordTwo())) {
            throw new ApplicationException(ExceptionEnum.PASSWORD_NOT_EQUAL);
    }
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new ApplicationException(ExceptionEnum.USER_IS_EXIST);
        }
        if (userRepository.existsByEmail(dto.getEmail())){
            throw  new ApplicationException(ExceptionEnum.EMAIL_IS_EXIST);
        }

        Role role = new Role();
        role.setRole(SecurityConfig.USER);

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(bCrypt.encode(dto.getPassword()));
        user.setEmail(dto.getEmail());
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        user.setEnabled(true);
        user.setCreateAccount(LocalDate.now());
        user.setRoles(new HashSet<>());
        user.getRoles().add(role);

        User saved = save(user);
        return saved;
    }

    /**
     * Получение текущего клиента
     * Authentication - хранится имя пользователя который воспользовался токеном
     * По имени находим пользователя и отдаем
     *
     * @return пользователь который авторизован
     */
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (Objects.isNull(authentication) || authentication.getPrincipal().equals("anonymousUser")) {
            return null;
        } else {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            Optional<User> users = userRepository.findByUsername(userDetails.getUsername());
            return users.orElseThrow(() ->
                    new UsernameNotFoundException("User not authorized."));
        }
    }

//    public User getCurrentUserOrElseThrow() throws ApplicationException {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (Objects.isNull(authentication)) throw new ApplicationException(ExceptionEnum.CURRENT_USER_NOT_FOUND);
//        return getCurrentUser(authentication);
//    }

}
