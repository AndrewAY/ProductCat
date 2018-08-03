package com.andrew.productcatalogue2.demo_init;

import com.andrew.productcatalogue2.exceptions.FileUploadException;
import com.andrew.productcatalogue2.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.io.*;
import java.sql.Connection;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/*This class is used for demos. It initialises the database with sample data, and sets up a folder on the users
hard drive for storing and uploading product images*/
@Component
@Slf4j
public class DemoInit {

    @Autowired
    DataSource datasource;

    @Autowired
    UserRepository userRepository;

    private static final int BUFFER_SIZE = 4096;

    private final String uploadImagePath;


    //  constructor injection needed to initialise final attributes
    public DemoInit(@Value("${upload.image.relative.path}") String uploadImagePath) {
        this.uploadImagePath = uploadImagePath;
    }


    /*  will be called right after the Spring context has been initialised */
    @EventListener
    public void onApplicationEvent(ContextRefreshedEvent event) {

        populateDatabase();
        setUpImageResourceDirectory();

        }





    // populates the database with sample records
    private void populateDatabase() {
        try {
          /*  checking if the 'user' table has already been populated. If it is empty, that means the database
                hasn't been initialised with sample data yet*/
            boolean hasUsers = userRepository.findAll().iterator().hasNext();
            if (hasUsers) {
                log.info("user records found");
            } else {
                log.info("No user records found. Will run script to initialise database with sample data for demo purposes");
                Connection connection = datasource.getConnection();
                ScriptUtils.executeSqlScript(connection, new ClassPathResource("/init/database-initialisation.sql"));

            }
        } catch(Exception e) {
            log.warn("Unable to initialise database with sample data",e);
        }
    }




    /*    makes a folder on the users hard drive to store and upload images
        Also populates it with default resources and images for the sample data*/
    private void setUpImageResourceDirectory() {

        //make the destination folder
//        uploadImagePath = "dynamic-resources/product-images/";
        String absUploadPath = new File(uploadImagePath).getAbsolutePath();
        File destDir = new File(absUploadPath);

        // check if the upload directory exists first. If not, attempt to create it
        boolean destDirExists = destDir.exists();
        String absDestDirPath = destDir.getAbsolutePath();
        if (!destDirExists) {
            log.info("Unable to find directory path: " + absDestDirPath + " on the system. Will attempt to create this directory path");
            boolean successful = destDir.mkdirs();
            if (successful) {
                log.info("directory was created successfully at " + absDestDirPath);
                // do nothing more
            }
            else {
                log.warn("Failed to create a directory for file uploads at:" + absDestDirPath);
                FileUploadException ex = new FileUploadException("File and image uploads will not be possible as unable to create the following directory path for uploads: " + destDir.getAbsolutePath());
                log.error("FileUploadException:", ex);
            }
        }

        try {
            //unzip the image resources and copy to dest folder on the user's drive
            InputStream is = getClass().getResourceAsStream("/init/product-images.zip");
            ZipInputStream zipIn = new ZipInputStream(is);
            ZipEntry entry = zipIn.getNextEntry();

            while (entry != null) {

                String destPath = new File(uploadImagePath +  entry.getName()).getAbsolutePath();
                System.out.println("first entry.: " + entry.getName());
                System.out.println("destPath: " + destPath);
                extractFile(zipIn, destPath);

                zipIn.closeEntry();
                entry = zipIn.getNextEntry();
            }


        }
        catch (Exception e) {
            log.error("Unable to copy dynamic resources folder onto hard drive. Will be unable to view existing or default product images",e);
        }

    }





    private void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
        byte[] bytesIn = new byte[BUFFER_SIZE];
        int read = 0;
        while ((read = zipIn.read(bytesIn)) != -1) {
            bos.write(bytesIn, 0, read);
        }
        bos.close();
    }

}
