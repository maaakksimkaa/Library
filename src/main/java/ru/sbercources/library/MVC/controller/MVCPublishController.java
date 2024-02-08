package ru.sbercources.library.MVC.controller;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.sbercources.library.dto.PublishDto;
import ru.sbercources.library.dto.RentBookDto;
import ru.sbercources.library.mapper.BookMapper;
import ru.sbercources.library.mapper.PublishMapper;
import ru.sbercources.library.model.Publish;
import ru.sbercources.library.service.BookService;
import ru.sbercources.library.service.PublishService;
import ru.sbercources.library.service.userDetails.CustomUserDetails;

@Controller
@RequestMapping("/publish")
public class MVCPublishController {

  private final BookService bookService;
  private final BookMapper bookMapper;
  private final PublishMapper mapper;
  private final PublishService service;

  public MVCPublishController(BookService bookService, BookMapper bookMapper, PublishMapper mapper, PublishService service) {
    this.bookService = bookService;
    this.bookMapper = bookMapper;
    this.mapper = mapper;
    this.service = service;
  }

  @GetMapping("/get-book/{bookId}")
  public String getBook(@PathVariable Long bookId, Model model) {
    model.addAttribute("book", bookMapper.toDto(bookService.getOne(bookId)));
    return "userBooks/getBook";
  }

  @PostMapping("/get-book")
  public String getBook(@ModelAttribute("publishForm") RentBookDto rentBookDto) {
    CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    rentBookDto.setUserId(Long.valueOf(customUserDetails.getUserId()));
    service.rentBook(rentBookDto);
    return "redirect:/books";
  }

  @GetMapping("/user-books/{id}")
  public String userBooks(
      @RequestParam(value = "page", defaultValue = "1") int page,
      @RequestParam(value = "size", defaultValue = "10") int pageSize,
      @PathVariable Long id,
      Model model
  ) {
    PageRequest pageRequest = PageRequest.of(page - 1, pageSize);
    Page<Publish> authorPage = service.listAllPaginated(pageRequest);
    List<PublishDto> publishDtos = authorPage
        .stream()
        .map(mapper::toDto)
        .toList();
    model.addAttribute("publishies", new PageImpl<>(publishDtos, pageRequest, authorPage.getTotalElements()));
    return "userBooks/viewAllUserBooks";
  }

  @GetMapping("/return-book/{id}")
  public String returnBook(@PathVariable Long id) {
    service.returnBook(id);
    return "redirect:/publish/user-books/" + service.getOne(id).getUser().getId();
  }


}
