package ru.sbercources.library.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.sbercources.library.model.Author;
import ru.sbercources.library.model.Book;
import ru.sbercources.library.model.Genre;
import ru.sbercources.library.repository.BookRepository;
import ru.sbercources.library.service.userDetails.CustomUserDetails;

@Service
public class BookService extends GenericService<Book> {

  //  Инжектим конкретный репозиторий для работы с таблицей books
  private final BookRepository repository;

  protected BookService(BookRepository repository) {
    //Передаем этот репозиторй в абстрактный севрис,
    //чтобы он понимал с какой таблицей будут выполняться CRUD операции
    super(repository);
    this.repository = repository;
  }

  /**
   * Метод для поиска книги
   * Этот метод относится только к книгам, поэтому его реализация пишется только в BookService
   * @param title - Название книги
   * @param genre - Жанр
   * @return - Лист найденных книг
   */
  public List<Book> search(String title, Genre genre) {
    return repository.findAllByGenreOrTitle(
        genre,
        title
    );
  }

  @Override
  public Book update(Book object) {

    return super.update(object);
  }

  public List<Book> searchByTitle(String title) {
    return repository.findAllByTitle(title);
  }

  public Page<Book> listAllPaginated(Pageable pageable) {
    return repository.findAll(pageable);
  }

  public Page<Book> listAllPaginatedForUser(Pageable pageable) {
    return repository.findAllByIsDeletedFalse(pageable);
  }

  public void block(Long id) {
    Book book = getOne(id);
    book.setDeleted(true);
    book.setDeletedBy(((CustomUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername());
    book.setDeletedWhen(LocalDateTime.now());
    update(book);
  }

  public void unblock(Long id) {
    Book book = getOne(id);
    book.setDeleted(false);
    book.setDeletedWhen(null);
    book.setDeletedBy(null);
    book.setUpdatedBy(((CustomUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername());
    book.setUpdatedWhen(LocalDateTime.now());
    update(book);
  }
}
