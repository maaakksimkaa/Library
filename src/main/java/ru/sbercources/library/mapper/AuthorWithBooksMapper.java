package ru.sbercources.library.mapper;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.sbercources.library.dto.AuthorDto;
import ru.sbercources.library.dto.AuthorWithBooksDto;
import ru.sbercources.library.model.Author;
import ru.sbercources.library.model.GenericModel;
import ru.sbercources.library.repository.BookRepository;

@Component
public class AuthorWithBooksMapper extends GenericMapper<Author, AuthorWithBooksDto> {

  private final ModelMapper mapper;
  private final BookRepository bookRepository;

  protected AuthorWithBooksMapper(ModelMapper mapper, BookRepository bookRepository) {
    super(mapper, Author.class, AuthorWithBooksDto.class);
    this.mapper = mapper;
    this.bookRepository = bookRepository;
  }

  @PostConstruct
  public void setupMapper() {
    mapper.createTypeMap(Author.class, AuthorWithBooksDto.class)
        .addMappings(m -> m.skip(AuthorWithBooksDto::setBooksIds)).setPostConverter(toDtoConverter());
    mapper.createTypeMap(AuthorWithBooksDto.class, Author.class)
        .addMappings(m -> m.skip(Author::setBooks)).setPostConverter(toEntityConverter());
  }

  @Override
  void mapSpecificFields(AuthorWithBooksDto source, Author destination) {
    destination.setBooks(bookRepository.findAllByIdIn(source.getBooksIds()));
  }

  @Override
  void mapSpecificFields(Author source, AuthorWithBooksDto destination) {
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
