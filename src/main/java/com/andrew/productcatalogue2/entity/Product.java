package com.andrew.productcatalogue2.entity;

import com.andrew.productcatalogue2.properties.GlobalPropertiesService;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/** Defines general attributes that are shared by all types of product. It should
 * be extended by a child class to create new product types. Getters and Setters exist for all properties
 * except 'updatedAt', which is changed automatically whenever a product is updated.
 * <p>
 * For data-binding purposes when using webpage templating engines such as Thymeleaf, the  java.time.ZonedDateTime creation date 'createdAt'
 * is retrieved and set as a String object in its getter and setter methods, although getCreatedAtAsZonedDateTime() can
 *  be used to retrived it as a java.time.ZonedDateTime instance.
 * </p>
 * <p>
 *     'ProductType' is converted from a Product.ProductType {@link ProductType} enum value
 *      *  into a String so it can be persisted to the database
 * </p>
 *
 *
 * @author Andrew Au-Young
 * @version 1.0.0
 * */
@Entity
@Table(name = "PRODUCT")
@Inheritance(strategy = InheritanceType.JOINED)
@Data
@ToString @EqualsAndHashCode
@Slf4j
public abstract class Product {

    /** Represents the type of product
     */
    public enum ProductType {
        MOVIE("Movies"), BOOK("Books");

        /** A pluralised form of the enum value suitable for displaying on the UI. */
        private final String pluralisedName;


        ProductType(String pluralisedName) {
            this.pluralisedName = pluralisedName;
        }

        /**
         * @return A pluralised form of the enum value suitable for displaying on the UI.
         */
        public String pluralisedName() {
            return pluralisedName;
        }

        public String toString() {
            return this.name();
        }
    }



    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    @SequenceGenerator(name="aaseq2", initialValue=50)
    @SequenceGenerator(name="PRODUCTID_SEQ", sequenceName="prodId_sequence", initialValue=50)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="PRODUCTID_SEQ")
    private Long productId;

    @Column(name = "title", nullable = false, length = 301)
    @Size(min=1, max=300, message="{title.size}")
    protected String title;

    @Column(length = 1000)
    @Size(max=1000, message="{description.size}")
    protected String description;

    @Column(nullable=false, precision = 10, scale=2) //creates correct formatting in database table
    @Digits(integer=8, fraction=2, message="{price.format}") @DecimalMin(value="0.00",message="{price.value}") @DecimalMax(value="99999999.99",message="{price.value}") //validates on form
    @NotNull(message="{price.value}")
    protected BigDecimal price;

    /** The date and time when record was added to database as an instance of  java.time.ZonedDateTime  */
    @Column(nullable=false)
    protected ZonedDateTime createdAt;

    @Column(nullable=false)
    protected ZonedDateTime updatedAt;


    /** The type of product. The value is converted from a Product.ProductType enum value
     *  into a String so it can be persisted to the database */
    @Column(nullable = false)
    protected String productType;

    /** The name of the image file to use for the product image, including the file extension */
    @Column
    protected String imageName;


    /**
     * @Return the product type as an enum of Product.ProductType
     **/
    public ProductType getProductType() {
        return ProductType.valueOf(productType);
    }


    /**
     * @param productType the product type as an enum of Product.ProductType
     * */
    public void setProductType(ProductType productType) {
        this.productType = productType.name();
    }


    /** Retrieves the creation date as a ZonedDateTime.
     * @Return creation date as a  java.time.ZonedDateTime
     * */
    public ZonedDateTime getCreatedAtAsZonedDateTime() {
        return createdAt;
    }

    public String getCreatedAt() {
        return createdAt.toString();
    }


    public void setCreatedAt(String creationDate) {
        createdAt = ZonedDateTime.parse(creationDate);
    }


    @PrePersist
    void createdAt() {
        String zoneId = GlobalPropertiesService.getTimeStampZoneId();
        Instant now = Instant.now();
        this.createdAt = this.updatedAt = ZonedDateTime.ofInstant(now, ZoneId.of(zoneId));
    }


    @PreUpdate
    void updatedAt() {
        String zoneId = GlobalPropertiesService.getTimeStampZoneId();
        Instant now = Instant.now();
        this.updatedAt = ZonedDateTime.ofInstant(now, ZoneId.of(zoneId));
    }

}
