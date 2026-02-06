package com.example.demo.Common;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AbstractMapperService {

    @Autowired
    protected ModelMapper modelMapper;

    // Generic method to convert DTO → Entity
    public <D, E> E toEntity(D dto, Class<E> entityClass) {
        if (dto == null)
            return null;
        return modelMapper.map(dto, entityClass);
    }

    // Generic method to convert Entity → DTO
    public <E, D> D toDto(E entity, Class<D> dtoClass) {
        if (entity == null)
            return null;
        return modelMapper.map(entity, dtoClass);
    }
}
