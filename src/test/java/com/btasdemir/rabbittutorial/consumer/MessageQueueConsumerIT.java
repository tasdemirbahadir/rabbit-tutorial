package com.btasdemir.rabbittutorial.consumer;

import com.btasdemir.rabbittutorial.model.dto.UserQueueDto;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.rule.OutputCapture;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.GenericContainer;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsEqual.equalTo;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(initializers = MessageQueueConsumerIT.Initializer.class)
public class MessageQueueConsumerIT {

    @TestConfiguration
    public static class CustomizeConfig {
        @Bean
        public RabbitTemplate rabbitTemplate(final ConnectionFactory connectionFactory,
                                             Jackson2JsonMessageConverter producerJackson2MessageConverter) {
            final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
            rabbitTemplate.setMessageConverter(producerJackson2MessageConverter);
            return rabbitTemplate;
        }

        @Bean
        public Jackson2JsonMessageConverter producerJackson2MessageConverter() {
            return new Jackson2JsonMessageConverter();
        }
    }

    @ClassRule
    public static GenericContainer rabbit = new GenericContainer("rabbitmq:3-management")
            .withExposedPorts(5672, 15672);

    @Value("${mq.user.update.queue-name}")
    private String queueName;

    @Rule
    public OutputCapture outputCapture = new OutputCapture();

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void it_should_handle_sent_message_successfully() {
        //Given
        UserQueueDto message = new UserQueueDto();
        message.setUserId(1L);

        //When
        rabbitTemplate.convertAndSend(queueName, message);

        //Then
        await().atMost(5, TimeUnit.SECONDS).until(isUserConsumedAsync(), is(equalTo(true)));
    }

    private Callable<Boolean> isUserConsumedAsync() {
        return () -> outputCapture.toString().contains("Consumed user");
    }

    public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        @Override
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues values = TestPropertyValues.of(
                    "spring.rabbitmq.host=" + rabbit.getContainerIpAddress(),
                    "spring.rabbitmq.port=" + rabbit.getMappedPort(5672),
                    "spring.rabbitmq.user=" + "guest",
                    "spring.rabbitmq.password=" + "guest",
                    "spring.rabbitmq.virtual-host=" + "/"
            );
            values.applyTo(configurableApplicationContext);
        }

    }
}