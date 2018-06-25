package com.btasdemir.rabbittutorial.consumer;

import com.btasdemir.rabbittutorial.model.dto.UserQueueDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class MessageQueueConsumer {

    private final static Logger logger = LoggerFactory.getLogger(MessageQueueConsumer.class);

    @RabbitListener(queues = "${mq.info.queue-name}")
    public void consume(final UserQueueDto userQueueDto) {
        logger.info("Consumed user data: {}", userQueueDto);
    }

}
