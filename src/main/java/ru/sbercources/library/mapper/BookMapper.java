package ru.sbercources.library.mapper;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.sbercources.library.dto.BookDto;
import ru.sbercources.library.model.Book;
import ru.sbercources.library.model.GenericModel;
import ru.sbercources.library.repository.AuthorRepository;

@Component
public class BookMapper extends GenericMapper<Book, BookDto> {

  private final ModelMapper mapper;
  private final AuthorRepository authorRepository;
  protected BookMapper(ModelMapper mapper, AuthorRepository authorRepository) {
    super(mapper, Book.class, BookDto.class);
    this.mapper = mapper;
    this.authorRepository = authorRepository;
  }

  @PostConstruct
  public void setupMapper() {
    mapper.createTypeMap(Book.class, BookDto.class)
        .addMappings(m -> m.skip(BookDto::setAuthorsIds)).setPostConverter(toDtoConverter());
    mapper.createTypeMap(BookDto.class, Book.class)
        .addMappings(m -> m.skip(Book::setAuthors)).setPostConverter(toEntityConverter());
  }

  @Override
  void mapSpecificFields(BookDto source, Book destination) {
    if (!Objects.isNull(source.getAuthorsIds())) {
      destination.setAuthors(authorRepository.findAllByIdIn(source.getAuthorsIds()));
    } else {
      destination.setAuthors(null);

    }
  }

  @Override
  void mapSpecificFields(Book source, BookDto destination) {
    destination.setAuthorsIds(getIds(source));
  }

  private Set<Long> getIds(Book Book) {
    return Objects.isNull(Book) || Objects.isNull(Book.getId())
        ? null
        : Book.getAuthors().stream()
            .map(GenericModel::getId)
            .collect(Collectors.toSet());
  }
}
