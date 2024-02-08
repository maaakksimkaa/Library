package ru.sbercources.library.rest.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.sbercources.library.dto.AddBookDto;
import ru.sbercources.library.dto.AuthorDto;
import ru.sbercources.library.dto.AuthorWithBooksDto;
import ru.sbercources.library.mapper.AuthorMapper;
import ru.sbercources.library.mapper.AuthorWithBooksMapper;
import ru.sbercources.library.model.Author;
import ru.sbercources.library.service.AuthorService;
import ru.sbercources.library.service.GenericService;


/**
 * Реазация контроллера для сущности Author
 * При наследовании этого класса от GenericController<T>,
 * автоматически реализуются основные CRUD операции
 */
@Slf4j
@RestController
@RequestMapping("/rest/author")
@SecurityRequirement(name = "Bearer Authentication")
public class AuthorController extends GenericController<Author, AuthorDto> {

  private final AuthorService service;
  private final AuthorWithBooksMapper authorWithBooksMapper;

  public AuthorController(AuthorService service, AuthorMapper mapper,  AuthorWithBooksMapper authorWithBooksMapper) {
    super(service, mapper);
    this.service = service;
    this.authorWithBooksMapper = authorWithBooksMapper;
  }

  @GetMapping("/author-books")
  public List<AuthorWithBooksDto> getAuthorsWithBooks() {
    return service.listAll().stream().map(authorWithBooksMapper::toDto).toList();
  }

  @PostMapping("/add-book")
  public AuthorDto addBook(@RequestBody AddBookDto addBookDto) {
    return authorWithBooksMapper.toDto(service.addBook(addBookDto));
  }
}
