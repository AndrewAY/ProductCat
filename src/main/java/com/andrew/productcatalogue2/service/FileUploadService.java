package com.andrew.productcatalogue2.service;

import org.springframework.web.multipart.MultipartFile;

/** Handles actions involving uploading files to the server
 *
 * @author Andrew Au-Young
 * @version 1.0.0
 * */
public interface FileUploadService {

    /** Handles uploading of image files
     * @param file the file to be uploaded
     * @param productId the id of the product
     */
     boolean uploadImage(MultipartFile file, Long productId);
}
