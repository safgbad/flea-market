package ru.skypro.flea.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.flea.service.ImageService;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.Set;

@Slf4j
@Service
public class ImageServiceImpl implements ImageService {

  private String imagesPath;
  private static final Set<String> ACCEPTABLE_EXTENSIONS = Set.of("png", "jpg", "jpeg");

  public ImageServiceImpl() {
    imagesPath = "src/main/resources/images";
  }

  public ImageServiceImpl(String imagesPath) {
    this.imagesPath = imagesPath;
  }

  @Override
  @PostConstruct
  public void checkCatalogue() {
    File catalogue = new File(imagesPath);
    if (!catalogue.exists()) {
      boolean result = catalogue.mkdir();
      if (result) {
        log.info("Catalogue was created.");
      } else {
        log.info("Failed to create catalogue.");
        throw new RuntimeException();
      }
    } else {
      log.info("Catalogue already exists.");
    }
  }

  @Override
  public void saveImage(MultipartFile multipartFile, String fileName) {
    String extension = "";
    String originalFileName = multipartFile.getOriginalFilename();
    if (originalFileName == null) {
      throw new RuntimeException();
    }
    int i = originalFileName.lastIndexOf(".");
    if (i >= 0) {
      extension = originalFileName.substring(i + 1);
    }
    if (!ACCEPTABLE_EXTENSIONS.contains(extension)) {
      throw new RuntimeException();
    }
    String newFileName = fileName + "." + extension;
    File filePath = new File(imagesPath);
    File file = new File(filePath.getAbsolutePath(), newFileName);
    try {
      multipartFile.transferTo(file);
      log.info("File was saved.");
    } catch (IOException e) {
      log.info("Failed to save file.");
      e.printStackTrace();
      throw new RuntimeException();
    }
  }

}
