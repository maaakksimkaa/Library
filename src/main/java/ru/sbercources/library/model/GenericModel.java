package ru.sbercources.library.model;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public abstract class GenericModel {

  @Id
  @Column(name="id", nullable = false)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "default_generator")
  private Long id;

  @Column(name = "created_by")
  private String createdBy;

  @Column(name = "created_when")
  private LocalDateTime createdWhen = LocalDateTime.now();

  @Column(name = "updated_when")
  private LocalDateTime updatedWhen;

  @Column(name = "updated_by")
  private String updatedBy;

  @Column(name = "is_deleted")
  private boolean isDeleted;

  @Column(name = "deleted_when")
  private LocalDateTime deletedWhen;

  @Column(name = "deleted_by")
  private String deletedBy;
}
