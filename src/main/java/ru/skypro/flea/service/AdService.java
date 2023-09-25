package ru.skypro.flea.service;

import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.flea.dto.AdDto;
import ru.skypro.flea.dto.AdsDto;
import ru.skypro.flea.dto.CreateOrUpdateAdDto;
import ru.skypro.flea.dto.ExtendedAdDto;
import ru.skypro.flea.model.Ad;

public interface AdService {

    AdsDto getAllAds();

    AdDto addAdd(MultipartFile image,
                 CreateOrUpdateAdDto properties,
                 Authentication authentication);

    ExtendedAdDto getAds(int id);

    void removeAd(int id,
                  Authentication authentication);

    AdDto updateAds(int id,
                    CreateOrUpdateAdDto properties,
                    Authentication authentication);

    AdsDto getAdsMe(Authentication authentication);

    byte[] updateImage(int id,
                       MultipartFile image,
                       Authentication authentication);

    Ad getAdById(int id);
}
