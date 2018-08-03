package com.andrew.productcatalogue2.search;

/**
 * Defines the order to sort the search results in
 *
 * @author Andrew Au-Young
 * @Version 1.0.0
 */
public enum SortOrder {

    CREATED_AT_DESC("Date added"),
    PRICE_ASC("Price-Low to High"),
    PRICE_DESC("Price-High to Low"),
    TITLE_ASC("Title");


    private String displayName;

    SortOrder(String displayName) {
        this.displayName = displayName;
    }

    /**
     * @Returns A reader-friendly {@link String} representation of the enum value
     * */
    public String getDisplayName() {
        return displayName;
    }


}
