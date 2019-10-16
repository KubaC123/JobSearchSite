package getjobin.it.portal.jobservice.infrastructure.config.kafka;

import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.MimeTypeUtils;

@Component
public class KafkaEventPublisher {

    public void sendEventOnTopic(MessageChannel topic, Object event) {
            topic.send(MessageBuilder
                    .withPayload(event)
                    .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
                    .build());

    }
}
