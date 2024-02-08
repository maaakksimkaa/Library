package ru.sbercources.library.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.sbercources.library.model.Book;
import ru.sbercources.library.model.User;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PublishDto extends GenericDto {

  private LocalDateTime rentDate;
  private LocalDateTime returnDate;
  private boolean returned;
  private Integer rentPeriod;
  private Integer amount;
  private Long bookId;
  private Long userId;
  private BookDto book;
  private UserDto user;

}
