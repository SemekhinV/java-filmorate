package ru.yandex.practicum.user.mapepr;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.user.dto.UserDto;
import ru.yandex.practicum.user.entity.User;

import java.util.List;

@Component
@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toDto(User user);

    User toEntity(UserDto dto);

    List<UserDto> toDtoList(List<User> users);
}
