package ru.skypro.flea.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

public interface ImageApi {

  @Operation(summary = "Image for frontend")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "OK"
      )
  })
  @ApiResponse(
      responseCode = "404",
      description = "Not Found",
      content = @Content
  )
  @RequestMapping(value = "/img/{imageName}",
      produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE},
      method = RequestMethod.GET)
  ResponseEntity<byte[]> downloadImage(@PathVariable String imageName);
}
