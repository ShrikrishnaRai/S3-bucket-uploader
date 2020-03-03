package com.nextnepal.imageuploader.resource;

import com.nextnepal.imageuploader.controller.MultipartS3Controller;
import com.nextnepal.imageuploader.domain.ImageUrlResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/uploadImage")
public class ImageUploadResource extends MultipartS3Controller {

    @PostMapping
    public ResponseEntity<ImageUrlResponse> uploadPicture(
            @RequestParam("imageFolderPath") String imagePath,
            @RequestParam("photo") final MultipartFile multipartFile) throws Exception {
        return ResponseEntity.status(HttpStatus.OK)
                .body(uploadImage(multipartFile, imagePath));
    }

    public String getString() {
        return "Hello World";
    }

}
