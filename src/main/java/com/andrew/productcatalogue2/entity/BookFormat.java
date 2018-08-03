package com.andrew.productcatalogue2.entity;

import lombok.*;

import javax.persistence.*;

/** Provided a book format to be used in products of type {@link Book}. Getters and setters are available for all fields.
 *
 * @author Andrew Au-Young
 * @version 1.0.0
 * */

@Entity
@Table(name="REF_BOOK_FORMAT")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookFormat {

    @Id
    @Column
    @GeneratedValue
    private int formatId;

    @Column
    private String format;


}
