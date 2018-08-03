package com.andrew.productcatalogue2.entity;


import lombok.*;

import javax.persistence.*;

/** Provided a movie format to be used in products of type {@link Movie}.Getters and setters are available for all fields.
 *
 * @author Andrew Au-Young
 * @version 1.0.0
 * */

@Entity
@Table(name="REF_MOVIE_FORMAT")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieFormat {


    @Id
    @Column(name="FORMAT_ID")
    @GeneratedValue
    private int formatId;

    @Column
    private String format;



}
