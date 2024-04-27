package com.jefy.img.controller;

import com.jefy.img.dto.Response;
import com.jefy.img.dto.ImageRequest;
import com.jefy.img.dto.ImageResponse;
import com.jefy.img.service.ImageFileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.hibernate.ObjectNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static com.jefy.img.dto.Constant.IMAGES_URL;
import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;
import static org.springframework.http.MediaType.IMAGE_PNG_VALUE;

/**
 * @Author JefYamba
 * @Email joph.e.f.yamba@gmail.com
 * @Since 25/04/2024
 */
@RestController
@CrossOrigin("*")
@RequestMapping(IMAGES_URL)
@RequiredArgsConstructor
public class ImageController {
    private final ImageFileService imageFileService;

    @Operation(
            description = "get the image by its name",
            responses = {
                @ApiResponse(
                        description = "Success",
                        responseCode = "200"
                ),
                @ApiResponse(
                        description = "Not found / Invalid parameter",
                        responseCode = "404"
                )
            }
    )
    @GetMapping(path = "/get/{fileName}")
    public ResponseEntity<Response<ImageResponse>> getImage(@PathVariable String fileName) {
        try {
            return ResponseEntity.ok(
                    Response.<ImageResponse>builder()
                            .timeStamp(now())
                            .status(OK)
                            .statusCode(OK.value())
                            .message("Images retrieved successfully")
                            .data(imageFileService.get(fileName))
                            .build()
            );
        } catch (ObjectNotFoundException e){
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(
            description = "get the image view by its full name",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Not found / Invalid parameter",
                            responseCode = "404"
                    ),
                    @ApiResponse(
                            description = "Internal Server Error / could not read the image file",
                            responseCode = "500"
                    )
            }
    )
    @GetMapping(path = "/{completeName}", produces = {IMAGE_PNG_VALUE, IMAGE_JPEG_VALUE})
    public ResponseEntity<?> getUrlImage(@PathVariable String completeName) {
        try {
            return ResponseEntity.ok(imageFileService.getImage(completeName));

        } catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }  catch (ObjectNotFoundException e){
            return ResponseEntity.notFound().build();
        } catch (IOException e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @Operation(
            description = "fetch all the images",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    )
            }
    )
    @GetMapping("")
    public ResponseEntity<Response<List<ImageResponse>>> getAll(){
        return ResponseEntity.ok(
                Response.<List<ImageResponse>>builder()
                        .timeStamp(now())
                        .status(OK)
                        .statusCode(OK.value())
                        .message("Images retrieved successfully")
                        .data(imageFileService.getAll())
                        .build()
        );
    }

    @Operation(
            description = "Register or update an image",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Bad request/ Invalid parameter",
                            responseCode = "400"
                    ),
                    @ApiResponse(
                            description = "Internal Server Error / could not read the image file",
                            responseCode = "500"
                    )
            }
    )
    @PostMapping("/upload")
    public ResponseEntity<Response<ImageResponse>> registerImage(@RequestPart String name, @RequestPart MultipartFile image) {
        ImageRequest imageRequest = ImageRequest.builder()
                .name(name)
                .image(image)
                .build();
        try {
            return ResponseEntity.ok(
                    Response.<ImageResponse>builder()
                            .timeStamp(now())
                            .status(OK)
                            .statusCode(OK.value())
                            .message("Image registered successfully")
                            .data(imageFileService.save(imageRequest))
                            .build()
            );
        } catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(
                    Response.<ImageResponse>builder()
                            .timeStamp(now())
                            .status(BAD_REQUEST)
                            .statusCode(BAD_REQUEST.value())
                            .message(e.getMessage())
                            .data(null)
                            .build()
            );
        } catch (IOException e){
            return ResponseEntity.internalServerError().body(
                    Response.<ImageResponse>builder()
                            .timeStamp(now())
                            .status(INTERNAL_SERVER_ERROR)
                            .statusCode(INTERNAL_SERVER_ERROR.value())
                            .message("Could not add image : " + e.getMessage())
                            .data(null)
                            .build()
            );
        }
    }

    @Operation(
            description = "Delete an image",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Not found/ Invalid parameter",
                            responseCode = "404"
                    ),
                    @ApiResponse(
                            description = "Internal Server Error / could not read the image file",
                            responseCode = "500"
                    )
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Response<Boolean>> deleteImage(@PathVariable Integer id) {
        try {
            imageFileService.delete(id);

            return ResponseEntity.ok(
                    Response.<Boolean>builder()
                            .timeStamp(now())
                            .status(OK)
                            .statusCode(OK.value())
                            .message("Image deleted successfully")
                            .data(true)
                            .build()

            );
        } catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(
                    Response.<Boolean>builder()
                            .timeStamp(now())
                            .status(NOT_FOUND)
                            .statusCode(NOT_FOUND.value())
                            .message(e.getMessage())
                            .data(false)
                            .build()
            );
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body(
                    Response.<Boolean>builder()
                            .timeStamp(now())
                            .status(INTERNAL_SERVER_ERROR)
                            .statusCode(INTERNAL_SERVER_ERROR.value())
                            .message("Could not delete image : " + e.getMessage())
                            .data(false)
                            .build()
            );
        }

    }
}
