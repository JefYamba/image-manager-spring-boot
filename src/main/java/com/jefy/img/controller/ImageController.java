package com.jefy.img.controller;

import com.jefy.img.dto.ImageRequest;
import com.jefy.img.entity.ImageFile;
import com.jefy.img.service.ImageFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static com.jefy.img.dto.Constant.IMAGES_BASE_URL;
import static org.springframework.http.MediaType.IMAGE_PNG_VALUE;

/**
 * @Author JefYamba
 * @Email joph.e.f.yamba@gmail.com
 * @Since 25/04/2024
 */
@RestController
@CrossOrigin("*")
@RequestMapping(IMAGES_BASE_URL)
@RequiredArgsConstructor
public class ImageController {
    private final ImageFileService imageFileService;

    @GetMapping("")
    public ResponseEntity<List<ImageFile>> getAll(){
        return ResponseEntity.ok(imageFileService.getAll());
    }

    @PostMapping("/upload")
    public ResponseEntity<ImageFile> addImage(@RequestPart String name, @RequestPart MultipartFile image) {
        ImageRequest imageRequest = ImageRequest.builder()
                .name(name)
                .image(image)
                .build();
        System.out.println(imageRequest.getImage().getOriginalFilename());
        try {
            System.out.println("before");
            ImageFile imageFile = imageFileService.create(imageRequest);
            System.out.println("after");
            return ResponseEntity.ok(imageFile);
        } catch (IOException e){
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping(path = "/image/{fileName}", produces = IMAGE_PNG_VALUE)
    public byte[] getServerImage(@PathVariable String fileName) throws IOException {
        return Files.readAllBytes(Paths.get(System.getProperty("user.dir") + "/src/main/resources/static/server_images/" + fileName));
    }
}
