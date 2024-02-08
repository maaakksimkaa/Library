package ru.sbercources.library.dto;

import java.util.Set;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthorDto extends GenericDto {

  @NotBlank(message = "Поле не должно быть пустым")
  private String authorFIO;
  @NotBlank(message = "Поле не должно быть пустым")
  private String lifePeriod;
  @NotBlank(message = "Поле не должно быть пустым")
  private String description;
  private Set<Long> booksIds;

}
