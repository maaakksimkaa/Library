package ru.sbercources.library.dto;

import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookWithAuthorsDto extends BookDto{

  private Set<AuthorDto> authors;
}
