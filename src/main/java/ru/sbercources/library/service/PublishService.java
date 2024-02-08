package ru.sbercources.library.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.sbercources.library.dto.RentBookDto;
import ru.sbercources.library.model.Author;
import ru.sbercources.library.model.Book;
import ru.sbercources.library.model.Publish;
import ru.sbercources.library.model.User;
import ru.sbercources.library.repository.GenericRepository;
import ru.sbercources.library.repository.PublishRepository;

@Service
public class PublishService extends GenericService<Publish> {

  private final PublishRepository repository;
  private final UserService userService;
  private final BookService bookService;
  protected PublishService(PublishRepository repository, UserService userService, BookService bookService) {
    super(repository);
    this.repository = repository;
    this.userService = userService;
    this.bookService = bookService;
  }

  public Page<Publish> listAllPaginated(Pageable pageable) {
    return repository.findAll(pageable);
  }

  public Publish rentBook(RentBookDto rentBookDto) {
    User user = userService.getOne(rentBookDto.getUserId());
    Book book = bookService.getOne(rentBookDto.getBookId());
    if(book.getAmount() - rentBookDto.getAmount() < 0) {
      return null;
    }
    book.setAmount(book.getAmount() - rentBookDto.getAmount());
    bookService.update(book);

    Publish publish = Publish.builder()
        .rentDate(LocalDateTime.now())
        .returned(false)
        .returnDate(LocalDateTime.now().plusMonths(rentBookDto.getRentPeriod()))
        .rentPeriod(rentBookDto.getRentPeriod())
        .amount(rentBookDto.getAmount())
        .user(user)
        .book(book)
        .build();
    return repository.save(publish);

  }

  public List<Publish> getUserPublishing(Long userId) {
    return (List<Publish>) userService.getOne(userId).getPublish();
  }

  public void returnBook(Long id) {
    Publish publish = getOne(id);
    publish.setReturned(true);
    publish.setReturnDate(LocalDateTime.now());
    Book book = publish.getBook();
    book.setAmount(book.getAmount() + publish.getAmount());
    update(publish);
    bookService.update(book);
  }
}
