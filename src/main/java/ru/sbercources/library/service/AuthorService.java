package java.ru.sbercources.library.service;

import java.time.LocalDateTime;
import java.util.List;
import org.springdoc.api.OpenApiResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.sbercources.library.dto.AddBookDto;
import ru.sbercources.library.model.Author;
import ru.sbercources.library.model.Book;
import ru.sbercources.library.repository.AuthorRepository;
import ru.sbercources.library.repository.BookRepository;
import ru.sbercources.library.repository.PublishRepository;
import ru.sbercources.library.service.userDetails.CustomUserDetails;

@Service
public class AuthorService extends GenericService<Author> {

  private final BookRepository bookRepository;
  private final BookService bookService;
  private final AuthorRepository authorRepository;
  private final PublishRepository publishRepository;

  public AuthorService(AuthorRepository repository,
      BookRepository bookRepository, BookService bookService, AuthorRepository authorRepository,
      PublishRepository publishRepository) {
    super(repository);
    this.bookRepository = bookRepository;
    this.bookService = bookService;
    this.authorRepository = authorRepository;
    this.publishRepository = publishRepository;
  }

  public Page<Author> searchByAuthorFIO(Pageable pageable, String fio) {
    return authorRepository.findAllByAuthorFIO(pageable, fio);
  }
  public Page<Author> listAllPaginated(Pageable pageable) {
    return authorRepository.findAll(pageable);
  }

  public Author addBook(AddBookDto addBookDto) {
    Author author = getOne(addBookDto.getAuthorId());
    Book book = bookService.getOne(addBookDto.getBookId());
    author.getBooks().add(book);
    return update(author);
  }

  //TODO посмотреть как можно сократить
  @Override
  public void delete(Long id) {
    List<Book> books = bookRepository.findBooksByAuthorsId(id);
    if(books.isEmpty()) {
      authorRepository.deleteById(id);
      return;
    }
    List<Long> bookIds = books.stream()
        .filter(i -> i.getAuthors().size() == 1)
        .map(Book::getId)
        .toList();

    if (bookIds.isEmpty()) {
      throw new OpenApiResourceNotFoundException("У данного автора нет книги где он один.");
    }
    bookIds.stream()
        .filter(i -> {
          if (publishRepository.findPublishByBookId(i).isEmpty()) {
            return true;
          } else {
            throw new OpenApiResourceNotFoundException("Книга с данным автором в данный момент арендована");
          }
        })
        .forEach(i -> {
          bookRepository.deleteById(i);
          authorRepository.deleteById(id);
        });
  }

  public void block(Long id) {
    Author author = getOne(id);
    author.setDeleted(true);
    author.setDeletedBy(((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername());
    author.setDeletedWhen(LocalDateTime.now());
    update(author);
  }

  public void unblock(Long id) {
    Author author = getOne(id);
    author.setDeleted(false);
    author.setDeletedBy(null);
    author.setDeletedWhen(null);
    author.setUpdatedBy(((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername());
    author.setUpdatedWhen(LocalDateTime.now());
    update(author);
  }

  public Page<Author> listAllPaginatedForUser(Pageable pageable) {
    return authorRepository.findAllByIsDeletedFalse(pageable);
  }
}
