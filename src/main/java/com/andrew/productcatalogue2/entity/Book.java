package com.andrew.productcatalogue2.entity;




import lombok.Data;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

/** Implementation of a Book product. Getters and setters are available for all fields.
 *
 * @author Andrew Au-Young
 * @version 1.0.0
 * */
@Entity(name = "Book")
@Data
public class Book extends Product {

    public Book() {
        productType = ProductType.BOOK.toString();
    }

    @ManyToOne(optional=false)
    @JoinColumn(name="LANGUAGE_ID",nullable = false)
    @NotNull(message="{null.languages}")
    private Language language;

    @ManyToOne(optional=false)
    @JoinColumn(name="FORMAT_ID",nullable = false)
    @NotNull(message="{null.movieformat}")
    private BookFormat format;

}
