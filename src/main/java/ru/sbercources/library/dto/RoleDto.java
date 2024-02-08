package ru.sbercources.library.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.sbercources.library.model.Role;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RoleDto {

  private Long id;
  private String title;
  private String description;


  public RoleDto (Role role) {
    this.id = role.getId();
    this.title = role.getTitle();
    this.description = role.getDescription();
  }
}
