package com.rajan.foodDeliveryApp.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/files")
public class FileUploadController {

    @Value("${file.upload-dir:src/main/resources/static/storage}")
    private String uploadDir;

    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return "File is empty.";
            }

            // Create directory if it does not exist
            File dir = new File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            // Generate a unique filename
            String filename = file.getOriginalFilename();
            Path filePath = Paths.get(uploadDir, filename);

            // Save the file
            Files.copy(file.getInputStream(), filePath);

            // Return the file path or URL (if served via HTTP)
            return "File uploaded successfully: " + filename;
        } catch (IOException e) {
            e.printStackTrace();
            return "File upload failed: " + e.getMessage();
        }
    }
}

