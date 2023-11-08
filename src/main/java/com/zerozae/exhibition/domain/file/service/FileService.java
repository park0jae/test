package com.zerozae.exhibition.domain.file.service;


import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.zerozae.exhibition.domain.file.exception.FileUploadFailureException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class FileService {

    @Value("${cloud.aws.s3.bucket}")
    private String BUCKET;
    private final AmazonS3Client amazonS3Client;

    public void upload(MultipartFile file, String filename) {
        try {
            ObjectMetadata metadata= new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            metadata.setContentLength(file.getSize());
            amazonS3Client.putObject(BUCKET, filename, file.getInputStream(), metadata);
        } catch(IOException e) {
            throw new FileUploadFailureException();
        }
    }

    public void delete(String fileName){
        DeleteObjectRequest request = new DeleteObjectRequest(BUCKET , fileName);
        amazonS3Client.deleteObject(request);
    }
}
