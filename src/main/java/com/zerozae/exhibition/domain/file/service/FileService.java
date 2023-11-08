package com.zerozae.exhibition.domain.file.service;


import com.zerozae.exhibition.domain.file.exception.FileUploadFailureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;

@Service
public class FileService {

    @Value("${upload.image.location}")
    private String location; // 파일을 업로드할 위치 주입

    @PostConstruct
    void postConstruct() { // 파일 업로드 될 디렉토리가 없을경우 생성
        File dir = new File(location);
        if (!dir.exists()) {
            dir.mkdir();
        }
    }

    public String getFullPath(String fileName) {
        return location + fileName;
    }

    public void upload(MultipartFile file, String fileName) {
        try {
            file.transferTo(new File(location + fileName));
        } catch (IOException e) {
            throw new FileUploadFailureException();
        }
    }

    public void delete(String filename) {
        new File(location + filename).delete();
    }
}
