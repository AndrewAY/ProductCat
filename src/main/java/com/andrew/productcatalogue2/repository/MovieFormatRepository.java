package com.andrew.productcatalogue2.repository;

import com.andrew.productcatalogue2.entity.MovieFormat;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface  MovieFormatRepository extends CrudRepository<MovieFormat,Integer> {
}
