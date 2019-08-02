package io.reflectoring;

import java.io.IOException;
import java.util.Collections;
import java.util.UUID;

import au.com.dius.pact.provider.PactVerifyProvider;
import au.com.dius.pact.provider.junit.PactRunner;
import au.com.dius.pact.provider.junit.Provider;
import au.com.dius.pact.provider.junit.loader.PactBroker;
import au.com.dius.pact.provider.junit.loader.PactFolder;
import au.com.dius.pact.provider.junit.target.Target;
import au.com.dius.pact.provider.junit.target.TestTarget;
import au.com.dius.pact.provider.spring.SpringRestPactRunner;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import static org.mockito.Mockito.*;

@RunWith(SpringRestPactRunner.class)
@Provider("user-service")
@SpringBootTest(classes=MessageProviderConfiguration.class)
//@PactFolder("../pact-message-consumer/target/pacts")
@PactBroker(host = "pact_broker", tags = "${pactbroker.tags:master}")
public class UserCreatedMessageProviderTest {

	@TestTarget
	public final Target target = new CustomAmqpTarget(Collections.singletonList("io.reflectoring"));

	private MessagePublisher publisher = Mockito.mock(MessagePublisher.class);

	private MessageProducer messageProducer = new MessageProducer(new ObjectMapper(), publisher);

	@PactVerifyProvider("a user created message")
	public String verifyUserCreatedMessage() throws IOException {
		// given
		doNothing().when(publisher).publishMessage(any(String.class), eq("user.created"));

		// when
		UserCreatedMessage message = UserCreatedMessage.builder()
						.messageUuid(UUID.randomUUID().toString())
						.user(User.builder()
										.id(42L)
										.name("Zaphod Beeblebrox")
										.build())
						.build();
		messageProducer.produceUserCreatedMessage(message);

		// then
		ArgumentCaptor<String> messageCapture = ArgumentCaptor.forClass(String.class);
		verify(publisher, times(1)).publishMessage(messageCapture.capture(), eq("user.created"));

		// returning the message
		return messageCapture.getValue();
	}
}
