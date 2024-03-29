package io.reflectoring;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Takes a {@link UserCreatedMessage}, converts it to a {@link String} and sends it to be published.
 */
class MessageProducer {

	private Logger logger = LoggerFactory.getLogger(MessageProducer.class);

	private MessagePublisher messagePublisher;

	MessageProducer(MessagePublisher messagePublisher) {
		this.messagePublisher = messagePublisher;
	}

	void produceUserCreatedMessage(UserCreatedMessage message) throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		String stringMessage = objectMapper.writeValueAsString(message);
		messagePublisher.publishMessage(stringMessage, "user.created");
		logger.info("Published message '{}'", stringMessage);
	}

}
