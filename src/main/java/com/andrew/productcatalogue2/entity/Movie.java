package com.andrew.productcatalogue2.entity;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

/** Implementation of a Movie product.Getters and setters are available for all fields.
 *
 * @author Andrew Au-Young
 * @version 1.0.0
 * */
@Entity(name = "Movie")
@Data
public class Movie extends Product {

public Movie() {
    productType = ProductType.MOVIE.toString();
}

 @ManyToMany
 @JoinTable(name="MOVIE_LANGUAGE",
            joinColumns = @JoinColumn(name="PRODUCT_ID"),
            inverseJoinColumns = @JoinColumn(name="LANGUAGE_ID"))
 @NotNull(message="{null.languages}")
     List<Language> languages;

    @ManyToOne(optional=false)
    @JoinColumn(name="FORMAT_ID",nullable = false)
    @NotNull(message="{null.movieformat}")
    private MovieFormat format;


    /**
     * Returns a more presentable, comma-separated list of the languages for presenting on the UI
     * @returns a String formatted line comprising this product instance's languages
     * */
    public String languagesToString() {
        List<String> languageList = languages.stream().map(l->l.getName()).collect(Collectors.toList());
       return languageList.toString().replace("[" ,"").replace("]" ,"");
    }

}
