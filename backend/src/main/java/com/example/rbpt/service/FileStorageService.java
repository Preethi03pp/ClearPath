package com.example.rbpt.service;

import com.example.rbpt.model.FileMeta;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.UUID;

@Service
public class FileStorageService {
  @Value("${app.upload.dir}") private String uploadDir;

  public FileStorageService() {}

  public FileMeta save(MultipartFile file) throws IOException {
    File dir = new File(uploadDir);
    if (!dir.exists()) dir.mkdirs();

    String ext = "";
    String orig = file.getOriginalFilename() != null ? file.getOriginalFilename() : "file";
    int i = orig.lastIndexOf('.');
    if (i > 0) ext = orig.substring(i);
    String stored = UUID.randomUUID().toString().replace("-", "") + ext;

    File dest = new File(dir, stored);
    file.transferTo(dest);

    FileMeta meta = new FileMeta();
    meta.id = UUID.randomUUID().toString();
    meta.fileName = stored;
    meta.originalName = orig;
    meta.size = file.getSize();
    meta.contentType = file.getContentType();
    meta.url = "/api/files/" + stored;
    return meta;
  }

  public File getFile(String storedName) {
    return new File(uploadDir, storedName);
  }

  public byte[] readFile(String storedName) throws IOException {
    return Files.readAllBytes(getFile(storedName).toPath());
  }
}
