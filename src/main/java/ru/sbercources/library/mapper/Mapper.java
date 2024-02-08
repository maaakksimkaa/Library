package ru.sbercources.library.mapper;

import java.util.Collection;
import java.util.List;
import ru.sbercources.library.dto.GenericDto;
import ru.sbercources.library.model.GenericModel;

public interface Mapper<E extends GenericModel, D extends GenericDto> {

  E toEntity(D dto);
  List<E> toEntities(List<D> dtos);

  D toDto(E entity);
  List<D> toDtos(List<E> entities);



}
