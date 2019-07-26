package de.kreuzwerker.cdc.userservice;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import au.com.dius.pact.provider.junit.IgnoreNoPactsToVerify;
import au.com.dius.pact.provider.junit.PactRunner;
import au.com.dius.pact.provider.junit.Provider;
import au.com.dius.pact.provider.junit.State;
import au.com.dius.pact.provider.junit.loader.PactBroker;
import au.com.dius.pact.provider.junit.loader.PactFolder;
import au.com.dius.pact.provider.junit.loader.PactUrl;
import au.com.dius.pact.provider.junit.target.HttpTarget;
import au.com.dius.pact.provider.junit.target.Target;
import au.com.dius.pact.provider.junit.target.TestTarget;
import au.com.dius.pact.provider.spring.SpringRestPactRunner;
import au.com.dius.pact.provider.spring.target.SpringBootHttpTarget;

import java.util.*;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@RunWith(SpringRestPactRunner.class)
@Provider("user-service")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@IgnoreNoPactsToVerify
//pact_broker is the service name in docker-compose
//, tags = "${pactbroker.tags:prod}"
@PactBroker(host = "pact_broker", tags = "${pactbroker.tags:master}")
@Ignore
public class GenericStateWithParameterContractTest {

    @TestTarget
    public final Target target = new SpringBootHttpTarget();

    @MockBean
    private UserService userService;

    @State("default")
    public void toDefaultState(Map<String, Object> params) {
        final boolean userExists = (boolean) params.get("userExists");
        if (userExists) {
            System.out.println("blablabla");
            User user = new User();
            user.setId("1");
            user.setLegacyId(UUID.randomUUID().toString());
            user.setName("Beth");
            user.setRole(UserRole.ADMIN);
            user.setLastLogin(new Date());
            List<Friend> friends = new ArrayList<>();
            friends.add(new Friend("2", "Ronald Smith"));
            friends.add(new Friend("3", "Matt Spencer"));
            user.setFriends(friends);
            when(userService.findUser(any())).thenReturn(user);
        } else {
            System.out.println("hehehe");
            when(userService.findUser(any())).thenThrow(NotFoundException.class);
        }
    }


}

