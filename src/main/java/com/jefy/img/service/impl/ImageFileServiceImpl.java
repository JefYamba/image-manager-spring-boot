package com.jefy.img.service.impl;

import com.jefy.img.dto.ImageRequest;
import com.jefy.img.dto.ImageResponse;
import com.jefy.img.entity.ImageFile;
import com.jefy.img.repository.ImageFileRepository;
import com.jefy.img.service.ImageFileService;
import lombok.RequiredArgsConstructor;
import org.hibernate.ObjectNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @Author JefYamba
 * @Email joph.e.f.yamba@gmail.com
 * @Since 25/04/2024
 */
@Service
@Transactional
@RequiredArgsConstructor
public class ImageFileServiceImpl implements ImageFileService {
    private final ImageFileRepository imageFileRepository;

    @Override
    public ImageResponse get(String fileName) throws ObjectNotFoundException {
        return ImageResponse.fromEntity(
                imageFileRepository.findByName(fileName).orElseThrow(
                        () -> new ObjectNotFoundException(ImageFile.class, fileName)
                )
        );
    }

    @Override
    public byte[] getImage(String imageCompleteName) throws IllegalArgumentException, IOException {
        if (imageCompleteName != null && !imageCompleteName.isEmpty()) {
            Optional<ImageFile> optionalImageFile = imageFileRepository.findByCompleteName(imageCompleteName);
            if (optionalImageFile.isPresent()) {
                return Files.readAllBytes(getImagePath(imageCompleteName));
            } else {
                throw new ObjectNotFoundException(ImageFile.class, imageCompleteName);
            }
        }else {
            throw new IllegalArgumentException("fileName is empty");
        }
    }

    @Override
    public List<ImageResponse> getAll() {
        return imageFileRepository.findAll().stream()
                .map(ImageResponse::fromEntity)
                .toList();
    }


    @Override
    public ImageResponse save(ImageRequest imageRequest) throws IllegalArgumentException, IOException{

        if (imageRequest != null && !imageRequest.getImage().isEmpty()){
            ImageFile imageFile;
            Optional<ImageFile> optionalImageFile = imageFileRepository.findByName(imageRequest.getName());

            if (optionalImageFile.isPresent()) {
                imageFile = optionalImageFile.get();

                // delete the previous image from the directory to prevent it not being deleted
                // if the extension of the new file is different from the previous
                deleteImageFileFromDirectory(imageFile.getCompleteName());

                // save the new file in the directory
                imageFile.setCompleteName(saveImageInDirectory(imageRequest));

            } else {
                imageFile = ImageFile.builder()
                        .name(imageRequest.getName())
                        .completeName(saveImageInDirectory(imageRequest))
                        .build();
            }

            return ImageResponse.fromEntity(imageFileRepository.save(imageFile));
        }else {
            throw new IllegalArgumentException("fileName is empty");
        }
    }

    @Override
    public void delete(Integer id) throws IllegalArgumentException, IOException  {
        Optional<ImageFile> imageFileOptional = imageFileRepository.findById(id);
        if (imageFileOptional.isPresent()) {
            imageFileRepository.delete(imageFileOptional.get());
            deleteImageFileFromDirectory(imageFileOptional.get().getCompleteName());
        } else {
            throw new IllegalArgumentException("Image with id: " + id + " does not exist");
        }

    }

    private String saveImageInDirectory(ImageRequest imageRequest) throws IOException {
        String imageCompleteName = imageRequest.getName() + "." + getImageExtension(imageRequest);
        Path imagePath = getImagePath(imageCompleteName);
        Files.copy(imageRequest.getImage().getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);

        return imageCompleteName;
    }

    private String getImageExtension(ImageRequest imageRequest) {
        return Arrays.stream(Objects.requireNonNull(
                imageRequest.getImage().getOriginalFilename()
        ).split("\\.")).toList().getLast();
    }

    private void deleteImageFileFromDirectory(String imageCompleteName) throws IOException {
        Path imagePath = getImagePath(imageCompleteName);
        if (imagePath.toFile().isFile() && imagePath.toFile().exists()){
            Files.delete(imagePath);
        } else {
            throw new FileNotFoundException(imageCompleteName);
        }
    }

    private Path getImagePath(String imageCompleteName) {
        String absolutePath = "src/main/resources/static/images";
        return Paths.get(absolutePath + File.separator + imageCompleteName);
    }
}
