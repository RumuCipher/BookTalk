package rumu.booktalk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import rumu.booktalk.config.JwtProperties;

@SpringBootApplication
@EnableConfigurationProperties(JwtProperties.class)
public class BookTalkApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookTalkApplication.class, args);
    }

}
