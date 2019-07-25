package de.kreuzwerker.cdc.userservice;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    public User findUser(String userId) {
        User user = new User();
        user.setId(userId);
        user.setLegacyId(UUID.randomUUID().toString());
        user.setName("Beth");
        user.setRole(UserRole.ADMIN);
        user.setLastLogin(new Date());
        List<Friend> friends = new ArrayList<>();
        friends.add(new Friend("2", "Ronald Smith"));
        friends.add(new Friend("3", "Matt Spencer"));
        user.setFriends(friends);
        return user;
    }
}
