package ru.skypro.flea.service;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {

  void checkCatalogue();

  String saveImage(MultipartFile multipartFile, String fileName);
}
