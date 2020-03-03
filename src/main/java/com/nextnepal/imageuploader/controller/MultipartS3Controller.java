package com.nextnepal.imageuploader.controller;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.nextnepal.imageuploader.component.Thumbnails;
import com.nextnepal.imageuploader.domain.ImageUrlResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * This controller is used for uploading files to s3 bucket
 */
public abstract class MultipartS3Controller extends MultipartController {

    @Autowired
    private AmazonS3 s3client;

    @Value("${endpointUrl}")
    private String endpointUrl;

    @Value("${bucketName}")
    private String bucketName;

    private String originalUrl;
    private String thumbnailUrl;

    private String uploadFileTos3bucket(String fileName, File file) {
        try {
            s3client.putObject(new PutObjectRequest(bucketName, fileName, file));
        } catch (AmazonServiceException e) {
            return "uploadFileTos3bucket().Uploading failed :" + e.getMessage();
        }
        return s3client.getUrl(bucketName, fileName).toString();
    }

    public ImageUrlResponse uploadImage(@RequestPart(value = "file") MultipartFile multipartFile, String imagePath) throws Exception {
        Thread thread = new uploadOriginal(imagePath, multipartFile);
        thread.start();
        Thread thread1 = new uploadThumbnail(imagePath, multipartFile);
        thread1.start();
        thread.join();
        thread1.join();
        return new ImageUrlResponse(originalUrl, thumbnailUrl);
    }

    class uploadThumbnail extends Thread {
        private MultipartFile multipartFile;
        private String imagePath;
        Thumbnails thumbnails = new Thumbnails();

        uploadThumbnail(String imagePath, MultipartFile multipartFile) {
            this.multipartFile = multipartFile;
            this.imagePath = imagePath;
        }

        @Override
        public void run() {
            try {
                File file = thumbnails.scaleImage(convertMultiPartToFile(multipartFile));
                thumbnailUrl = uploadFileTos3bucket(imagePath + file.getName(), file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    class uploadOriginal extends Thread {
        private String imagePath;
        private MultipartFile multipartFile;


        uploadOriginal(String imagePath, MultipartFile multipartFile) {
            this.imagePath = imagePath;
            this.multipartFile = multipartFile;
        }

        @Override
        public void run() {
            try {
                originalUrl = uploadFileTos3bucket(imagePath + multipartFile.getOriginalFilename(),
                        convertMultiPartToFile(multipartFile));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
