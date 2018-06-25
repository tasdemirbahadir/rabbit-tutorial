package com.btasdemir.rabbittutorial;

import com.btasdemir.rabbittutorial.configuration.MessageQueueConfigurationProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(MessageQueueConfigurationProperties.class)
public class RabbitTutorialApplication {

    public static void main(String[] args) {
        SpringApplication.run(RabbitTutorialApplication.class, args);
    }
}
