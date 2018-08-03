package com.andrew.productcatalogue2.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;

@Service
@Slf4j
public class ImageServiceImpl implements ImageService {


    private final String UPLOAD_IMAGE_PATH;
    private final String IMAGE_FILE_EXTENSION_TYPE;


public ImageServiceImpl(@Value("${upload.image.relative.path}") String uploadImagePath,
                        @Value("${image.file.extension.type}") String imageExtensionType) {
    UPLOAD_IMAGE_PATH = uploadImagePath;
    IMAGE_FILE_EXTENSION_TYPE = imageExtensionType;
}



    @Override
    public byte[] getImage(Long productId) {
        String imagePath = UPLOAD_IMAGE_PATH + productId + IMAGE_FILE_EXTENSION_TYPE;

        try {
            byte[] imageToReturn;

            // does image exist?
            File image = new File(imagePath);
            boolean imageExists = image.exists();

            if (imageExists) {
                InputStream imageInputStream = new BufferedInputStream(
                        new FileInputStream(imagePath));
                imageToReturn = IOUtils.toByteArray(imageInputStream);
                imageInputStream.close();
            } else {
                // if existing image doesn't exist for this productId then use a default image
                imagePath = UPLOAD_IMAGE_PATH + "default-image" + IMAGE_FILE_EXTENSION_TYPE;
                boolean defaultImageExists = new File(imagePath).exists();
                if (defaultImageExists) {
                    InputStream imageInputStream = new BufferedInputStream(
                            new FileInputStream(imagePath));
                    imageToReturn =  IOUtils.toByteArray(imageInputStream);
                    imageInputStream.close();
                } else {
                    FileNotFoundException ex = new FileNotFoundException();
                    log.info("Unable to find a default image file named default-image.jpg", ex);
                    imageToReturn =  null;
                }
            }

            return imageToReturn;
        }
        catch (IOException e) {
            log.warn("Error while trying to retrieve file at: " + imagePath);
            return null;
        }
    }
}
