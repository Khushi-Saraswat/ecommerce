package com.example.demo.Common;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

// Generic Abstract class for mapping between Entity and DTO
@Component
public abstract class AbstractMapperService<E, D> {

    private Class<E> entityClass;
    private Class<D> dtoClass;

    @Autowired
    protected ModelMapper modelMapper;

    public E convertDtoToEntity(D dto) {
        return modelMapper.map(dto, entityClass);
    }

    public D convertEntityToDto(E entity) {
        return modelMapper.map(entity, dtoClass);
    }
}
