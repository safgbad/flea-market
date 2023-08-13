package ru.skypro.flea.service;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import ru.skypro.flea.exception.UnsupportedImageTypeException;
import ru.skypro.flea.service.impl.ImageServiceImpl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.*;

class ImageServiceTest {

    private final String imagesPath = "src/test/resources/ru/skypro/flea/service";

    private final ImageServiceImpl out = new ImageServiceImpl(imagesPath);

    @Test
    public void catalogueCreationTest() {
        File catalogue = new File(imagesPath);
        catalogue.delete();
        out.checkCatalogue();

        assertTrue(catalogue.exists());
    }

    @Test
    public void saveImageShouldThrowExceptionWhenMultipartFileOriginalFilenameIsNullTest() {
        out.checkCatalogue();

        MockMultipartFile multipartFile = new MockMultipartFile(
                "smth.smth",
                null,
                MediaType.ALL_VALUE,
                new byte[]{});

        assertThrows(UnsupportedImageTypeException.class, () -> out.saveImage(multipartFile, ""));
    }

    @Test
    public void saveImageShouldThrowExceptionWhenMultipartFileFormatIsNotAcceptedTest() {
        out.checkCatalogue();

        MockMultipartFile multipartFile = new MockMultipartFile(
                "smth.smth",
                "smth.smth",
                MediaType.ALL_VALUE,
                new byte[]{});

        assertThrows(UnsupportedImageTypeException.class, () -> out.saveImage(multipartFile, ""));
    }

    @Test
    public void saveImageShouldSaveWithCorrectNameInCorrectCatalogueTest() throws IOException {
        out.checkCatalogue();

        String format = "png";
        byte[] bytes = new byte[]{1, 2, 3};
        MockMultipartFile multipartFile = new MockMultipartFile(
                "image" + '.' + format,
                "image" + '.' + format,
                "image/" + format,
                bytes);

        String fileName = "user-1";
        out.saveImage(multipartFile, fileName);

        File image = new File(imagesPath + '/' + fileName + '.' + format);
        byte[] fileBytes = Files.readAllBytes(image.toPath());

        assertTrue(image.exists());
        assertArrayEquals(fileBytes, bytes);
    }

}
