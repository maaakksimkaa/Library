package ru.sbercources.library.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "book")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SequenceGenerator(name = "default_generator", sequenceName = "books_seq", allocationSize = 1)
public class Book extends GenericModel{

  @Column(name = "download_link")
  private String downloadLink;

  @Column(name = "title")
  private String title;

  @Column(name = "genre")
  @Enumerated
  private Genre genre;

  @Column(name = "storage_place")
  private String storagePlace;

  @Column(name = "amount")
  private Integer amount;

  @Column(name = "publish_year")
  private String publishYear;

  @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
//  @JsonIgnore //убирает рекурсию
  @JoinTable(
      name = "books_authors",
      joinColumns = @JoinColumn(name = "book_id"),
      foreignKey = @ForeignKey(name = "FK_BOOKS_AUTHORS"),
      inverseJoinColumns = @JoinColumn(name = "author_id"),
      inverseForeignKey = @ForeignKey(name = "FK_AUTHORS_BOOKS"))
  private Set<Author> authors = new HashSet<>();

  @Builder
  public Book(Long id, String createdBy, LocalDateTime createdWhen, LocalDateTime updatedWhen, String updatedBy, boolean isDeleted,
      LocalDateTime deletedWhen, String deletedBy, String downloadLink, String title, Genre genre, String storagePlace, Integer amount,
      String publishYear, Set<Author> authors) {
    super(id, createdBy, createdWhen, updatedWhen, updatedBy, isDeleted, deletedWhen, deletedBy);
    this.downloadLink = downloadLink;
    this.title = title;
    this.genre = genre;
    this.storagePlace = storagePlace;
    this.amount = amount;
    this.publishYear = publishYear;
    this.authors = authors;
  }
}
