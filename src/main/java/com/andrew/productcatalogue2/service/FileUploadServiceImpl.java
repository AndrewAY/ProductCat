package com.andrew.productcatalogue2.service;

import com.andrew.productcatalogue2.exceptions.FileUploadException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;


@Service
@Slf4j
public class FileUploadServiceImpl implements FileUploadService {


    private final String uploadImagePath;

    //  constructor injection needed to initialise final attributes
    public FileUploadServiceImpl(@Value("${upload.image.relative.path}") String uploadImagePath) {
        this.uploadImagePath = uploadImagePath;
    }

    @PreAuthorize("hasRole('ADMIN') OR hasRole('SUPER')")
    public boolean uploadImage(MultipartFile file, Long productId) {

        try {
                String absUploadPath = new File(uploadImagePath).getAbsolutePath();
                File uploadDir = new File(absUploadPath);
                boolean uploadDirExists = uploadDir.exists();
                String absUploadDirPath = uploadDir.getAbsolutePath();

                // check if the upload directory exists first. If not, attempt to create it
                if (!uploadDirExists) {
                    log.info("Unable to find directory path: " + absUploadDirPath + " on the system. Will attempt to create this directory path");
                    boolean successful = uploadDir.mkdirs();
                    if (successful) {
                        log.info("directory was created successfully at " + absUploadDirPath);
                        // do nothing
                    }
                    else {
                        log.warn("Failed to create a directory for file uploads at:" + absUploadDirPath);
                        FileUploadException ex = new FileUploadException("Unable to find and then create the following directory path for file uploads: " + absUploadDirPath);
                        log.warn("FileUploadException:", ex);
                        return false;
                        }
                }


                if (!file.isEmpty()) {
                    //set filename to the product Id number of the product, and then upload it
                    String fileName = productId.toString() + ".jpg";
                    InputStream is = file.getInputStream();
                    Files.copy(is, Paths.get(uploadImagePath + fileName), StandardCopyOption.REPLACE_EXISTING);
                    is.close();
                    return true;
                }

            } //end of try

        catch (IOException e) {
            log.warn("FileUploadService: IOException caught when trying to upload file",e);
            return false;
        }

        //unable to upload file
        return false;

        }



}







