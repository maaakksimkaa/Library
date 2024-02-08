package ru.sbercources.library.service.userDetails;

import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.sbercources.library.model.User;
import ru.sbercources.library.repository.UserRepository;

@Slf4j
@Service
public class CustomUserDetailsService implements UserDetailsService {

  private final UserRepository userRepository;

  @Value("${spring.security.user.name}")
  private String adminUserName;
  @Value("${spring.security.user.password}")
  private String adminPassword;
  @Value("${spring.security.user.roles}")
  private String adminRole;

  public CustomUserDetailsService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    if (username.equals(adminUserName)) {
      return new CustomUserDetails(null, username, adminPassword, List.of(new SimpleGrantedAuthority("ROLE_ADMIN")));
    } else {
      User user = userRepository.findUserByLoginAndDeletedFalse(username);
      List<GrantedAuthority> authorities = new ArrayList<>();
      authorities.add(new SimpleGrantedAuthority(user.getRole().getId() == 1L ? "ROLE_USER" : "ROLE_LIBRARIAN"));
      return new CustomUserDetails(user.getId().intValue(), username, user.getPassword(), authorities);
    }
  }
}
