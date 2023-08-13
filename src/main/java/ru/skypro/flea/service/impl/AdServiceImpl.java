package ru.skypro.flea.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.flea.dto.AdDto;
import ru.skypro.flea.dto.AdsDto;
import ru.skypro.flea.dto.CreateOrUpdateAdDto;
import ru.skypro.flea.dto.ExtendedAdDto;
import ru.skypro.flea.exception.FileSystemError;
import ru.skypro.flea.exception.ResourceWithSpecifiedIdNotFoundException;
import ru.skypro.flea.exception.UnsupportedImageTypeException;
import ru.skypro.flea.mapper.AdMapper;
import ru.skypro.flea.model.Ad;
import ru.skypro.flea.repository.AdRepository;
import ru.skypro.flea.service.AdService;
import ru.skypro.flea.service.ImageService;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class AdServiceImpl implements AdService {

    private static final String NO_PHOTO = "/no-photo.png";

    private final AdMapper adMapper;

    private final AdRepository adRepository;

    private final ImageService imageService;

    @Override
    public AdsDto getAllAds() {
        List<Ad> ads = adRepository.findAll();

        return adMapper.toAdsDto(ads);
    }

    @Override
    public AdDto addAdd(MultipartFile image,
                        CreateOrUpdateAdDto properties) {
        Ad ad = adMapper.createAdFromDto(properties);
        ad.setImage("not_specified_yet");
        adRepository.save(ad);
        int id = ad.getId();
        try {
            String imageName = imageService.saveImage(image, "ad-" + id);
            ad.setImage('/' + imageName);
        } catch (UnsupportedImageTypeException | FileSystemError e) {
            ad.setImage(NO_PHOTO);
        }
        adRepository.save(ad);

        return adMapper.toAdDto(ad);
    }

    @Override
    public ExtendedAdDto getAds(int id) {
        Optional<Ad> adOptional = adRepository.findById(id);
        if (adOptional.isEmpty()) {
            throw new ResourceWithSpecifiedIdNotFoundException(
                    String.format("Ad with specified id (%d) is not found", id));
        }

        return adMapper.toExtendedAdDto(adOptional.get());
    }

    @Override
    public void removeAd(int id) {
        if (!adRepository.existsById(id)) {
            throw new ResourceWithSpecifiedIdNotFoundException(
                    String.format("Ad with specified id (%d) is not found", id));
        }
        adRepository.deleteById(id);
    }

    @Override
    public AdDto updateAds(int id,
                           CreateOrUpdateAdDto properties) {
        Optional<Ad> adOptional = adRepository.findById(id);
        if (adOptional.isEmpty()) {
            throw new ResourceWithSpecifiedIdNotFoundException(
                    String.format("Ad with specified id (%d) is not found", id));
        }
        Ad ad = adOptional.get();
        adMapper.updateAdFromDto(ad, properties);
        adRepository.save(ad);

        return adMapper.toAdDto(ad);
    }

    // TODO: getAdsMe (need the user id from authorization)

    @Override
    public byte[] updateImage(int id,
                              MultipartFile image) {
        if (!adRepository.existsById(id)) {
            throw new ResourceWithSpecifiedIdNotFoundException(
                    String.format("Ad with specified id (%d) is not found", id));
        }
        try {
            byte[] bytes = image.getBytes();
            String imageName = imageService.saveImage(image, "ad-" + id);
            adRepository.updateImageById('/' + imageName, id);
            return bytes;
        } catch (IOException | UnsupportedImageTypeException | FileSystemError e) {
            return null;
        }
    }

}
