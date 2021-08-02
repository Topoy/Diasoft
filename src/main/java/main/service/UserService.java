package main.service;

import main.api.response.AddUserResponse;
import main.api.response.UserByIdResponse;
import main.api.response.UserDeleteResponse;
import main.api.response.UserResponse;
import main.api.unit.AddUserParametersUnit;
import main.model.User;
import main.repository.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final Logger logger = LogManager.getLogger(UserService.class);

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponse getUsers() {
        return getUserResponse(userRepository.findAll());
    }

    public UserByIdResponse getUserById(int id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Нет элемента с id: " + id));
        UserByIdResponse userByIdResponse = new UserByIdResponse();
        userByIdResponse.setId(user.getUserId());
        userByIdResponse.setName(user.getName());
        userByIdResponse.setPass(user.getPass());
        userByIdResponse.setMail(user.getMail());
        return userByIdResponse;
    }

    public AddUserResponse addUser(AddUserParametersUnit addUserParametersUnit) {
        return createUser(addUserParametersUnit, new User());
    }

    public AddUserResponse editUser(AddUserParametersUnit addUserParametersUnit, int id) {
        User user = userRepository.findById(id).orElseThrow( () -> {
            logger.info("Пользователь с id: " + id + " не найден.");
            return new NoSuchElementException("Пользователь с id: " + id + " не найден.");
        });
        return createUser(addUserParametersUnit, user);
    }

    public UserDeleteResponse deleteUser(int id) {
        UserDeleteResponse userDeleteResponse = new UserDeleteResponse();
        User user = userRepository.findById(id).orElseThrow( () -> {
            logger.info("Удаление невозможно, так как пользователь с id: " + id + " не найден.");
            return new NoSuchElementException("Пользователь с таким id не найден");
        });
        userRepository.delete(user);
        userDeleteResponse.setResult(true);
        return userDeleteResponse;
    }

    public UserResponse getUsersWithoutMailChecking() {
        return getUserResponse(userRepository.getUsersWithoutMailChecking());
    }

    public UserResponse getUsersWithExpiredAccess() {
        return getUserResponse(userRepository.getUsersWithExpiredAccess());
    }

    public UserResponse getUsersWithoutLogin() {
        return getUserResponse(userRepository.getUsersWithoutLogin());
    }

    public UserResponse getTopUsersWithPassFail() {
        return getUserResponse(userRepository.getTopUsersWithPassFail());
    }

    private UserResponse getUserResponse(List<User> specialList) {
        UserResponse userResponse = new UserResponse();
        userResponse.setCount(specialList.size());
        userResponse.setUsers(specialList);
        return userResponse;
    }

    private AddUserResponse createUser(AddUserParametersUnit addUserParametersUnit, User user) {
        AddUserResponse addUserResponse = new AddUserResponse();

        if (addUserParametersUnit.getId() < 0) {
            addUserResponse.setResult(false);
            addUserResponse.setErrors(addUserParametersUnit.getName(), addUserParametersUnit.getPass(),
                    addUserParametersUnit.getMail());
        }
        else {
            addUserResponse.setResult(true);
            user.setName(addUserParametersUnit.getName());
            user.setPass(addUserParametersUnit.getPass());
            user.setMail(addUserParametersUnit.getMail());

            userRepository.save(user);
        }
        return addUserResponse;
    }

}
