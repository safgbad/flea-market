package ru.skypro.flea.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.skypro.flea.dto.AdDto;
import ru.skypro.flea.dto.AdsDto;
import ru.skypro.flea.dto.CreateOrUpdateAdDto;
import ru.skypro.flea.dto.ExtendedAdDto;
import ru.skypro.flea.exception.ResourceWithSpecifiedIdNotFoundException;
import ru.skypro.flea.mapper.AdMapper;
import ru.skypro.flea.model.Ad;
import ru.skypro.flea.repository.AdRepository;
import ru.skypro.flea.service.impl.AdServiceImpl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdServiceTest {

    @Mock
    private AdMapper mapperMock;

    @Mock
    private AdRepository repositoryMock;

    @InjectMocks
    private AdServiceImpl out;

    @Test
    public void getAllAdsTest() {
        int id = 1;
        Ad ad = new Ad();
        ad.setId(id);
        List<Ad> ads = Collections.singletonList(ad);

        AdDto adDto = new AdDto();
        adDto.setPk(id);

        AdsDto adsDto = new AdsDto();
        adsDto.setResults(Collections.singletonList(adDto));
        adsDto.setCount(adsDto.getResults().size());

        when(repositoryMock.findAll()).thenReturn(ads);
        when(mapperMock.toAdsDto(ads)).thenReturn(adsDto);

        AdsDto result = out.getAllAds();

        assertEquals(result, adsDto);
    }

    @Test
    public void addAddTest() {
        // TODO when ImageService is ready
    }

    @Test
    public void getAdsShouldThrowExceptionWhenAdWithSpecifiedIdNotFoundTest() {
        when(repositoryMock.findById(any())).thenReturn(Optional.empty());

        assertThrows(ResourceWithSpecifiedIdNotFoundException.class, () -> out.getAds(1));
    }

    @Test
    public void getAdsShouldReturnCorrectDtoWhenAdWithSpecifiedIdFoundTest() {
        int id = 1;
        Ad ad = new Ad();
        ad.setId(id);

        ExtendedAdDto dto = new ExtendedAdDto();
        dto.setPk(ad.getId());

        when(repositoryMock.findById(id)).thenReturn(Optional.of(ad));
        when(mapperMock.toExtendedAdDto(ad)).thenReturn(dto);

        ExtendedAdDto result = out.getAds(id);

        assertEquals(result, dto);
    }

    @Test
    public void removeAdShouldThrowExceptionWhenAdWithSpecifiedIdNotFoundTest() {
        when(repositoryMock.existsById(any())).thenReturn(false);

        assertThrows(ResourceWithSpecifiedIdNotFoundException.class, () -> out.removeAd(1));
    }

    @Test
    public void removeAdShouldCallDeleteByIdRepositoryMethodWhenAdWithSpecifiedIdFoundTest() {
        when(repositoryMock.existsById(any())).thenReturn(true);

        int id = 1;
        out.removeAd(id);

        verify(repositoryMock).deleteById(id);
    }

    @Test
    public void updateAdsShouldThrowExceptionWhenAdWithSpecifiedIdNotFoundTest() {
        when(repositoryMock.findById(any())).thenReturn(Optional.empty());

        assertThrows(ResourceWithSpecifiedIdNotFoundException.class, () -> out.updateAds(1, null));
    }

    @Test
    public void updateAdsShouldReturnCorrectDtoWhenAdWithSpecifiedIdFoundTest() {
        int id = 1;
        Ad ad = new Ad();
        ad.setId(id);

        CreateOrUpdateAdDto createOrUpdateAdDto = new CreateOrUpdateAdDto();
        createOrUpdateAdDto.setTitle("smth.");

        AdDto adDto = new AdDto();
        adDto.setPk(id);
        adDto.setTitle(createOrUpdateAdDto.getTitle());

        when(repositoryMock.findById(id)).thenReturn(Optional.of(ad));

        doNothing().when(mapperMock).updateAdFromDto(ad, createOrUpdateAdDto);
        ad.setTitle(createOrUpdateAdDto.getTitle());

        when(mapperMock.toAdDto(ad)).thenReturn(adDto);

        AdDto result = out.updateAds(id, createOrUpdateAdDto);

        assertEquals(result, adDto);

        verify(mapperMock).updateAdFromDto(ad, createOrUpdateAdDto);
        verify(repositoryMock).save(ad);
    }

    // TODO: getAdsMeTest (need the user id from authorization)

    // TODO: updateImageTest (when ImageService is ready)

}
