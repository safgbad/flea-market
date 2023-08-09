package ru.skypro.flea.controller.impl;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.flea.controller.AdApi;
import ru.skypro.flea.dto.AdDto;
import ru.skypro.flea.dto.AdsDto;
import ru.skypro.flea.dto.CreateOrUpdateAdDto;
import ru.skypro.flea.dto.ExtendedAdDto;
import ru.skypro.flea.service.AdService;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@Tag(name = "Ads")
public class AdApiController implements AdApi {

    private final AdService adService;

    @Override
    public ResponseEntity<AdsDto> getAllAds() {
        AdsDto ads = adService.getAllAds();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ads);
    }

    @Override
    public ResponseEntity<AdDto> addAd(MultipartFile image,
                                       CreateOrUpdateAdDto properties) {
        AdDto ad = adService.addAdd(image, properties);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ad);
    }

    @Override
    public ResponseEntity<ExtendedAdDto> getAds(int id) {
        ExtendedAdDto ad = adService.getAds(id);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ad);
    }

    @Override
    public ResponseEntity<Void> removeAd(int id) {
        adService.removeAd(id);

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @Override
    public ResponseEntity<AdDto> updateAds(int id,
                                           CreateOrUpdateAdDto properties) {
        AdDto ad = adService.updateAds(id, properties);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ad);
    }

    @Override
    public ResponseEntity<AdsDto> getAdsMe() {
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<byte[]> updateImage(int id,
                                              MultipartFile image) {
        return ResponseEntity.ok().build();
    }

}
