package ru.skypro.flea.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.flea.dto.AdDto;
import ru.skypro.flea.dto.AdsDto;
import ru.skypro.flea.dto.CreateOrUpdateAdDto;
import ru.skypro.flea.dto.ExtendedAdDto;
import ru.skypro.flea.exception.ActionForbiddenException;
import ru.skypro.flea.exception.FileSystemError;
import ru.skypro.flea.exception.ResourceWithSpecifiedIdNotFoundException;
import ru.skypro.flea.exception.UnsupportedImageTypeException;
import ru.skypro.flea.mapper.AdMapper;
import ru.skypro.flea.model.Ad;
import ru.skypro.flea.model.User;
import ru.skypro.flea.repository.AdRepository;
import ru.skypro.flea.service.AdService;
import ru.skypro.flea.service.ImageService;
import ru.skypro.flea.service.UserService;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AdServiceImpl implements AdService {

    private static final String NO_PHOTO = "/no-photo.png";

    private final AdMapper adMapper;

    private final AdRepository adRepository;

    private final ImageService imageService;

    private final UserService userService;


    @Override
    public AdsDto getAllAds() {
        List<Ad> ads = adRepository.findAllByOrderByIdDesc();

        return adMapper.toAdsDto(ads);
    }

    @Override
    public AdDto addAdd(MultipartFile image,
                        CreateOrUpdateAdDto properties,
                        Authentication authentication) {
        Ad ad = adMapper.createAdFromDto(properties);
        ad.setImage("not_specified_yet");
        adRepository.save(ad);
        int id = ad.getId();
        try {
            String imageName = imageService.saveImage(image, "ad-" + id);
            ad.setImage("/img/" + imageName);
        } catch (UnsupportedImageTypeException | FileSystemError e) {
            ad.setImage(NO_PHOTO);
        }
        User user = userService.getUserByEmail(authentication.getName());
        ad.setUser(user);
        adRepository.save(ad);

        return adMapper.toAdDto(ad);
    }

    @Override
    public ExtendedAdDto getAds(int id) {
        Ad ad = getAdById(id);

        return adMapper.toExtendedAdDto(ad);
    }

    @Override
    public void removeAd(int id,
                         Authentication authentication) {
        Ad ad = getAdById(id);
        checkIfUserHasAccessToManageTheAd(ad, authentication, "delete");
        adRepository.deleteById(id);
    }

    @Override
    public AdDto updateAds(int id,
                           CreateOrUpdateAdDto properties,
                           Authentication authentication) {
        Ad ad = getAdById(id);
        checkIfUserHasAccessToManageTheAd(ad, authentication, "update");
        adMapper.updateAdFromDto(ad, properties);
        adRepository.save(ad);

        return adMapper.toAdDto(ad);
    }

    @Override
    public AdsDto getAdsMe(Authentication authentication) {
        User user = userService.getUserByEmail(authentication.getName());
        List<Ad> ads = adRepository.findAdsByUserOrderByIdDesc(user);

        return adMapper.toAdsDto(ads);
    }

    @Override
    public byte[] updateImage(int id,
                              MultipartFile image,
                              Authentication authentication) {
        Ad ad = getAdById(id);
        checkIfUserHasAccessToManageTheAd(ad, authentication, "update");
        try {
            byte[] bytes = image.getBytes();
            String imageName = imageService.saveImage(image, "ad-" + id);
            ad.setImage("/img/" + imageName);
            adRepository.save(ad);
            return bytes;
        } catch (IOException | UnsupportedImageTypeException | FileSystemError e) {
            return null;
        }
    }

    @Override
    public Ad getAdById(int id) {
        Optional<Ad> adOptional = adRepository.findById(id);
        if (adOptional.isEmpty()) {
            String message = String.format("Ad with specified id (%d) is not found", id);
            log.info(message);
            throw new ResourceWithSpecifiedIdNotFoundException(message);
        }
        return adOptional.get();
    }

    private void checkIfUserHasAccessToManageTheAd(Ad ad,
                                                   Authentication authentication,
                                                   String action) {
        if (!ad.getUser().getUsername().equals(authentication.getName())
                && !userService.isAuthorizedUserAdmin(authentication)) {
            String message = String.format("User %s is not allowed to %s the ad (%d)",
                    authentication.getName(), action, ad.getId());
            log.info(message);
            throw new ActionForbiddenException(message);
        }
    }

}
