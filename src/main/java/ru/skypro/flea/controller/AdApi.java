package ru.skypro.flea.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.flea.dto.AdDto;
import ru.skypro.flea.dto.AdsDto;
import ru.skypro.flea.dto.CreateOrUpdateAdDto;
import ru.skypro.flea.dto.ExtendedAdDto;

import javax.validation.Valid;

@Validated
public interface AdApi {

  @PreAuthorize("permitAll()")
  @Operation(summary = "Get all ads")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "OK"
      )
  })
  @RequestMapping(value = "/ads",
      produces = MediaType.APPLICATION_JSON_VALUE,
      method = RequestMethod.GET)
  ResponseEntity<AdsDto> getAllAds();

  @PreAuthorize("hasAuthority('USER')")
  @Operation(summary = "Add an add")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "201",
          description = "Created",
          content = @Content(
              mediaType = MediaType.APPLICATION_JSON_VALUE,
              schema = @Schema(
                  implementation = AdDto.class
              )
          )
      ),
      @ApiResponse(
          responseCode = "401",
          description = "Unauthorized",
          content = @Content
      )
  })
  @RequestMapping(value = "/ads",
      consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE,
      method = RequestMethod.POST)
  ResponseEntity<AdDto> addAd(@RequestPart(name = "image") MultipartFile image,
                              @RequestPart(name = "properties") @Valid CreateOrUpdateAdDto properties,
                              Authentication authentication);

  @PreAuthorize("hasAuthority('USER')")
  @Operation(summary = "Get ad info")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "OK",
          content = @Content(
              mediaType = MediaType.APPLICATION_JSON_VALUE,
              schema = @Schema(
                  implementation = ExtendedAdDto.class
              )
          )
      ),
      @ApiResponse(
          responseCode = "401",
          description = "Unauthorized",
          content = @Content
      ),
      @ApiResponse(
          responseCode = "404",
          description = "Not Found",
          content = @Content
      )
  })
  @RequestMapping(value = "/ads/{id}",
      produces = MediaType.APPLICATION_JSON_VALUE,
      method = RequestMethod.GET)
  ResponseEntity<ExtendedAdDto> getAds(@PathVariable int id);

  @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
  @Operation(summary = "Delete ad")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "204",
          description = "No Content"
      ),
      @ApiResponse(
          responseCode = "401",
          description = "Unauthorized"
      ),
      @ApiResponse(
          responseCode = "403",
          description = "Forbidden"
      ),
      @ApiResponse(
          responseCode = "404",
          description = "Not Found"
      )
  })
  @RequestMapping(value = "/ads/{id}",
      produces = MediaType.APPLICATION_JSON_VALUE,
      method = RequestMethod.DELETE)
  ResponseEntity<Void> removeAd(@PathVariable int id,
                                Authentication authentication);

  @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
  @Operation(summary = "Update ad info")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "OK",
          content = @Content(
              mediaType = MediaType.APPLICATION_JSON_VALUE,
              schema = @Schema(
                  implementation = AdDto.class
              )
          )
      ),
      @ApiResponse(
          responseCode = "401",
          description = "Unauthorized",
          content = @Content
      ),
      @ApiResponse(
          responseCode = "403",
          description = "Forbidden",
          content = @Content
      ),
      @ApiResponse(
          responseCode = "404",
          description = "Not Found",
          content = @Content
      )
  })
  @RequestMapping(value = "/ads/{id}",
      produces = MediaType.APPLICATION_JSON_VALUE,
      method = RequestMethod.PATCH)
  ResponseEntity<AdDto> updateAds(@PathVariable int id,
                                  @RequestBody @Valid CreateOrUpdateAdDto properties,
                                  Authentication authentication);

  @PreAuthorize("hasAuthority('USER')")
  @Operation(summary = "Get authorized user's ads")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "OK",
          content = @Content(
              mediaType = MediaType.APPLICATION_JSON_VALUE,
              schema = @Schema(
                  implementation = AdsDto.class
              )
          )
      ),
      @ApiResponse(
          responseCode = "401",
          description = "Unauthorized",
          content = @Content
      )
  })
  @RequestMapping(value = "/ads/me",
      produces = MediaType.APPLICATION_JSON_VALUE,
      method = RequestMethod.GET)
  ResponseEntity<AdsDto> getAdsMe(Authentication authentication);

  @PreAuthorize("hasAuthority('USER')")
  @Operation(summary = "Update ad's image")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "OK"
      ),
      @ApiResponse(
          responseCode = "401",
          description = "Unauthorized",
          content = @Content
      ),
      @ApiResponse(
          responseCode = "403",
          description = "Forbidden",
          content = @Content
      ),
      @ApiResponse(
          responseCode = "404",
          description = "Not Found",
          content = @Content
      )
  })
  @RequestMapping(value = "/ads/{id}/image",
      consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
      produces = MediaType.APPLICATION_OCTET_STREAM_VALUE,
      method = RequestMethod.PATCH)
  ResponseEntity<byte[]> updateImage(@PathVariable int id,
                                     @RequestPart(name = "image") MultipartFile image,
                                     Authentication authentication);

}
