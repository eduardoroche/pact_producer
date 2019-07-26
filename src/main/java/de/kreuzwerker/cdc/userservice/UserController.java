package de.kreuzwerker.cdc.userservice;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users/ab/{userId}")
    public User getUser(@PathVariable String userId) {
        return userService.findUser(userId);
    }

    @GetMapping("/users/name/{name}")
    public User getUserByName(@PathVariable String name) {
        return userService.findUser(name);
    }

}
