package com.jefy.img.controller;

import com.jefy.img.dto.ImageRequest;
import com.jefy.img.dto.ImageResponse;
import com.jefy.img.service.ImageFileService;
import lombok.RequiredArgsConstructor;
import org.hibernate.ObjectNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static com.jefy.img.dto.Constant.IMAGES_URL;
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

    @GetMapping(path = "/get/{fileName}")
    public ResponseEntity<ImageResponse> getImage(@PathVariable String fileName) {
        try {
            ImageResponse imageResponse = imageFileService.get(fileName);

            return ResponseEntity.ok(imageResponse);
        } catch (ObjectNotFoundException e){
            return ResponseEntity.notFound().build();
        }
    }

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

    @GetMapping("")
    public ResponseEntity<List<ImageResponse>> getAll(){
        return ResponseEntity.ok(imageFileService.getAll());
    }

    @PostMapping("/upload")
    public ResponseEntity<?> registerImage(@RequestPart String name, @RequestPart MultipartFile image) {
        ImageRequest imageRequest = ImageRequest.builder()
                .name(name)
                .image(image)
                .build();
        try {
            return ResponseEntity.ok(imageFileService.save(imageRequest));
        } catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IOException e){
            return ResponseEntity.internalServerError().body("Could not add image : " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteImage(@PathVariable Integer id) {
        try {
            imageFileService.delete(id);

            return ResponseEntity.ok("Image deleted successfully");
        } catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Could not delete image : " + e.getMessage());
        }

    }
}
