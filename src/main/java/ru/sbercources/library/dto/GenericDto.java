package ru.sbercources.library.dto;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public abstract class GenericDto {

  private Long id;
  private String createdBy;
  private LocalDateTime createdWhen;
  private LocalDateTime updatedWhen;
  private String updatedBy;
  private boolean isDeleted;
  private LocalDateTime deletedWhen;
  private String deletedBy;
}
