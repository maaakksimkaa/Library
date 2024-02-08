package ru.sbercources.library.rest.controller;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.sbercources.library.dto.PublishDto;
import ru.sbercources.library.dto.RentBookDto;
import ru.sbercources.library.mapper.PublishMapper;
import ru.sbercources.library.model.Publish;
import ru.sbercources.library.service.PublishService;

@Slf4j
@RestController
@RequestMapping("/rest/publish")
public class PublishController extends GenericController<Publish, PublishDto> {

  private final PublishService service;
  private final PublishMapper mapper;

  protected PublishController(PublishService service, PublishMapper mapper) {
    super(service, mapper);
    this.service = service;
    this.mapper = mapper;
  }

  @PostMapping("rent-book")
  public PublishDto rentBook(@RequestBody RentBookDto rentBookDto) {
    return mapper.toDto(service.rentBook(rentBookDto));
  }

  @GetMapping("user-publishing/{userId}")
  public List<PublishDto> getUserPublishing(@PathVariable Long userId) {
    return mapper.toDtos(service.getUserPublishing(userId));
  }
}
