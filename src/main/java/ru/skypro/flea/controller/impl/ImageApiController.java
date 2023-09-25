package ru.skypro.flea.controller.impl;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.skypro.flea.controller.ImageApi;

import java.io.*;
import java.nio.file.Files;

@RestController
public class ImageApiController implements ImageApi {

  private static final String IMAGES_PATH = "src/main/resources/images";

  @Override
  public ResponseEntity<byte[]> downloadImage(String imageName) {
    File filePath = new File(IMAGES_PATH);
    File file = new File(filePath.getAbsolutePath(), imageName);
    try {
      return ResponseEntity.ok(Files.readAllBytes(file.toPath()));
    } catch (IOException e) {
      return ResponseEntity.notFound().build();
    }
  }
}
