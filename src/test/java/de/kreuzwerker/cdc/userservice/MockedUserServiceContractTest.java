package de.kreuzwerker.cdc.userservice;

import au.com.dius.pact.provider.junit.Provider;
import au.com.dius.pact.provider.junit.State;
import au.com.dius.pact.provider.junit.loader.PactBroker;
import au.com.dius.pact.provider.junit.loader.PactFilter;
import au.com.dius.pact.provider.junit.target.Target;
import au.com.dius.pact.provider.junit.target.TestTarget;
import au.com.dius.pact.provider.spring.SpringRestPactRunner;
import au.com.dius.pact.provider.spring.target.SpringBootHttpTarget;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRestPactRunner.class)
@Provider("user-service")
//@PactFolder("pacts")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@Ignore
@PactBroker(host = "pact_broker", tags = "${pactbroker.tags:master}")
//@PactFilter({"provider test users"})
public class MockedUserServiceContractTest {

    @TestTarget
    public final Target target = new SpringBootHttpTarget();

    @MockBean
    private UserService userService;

    @State("User 3 exists")
    public void user1Exists() {
        User user = new User();
        user.setId("3");
        user.setLegacyId(UUID.randomUUID().toString());
        user.setName("Beth");
        user.setRole(UserRole.ADMIN);
        user.setLastLogin(new Date());
        List<Friend> friends = new ArrayList<>();
        friends.add(new Friend("2", "Ronald Smith"));
        friends.add(new Friend("3", "Matt Sspencer"));
        user.setFriends(friends);
        when(userService.findUser(any())).thenReturn(user);
        //when(userService.findUser(any())).thenThrow(NotFoundException.class);
    }

    @State("User 1 exists")
    public void user1Exists1() {
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
        //when(userService.findUser(any())).thenThrow(NotFoundException.class)
    }

    @State("User 2 does not exist")
    public void user2DoesNotExist() {
        when(userService.findUser(any())).thenThrow(NotFoundException.class);
    }

    @State("Old User 1 exists")
    public void user1Exists2() {
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
        //when(userService.findUser(any())).thenReturn(user);
        when(userService.findUser(any())).thenThrow(NotFoundException.class);
    }

}

