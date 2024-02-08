package ru.sbercources.library.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name ="authors")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SequenceGenerator(name = "default_generator", sequenceName = "authors_seq", allocationSize = 1)
public class Author extends GenericModel {

  @Column(name = "author_fio")
  private String authorFIO;

  @Column(name = "life_period")
  private String lifePeriod;

  @Column(name = "description")
  private String description;

  @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
//  @JsonIgnore //убирает рекурсию
  @JoinTable(
      name = "books_authors",
      joinColumns = @JoinColumn(name = "author_id"),
      foreignKey = @ForeignKey(name = "FK_AUTHORS_BOOKS"),
      inverseJoinColumns = @JoinColumn(name = "book_id"),
      inverseForeignKey = @ForeignKey(name = "FK_BOOKS_AUTHORS"))
  private Set<Book> books = new HashSet<>();


  @Builder
  public Author(Long id, String createdBy, LocalDateTime createdWhen, LocalDateTime updatedWhen, String updatedBy, boolean isDeleted,
      LocalDateTime deletedWhen, String deletedBy, String authorFIO, String lifePeriod, String description,
      Set<Book> books) {
    super(id, createdBy, createdWhen, updatedWhen, updatedBy, isDeleted, deletedWhen, deletedBy);
    this.authorFIO = authorFIO;
    this.lifePeriod = lifePeriod;
    this.description = description;
    this.books = books;
  }
}
