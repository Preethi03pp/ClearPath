package com.example.rbpt.model;

import java.time.Instant;

public class FileMeta {
  public String id;
  public String fileName;
  public String originalName;
  public String contentType;
  public long size;
  public String url;
  public Instant uploadedAt = Instant.now();
}
