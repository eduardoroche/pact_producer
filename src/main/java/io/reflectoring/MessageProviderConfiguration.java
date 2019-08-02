package io.reflectoring;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
class MessageProviderConfiguration {

	@Bean
	TopicExchange topicExchange() {
		return new TopicExchange("myExchange");
	}


	/*@Bean
	public ConnectionFactory connectionFactory() {
		CachingConnectionFactory connectionFactory = new CachingConnectionFactory("localhost");
		connectionFactory.setUsername("guest");
		connectionFactory.setPassword("guest");
		return connectionFactory;
	}

	@Bean
	public RabbitTemplate rabbitTemplate() {
		RabbitTemplate template = new RabbitTemplate(connectionFactory());
		return template;
	}*/

	@Bean
	MessageProducer messageProvider(MessagePublisher publisher) {
		return new MessageProducer(publisher);
	}

	@Bean
	MessagePublisher messagePublisher(RabbitTemplate rabbitTemplate, TopicExchange topicExchange) {
		return new MessagePublisher(rabbitTemplate, topicExchange);
	}

	@Bean
	SendMessageJob job(MessageProducer messageProducer) {
		return new SendMessageJob(messageProducer);
	}


}
