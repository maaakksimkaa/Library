package ru.sbercources.library.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.sbercources.library.dto.UserDto;
import ru.sbercources.library.model.User;

@Component
public class UserMapper extends GenericMapper<User, UserDto>{

  protected UserMapper(ModelMapper mapper) {
    super(mapper, User.class, UserDto.class);
  }
}
