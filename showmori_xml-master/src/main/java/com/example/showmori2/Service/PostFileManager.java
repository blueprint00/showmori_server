package com.example.showmori2.Service;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Component
class PostFileManager {
    public void fileUpload(MultipartFile file, String filepath) throws IOException {
        file.transferTo(new File(filepath));
    }
}
