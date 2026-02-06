package com.example.demo.service.impl;

import java.util.Collections;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;

@Service
public class CloudinaryService {

    @Autowired
    private Cloudinary cloudinary;

    public String uploadImage(MultipartFile file) {
        try {
            Map uploadResult = cloudinary.uploader()
                    .upload(file.getBytes(), Collections.emptyMap());

            return uploadResult.get("secure_url").toString(); // final image URL
        } catch (Exception e) {
            throw new RuntimeException("Image upload failed", e);
        }
    }

    public Map deleteImage(String publicId) throws Exception {

        Map result = cloudinary.uploader().destroy(publicId, Collections.emptyMap());
        return result;
    }

    public String getImageUrl(String publicId) {
        return cloudinary.url().generate(publicId);
    }
}
