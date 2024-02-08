package ru.sbercources.library.mapper;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.sbercources.library.dto.BookWithAuthorsDto;
import ru.sbercources.library.model.Book;
import ru.sbercources.library.model.GenericModel;
import ru.sbercources.library.repository.AuthorRepository;

@Component
public class BookWithAuthorsMapper extends GenericMapper<Book, BookWithAuthorsDto>{

  private final ModelMapper mapper;
  private final AuthorRepository authorRepository;

  protected BookWithAuthorsMapper(ModelMapper mapper, AuthorRepository authorRepository) {
    super(mapper, Book.class, BookWithAuthorsDto.class);
    this.mapper = mapper;
    this.authorRepository = authorRepository;
  }

  @PostConstruct
  public void setupMapper() {
    mapper.createTypeMap(Book.class, BookWithAuthorsDto.class)
        .addMappings(m -> m.skip(BookWithAuthorsDto::setAuthorsIds)).setPostConverter(toDtoConverter());
    mapper.createTypeMap(BookWithAuthorsDto.class, Book.class)
        .addMappings(m -> m.skip(Book::setAuthors)).setPostConverter(toEntityConverter());
  }

  @Override
  void mapSpecificFields(BookWithAuthorsDto source, Book destination) {
    destination.setAuthors(authorRepository.findAllByIdIn(source.getAuthorsIds()));
  }

  @Override
  void mapSpecificFields(Book source, BookWithAuthorsDto destination) {
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
