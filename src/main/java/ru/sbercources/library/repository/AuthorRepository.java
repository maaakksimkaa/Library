package ru.sbercources.library.repository;

import java.util.List;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.sbercources.library.model.Author;
import ru.sbercources.library.model.Book;

@Repository
public interface AuthorRepository extends GenericRepository<Author> {

  Set<Author> findAllByIdIn(Set<Long> ids);

  Page<Author> findAllByAuthorFIO(Pageable pageable, String authorFIO);

  Page<Author> findAllByIsDeletedFalse(Pageable pageable);

}
