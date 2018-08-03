package com.andrew.productcatalogue2.repository;

import com.andrew.productcatalogue2.entity.Movie;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MovieRepository extends CrudRepository<Movie,Long>{
}
