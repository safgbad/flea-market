package ru.skypro.flea.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.flea.dto.AdDto;
import ru.skypro.flea.dto.AdsDto;
import ru.skypro.flea.dto.CreateOrUpdateAdDto;
import ru.skypro.flea.dto.ExtendedAdDto;

public interface AdService {

    AdsDto getAllAds();

    AdDto addAdd(MultipartFile image,
                 CreateOrUpdateAdDto properties);

    ExtendedAdDto getAds(int id);

    void removeAd(int id);

    AdDto updateAds(int id,
                    CreateOrUpdateAdDto properties);

    byte[] updateImage(int id,
                       MultipartFile image);
}
