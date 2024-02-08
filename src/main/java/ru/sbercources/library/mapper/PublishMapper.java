package ru.sbercources.library.mapper;

import javax.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.sbercources.library.dto.PublishDto;
import ru.sbercources.library.model.Publish;
import ru.sbercources.library.repository.BookRepository;
import ru.sbercources.library.repository.UserRepository;

@Component
public class PublishMapper extends GenericMapper<Publish, PublishDto> {

  private final BookRepository bookRepository;
  private final UserRepository userRepository;

  protected PublishMapper(ModelMapper mapper, BookRepository bookRepository, UserRepository userRepository) {
    super(mapper, Publish.class, PublishDto.class);
    this.bookRepository = bookRepository;
    this.userRepository = userRepository;
  }

  @PostConstruct
  public void setupMapper() {
    super.mapper.createTypeMap(Publish.class, PublishDto.class)
        .addMappings(m -> m.skip(PublishDto::setUserId)).setPostConverter(toDtoConverter())
        .addMappings(m -> m.skip(PublishDto::setBookId)).setPostConverter(toDtoConverter());
//    super.mapper.createTypeMap(PublishDto.class, Publish.class)
//        .addMappings(m -> m.skip(Publish::setUser)).setPostConverter(toEntityConverter())
//        .addMappings(m -> m.skip(Publish::setBook)).setPostConverter(toEntityConverter());
  }

  @Override
  void mapSpecificFields(PublishDto source, Publish destination) {
    destination.setBook(bookRepository.findById(source.getBookId()).orElseThrow());
    destination.setUser(userRepository.findById(source.getUserId()).orElseThrow());
  }

  @Override
  void mapSpecificFields(Publish source, PublishDto destination) {
    destination.setUserId(source.getUser().getId());
    destination.setBookId(source.getBook().getId());
  }

}
