package ru.skypro.flea.service.impl;

import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.flea.dto.AdDto;
import ru.skypro.flea.dto.AdsDto;
import ru.skypro.flea.dto.CreateOrUpdateAdDto;
import ru.skypro.flea.dto.ExtendedAdDto;
import ru.skypro.flea.exception.ResourceWithSpecifiedIdNotFoundException;
import ru.skypro.flea.mapper.AdMapper;
import ru.skypro.flea.model.Ad;
import ru.skypro.flea.repository.AdRepository;
import ru.skypro.flea.service.AdService;

import java.util.List;
import java.util.Optional;

@Service
public class AdServiceImpl implements AdService {

    private final AdMapper adMapper = Mappers.getMapper(AdMapper.class);

    private final AdRepository adRepository;

    public AdServiceImpl(AdRepository adRepository) {
        this.adRepository = adRepository;
    }

    @Override
    public AdsDto getAllAds() {
        List<Ad> ads = adRepository.findAll();

        return adMapper.toAdsDto(ads);
    }

    @Override
    public AdDto addAdd(MultipartFile image,
                        CreateOrUpdateAdDto properties) {
        Ad ad = adMapper.createAdFromDto("image link", properties); // TODO: to add image link when ImageService is ready
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

    // TODO: updateImage (when ImageService is ready)

}
