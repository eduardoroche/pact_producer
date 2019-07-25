package de.kreuzwerker.cdc.userservice;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import au.com.dius.pact.provider.junit.Provider;
import au.com.dius.pact.provider.junit.State;
import au.com.dius.pact.provider.junit.loader.PactFolder;
import au.com.dius.pact.provider.junit.target.Target;
import au.com.dius.pact.provider.junit.target.TestTarget;
import au.com.dius.pact.provider.spring.SpringRestPactRunner;
import au.com.dius.pact.provider.spring.target.SpringBootHttpTarget;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@RunWith(SpringRestPactRunner.class)
@Provider("user-service")
@PactFolder("pacts")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Ignore
public class MockedUserServiceContractTest {

    @TestTarget
    public final Target target = new SpringBootHttpTarget();

    @MockBean
    private UserService userService;

    @State("User 1 exists")
    public void user1Exists() {
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
    }

    @State("User 2 does not exist")
    public void user2DoesNotExist() {
        when(userService.findUser(any())).thenThrow(NotFoundException.class);
    }

}
