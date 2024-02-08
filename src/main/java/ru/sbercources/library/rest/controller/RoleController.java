package ru.sbercources.library.rest.controller;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.sbercources.library.model.Role;
import ru.sbercources.library.service.RoleService;

@RestController
@RequestMapping("/role") //localhost:9090/role
public class RoleController {

  private final RoleService service;

  public RoleController(RoleService service) {
    this.service = service;
  }

  @GetMapping("/list") //localhost:9090/role/list
  public List<Role> list() {
    return service.getList();
  }

}
