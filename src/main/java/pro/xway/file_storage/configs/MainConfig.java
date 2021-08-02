package pro.xway.file_storage.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@Import({SecurityConfig.class})
public class MainConfig {

    @Bean
    public BCryptPasswordEncoder bcrypt() {
        return new BCryptPasswordEncoder();
    }

}
