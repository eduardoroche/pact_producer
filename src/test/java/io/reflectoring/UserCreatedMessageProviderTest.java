package io.reflectoring;

import java.io.IOException;
import java.util.*;

import au.com.dius.pact.provider.PactVerifyProvider;
import au.com.dius.pact.provider.junit.PactRunner;
import au.com.dius.pact.provider.junit.Provider;
import au.com.dius.pact.provider.junit.State;
import au.com.dius.pact.provider.junit.loader.PactBroker;
import au.com.dius.pact.provider.junit.loader.PactFilter;
import au.com.dius.pact.provider.junit.loader.PactFolder;
import au.com.dius.pact.provider.junit.target.AmqpTarget;
import au.com.dius.pact.provider.junit.target.Target;
import au.com.dius.pact.provider.junit.target.TestTarget;
import au.com.dius.pact.provider.spring.SpringRestPactRunner;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.kreuzwerker.cdc.userservice.Friend;
import de.kreuzwerker.cdc.userservice.UserRole;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import static org.mockito.Mockito.*;

@RunWith(PactRunner.class)
@Provider("user-service-messaging")
@PactBroker(host = "pact_broker", tags = "${pactbroker.tags:master}")
public class UserCreatedMessageProviderTest {

	//TestTarget
	//public final Target target = new CustomAmqpTarget(Collections.singletonList("io.reflectoring"));

	@TestTarget
	public final Target target = new CustomAmqpTarget(Collections.singletonList("io.reflectoring"));

	private MessagePublisher publisher = Mockito.mock(MessagePublisher.class);

	private MessageProducer messageProducer = new MessageProducer(publisher);

	//@State("some state")
	//public void someProviderState() {
	//	when(any()).thenReturn(42L);
	//}

	@PactVerifyProvider("a user created message")
	public String verifyUserCreatedMessage() throws IOException {
		// given
		doNothing().when(publisher).publishMessage(any(String.class), eq("user.created"));

		// when
		UserCreatedMessage message = UserCreatedMessage.builder()
						.messageUuid(UUID.randomUUID().toString())
						.user(User.builder()
										.id("44")
										.name("Zaphod")
										.build())
						.build();
		messageProducer.produceUserCreatedMessage(message);

		// then
		ArgumentCaptor<String> messageCapture = ArgumentCaptor.forClass(String.class);
		//"{\"messageUuid\":\"689129ab-8e6e-44c4-abde-7b9fac4364c6\",\"user\":{\"id\":4238994195100213922,\"name\":\"Zaphpod\"}}"
		verify(publisher, times(1)).publishMessage(messageCapture.capture(), eq("user.created"));

		// returning the message
		System.out.println("ABC: " + messageCapture.getValue());
		return messageCapture.getValue();
	}
}
