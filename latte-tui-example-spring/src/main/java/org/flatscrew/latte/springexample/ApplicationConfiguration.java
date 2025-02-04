package org.flatscrew.latte.springexample;

import com.github.javafaker.Faker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfiguration {

    @Bean
    public Faker faker() {
        return new Faker();
    }
}
