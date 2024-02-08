package ru.sbercources.library.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.sbercources.library.model.Publish;

@Repository
public interface PublishRepository extends GenericRepository<Publish> {
  List<Publish> findPublishByBookId(Long bookId);
}
