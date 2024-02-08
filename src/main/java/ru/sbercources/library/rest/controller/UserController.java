package ru.sbercources.library.rest.controller;

import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.sbercources.library.dto.LoginDto;
import ru.sbercources.library.dto.UserDto;
import ru.sbercources.library.mapper.UserMapper;
import ru.sbercources.library.model.User;
import ru.sbercources.library.security.JwtTokenUtil;
import ru.sbercources.library.service.GenericService;
import ru.sbercources.library.service.UserService;
import ru.sbercources.library.service.userDetails.CustomUserDetailsService;

@Slf4j
@RestController
@RequestMapping("/rest/user")
public class UserController extends GenericController<User, UserDto> {

  private final JwtTokenUtil jwtTokenUtil;
  private final CustomUserDetailsService customUserDetailsService;
  private final UserService service;
  protected UserController(UserService service, UserMapper mapper, JwtTokenUtil jwtTokenUtil, CustomUserDetailsService customUserDetailsService) {
    super(service, mapper);
    this.jwtTokenUtil = jwtTokenUtil;
    this.service = service;
    this.customUserDetailsService = customUserDetailsService;
  }

  @PostMapping("/auth")
  public ResponseEntity<?> auth(@RequestBody LoginDto loginDto) {
    Map<String, Object> response = new HashMap<>();

    if(!service.checkPassword(loginDto)) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized user!\nWrongPassword");
    }
    UserDetails foundUser = customUserDetailsService.loadUserByUsername(loginDto.getLogin());
    String token = jwtTokenUtil.generateToken(foundUser);
    response.put("token", token);
    response.put("authorities", foundUser.getAuthorities());
    return ResponseEntity.ok().body(response);

  }
}
