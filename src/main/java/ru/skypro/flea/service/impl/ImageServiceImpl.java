package ru.skypro.flea.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.flea.exception.FileSystemError;
import ru.skypro.flea.exception.UnsupportedImageTypeException;
import ru.skypro.flea.service.ImageService;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.Set;

@Slf4j
@Service
public class ImageServiceImpl implements ImageService {

  private final String imagesPath;
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
      boolean result = catalogue.mkdirs();
      if (result) {
        log.info("Image resource catalogue was created.");
      } else {
        throw new FileSystemError("Failed to create image resource catalogue");
      }
    } else {
      log.info("Image resource catalogue already exists.");
    }
  }

  @Override
  public void saveImage(MultipartFile multipartFile, String fileName) {
    String extension = "";
    String multipartFileName = multipartFile.getName();
    int i = multipartFileName.lastIndexOf(".");
    if (i >= 0) {
      extension = multipartFileName.substring(i + 1);
    }
    if (!ACCEPTABLE_EXTENSIONS.contains(extension)) {
      throw new UnsupportedImageTypeException("Image must be in JPEG/PNG format");
    }
    String newFileName = fileName + "." + extension;
    File filePath = new File(imagesPath);
    File file = new File(filePath.getAbsolutePath(), newFileName);
    try {
      multipartFile.transferTo(file);
      log.info("File was saved.");
    } catch (IOException e) {
      log.error("Failed to save file.");
      e.printStackTrace();
      throw new FileSystemError("Failed to save file");
    }
  }

}
