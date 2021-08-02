package main.controller;


import main.api.response.AddUserResponse;
import main.api.response.UserByIdResponse;
import main.api.response.UserDeleteResponse;
import main.api.response.UserResponse;
import main.api.unit.AddUserParametersUnit;
import main.service.UserService;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.*;

@ComponentScan({"main.service"})
@RestController
@RequestMapping("/api/user")
public class APIUserController {

    private final UserService userService;

    public APIUserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "")
    public UserResponse getUsers() {
        return userService.getUsers();
    }

    @GetMapping(value = "/{id}")
    public UserByIdResponse getUserById(@PathVariable("id") int id) {
        return userService.getUserById(id);
    }

    @PostMapping(value = "")
    public AddUserResponse addUser(@RequestBody AddUserParametersUnit addUserParametersUnit) {
        return userService.addUser(addUserParametersUnit);
    }

    @PutMapping(value = "/{id}")
    public AddUserResponse editUser(@RequestBody AddUserParametersUnit addUserParametersUnit,
                                    @PathVariable("id") int id) {
        return userService.editUser(addUserParametersUnit, id);
    }

    @DeleteMapping(value = "/{id}")
    public UserDeleteResponse deleteUser(@PathVariable("id") int id) {
        return userService.deleteUser(id);
    }

    @GetMapping(value = "/withoutMailChecking")
    public UserResponse getUsersWithoutMailChecking() {
        return userService.getUsersWithoutMailChecking();
    }

    @GetMapping(value = "/withExpiredAccess")
    public UserResponse getUsersWithExpiredAccess() {
        return userService.getUsersWithExpiredAccess();
    }

    @GetMapping(value = "/withoutLogin")
    public UserResponse getUsersWithoutLogin() {
        return userService.getUsersWithoutLogin();
    }

    @GetMapping(value = "/withPassFail")
    public UserResponse getTopUsersWithPassFail() {
        return userService.getTopUsersWithPassFail();
    }


}
