package ru.sbercources.library.dto;

import lombok.Data;

@Data
public class RentBookDto {
  Long bookId;
  Long userId;
  Integer amount;
  Integer rentPeriod;
}
