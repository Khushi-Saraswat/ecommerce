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

    public Map deleteImage(String imageUrlOrPublicId) throws Exception {

        if (imageUrlOrPublicId == null || imageUrlOrPublicId.isBlank()) {
            return Collections.emptyMap();
        }

        String publicId = imageUrlOrPublicId;

        // ✅ If user passed full Cloudinary URL, extract publicId
        if (imageUrlOrPublicId.contains("/upload/")) {

            // get part after /upload/
            publicId = imageUrlOrPublicId.substring(imageUrlOrPublicId.indexOf("/upload/") + 8);

            // remove version v12345/
            publicId = publicId.replaceAll("^v\\d+/", "");

            // remove extension (.jpg/.png/.webp etc.)
            if (publicId.contains(".")) {
                publicId = publicId.substring(0, publicId.lastIndexOf("."));
            }
        }

        return cloudinary.uploader().destroy(publicId, Collections.emptyMap());
    }

    public String getImageUrl(String publicId) {
        return cloudinary.url().generate(publicId);
    }
}
