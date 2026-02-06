package com.example.demo.dto;

import java.util.List;

import org.springframework.data.domain.Page;

public class ProductPageDto<T> {

    List<T> products;

    CustomDto customDto;

    public ProductPageDto(Page<T> page) {
        this.products = page.getContent();
        this.customDto = new CustomDto(page.getTotalElements(),
                page.getTotalPages(), page.getNumber(), page.getSize());

    }

}
