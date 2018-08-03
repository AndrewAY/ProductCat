package com.andrew.productcatalogue2.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;

/** Represents a language can can be used as property values for products.
 * Getters and setters are available for all fields.
 *
 * @author Andrew Au-Young
 * @version 1.0.0
 * */
@Entity
@Table (name="REF_LANGUAGE")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Language {

    @Id
    @Column(name="LANGUAGE_ID")
    @GeneratedValue
    private Integer languageId;

    @Column
    private String name;


}
