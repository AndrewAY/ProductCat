package com.andrew.productcatalogue2.service;

import java.io.IOException;

/** Handles the retrieval of images from the server
 *
 * @author Andrew Au-Young
 * @version 1.0.0
 * */
public interface ImageService  {

   /**
    *
    * @param productId of the product
    * @return the requested image file, or a default image if a specific image doesn't exist for the product
    * @throws IOException
    */
   byte[] getImage(Long productId) throws IOException;
}
