package com.andrew.productcatalogue2.controller;

import com.andrew.productcatalogue2.service.ImageService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.*;
import java.nio.file.Paths;

@Controller
@Slf4j
@RequestMapping("/")
public class ImageController {

    @Autowired
    ImageService imageService;

    private final String PRODUCT_IMAGE_URL = "/dynamic-resources/product-images/";


    @GetMapping(value= PRODUCT_IMAGE_URL + "{productId}", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[]  getProductImage(@PathVariable("productId") Long productId) throws IOException {
        return imageService.getImage(productId);
    }

}
