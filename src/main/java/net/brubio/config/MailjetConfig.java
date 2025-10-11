package net.brubio.config;

import net.brubio.model.MailjetJavaMailSender;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;

@Configuration
public class MailjetConfig {

    @Value("${MAILJET_API_KEY}")
    private String apiKey;

    @Value("${MAILJET_SECRET_KEY}")
    private String secretKey;

    @Bean
    public JavaMailSender javaMailSender() {
        return new MailjetJavaMailSender(apiKey, secretKey);
    }
}
