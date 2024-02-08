package ru.sbercources.library.MVC.controller;

import io.swagger.v3.oas.annotations.Hidden;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.sbercources.library.dto.BookDto;
import ru.sbercources.library.dto.BookWithAuthorsDto;
import ru.sbercources.library.mapper.BookMapper;
import ru.sbercources.library.mapper.BookWithAuthorsMapper;
import ru.sbercources.library.model.Book;
import ru.sbercources.library.service.BookService;
import ru.sbercources.library.service.userDetails.CustomUserDetails;

@Hidden
@Controller
@RequestMapping("books")
public class MVCBookController {

  private final BookService service;
  private final BookWithAuthorsMapper bookWithAuthorsMapper;
  private final BookMapper mapper;

  public MVCBookController(BookService service, BookWithAuthorsMapper bookWithAuthorsMapper, BookMapper mapper) {
    this.service = service;
    this.bookWithAuthorsMapper = bookWithAuthorsMapper;
    this.mapper = mapper;
  }

  @GetMapping("")
  public String getAll(
      @RequestParam(value = "page", defaultValue = "1") int page,
      @RequestParam(value = "size", defaultValue = "5") int pageSize,
      Model model
  ) {
    CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    PageRequest pageRequest = PageRequest.of(page - 1, pageSize, Sort.by(Direction.ASC, "title"));
    Page<Book> authorPage = null;
    if(customUserDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER"))) {
      authorPage = service.listAllPaginatedForUser(pageRequest);
    } else {
      authorPage = service.listAllPaginated(pageRequest);
    }
    List<BookWithAuthorsDto> bookDtos = authorPage
        .stream()
        .map(bookWithAuthorsMapper::toDto)
        .toList();
    model.addAttribute("books", new PageImpl<>(bookDtos, pageRequest, authorPage.getTotalElements()));
    return "books/viewAllBooks";
  }

  @GetMapping("/add")
  public String create() {
    return "books/addBook"; //путь до html файла
  }

  @PostMapping("/add")
  public String create(@ModelAttribute("bookForm") BookDto bookDto) {
    service.create(mapper.toEntity(bookDto));
    return "redirect:/books";
  }

  @GetMapping("/delete/{id}")
  public String delete(@PathVariable Long id) {
    service.delete(id);
    return "redirect:/books";
  }

  @GetMapping("/update/{id}")
  public String update(@PathVariable Long id, Model model) {
    model.addAttribute("book", bookWithAuthorsMapper.toDto(service.getOne(id)));
    return "books/updateBook"; //путь до html файла
  }

  @PostMapping("/update")
  public String update(@ModelAttribute("bookForm") BookDto bookDto) {
    Book book = service.getOne(bookDto.getId());
    book.setTitle(book.getTitle());
    book.setGenre(book.getGenre());
    book.setAmount(book.getAmount());
    book.setPublishYear(book.getPublishYear());
    book.setStoragePlace(book.getStoragePlace());
    service.update(book);
    return "redirect:/books";
  }

  @PostMapping("/search")
  public String searchByAuthorFIO(Model model, @ModelAttribute("searchAuthors") BookDto bookDto) {
    if (bookDto.getTitle().trim().equals("")) {
      model.addAttribute("books", bookWithAuthorsMapper.toDtos(service.listAll()));
    } else {
      model.addAttribute("books", bookWithAuthorsMapper.toDtos(service.searchByTitle(bookDto.getTitle())));
    }
    return "books/viewAllBooks";
  }

  @GetMapping("/{id}")
  public String getOne(@PathVariable Long id, Model model) {
    model.addAttribute("book", bookWithAuthorsMapper.toDto(service.getOne(id)));
    return "books/viewBook";
  }

  @GetMapping("/block/{id}")
  public String blockBook(@PathVariable Long id) {
    service.block(id);
    return "redirect:/books";
  }

  @GetMapping("/unblock/{id}")
  public String unblockBook(@PathVariable Long id) {
    service.unblock(id);
    return "redirect:/books";
  }
}
