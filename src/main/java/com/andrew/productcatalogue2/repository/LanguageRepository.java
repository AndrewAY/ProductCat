package com.andrew.productcatalogue2.repository;

import com.andrew.productcatalogue2.entity.Language;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LanguageRepository extends CrudRepository<Language,Integer>{
}
