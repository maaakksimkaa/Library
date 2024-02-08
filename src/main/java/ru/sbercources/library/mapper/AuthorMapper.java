package ru.sbercources.library.mapper;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.sbercources.library.dto.AuthorDto;
import ru.sbercources.library.model.Author;
import ru.sbercources.library.model.GenericModel;
import ru.sbercources.library.repository.BookRepository;

@Component
public class AuthorMapper extends GenericMapper<Author, AuthorDto> {

  private final ModelMapper mapper;
  private final BookRepository bookRepository;

  protected AuthorMapper(ModelMapper mapper, BookRepository bookRepository) {
    super(mapper, Author.class, AuthorDto.class);
    this.mapper = mapper;
    this.bookRepository = bookRepository;
  }

  @PostConstruct
  public void setupMapper() {
    mapper.createTypeMap(Author.class, AuthorDto.class)
        .addMappings(m -> m.skip(AuthorDto::setBooksIds)).setPostConverter(toDtoConverter());
    mapper.createTypeMap(AuthorDto.class, Author.class)
        .addMappings(m -> m.skip(Author::setBooks)).setPostConverter(toEntityConverter());
  }

  @Override
  void mapSpecificFields(AuthorDto source, Author destination) {
    if (!Objects.isNull(source.getBooksIds())) {
      destination.setBooks(bookRepository.findAllByIdIn(source.getBooksIds()));
    } else {
      destination.setBooks(null);
    }
  }

  @Override
  void mapSpecificFields(Author source, AuthorDto destination) {
    destination.setBooksIds(getIds(source));
  }

  private Set<Long> getIds(Author author) {
    return Objects.isNull(author) || Objects.isNull(author.getId())
        ? null
        : author.getBooks().stream()
            .map(GenericModel::getId)
            .collect(Collectors.toSet());
  }
}
