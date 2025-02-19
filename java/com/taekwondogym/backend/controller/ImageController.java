package com.taekwondogym.backend.controller;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;

@RestController
public class ImageController {

    private final ResourceLoader resourceLoader;

    public ImageController(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @GetMapping("/uploads/images/achievements/{filename:.+}")
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        File file = new File("src/main/resources/uploads/images/achievements/" + filename);
        Resource resource = resourceLoader.getResource("file:" + file.getAbsolutePath());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filename + "\"")
                .contentType(MediaType.IMAGE_JPEG) // or MediaType.IMAGE_PNG, depending on your images
                .body(resource);
    }
    @GetMapping("/uploads/images/products/{filename:.+}")
    public ResponseEntity<Resource> serveProductFile(@PathVariable String filename) {
        File file = new File("src/main/resources/uploads/images/products/" + filename);
        Resource resource = resourceLoader.getResource("file:" + file.getAbsolutePath());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filename + "\"")
                .contentType(MediaType.IMAGE_JPEG) // or MediaType.IMAGE_PNG, depending on your images
                .body(resource);
    }
    @GetMapping("/uploads/images/orders/{filename:.+}")
    public ResponseEntity<Resource> serveReceiptFile(@PathVariable String filename) {
        File file = new File("src/main/resources/uploads/images/orders/" + filename);
        Resource resource = resourceLoader.getResource("file:" + file.getAbsolutePath());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filename + "\"")
                .contentType(MediaType.IMAGE_JPEG) // or MediaType.IMAGE_PNG, depending on your images
                .body(resource);
    }
    @GetMapping("/uploads/images/trainers/{filename:.+}")
    public ResponseEntity<Resource> serveTrainers(@PathVariable String filename) {
        File file = new File("src/main/resources/uploads/images/trainers/" + filename);
        Resource resource = resourceLoader.getResource("file:" + file.getAbsolutePath());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filename + "\"")
                .contentType(MediaType.IMAGE_JPEG) // or MediaType.IMAGE_PNG, depending on your images
                .body(resource);
    }
    
}
