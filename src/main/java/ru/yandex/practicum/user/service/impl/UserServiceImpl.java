package ru.yandex.practicum.user.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.exception.EntityExistException;
import ru.yandex.practicum.user.dto.UserDto;
import ru.yandex.practicum.user.entity.Friend;
import ru.yandex.practicum.user.mapepr.UserMapper;
import ru.yandex.practicum.user.repository.FriendRepository;
import ru.yandex.practicum.user.repository.UserRepository;
import ru.yandex.practicum.user.service.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    private final FriendRepository friendRepository;

    private final UserMapper mapper;

    @Override
    public UserDto save(UserDto user) {

        return mapper.toDto(repository.save(mapper.toEntity(user)));
    }

    @Override
    public UserDto update(Long id, UserDto user) {

        var fromDb = repository.findById(id).orElseThrow(
                () -> {
                    throw new EntityExistException("User with id = " + id + "not found.");
                });

        if (user.getEmail() != null) {

            fromDb.setEmail(user.getEmail());
        }

        if (user.getLogin() != null) {

            fromDb.setLogin(user.getLogin());
        }

        if (user.getName() != null) {

            fromDb.setName(user.getName());
        }

        if (user.getBirthday() != null) {

            fromDb.setBirthday(user.getBirthday());
        }

        repository.save(fromDb);

        return mapper.toDto(fromDb);
    }

    @Override
    public UserDto getById(Long id) {

        var user = repository.findById(id).orElseThrow(
                () -> {
                    throw new EntityExistException("User with id = " + id + "not found.");
                });

        var response = mapper.toDto(user);

        response.setFriends(friendRepository.getAllByUserId(id));

        return response;
    }

    @Override
    public void delete(Long id) {

        repository.findById(id).orElseThrow(
                () -> {
                    throw new EntityExistException("User with id = " + id + "not found.");
                });

        repository.deleteById(id);
    }

    @Override
    public UserDto addFriend(Long userId, Long friendId) {

        var user = repository.findById(userId).orElseThrow(
                () -> {
                    throw new EntityExistException("User with id = " + userId + "not found.");
                });

        repository.findById(friendId).orElseThrow(
                () -> {
                    throw new EntityExistException("User with id = " + friendId + "not found.");
                });

        var newfriend = friendRepository.save(new Friend(null, friendId, user));

        var response =  mapper.toDto(repository.save(user));

        response.addFriend(newfriend);

        return response;
    }

    @Override
    public void removeFriend(Long userId, Long friendId) {

        repository.findById(userId).orElseThrow(
                () -> {
                    throw new EntityExistException("User with id = " + userId + "not found.");
                });

        repository.findById(friendId).orElseThrow(
                () -> {
                    throw new EntityExistException("User with id = " + friendId + "not found.");
                });

        friendRepository.deleteByUserIdAndFriendId(userId, friendId);
    }

    @Override
    public List<UserDto> getFriends(Long userId) {

        var friendIds = friendRepository.getAllByUserId(userId).stream().map(s -> s.getUser().getId()).toList();

        return mapper.toDtoList(repository.findAllById(friendIds));
    }
//
//    public List<User> getMutualFriends(int userId, int otherId) {
//
//        isIdValid(userId, otherId, "likes");
//
//        return repository
//                .getEntity(userId)
//                .getFriends()
//                .stream()
//                .filter(id -> repository.getEntity(otherId).getFriends().contains(id))
//                .map(this::getData)
//                .collect(Collectors.toList());
//    }

}
