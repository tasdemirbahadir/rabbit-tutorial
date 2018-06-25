package com.btasdemir.rabbittutorial.consumer;

import com.btasdemir.rabbittutorial.model.dto.UserQueueDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class MessageQueueConsumer {

    private final static Logger logger = LoggerFactory.getLogger(MessageQueueConsumer.class);

    @RabbitListener(queues = "${mq.user.update.queue-name}")
    public void consume(final UserQueueDto userQueueDto) {
        logger.debug("Consumed user data: {}", userQueueDto);
    }

}
