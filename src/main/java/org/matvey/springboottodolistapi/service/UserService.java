package org.matvey.springboottodolistapi.service;

import lombok.RequiredArgsConstructor;
import org.matvey.springboottodolistapi.exception.user.UserAlreadyExistsException;
import org.matvey.springboottodolistapi.exception.user.UserNotFoundException;
import org.matvey.springboottodolistapi.model.User;
import org.matvey.springboottodolistapi.repo.UserRepo;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    public User findUserByIdOrThrow(Long id) {
        return userRepo.findById(id).orElseThrow(
                () -> new UserNotFoundException("User with id " + id + " not found")
        );
    }

    public User findUserByUsernameOrThrow(String username) {
        return userRepo.findByUsername(username).orElseThrow(
                () -> new UserNotFoundException("User with username " + username + " not found")
        );
    }

    public void isUserExistsByUsername(String username) {
        if (userRepo.findByUsername(username).isPresent()) {
            throw new UserAlreadyExistsException("User with username " + username + " already exists");
        }
    }

    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    public User getUserById(Long id) {
        return findUserByIdOrThrow(id);
    }

    public User findUserByUsername(String username) {
        return findUserByUsernameOrThrow(username);
    }

    public User createUser(User user) {
        isUserExistsByUsername(user.getUsername());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepo.save(user);
    }

    public User updateUser(User user) {
        User oldUser = findUserByIdOrThrow(user.getId());

        if (!user.getUsername().equals(oldUser.getUsername())) {
            isUserExistsByUsername(user.getUsername());
        }
        return userRepo.save(user);
    }

    public void deleteUser(Long id) {
        findUserByIdOrThrow(id);
        userRepo.deleteById(id);
    }
}
