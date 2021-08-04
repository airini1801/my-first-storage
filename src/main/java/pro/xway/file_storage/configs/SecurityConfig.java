package pro.xway.file_storage.configs;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import pro.xway.file_storage.controllers.Urls;
import pro.xway.file_storage.services.UserService;

public class SecurityConfig extends WebSecurityConfigurerAdapter {

    public static final String USER = "USER";
    private final UserService  userService;

    public SecurityConfig(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected void configure(HttpSecurity config) throws Exception {
        config
                .authorizeRequests()
                .antMatchers( "/css/**", "/js/**").permitAll()
                .antMatchers(Urls.REGISTRATION + "/**").permitAll()
                .anyRequest().authenticated()
                .and()

                .formLogin()
                .loginPage("/login").permitAll()
                .defaultSuccessUrl("/")
                .failureForwardUrl("/")
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .permitAll();

    }

    @Autowired
    private BCryptPasswordEncoder bcrypt;


    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder builder) throws Exception {
//        builder.inMemoryAuthentication()
//                .passwordEncoder(bcrypt)
//                .withUser("user")
//                .password(bcrypt.encode("123"))
//                .roles(USER);
        builder.userDetailsService(userService)
                .passwordEncoder(bcrypt);
    }
}
