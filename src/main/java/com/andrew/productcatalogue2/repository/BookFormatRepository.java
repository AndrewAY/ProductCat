package com.andrew.productcatalogue2.repository;

import com.andrew.productcatalogue2.entity.BookFormat;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookFormatRepository extends CrudRepository<BookFormat,Integer> {
}
