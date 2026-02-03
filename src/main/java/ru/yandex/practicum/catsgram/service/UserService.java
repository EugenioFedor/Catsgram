package ru.yandex.practicum.catsgram.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.catsgram.exception.ConditionsNotMetException;
import ru.yandex.practicum.catsgram.exception.DuplicatedDataException;
import ru.yandex.practicum.catsgram.exception.NotFoundException;
import ru.yandex.practicum.catsgram.model.User;

import java.time.Instant;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {

    private final Map<Long, User> users = new HashMap<>();

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    public Collection<User> findAll() {
        return users.values();
    }

    public User getUserById(Long userId) {
        if (users.containsKey(userId)) {
            return users.get(userId);
        } else {
            throw new NotFoundException("Пользователь с id = " + userId + " не найден");
        }
    }

    public User create(User user) {
        // проверяем выполнение необходимых условий
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new ConditionsNotMetException("Имейл должен быть указан");
        }
        boolean emailExists = users.values()
                .stream()
                .anyMatch(u -> u.getEmail().equals(user.getEmail()));
        if (emailExists) {
            throw new DuplicatedDataException("Этот имейл уже используется");
        }

        // формируем дополнительные данные
        user.setId(getNextId());
        user.setRegistrationDate(Instant.now());
        // сохраняем новую публикацию в памяти приложения
        users.put(user.getId(), user);
        return user;
    }

    public User put(User user) {
        if (user.getId() == null) {
            throw new ConditionsNotMetException("Id должен быть указан");
        }
        if (!users.containsKey(user.getId())) {
            throw new ConditionsNotMetException("Пользователь с таким id не найден");
        }
        User existingUser = users.get(user.getId());
        // Обновление email, если передан
        if (user.getEmail() != null) {
            boolean emailUsed = users.values()
                    .stream()
                    .anyMatch(u -> u.getEmail().equals(user.getEmail()) &&
                            !u.getId().equals(existingUser.getId()));
            if (emailUsed) {
                throw new DuplicatedDataException("Этот имейл уже используется");
            }
            existingUser.setEmail(user.getEmail());
        }

        // Обновление имени, если передано
        if (user.getUsername() != null) {
            existingUser.setUsername(user.getUsername());
        }

        // Обновление пароля, если передан
        if (user.getPassword() != null) {
            existingUser.setPassword(user.getPassword());
        }

        return existingUser;
    }
}
