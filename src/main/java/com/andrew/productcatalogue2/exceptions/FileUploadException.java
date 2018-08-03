package com.andrew.productcatalogue2.exceptions;


import com.andrew.productcatalogue2.entity.Product;

/** Can be used during the process of uploading files to indicate
 *  problems preventing successful uploads
 *
 * @author Andrew Au-Young
 * @version 1.0.0
 * */
public class FileUploadException extends RuntimeException {

    public FileUploadException() {
        super("Unable to upload file");
    }

    public FileUploadException(String message) {
        super(message);
    }

    public FileUploadException(String message, Throwable cause) {
        super(message);
        initCause(cause);
    }
}
